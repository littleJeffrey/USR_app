package com.Natuo;

public class DoubleCheck {

    public DoubleCheck(){

    }

    public boolean checkDegreeCondition(double degree){
        if((degree >= 0)  && ( degree<= 40)) return true;
        else return false;
    }

    public boolean checkHumityCondition(double humity){
        if((humity >= 10)  && ( humity <= 100)) return true;
        else return false;
    }

    public boolean checkNotNull(String text){
        String empty = "";
        if (!text.equals(empty)) return true;
        else return false;
    }

}