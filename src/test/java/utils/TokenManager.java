package utils;

public class TokenManager {
    // ThreadLocal to hold token per thread
    private static ThreadLocal<String> token = new ThreadLocal<>();

    // Set token for current thread
    public static void setToken(String t) {
        token.set(t);
    }

    // Get token for current thread
    public static String getToken() {
        return token.get();
    }

    // Clear token for thread (optional cleanup)
    public static void clearToken() {
        token.remove();
    }
}

