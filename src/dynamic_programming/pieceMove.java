package dynamic_programming;

public class pieceMove {
    public static void main(String[] args) {
        int[][] testCase = new int[][]{
                {1, 3, 5, 9},
                {2, 1, 3, 4},
                {5, 2, 6, 7},
                {6, 8, 4, 3}
        };
        System.out.println(leastLength(testCase));
    }

    public static int leastLength(int[][] martix) {
        int n = martix.length;
        int[][] states = new int[2 * (n - 1)+1][n];
        states[0][0] = martix[0][0];
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i + 1; j++) {
                if (j == 0) {
                    states[i][j] = states[i - 1][j] + martix[i][0];
                } else if (j == i) {
                    states[i][j] = states[i - 1][j - 1] + martix[0][j];
                } else {
                    states[i][j] = Math.min(states[i - 1][j], states[i - 1][j - 1]) + martix[i - j][j];
                }
            }
        }
        for (int i = n; i <= 2 * (n - 1); i++) {
            // 矩阵变幻数
            int i1 = i - n + 1;
            for (int j = 0; j < n - i1; j++) {
                states[i][j] = Math.min(states[i - 1][j], states[i - 1][j + 1]) + martix[i - j - i1][j + i1];
            }
        }
        return states[2 * (n - 1)][0];
    }
}
