package pe.edu.uni.fiis.so.simulation.process;

import pe.edu.uni.fiis.so.util.ArgumentParser;

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

        List<String> lineList = null;
        if (line.startsWith("#")) {
            this.type = TYPE_DESCRIPTION;
            line = line.substring(1);
            List<String> desList = ArgumentParser.parse(line);
            desList.set(0, desList.get(0).replace(":", ""));
            this.function = desList.get(0);
            desList.remove(0);
            this.arguments = desList;
        } else {
            lineList = ArgumentParser.parse(line);
            if (lineList.isEmpty()) {
                this.type = TYPE_BLANK;

            } else if (lineList.size() == 1 && lineList.get(1).endsWith(":")) {
                this.type = TYPE_LABEL;
            } else {
                this.type = TYPE_INSTRUCTION;
            }
            this.function = lineList.get(0);
            lineList.remove(0);
            this.arguments = lineList;
        }
    }

    public int getAddress() {
        return address;
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

    public boolean isLabel() {
        return type == TYPE_LABEL;
    }
}
