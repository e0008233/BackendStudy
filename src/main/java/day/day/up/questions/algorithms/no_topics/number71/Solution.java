package day.day.up.questions.algorithms.no_topics.number71;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


//https://cloud.tencent.com/developer/article/1660069
public class Solution {
    public String simplifyPath(String path) {
        String[] paths = path.split("/");
        List<String> directories = new ArrayList<>();

        for (String directory:paths){
            if (directory.equals("")) continue;
            if (directory.equals(".")) continue;
            else if (directory.equals("..")){
                if (directories.isEmpty()) continue;
                else directories.remove(directories.size()-1);
            }
            else{
                directories.add(directory);
            }
        }

        String ans="";
        if (directories.isEmpty()) return "/";

        for (int i=0;i<directories.size();i++){
            ans = ans+"/"+directories.get(i);
        }

        return ans;
    }
}