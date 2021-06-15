package day.day.up.questions.algorithms.data_structure.array.number240;


//直接使用双指针也是可以的，i指向0，j指向列数，这样第一个被验证的数就是二维数组右上角的数字，假如这个数字等于 target，直接返回 true；
// 若大于 target，说明要减小数字，则列数j自减1；若小于 target，说明要增加数字，行数i自增1。若 while 循环退出了还是没找到 target，直接返回 false 即可
class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {
        int row = 0;
        int col = matrix[0].length-1;
        while(row<matrix.length && col>=0){
            if (matrix[row][col]==target) return true;
            if (matrix[row][col]<target) row++;
            else if (matrix[row][col]>target) col--;

        }

        return false;
    }
}