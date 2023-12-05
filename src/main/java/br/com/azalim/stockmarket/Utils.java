package br.com.azalim.stockmarket;

public class Utils {

    /**
     * Checks if the current thread is a JUnit test. This is useful to avoid
     * singleton design pattern restrictions when unit testing.
     *
     * @return true if the current thread is a JUnit test.
     */
    public static boolean isJUnitTest() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }

}
