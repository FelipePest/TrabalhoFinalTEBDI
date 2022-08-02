package com.trabalho.models;

import java.util.ArrayList;
import java.util.Comparator;

public class Resources {

    String resources;
    ArrayList<Boolean> bool;
    double value;

    public Resources(String resource, ArrayList<Boolean> q){
        this.resources = resource;
        this.bool = q;
        this.value = 0;
    }
    
    public String getResources(){
        return resources;
    }

    public void setResoucers(String resource){
        this.resources = resource;
    }

    public ArrayList<Boolean> getBool(){
        return bool;
    }

    public void setBool(ArrayList<Boolean> b){
        this.bool = b;
    }

    public double getValue(){
        return value;
    }

    public void setValue(Double value){
        this.value = value;
    }

    public static Comparator<Resources> ResourceValueComparator = new Comparator<Resources>() {
        public int compare(Resources r1, Resources r2) {
            double value1 = r1.getValue();
            double value2 = r2.getValue();

            double result =  value2 - value1;

            if (result > 0){
                return 1;
            }else if(result == 0){
                return 0;
            }else{
                return -1;
            }
            }
         
    };
}
