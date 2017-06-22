package pe.edu.uni.fiis.so.statistic;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by vcueva on 6/21/17.
 */
public class StatisticParser {

    public static StatisticModel parse(String line) {

        String type = null;
        StatisticModel sm = null;
        ArrayList<String> myList = null;
        if (line.contains("(")){
            type = line.substring(0,line.indexOf("("));
        }else if (line.contains("[")){
            type = line.substring(0,3);
        }

        switch (type){
            case "uniform":
                line = line.substring(8);
                line = line.substring(0,line.indexOf(")"));
                StringTokenizer st = new StringTokenizer(line,",");

                while(st.hasMoreTokens()){
                    myList.add(st.nextToken());
                }
                sm = new UniformDistribution(Integer.parseInt(myList.get(0)),Integer.parseInt(myList.get(1)));
                break;
            case "normal":
                line = line.substring(7);
                line = line.substring(0,line.indexOf(")"));
                StringTokenizer st2 = new StringTokenizer(line,",");

                while(st2.hasMoreTokens()){
                    myList.add(st2.nextToken());
                }
                sm = new NormalDistribution(Double.parseDouble(myList.get(0)),Double.parseDouble(myList.get(1)));
                break;
            case "exp":
                line = line.substring(4);
                line = line.substring(0,line.indexOf(")"));

                sm = new ExponentialDistribution(Double.parseDouble(line));
                break;
            case "int":
                sm = new OriginalDistribution(line);
                break;
        }

        return sm;
    }
}
