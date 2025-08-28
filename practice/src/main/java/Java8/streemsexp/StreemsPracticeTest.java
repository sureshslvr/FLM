package Java8.streemsexp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreemsPracticeTest {

    public static void main(String[] args) {
        List<Integer> numbers= new ArrayList<>(Arrays.asList(10,20,30,22));
        List<Integer> plus=numbers.stream().map((i)->i+1).collect(Collectors.toList());
        List<Integer> plus1=numbers.stream().map((i)->i+1).toList();
        System.out.println(numbers);
        System.out.println(plus);
        System.out.println(plus1);
    }
}
