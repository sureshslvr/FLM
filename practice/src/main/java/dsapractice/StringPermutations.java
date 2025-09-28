package dsapractice;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates all permutations of a given string using backtracking. Uses List.removeLast() (Java 21+) for clean backtracking.
 */
public class StringPermutations {
    /**
     * Backtracks to build all permutations of string s.
     *
     * @param resultlist The final list holding all permutations (each as list of characters).
     * @param tempList   The current permutation being built.
     * @param s          The string to permute.
     */
    public static List<List<String>> stringBackTrace(List<List<String>> resultlist, List<String> tempList, String s) {
        // If the temporary list has reached the length of the input string, add a copy of it as a full permutation.
        if (tempList.size() == s.length()) {
            resultlist.add(new ArrayList<>(tempList));
        }
        // Iterate through each character in the input string.
        for (char c : s.toCharArray()) {
            // If the character is already in the current permutation, skip it.
            if (tempList.contains(String.valueOf(c))) { continue;   }
            // Add character to current permutation.
            tempList.add(String.valueOf(c));
            // Recursively build further.
            stringBackTrace(resultlist, tempList, s);
            // Backtrack: remove last character to try next option.
            tempList.removeLast();
        }
        return resultlist;
    }
    public static void main(String[] args) {
        String s = "ABC";
        List<List<String>> permutations = stringBackTrace(new ArrayList<>(), new ArrayList<>(), s);
        // Print results: each list contains one permutation.
        System.out.println("All permutations of \"" + s + "\":");
        for (List<String> perm : permutations) {
            System.out.println(perm);
        }
    }
}

