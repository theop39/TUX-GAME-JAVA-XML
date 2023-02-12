/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

public class Chronometre {
    private long begin;
    private long end;
    private long current;
    private int limite;

    public Chronometre(int limite) {
        this.begin = 0;
        this.end = 0;
        this.current = 0;
        this.limite = limite;
    }
    
    //lancer le chrono
    public void start(){
        this.begin = System.currentTimeMillis();
    }
    //arrété le chrono
    public void stop(){
        this.end = System.currentTimeMillis();
    }
    
    public long getTime() {
        return System.currentTimeMillis();
    }
 
    
    //temps écoulé en ms
    public long getMilliseconds() {
        return this.end - this.begin;
    }
 
    //temps écoulé en seconde
    public int getSeconds() {
        return (int) ((end - begin) / 1000.0);
    }
 
    //temps écoulé en minute
    public double getMinutes() {
        return (end - begin) / 60000.0;
    }
 
    //temps écoulé en heure
    public double getHours() {
        return (end - begin) / 3600000.0;
    }
    

    //indique si la limite a été dépassé ou non
    public boolean remainsTime() {
        
        current = (long) (System.currentTimeMillis() / 1000.0);
        
        
        return (current - (begin / 1000.0) <= this.limite);
    }
}
