package pe.edu.uni.fiis.so.util;

import java.util.List;

/**
 * Created by vcueva on 6/21/17.
 */
public class TimeParser {

    private static String[] sizes = {"ms", "s", "m", "h", "d"};
    private static int[] div = {1000, 60, 60, 24, 1000};

    public static long parse(List<String> args) {
        long ans = 0;
        for (String arg : args) {
            ans += parse(arg);
        }
        return ans;
    }

    public static long parse(String line) {
        line = line.trim().toLowerCase();
        if (line.length() < 2) {
            throw new RuntimeException("Invalid format");
        }

        int i = line.length() - 1;
        while (i >= 0 && line.charAt(i) > '9') {
            i--;
        }

        long ans = Long.parseLong(line.substring(0, i + 1));
        String sz = line.substring(i + 1);
        for (int j = 0; j < sizes.length; j++) {
            if (sz.equals(sizes[j])) {
                break;
            }
            ans *= div[j];
        }

        return ans;
    }

    public static String toString(long milliseconds) {
        StringBuilder ans = new StringBuilder();
        int x, p = 0;
        while (milliseconds > 0) {
            x = (int) (milliseconds % div[p]);
            if (x > 0) {
                if (ans.length() > 0) {
                    ans.insert(0, " ");
                }
                ans.insert(0, x + sizes[p]);
            }
            milliseconds /= div[p];
            p++;
        }

        return ans.toString();
    }

    public static String format(long milliseconds) {
        int ms = (int) (milliseconds % 1000);
        milliseconds /= 1000;
        int s = (int) (milliseconds % 60);
        milliseconds /= 60;
        int m = (int) (milliseconds % 60);
        int h = (int) (milliseconds / 60);
        return String.format("%02d:%02d:%02d:%03d", h, m, s, ms);
    }
}
