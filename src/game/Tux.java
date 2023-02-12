/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import env3d.Env;
import env3d.advanced.EnvNode;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;

public class Tux extends EnvNode { //pour gérer le comportement des personnages 
    private Room room;
    private Env env;

    //gerere ses interactions avec l'environnement 
    public Tux(Env env, Room room) {
        
        this.room = room;
        this.env = env;
        setScale(4.0); //définir taille de tuxe
        this.setRotateY(rotateY);
        setX(room.getWidth()/2);// positionnement au milieu de la largeur de la room
        setY(getScale() * 1.1); // positionnement en hauteur basé sur la taille de Tux => gere éloignement (c.a.d par rapport à la taille)
        setZ(room.getDepth()/2); // positionnement au milieu de la profondeur de la room
        setTexture("models/tux/tux_angry.png");
        setModel("models/tux/tux.obj"); 
    }
     
    public void deplace(JeuDevineLeMotOrdre jeu) {
        
        ArrayList<Letter> lettres = jeu.getLettres();
        Boolean check =  jeu.collisionAvecUneDesLettre();
        
        if (env.getKeyDown(Keyboard.KEY_Z) || env.getKeyDown(Keyboard.KEY_UP)) {// Fleche 'haut' ou Z
        
            
        // Haut
            
            if (this.getZ()-1>0) {
                
                this.setRotateY(180); //il se retourne
                
                if (!check)
                    this.setZ(this.getZ() - 1.0);//on deplace tux selon l'axe Z
                
                check = jeu.collisionAvecUneDesLettre(); //collision avec une lettre => check == true
                
                if (check)
                    this.setZ(this.getZ() + 1.0); //si check => on repositionne tux a sa coordonnée précédente
            }
 
        } 
        if (env.getKeyDown(Keyboard.KEY_Q) || env.getKeyDown(Keyboard.KEY_LEFT)) { // Fleche 'gauche' ou Q 
            if ((this.getX()- 1) >  8) {
            
                this.setRotateY(270); //il se  tourne
                
                if(!check)
                    this.setX(this.getX() - 1.0); //on deplace tux selon l'axe X
                
                check = jeu.collisionAvecUneDesLettre();

                if(check)
                    this.setX(this.getX() + 1.0); //si check => on repositionne tux a sa coordonnée précédente
            }
            
         
        }
        if (env.getKeyDown(Keyboard.KEY_D) || env.getKeyDown(Keyboard.KEY_RIGHT)) {//Fleche 'droite' ou D
             if ((this.getX()- 1)< 92) {
                     this.setRotateY(90); //il se  tourne
                     if(!check)
                        this.setX(this.getX() + 1.0); //on deplace tux selon l'axe X
                     
                     check = jeu.collisionAvecUneDesLettre();

                     if(check)
                         this.setX(this.getX() - 1.0); //si check => on repositionne tux a sa coordonnée précédente

             }
        }
        if (env.getKeyDown(Keyboard.KEY_S) || env.getKeyDown(Keyboard.KEY_DOWN)) {//Fleche 'bas' ou S
            if (this.getZ()+ 1 < 100) {
               
                this.setRotateY(0); //il se retourne
                
                if (!check)
                    this.setZ(this.getZ() + 1.0);     //on deplace tux selon l'axe Z     
  
                check = jeu.collisionAvecUneDesLettre();
                if (check) //si check => on repositionne tux a sa coordonnée précédente
                    this.setZ(this.getZ() - 1.0);
                
            }
            
            
        }
        
    } 
}
