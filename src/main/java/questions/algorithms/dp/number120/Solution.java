package questions.algorithms.dp.number120;


import java.util.List;

public class Solution {
    public int minimumTotal(List<List<Integer>> triangle) {
        int minPath = Integer.MAX_VALUE;
        int row = triangle.size();
        int column = triangle.get(row-1).size();
        Integer[][] result = new Integer[row][column];

        for (int i =0; i<row; i++){
            for (int j = 0; j<i+1;j++){
                if (i==0){
                    result[i][j] = triangle.get(i).get(j);
                }else{
                    int columnBefore = j-1;
                    if (columnBefore<0) columnBefore = 0;
                    int columnAfter = j;
                    if (columnAfter>triangle.get(i-1).size()-1) columnAfter=triangle.get(i-1).size()-1;

                    result[i][j] = Math.min(result[i-1][columnAfter], result[i-1][columnBefore]) + triangle.get(i).get(j);
                }
            }
        }
        for (int i = 0; i <result[row-1].length; i ++){
            if (result[row-1][i]!=null&&result[row-1][i]<minPath) minPath=result[row-1][i];
        }
        return minPath;
    }
}