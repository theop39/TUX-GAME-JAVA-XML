/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aleatoire;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Random {
    private double min;
    private double max;
    private double range;

    public Random(double min, double max) {
        setMinMax(min, max);
        if (range == 0.0) {
            range = 1.0;
        }
    }

    public void setMin(double min) {
        setMinMax(min, max);
    }

    public void setMax(double max) {
        setMinMax(min, max);
    }
    
    public void setMinMax(double min, double max) {
        if (min <= max) {
            this.min = min;
            this.max = max;
        } else {
            this.min = max;
            this.max = min;
        }
        range = (max - min);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getRange() {
        return range;
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * ((max + 1) - min)) + min);
    }
    
    
    public static String shuffle(String input){
        java.util.List<Character> characters = new ArrayList<Character>();
        
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        
        StringBuilder output = new StringBuilder(input.length());
        
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }
   
}
