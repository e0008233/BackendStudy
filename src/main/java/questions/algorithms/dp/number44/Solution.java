package questions.algorithms.dp.number44;


public class Solution {
//    boolean dp[][] = new boolean[][];
    public boolean isMatch(String s, String p) {
//        System.out.println("___________________");
//        System.out.println("s: " + s + "length: " + s.length());
//        System.out.println("p: " + p);

        if (p.isEmpty()) return s.isEmpty();
        if (p.equals("*")) return true;
//
        if (s.isEmpty()) {
            for (int i=0; i <p.length(); i++){
                if (p.charAt(i)!='*') return false;
            }
            return true;
        }
        if (p.charAt(0)=='*'){
            return isMatch(s,p.substring(1)) ||
                    isMatch(s.substring(1),p) ||
                    isMatch(s.substring(1),p.substring(1));
        }else{
            return isFirstMatch(s,p) && isMatch(s.substring(1),p.substring(1));
        }

    }

    public boolean isFirstMatch(String text, String pattern){
        if (text.isEmpty()) return false;
        if (text.charAt(0)==pattern.charAt(0) || pattern.charAt(0)=='?') return true;
        return false;
    }
}