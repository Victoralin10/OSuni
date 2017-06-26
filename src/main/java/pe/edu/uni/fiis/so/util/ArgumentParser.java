package pe.edu.uni.fiis.so.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vcueva on 6/21/17.
 */
public class ArgumentParser {

    public static List<String> parse(String line) {
        line = line.trim();
        ArrayList<String> ans = new ArrayList<>();

        boolean quotes = false;
        StringBuilder arg = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '\\') {
                i++;
                arg.append(line.charAt(i));
            } else if (line.charAt(i) == '"') {
                quotes = !quotes;
            } else if (quotes) {
                arg.append(line.charAt(i));
            } else if (line.charAt(i) == ' ') {
                if (arg.length() > 0) {
                    ans.add(arg.toString());
                    arg = new StringBuilder();
                }
            } else {
                arg.append(line.charAt(i));
            }
        }

        if (arg.length() > 0) {
            ans.add(arg.toString());
        }

        return ans;
    }
}
