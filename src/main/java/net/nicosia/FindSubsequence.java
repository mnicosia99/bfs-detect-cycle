package net.nicosia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindSubsequence {

    public static void main(String[] args) {
        // String[] Sp = {"A", "B", "F", "G", "H", "A", "B", "C", "G", "I", "H"};
        String[] S = {"A", "B", "F", "G", "H", "A", "B", "C", "G", "I", "H"};
        String[] Sp = {"B", "Z"};
        List<Integer> m = new ArrayList<>();

        int i = 0;
        int j = 0;

        for (; i < Sp.length; i++) {
            for (; j < S.length; j++) {
                if (Sp[i].equals(S[j])) {
                    m.add(j);
                    break;
                }
            }
            if (j >= S.length) {
                break;
            }
        }

        System.out.println("Sequence S " + Arrays.asList(S));
        System.out.println("Sequence S prime " + Arrays.asList(Sp));

        if (m.size() == Sp.length) {
            System.out.println("YES");
            System.out.println("Indices in sequence S where events in sequence S prime were found " + m);
        } else {
            System.out.println("NO");
        }
    }
    
}
