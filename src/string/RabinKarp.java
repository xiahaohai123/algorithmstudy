package string;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RK算法demo
 * 在BF算法上引入hash
 * 此demo仅使用小写字母，所以使用26进制
 */
public class RabinKarp {

    static class TestCase {
        String mainStr;
        String pattern;
        Object want;

        public TestCase(String mainStr, String pattern, Object want) {
            this.mainStr = mainStr;
            this.pattern = pattern;
            this.want = want;
        }
    }

    public static void main(String[] args) {
        List<TestCase> testCaseList = new ArrayList<>();
        testCaseList.add(new TestCase("abc", "bc", true));
        testCaseList.add(new TestCase("ab", "c", false));
        testCaseList.add(new TestCase("dbcede", "cede", true));
        testCaseList.add(new TestCase("dbcede", "a", false));

        RabinKarp rabinKarp = new RabinKarp();
        for (TestCase testCase : testCaseList) {
            boolean result = rabinKarp.matchLowercase(testCase.mainStr, testCase.pattern);
            if (!testCase.want.equals(result)) {
                System.out.println("test failed: " + testCase.mainStr + "\t" + testCase.pattern);
            }
        }
    }

    private Map<Integer, List<Long>> table = new HashMap<>();

    /**
     * 假设pattern的长度为m，主串中子串的起始位置为i
     * h[i] = 子串S[i,i+m-1]的hash
     * h[i-1] = 子串S[i-1,i+m-2]的hash
     * @param mainStr 主串
     * @param pattern 模式串
     * @return 是否匹配
     */
    public boolean matchLowercase(String mainStr, String pattern) {
        if (mainStr == null) return false;
        int length = pattern.length();
        if (length <= 0) return true;
        int mLength = mainStr.length();
        if (mLength < length) return false;

        String s0 = mainStr.substring(0, length);
        char lastChar = s0.charAt(0);
        long lastHash = hash(s0);
        long hp = hash(pattern);
        if (hp == lastHash) return true;

        int end = mLength - length + 1;

        for (int i = 1; i < end; i++) {
            String si = mainStr.substring(i, i + length);
            long hi = deriveHash(lastHash, lastChar, si.charAt(length - 1), length);
            if (hi == hp) {
                return true;
            }
            lastHash = hi;
            lastChar = si.charAt(0);
        }

        return false;
    }

    /**
     * hash算法类似于2进制转换为10进制的算法，不过在这里使用的是26进制转换为10进制的算法
     * @param str 被hash的字符串
     * @return hash值
     */
    private long hash(String str) {
        int length = str.length();
        int hash = 0;
        for (int i = 0; i < length; i++) {
            int num = str.charAt(i) - 'a';
            hash += pow(26, length - i - 1) * num;
        }
        return hash;
    }

    /**
     * 递推hash，基于hash算法，可以由h0递推出h1
     * @param lastHash 上一个hash值
     * @param lastChar 上一个hash值递推本hash需要去除的字符
     * @param nextChar 上一个hash值递推本hash需要新增的字符
     * @param length   子串长度
     * @return 本hash
     */
    private long deriveHash(long lastHash, char lastChar, char nextChar, int length) {
        return (lastHash - pow(26, length - 1) * (lastChar - 'a')) * 26 + (nextChar - 'a');
    }

    private long pow(int a, int b) {
        List<Long> results = table.computeIfAbsent(a, k -> new ArrayList<>());
        while (results.size() <= b) {
            results.add(null);
        }
        if (results.get(b) != null) {
            return results.get(b);
        }
        if (b == 0) return 1;
        long result = a;
        for (int i = 1; i < b; i++) {
            result *= a;
        }
        results.set(b, result);
        return result;
    }
}
