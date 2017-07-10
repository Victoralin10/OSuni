package pe.edu.uni.fiis.so.simulation.process;

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

    public Code() {
        this.state = STATE_LOADING;
    }

    public void load(List<String> lines) {
        if (this.state == STATE_LOADED) {
            throw new RuntimeException("The code is already loaded.");
        }

        int nroLine = 0;
        for (String line : lines) {
            sentences.add(new Sentence(line, nroLine++));
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
}
