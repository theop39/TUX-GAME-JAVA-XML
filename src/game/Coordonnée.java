/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;


public class Coordonnée {
    private double x;
    private double y;
    private double z;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
    
      public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.x = z;
    }

    public Coordonnée() {
        this.x = 0.0;
        this.y = 0.0;
        this.x = 0.0;
    }  
      
    public Coordonnée(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Coordonnée(Coordonnée c) {
        this.x = c.x;
        this.y = c.y;
        this.z = c.z;
    }
    
    @Override
    public String toString() {
        return "("+x+","+y+","+z+")"; 
    }
    
    public Boolean isEqual(Coordonnée pos) {
        return ((pos.x == x) && (pos.y == y) && (pos.z == z));
    }
}
    
