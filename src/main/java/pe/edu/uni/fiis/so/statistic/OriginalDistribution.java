package pe.edu.uni.fiis.so.statistic;


import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Luis Angel on 21/06/2017.
 */
public class OriginalDistribution implements StatisticModel {

    private String numbers;

    public OriginalDistribution(String numbers) {
        this.numbers = numbers;
    }

    public static ArrayList<String> parseLine(String str){
        ArrayList<String> ListNum = null;

        String type = str.substring(0,3);


        if (type.equals("int") || type.equals("str")){

            str = str.substring(4);
            str = str.replace(" ","");
            str = str.substring(0,str.indexOf("]"));

            StringTokenizer st = new StringTokenizer(str,",");

            while(st.hasMoreTokens()){
                ListNum.add(st.nextToken());
                //System.out.println(st.nextToken());
            }

        }else{
            System.out.println("Cadena no valida");
        }
        return ListNum;

    }

    @Override
    public long nextLongRandom() {
        ArrayList<String> num = parseLine(numbers);
        double r = Math.random();
        int pos = (int)(r * (num.size()));
        return Long.parseLong(num.get(pos));
    }

    @Override
    public double nexDoubleRandom() {
        return 0;
    }


}

