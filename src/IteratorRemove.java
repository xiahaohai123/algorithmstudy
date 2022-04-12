import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IteratorRemove {
    public static void main(String[] args) {
        List<Integer> list = Stream.iterate(0, n -> n + 1).limit(5).collect(Collectors.toList());
        list.forEach(System.out::println);

        System.out.println();

        list.removeIf(next -> next == 3);
        list.forEach(System.out::println);
    }
}
