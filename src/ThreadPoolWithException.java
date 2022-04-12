import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolWithException {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("main thread start");

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 15, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

        Callable<String> callable = () -> {
            throw new RuntimeException();
        };
        List<Callable<String>> callableList = new ArrayList<>();
        callableList.add(callable);
        callableList.add(callable);

        List<Future<String>> futures = null;
        try {
            futures = threadPoolExecutor.invokeAll(callableList);
        } catch (InterruptedException e) {
            System.out.println("caught InterruptedException");
            e.printStackTrace();
        }
        assert futures != null;
        for (Future<String> future : futures) {
            try {
                System.out.println(future.get());
            } catch (ExecutionException e) {
                System.out.println("caught ExecutionException");
                e.printStackTrace();
            } catch (RuntimeException e) {
                System.out.println("caught RuntimeException");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("caught Exception");
                e.printStackTrace();
            }
        }

        Thread.sleep(1000);
        threadPoolExecutor.shutdown();
        System.out.println("main thread done");
    }
}
