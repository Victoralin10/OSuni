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
            type = TYPE_DESCRIPTION;
            line = line.substring(1);
            List<String> desList = ArgumentParser.parse(line);
            desList.set(0, desList.get(0).replace(":", ""));
            function = desList.get(0);
            desList.remove(0);
            arguments = desList;
        } else {
            lineList = ArgumentParser.parse(line);
            if (lineList.isEmpty()) {
                type = TYPE_BLANK;
            } else if (lineList.size() == 1 && lineList.get(0).endsWith(":")) {
                type = TYPE_LABEL;
                function = lineList.get(0);
                function = function.substring(0, function.length() - 1);
            } else {
                type = TYPE_INSTRUCTION;
                function = lineList.get(0);
            }
            if (lineList.size() > 0) {
                lineList.remove(0);
            }
            arguments = lineList;
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

    public boolean isBlank() {
        return type == TYPE_BLANK;
    }
}
