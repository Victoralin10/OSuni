package pe.edu.uni.fiis.so.statistic;

/**
 * Created by vcueva on 6/21/17.
 */
public class StatisticParser {

    public static StatisticModel parse(String line) {

        String type = null;
        StatisticModel sm = null;
        String[] myList = null;
        if (line.contains("(")) {
            type = line.substring(0, line.indexOf("("));
        } else if (line.contains("[")) {
            type = line.substring(0, 3);
        }

        switch (type) {
            case "uniform":
                line = line.substring(8);
                line = line.substring(0, line.indexOf(")"));
                line = line.replace(" ", "");
                myList = line.split(",");
                sm = new UniformDistribution(Integer.parseInt(myList[0]), Integer.parseInt(myList[1]));
                break;
            case "normal":
                line = line.substring(7);
                line = line.substring(0, line.indexOf(")"));
                line = line.replace(" ", "");
                myList = line.split(",");
                sm = new NormalDistribution(Double.parseDouble(myList[0]), Double.parseDouble(myList[1]));
                break;
            case "exp":
                line = line.substring(4);
                line = line.substring(0, line.indexOf(")"));
                line = line.replace(" ", "");
                sm = new ExponentialDistribution(Double.parseDouble(line));
                break;
            case "int":
                sm = new OriginalDistribution(line);
                break;
            case "str":
                sm = new OriginalDistribution(line);
                break;
        }

        return sm;
    }

}

