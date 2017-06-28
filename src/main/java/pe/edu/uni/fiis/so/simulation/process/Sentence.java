package pe.edu.uni.fiis.so.simulation.process;

import java.util.List;

/**
 * Created by vcueva on 6/28/17.
 */
public class Sentence {
    public static final int TYPE_BLANK = 1;
    public static final int TYPE_DESCRIPTION = 2;
    public static final int TYPE_LABEL = 4;
    public static final int TYPE_INSTRUCTION = 8;

    private int address;
    private int type;
    private String function;
    private List<String> arguments;

    public Sentence(String line, int address) {
        this.address = address;

        if (line.startsWith("#")) {
            this.type = TYPE_DESCRIPTION;
        }
    }

    public int getType() {
        return type;
    }

    public String getFunction() {
        return function;
    }

    public List<String> getArguments() {
        return arguments;
    }
}
