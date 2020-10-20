package by.gradomski.util;

import by.gradomski.utils.StringUtils;

public class Utils {
    public boolean isAllPositiveNumbers(String... str){
        String[] strArray = str;
        StringUtils stringUtils = new StringUtils();
        for(String s: strArray){
            if(!stringUtils.isPositiveNumber(s)){
                System.out.println(s + " negative!");
                System.out.println("end...");
                return false;
            }
            System.out.println(s + " positive");
        }
        return true;
    }
}