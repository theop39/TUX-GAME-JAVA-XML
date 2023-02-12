/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;


import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class Partie {
    private final String date;
    private final String mot;
    private final int niveau;
    private int trouvé;
    private int temps; 
    
    public Partie(String date, String mot, int niveau) {
        this.date = date;
        this.mot = mot;
        this.niveau = niveau;
    }
    
   public Element getPartie(Document doc) {
       return null;
   }

    public void setTrouvéAvecNbLettresRestantes(int nbLettresRestantes) {
        double res;
      
        res = (double) this.mot.length() - nbLettresRestantes;
      
        res /= this.mot.length();

        res *= 100.0;
        
        trouvé = (int) res;
    }
    
    public void setTrouvé(int trouvé) {
        this.trouvé = trouvé;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }

    public int getNiveau() {
        return niveau;
    }

    public String getDate() {
        return date;
    }

    public int getTrouvé() {
        return trouvé;
    }

    public int getTemps() {
        return temps;
    }

    public String getMot() {
        return mot;
    }
    
    
     
    @Override
    public String toString(){
        return "- date: " + date + " ,mot: " + mot + ",niveau: " + niveau + " ,trouvé: " + trouvé + " ,temps: " + temps;
        
    }
    
}
