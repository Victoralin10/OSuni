package pe.edu.uni.fiis.so.util;

/**
 * Created by vcueva on 6/24/17.
 */
public class Lib {

    public static void assertTrue(boolean expresion) {
        if (!expresion)
            throw new AssertionError();
    }
}
