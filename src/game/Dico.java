/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import aleatoire.Random;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.IOException;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.logging.*;


/**
 *
 * @author Theo Patrac, Bastien Riado
 */
public class Dico {
    
    //liste des mots par niveau
    public ArrayList<String> listeNiveau1;
    public ArrayList<String> listeNiveau2;
    public ArrayList<String> listeNiveau3;
    public ArrayList<String> listeNiveau4;
    public ArrayList<String> listeNiveau5;
    public String cheminFichierDico; //src/xml/dico.xml 
    
    public Dico(String cheminFichierDico) {
            this.cheminFichierDico = cheminFichierDico;
            
            listeNiveau1 = new ArrayList<String>();
            listeNiveau2 = new ArrayList<String>();
            listeNiveau3 = new ArrayList<String>();
            listeNiveau4 = new ArrayList<String>();
            listeNiveau5 = new ArrayList<String>();
    }
    
    //avoir un mot d'un niveau aléatoirement 
    public String getMotDepuisListeNiveaux(int niveau){
        
        switch (this.vérifieNiveau(niveau)) {
            case 1:
                return getMotDepuisListe(listeNiveau1);
            case 2:
                return getMotDepuisListe(listeNiveau2);
            case 3:
                return getMotDepuisListe(listeNiveau3);
            case 4:
                return getMotDepuisListe(listeNiveau4);
            case 5:
                return getMotDepuisListe(listeNiveau5);
            default:
                return "defaut";
        }
    }
    
    //ajoute un mot dans une des listes, selon le niveau
    public void ajouteMotADico(int niveau, String mot) {
        mot = this.vérifieMot(mot);
        niveau = this.vérifieNiveau(niveau);
        
           switch (niveau) {
            case 1:
                if (!listeNiveau1.contains(mot)) {
                    listeNiveau1.add(mot);    
                }
                break;
            case 2:
                 if (!listeNiveau2.contains(mot)) {
                    listeNiveau2.add(mot);    
                }
                 break;
            case 3:
                 if (!listeNiveau3.contains(mot)) {
                    listeNiveau3.add(mot);    
                 }
                 break;
            case 4:
                if (!listeNiveau4.contains(mot)) {
                    listeNiveau4.add(mot);    
                }
                break;
            case 5:
                if (!listeNiveau5.contains(mot)) {
                    listeNiveau5.add(mot);    
                }
                 break;
        }
    }
    
 
    public String getCheminFichierDico() {
        return this.cheminFichierDico;
    }
               

    //vérifie si le niveau est correct
    private int vérifieNiveau(int niveau) {
        
        if (niveau >= 1 && niveau <= 5) {//cas correst
            return niveau;
        } else {
            //niveau incorrect => on genere un nombre entre 1 et 5
            int niveauRandom = Random.getRandomNumber(0, 5);
        
            return niveauRandom;
        }
    }
    
    private void viderListes() {
        this.listeNiveau1.clear();
        this.listeNiveau2.clear();
        this.listeNiveau3.clear();
        this.listeNiveau4.clear();
        this.listeNiveau5.clear();

    }
    
    
    private String vérifieMot(String mot) {
        //a completer
        
        return mot;
    } 
                   

    private String getMotDepuisListe(ArrayList<String> list) {
        if (!list.isEmpty()) {//la liste a au moins un élémént
            int nbElements = list.size();
            
           if (nbElements == 1) { //cas un element
                return list.get(0);
            } else { //cas plus de 1 élément
                
                int indiceAleatoire  = Random.getRandomNumber(0, nbElements - 1);
                
                return list.get(indiceAleatoire);
            }
        } 
        else { //cas la liste est vide 
            return null; 
        }
    }
    
     //cette méthode va parser le fichier dico.xml et charger initialiser les 5 liste de mot via DOM
    public void lireDictionnaireDOM(String path) throws SAXException, IOException {
        // crée un parser de type DOM
        DOMParser parser = new DOMParser();
        
        // parse le document XML correspondant au fichier filename dans le chemin path

           
        parser.parse(path + this.cheminFichierDico);
        Document doc = parser.getDocument();
        
        
        // récupère la liste des éléments nommés niveau dans le xml
        NodeList niveauxListe = doc.getElementsByTagName("niveau");
        
        this.viderListes();
        
        //on va parcours chaque niveau ici
        for (int i = 0; i < niveauxListe.getLength(); i++) {
            //on recupere la liste des mots
            
            Element niveau = (Element) niveauxListe.item(i); //element contient un niveau
            
            //on recupere la difficulté pour chaque niveau
            String difficultee = niveau.getAttribute("difficultee");
            
           
            int niveauCourrant = Integer.parseInt(difficultee);               
           
            
            //liste qui contient tous les mots 
            NodeList motsListe = niveau.getElementsByTagName("mot");
            
            //on parcours tous les mots 

            for (int j = 0; j < motsListe.getLength(); j++) {
                Element motNode = (Element) motsListe.item(j); //on recupere chaque mot
                
                
                //on recupere la valeur de chaque mot
                String mot = motNode.getTextContent();
                
                //on ajoute le mot au dictionnaire
                this.ajouteMotADico(niveauCourrant, mot);
            }
        } 
       
    }
    
    //cette méthode va parser le fichier dico.xml et charger initialiser les 5 liste de mot via SAX
    public void lireDictionnaireSAX() {
            
            this.viderListes();
       
            
            try{ 
			// création d'une fabrique de parseurs SAX 
			SAXParserFactory fabrique = SAXParserFactory.newInstance(); 
  
			// création d'un parseur SAX 
			SAXParser parseur = fabrique.newSAXParser(); 
  
			// lecture d'un fichier XML avec un DefaultHandler 
			File fichier = new File("src/xml/dico.xml"); 
			DefaultHandler gestionnaire = new NiveauHandler(this); 
			parseur.parse(fichier, gestionnaire); 
  
		}catch(ParserConfigurationException ex){ 
			Logger lgr = Logger.getLogger(Dico.class.getName());
                        lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}catch(SAXException ex){ 
			Logger lgr = Logger.getLogger(Dico.class.getName());
                        lgr.log(Level.SEVERE, ex.getMessage(), ex); 
		}catch(IOException ex){ 
			Logger lgr = Logger.getLogger(Dico.class.getName());
                        lgr.log(Level.SEVERE, ex.getMessage(), ex); 
		}
        }
}
