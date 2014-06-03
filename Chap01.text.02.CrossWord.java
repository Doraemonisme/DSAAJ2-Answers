//Chap01.text.02.CrossWord.java

import java.util.*;

public class Solution {
    private static final int MULTIPLIER = 10000;

    public static void main(String[] args) {
        String[] words = new String[]{
                "this", "wats", "o", "thh",
                "z", "twah", "taht", "twahi",
                "shgof", "twof", "", "gaah",
                "twofgaah", "taagdh", "tahtgs", "ffffffffff"};

        char[][] matrix = new char[][]{
                {'t', 'h', 'i', 's'},
                {'w', 'a', 't', 's'},
                {'o', 'a', 'h', 'g'},
                {'f', 'g', 'd', 't'}
        };
        /*
        int m = 10000, n = 10000;
        char[][] matrix = new char[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = (char) ('a' + new Random().nextInt(26));
            }
        }*/
        Solution solution = new Solution();
        for (String s : words) {
            System.out.println(s + " : " + solution.containsWord(matrix, s));
        }
    }

    public boolean containsWord(char[][] matrix, String word) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                Set<Integer> used = new HashSet<>();
                if (bfs(matrix, i, j, word, used)) return true;
            }
        }
        return false;
    }

    private boolean bfs(char[][] matrix, int i, int j, String word, Set<Integer> used) {
        if (word.isEmpty()) return true;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(i * MULTIPLIER + j);
        int k = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            boolean found = false;
            for (int l = 0; l < size; l++) {
                int v = queue.poll();
                int ii = v / MULTIPLIER;
                int jj = v % MULTIPLIER;
                if (word.charAt(k) == matrix[ii][jj]) {
                    used.add(v);//bug here! some nodes added here can lead to dead ends
                                // and need to be removed from the visited set later on
                    found = true;
                    for (int a = ii - 1; a <= ii + 1; a++) {
                        for (int b = jj - 1; b <= jj + 1; b++) {
                            if (a >= 0 && a < matrix.length && b >= 0 && b < matrix[0].length
                                    && !used.contains(a * MULTIPLIER + b)) {
                                queue.add(a * MULTIPLIER + b);
                            }
                        }
                    }
                }
            }
            if (!found) return false;
            k++;
            if (k == word.length()) return true;
        }
        return false;
    }
}
