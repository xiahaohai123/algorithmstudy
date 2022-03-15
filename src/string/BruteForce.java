package string;

public class BruteForce {

    public static void main(String[] args) {
        BruteForce bruteForce = new BruteForce();
        String mainStr1 = "baddef";
        String pattern1 = "abc";
        System.out.println(bruteForce.match(mainStr1, pattern1));
        String mainStr2 = "aaaccc";
        String pattern2 = "ac";
        System.out.println(bruteForce.match(mainStr2, pattern2));
    }

    public boolean match(String mainStr, String pattern) {
        if (mainStr == null || pattern == null) {
            return false;
        }
        int ml = mainStr.length();
        int pl = pattern.length();
        for (int i = 0; i < ml - pl + 1; i++) {
            boolean matched = true;
            for (int j = 0; j < pl; j++) {
                if (mainStr.charAt(i + j) != pattern.charAt(j)) {
                    matched = false;
                    break;
                }
            }
            if (matched) {
                return true;
            }
        }
        return false;
    }
}

