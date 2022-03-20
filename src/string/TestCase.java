package string;

public class TestCase {
    String mainStr;
    String pattern;
    Object want;

    public TestCase(String mainStr, String pattern, Object want) {
        this.mainStr = mainStr;
        this.pattern = pattern;
        this.want = want;
    }
}
