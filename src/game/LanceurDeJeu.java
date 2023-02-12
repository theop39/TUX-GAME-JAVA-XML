/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package game;


public class LanceurDeJeu {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //declare un jeu
        Jeu jeu;
         //Instancie un nouveau jeu
        jeu = new JeuDevineLeMotOrdre();
        //execute le jeu
        jeu.execute();
        
    }
    
}
