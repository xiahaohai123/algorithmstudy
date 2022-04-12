import java.io.IOException;
import java.util.stream.Stream;

public class LambdaThrow {

    public static void main(String[] args) {
        try {
            Stream.iterate(0, n -> n + 1).limit(5).forEach(i -> {
                throw new RuntimeException("异常");
            });
        } catch (Exception e) {
            System.out.println("caught the exception");
            e.printStackTrace();
        }
    }
}
