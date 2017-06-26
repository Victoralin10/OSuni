package pe.edu.uni.fiis.so.util;

import java.util.List;

/**
 * Created by vcueva on 6/21/17.
 */
public class SizeParser {

    private static String sizes = "KMGT";

    public static long parse(List<String> args) {
        long ans = 0;
        for (String arg : args) {
            ans += parse(arg);
        }
        return ans;
    }

    public static long parse(String line) {
        line = line.trim().toUpperCase();
        if (line.length() < 2) {
            throw new RuntimeException("Invalid format");
        }

        if (line.charAt(line.length() - 1) != 'B') {
            throw new RuntimeException("Invalid format");
        }

        String n;
        int p = 0;
        if (line.charAt(line.length() - 2) <= '9') {
            n = line.substring(0, line.length() - 1);
        } else {
            n = line.substring(0, line.length() - 2);
            p = sizes.indexOf(line.charAt(line.length() - 2));
            if (p == -1) {
                throw new RuntimeException("Invalid format");
            } else {
                p++;
            }
        }
        if (n.isEmpty()) {
            throw new RuntimeException("Invalid format");
        }

        return Long.parseLong(n)*(1L<<(p*10));
    }

    public static String toString(long size) {
        StringBuilder ans = new StringBuilder();
        int p = 0;
        long x;
        while (size > 0) {
            x = size % (1<<10);
            if (x > 0) {
                if (ans.length() > 0) {
                    ans.insert(0, " ");
                }
                if (p > 0) {
                    ans.insert(0, x + "" + sizes.charAt(p - 1) + "b");
                } else {
                    ans.insert(0, x + "b");
                }
            }
            size >>= 10;
            p++;
        }
        return ans.toString();
    }
}
