package pe.edu.uni.fiis.so.simulation.process;

import pe.edu.uni.fiis.so.util.SizeParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by vcueva on 6/28/17.
 */
public class Code {
    public static final int STATE_LOADING = 1;
    public static final int STATE_LOADED = 2;

    private int state;

    private List<Sentence> sentences;
    private Map<String, Integer> labelMap;

    private String name;
    private long size;

    public Code() {
        this.state = STATE_LOADING;
        sentences = new ArrayList<>();
        this.name = "unnamed";
        this.size = 50*1024*1024;
    }

    public void load(List<String> lines) {
        if (this.state == STATE_LOADED) {
            throw new RuntimeException("The code is already loaded.");
        }

        int nroLine = 0;
        for (String line : lines) {
            Sentence sentence = new Sentence(line, nroLine++);
            if (sentence.getType() == Sentence.TYPE_DESCRIPTION) {
                if (sentence.getFunction().equals("name")) {
                    this.name = sentence.getArguments().get(0);
                } else {
                    this.size = SizeParser.parse(sentence.getArguments());
                }
            }
            sentences.add(sentence);
        }
        labelMap = new TreeMap<>();
        for (Sentence sentence : sentences) {
            if (sentence.isLabel()) {
                labelMap.put(sentence.getFunction(), sentence.getAddress());
            }
        }
    }

    public Sentence getSentenceByAddress(int address) {
        return sentences.get(address);
    }

    public Sentence getSentenceByLabel(String label) {
        return getSentenceByAddress(labelMap.get(label));
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
