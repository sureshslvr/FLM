package dsapractice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CountEvenAndOddNumbersInAnArray {
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        System.out.println("enter size of the array");
        int size= sc.nextInt();
        int[] numbers= new int[size];
        System.out.println("enter " +size+" elements one by one to add in array");
        for(int i=0;i<size;i++){
            numbers[i]=sc.nextInt();
        }
        //int[] numbers = {10, 21, 4, 7, 18, 13};
        /*Expected output:
        Even count: 3
        Odd count: 3 */
        //countNumbers(numbers);
        //countEvenOddNumbersUsingJava8(numbers);
        countEvenOddNumbersUsingAList(numbers);
    }
    public static void countNumbers(int[] nums){
        int even = 0,odd = 0;
        for (int i:nums) {
            if(i%2==0){
                even++;
            }else {
                odd++;
            }
        }
        System.out.println("Even count: "+even);
        System.out.println("Odd count: "+odd);
    }

    public static void countEvenOddNumbersUsingJava8(int[] nums){
        long even = Arrays.stream(nums).filter(i->i%2==0).count();
        long odd = Arrays.stream(nums).filter(i->i%2!=0).count();
        System.out.println("Even count: "+even);
        System.out.println("Odd count: "+odd);
    }

    public static void countEvenOddNumbersUsingAList(int[] nums) {
        List<Integer> numbers = new ArrayList<>();
        for (int i : nums) {
            numbers.add(i);
        }

        long even = numbers.stream().filter(i -> i % 2 == 0).count();
        long odd = numbers.stream().filter(i -> i % 2 != 0).count();

        List<Integer> evenList = numbers.stream().filter(i -> i % 2 == 0).toList();
        List<Integer> oddList = numbers.stream().filter(i -> i % 2 != 0).toList();

        System.out.println("Even count: " + even);
        System.out.println("Even numbers: " + evenList);
        System.out.println("Odd count: " + odd);
        System.out.println("Odd numbers: " + oddList);
    }


}