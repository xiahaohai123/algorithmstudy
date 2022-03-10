package dynamic_programming;


public class YangHuiTriangle {
    public static void main(String[] args) {

        int[][] testCase = new int[][]{{5}, {7, 8}, {2, 3, 4}, {4, 9, 6, 1}, {2, 7, 9, 4, 5}};
        System.out.println(yangHuiTriangle(testCase));
    }

    public static int yangHuiTriangle(int[][] matrix) {
        int n = matrix.length;
        int[][] states = new int[n][n];
        states[0][0] = matrix[0][0]; // 初始条件
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                // 左边界
                if (j == 0) states[i][0] = states[i - 1][0] + matrix[i][0];
                    // 右边界
                else if (j == matrix[i].length - 1) states[i][j] = states[i - 1][j - 1] + matrix[i][j];
                else {
                    // 保存由上到下的最短路径
                    states[i][j] = Math.min(states[i - 1][j] + matrix[i][j], states[i - 1][j - 1] + matrix[i][j]);
                }
            }
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < states[n - 1].length; i++) {
            min = Math.min(min, states[n - 1][i]);
        }
        return min;
    }

}
