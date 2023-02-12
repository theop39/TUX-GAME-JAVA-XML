/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.util.ArrayList;

public class JeuDevineLeMotOrdre extends Jeu {
    private Chronometre chrono;
    private int nbLettresRestantes;
    
    public JeuDevineLeMotOrdre() {
        super();
    }

    @Override
    protected void démarrePartie(Partie partie) {
        this.chrono = new Chronometre(600);
        this.chrono.start();
        
        this.nbLettresRestantes = mot.length();
       
    }

    @Override
    protected void appliqueRegles(Partie partie) {  
        
        partie.setTrouvéAvecNbLettresRestantes(this.nbLettresRestantes);
        
        if (!this.chrono.remainsTime()) {//on a dépassé la limite de temps !
            this.chrono.stop();
            this.partieTerminee = true;
        } else if (this.nbLettresRestantes == 0) {    
            this.partieTerminee = true;
        }
    }

    @Override
    protected void terminePartie(Partie partie) {
        //stop chrono si il reste du temps
        
        if (this.chrono.remainsTime()) {
            this.chrono.stop();
        }
        
         partie.setTrouvéAvecNbLettresRestantes(this.nbLettresRestantes);
         partie.setTemps(this.chrono.getSeconds());
        
     
    }
  
    
    @Override
    protected void tuxTrouveLettre(Letter lettre) {
       //doit etre appelé a chaque collision
       //si la fonction est appelée alors il y a forcement eu une collision !!!
        
       if (lettre.getL() == mot.charAt(0)) {//la lettre en collision correspond a la premiere des lettres qu'il reste a determiner
         
           
           this.env.removeObject(lettre);//on retire la lettre de l'environnement via sa reference 
           this.getLettres().remove(lettre);  //on retire la lettre de la liste des lettres via sa reference
           this.mot = this.mot.substring(1);
           
           this.nbLettresRestantes--; 
          
           
       }
  
 
    }
    
    
}
