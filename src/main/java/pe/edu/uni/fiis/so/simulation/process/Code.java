package pe.edu.uni.fiis.so.simulation.process;

import java.util.List;

/**
 * Created by vcueva on 6/28/17.
 */
public class Code {
    public static final int STATE_LOADING = 1;
    public static final int STATE_LOADED = 2;

    private List <Sentence> sentences;
    private int state;

    public Code() {
        this.state = STATE_LOADING;
    }

    public void load(List<String> lines) {
        if (this.state == STATE_LOADED) {
            throw new RuntimeException("The code is already loaded.");
        }

        int nroLine = 0;
        for (String line: lines) {
            sentences.add(new Sentence(line, nroLine++));
        }
    }

    public List<Sentence> getSentences() {
        return sentences;
    }
}