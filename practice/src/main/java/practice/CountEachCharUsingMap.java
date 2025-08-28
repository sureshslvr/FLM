package practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CountEachCharUsingMap {
    public static void main(String[] args) {

        ArrayList<Character> arrayList = new ArrayList<>(Arrays.asList('A','B','C','D','B','C','E'));

        HashMap<Character,Integer> result= new HashMap<>();

        for (Character c : arrayList) {
            if(result.containsKey(c)){
                Integer count=result.get(c);
                result.put(c,count+1);
            }
            else {
                result.put(c,1);
            }
        }
        System.out.println(result);



     }
}
