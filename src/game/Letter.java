/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import env3d.Env;
import env3d.advanced.EnvNode;
import java.security.InvalidParameterException;


public class Letter extends EnvNode {
    private Room room;
    private Env env;
    private char l;
    
    public Letter(Env env, Room room, double x, double y, char l) throws InvalidParameterException {
        this.room = room;
        this.env = env;
        setScale(3); //définir taille de la lettre
        setX(x);
        setY(getScale() * 1.1);
        setZ(y);
        
        if ((l >= 'a' && l <= 'z') || (l >= 'A' && l <= 'Z') || l == '-') {
            setTexture("models/letter/" + l + ".png");
            setL(l);
            
        } else {
           throw new InvalidParameterException("Attention: caractère invalide.");
        }
        
        setModel("models/letter/cube.obj"); 
       
    }

    public char getCaractereDeLaLettre() {
        return l;
    }

    public char getL() {
        return l;
    }
    
    private void setL(char l) {
        this.l = l;
    }
    
    
}
