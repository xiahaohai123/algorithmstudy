package string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * BM算法
 * https://time.geekbang.org/column/article/71525
 * 先使用坏字符规则，如果坏字符规则失效了再使用好后缀规则，好后缀规则会在第一次匹配就出现坏字符的时候失效
 */
public class BoyerMoore {
    /** 字符集包含字符数量 */
    private static final int SIZE = 256;

    public static void main(String[] args) {
        List<TestCase> testList = new ArrayList<>();
        testList.add(new TestCase("abcacabdc", "abd", 5));
        testList.add(new TestCase("aaaaaabcaaaaabcaaaaaaaaaa", "caaaaabc", 7));

        BoyerMoore boyerMoore = new BoyerMoore();

        for (TestCase testCase : testList) {
            int index = boyerMoore.match(testCase.mainStr, testCase.pattern);
            if (!testCase.want.equals(index)) {
                System.out.println("test failed: " + testCase.mainStr + "\t" + testCase.pattern + "\twant: " + testCase.want + "\tgot: " + index);
            }
        }
    }

    public int match(String mainStr, String pattern) {
        return bmFind(mainStr.toCharArray(), pattern.toCharArray());
    }

    private int bmFind(char[] mainString, char[] pattern) {
        int[] hashTable = generateBCHashTable(pattern);
        int pLength = pattern.length;
        int[] suffix = new int[pLength];
        boolean[] prefix = new boolean[pLength];
        generateGoodSuffixTable(pattern, pLength, suffix, prefix);
        int endIndex = mainString.length - pLength + 1;

        for (int i = 0; i < endIndex; ) {
            int j = pattern.length - 1;
            for (; j >= 0; j--) {
                if (pattern[j] != mainString[i + j]) {
                    char badChar = mainString[i + j];
                    int rollLengthByBC = moveByBadChar(j, badChar, hashTable);
                    int rollLengthByGS = 0;
                    if (j < pLength - 1) { // 好后缀需要存在才能后滚，不然会直接返回pLength
                        rollLengthByGS = moveByGS(j, pLength, suffix, prefix);
                    }

                    i += Math.max(rollLengthByBC, rollLengthByGS);
                    break;
                }
            }
            // 全匹配到了j会变成-1
            if (j < 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 构建BC算法的模式串hash表，提高坏字符匹配效率
     * @param pattern 模式串
     * @return hash表 下标为字符对应的ascii值，值为该字符最后一次出现在模式串中的index
     */
    private int[] generateBCHashTable(char[] pattern) {
        int[] hashTable = new int[256];
        Arrays.fill(hashTable, -1);
        for (int i = 0; i < pattern.length; i++) {
            int ascii = pattern[i];
            hashTable[ascii] = i;
        }
        return hashTable;
    }

    /**
     * 坏字符匹配规则
     * 有可能出现向前滚动的问题
     * @param mainString 主串
     * @param pattern    模式串
     * @return 匹配到的index，没有匹配到则返回-1
     */
    public int badChar(char[] mainString, char[] pattern) {
        int[] hashTable = generateBCHashTable(pattern);
        int endIndex = mainString.length - pattern.length + 1;
        // 自增交给滚动
        for (int i = 0; i < endIndex; ) {
            int j = pattern.length - 1;
            for (; j >= 0; j--) {
                if (pattern[j] != mainString[i + j]) {
                    char badChar = mainString[i + j];
                    int rollLength = moveByBadChar(j, badChar, hashTable);
                    if (rollLength <= 0) {
                        i++;
                        break;
                    }
                    i += rollLength;
                    break;
                }
            }
            // 全匹配到了j会变成-1
            if (j == -1) {
                return i;
            }
        }
        return -1;
    }

    private int moveByBadChar(int j, char badChar, int[] hashTable) {
        int xi = hashTable[badChar];
        return j - xi;
    }

    /**
     * 好后缀匹配规则
     * mainStr = aaaaaabcaaaaabcaaaaaaaaaa
     * pattern = caaaaabc
     * m = 8
     * j = 5
     * r = j + 2 = 7
     * @param mainStr 主串
     * @param pattern 模式串
     * @return 匹配到的index，没有匹配到则返回-1
     */
    public int goodSuffix(char[] mainStr, char[] pattern) {
        int pLength = pattern.length;
        int[] suffix = new int[pLength];
        boolean[] prefix = new boolean[pLength];
        generateGoodSuffixTable(pattern, pLength, suffix, prefix);
        int endIndex = mainStr.length - pLength + 1;
        for (int i = 0; i < endIndex; ) {
            int j = pLength - 1;
            while (j >= 0) {
                if (pattern[j] != mainStr[i + j]) {
                    if (j < pLength - 1) {
                        i += moveByGS(j, pLength, suffix, prefix);
                    } else {
                        i++;
                    }
                    break;
                }
                j--;
            }
            if (j == -1) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据好后缀规则移动，返回需要移动的步数
     * @param j      坏字符出现的位置
     * @param length 模式窜的长度
     * @param suffix 好后缀的后缀映射表
     * @param prefix 前缀存在表
     * @return 需要移动的步数
     */
    private int moveByGS(int j, int length, int[] suffix, boolean[] prefix) {
        int suffixLength = length - j - 1;
        if (suffix[suffixLength] != -1) {
            return j - suffix[suffixLength] + 1;
        }
        for (int k = suffixLength - 1; k > 0; k--) {
            if (prefix[k]) {
                return length - k;
            }
        }
        return length;
    }

    /**
     * 预处理模式串以创建好后缀映射表
     * @param pattern 模式串
     * @param length  模式串长度
     * @param suffix  后缀表
     * @param prefix  前缀表，表示是否存在这个前缀
     */
    private void generateGoodSuffixTable(char[] pattern, int length, int[] suffix, boolean[] prefix) {
        for (int i = 0; i < length; i++) {
            suffix[i] = -1;
            prefix[i] = false;
        }

        for (int i = 0; i < length - 1; i++) {
            int j = i;
            int publicSuffixLength = 0;
            // 从模式串的头部、从子串的尾部开始匹配
            while (j >= 0 && pattern[j] == pattern[length - 1 - publicSuffixLength]) {
                // 标记最后一个公共后缀子串的起始下标
                publicSuffixLength++;
                suffix[publicSuffixLength] = j;
                j--;
            }
            if (j == -1) {
                // 匹配到了头部，则表示该后缀子串也是模式串的前缀子串
                prefix[publicSuffixLength] = true;
            }
        }
    }
}
