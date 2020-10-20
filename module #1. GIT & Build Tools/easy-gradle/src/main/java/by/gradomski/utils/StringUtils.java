package by.gradomski.utils;

import org.apache.commons.lang3.math.NumberUtils;

public class StringUtils {
    private static final double ZERO = 0.0;

    public boolean isPositiveNumber(String str){
        if(NumberUtils.isCreatable(str)){
            Number number = NumberUtils.createNumber(str);
            double value = number.doubleValue();
            return Double.compare(value, ZERO) > 0;
        }
        return false;
    }
}