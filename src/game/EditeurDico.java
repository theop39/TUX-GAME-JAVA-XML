/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.FileOutputStream;
import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author Theo Patrac
 */
public class EditeurDico {
    private Document doc;
  
    
    public EditeurDico() {
    }
    
    //cette méthode va parser le fichier dico.xml et initialiser l'attribut doc (doc vaut alors l'ensemble du doc xml) 
    private void lireDOM(String fichier) throws SAXException, IOException {
        DOMParser parser = new DOMParser();

        
        // parse le document XML correspondant au fichier filename dans le chemin pa
        parser.parse("src/xml/" + fichier);
        
          this.doc = parser.getDocument();
    }
    
    //ecrire dans le fichier le XML correspond à ce qui est stockée en mémoire par l'attribut doc
    private void ecrireDOM(String fichier) {
        
        try {
            FileOutputStream output =  new FileOutputStream("src/xml/" + fichier);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(this.doc);
            StreamResult result = new StreamResult(output);

            transformer.transform(source, result);
        } catch(Exception e) {
            System.err.println("Erreur, ecrireDOM() : lors de l'écriture dans le fichier" + "src/xml/" + fichier);
        }
    }
    
    //ajouter un mot dans le dictionnaire 
    public boolean ajouterMot(String mot, double niveau)  {
        
        if (isValidWordForDico(niveau, mot)) {
            
            //lecture du DOM
            
            try {
                lireDOM("dico.xml");
            } catch(Exception e) {
                System.err.println("lireDOM() : Erreur lors de l'ouverture du dictionnaire dico.xml" );
            }
                      
            //on recupere la liste des niveaux
            NodeList listeNoeuds = doc.getElementsByTagName("niveau");            
            
            //on recupere le bon niveau
            Element niveauNode = (Element) listeNoeuds.item((int) niveau - 1);
   
            NodeList words = niveauNode.getElementsByTagName("mot");           

            //vérifie que le mot n'est pas déja présent 
            int i = 0;
            
            while (i < words.getLength() && !words.item(i).getTextContent().equals(mot)) {
                i++;
            }
            
            if (i >= words.getLength()) {
                //creation du nouveau mot
                Element nouveauMotNode = (Element) doc.createElement("mot");
                nouveauMotNode.setTextContent(mot);
            
                //on ajoute le mot dans le dictionnaire (en mémoire)
            
                niveauNode.appendChild(nouveauMotNode);
                //on écrit ce qu'il y a en mémoire dans le fichier XML
                ecrireDOM("dico.xml");
                
                return true;
            } else {
                 // cas où le mot est déja présent dans le dictionnaire
                System.err.println("Erreur, ajouterMot(): le mot " + mot + " est déja présent dans le niveau " + (int) niveau);
                
                return false;
            }
        
        } else {
            
            System.err.println("Erreur, ajouterMot(): impossible d'ajouter le mot " + mot + " dans le dictionnaire du jeu");
            //System.err.println("Le mot doit être de longueur 2n+2 (où n est le niveau)");

            return false;
        }
    } 
   
     private Boolean isValidWord(String mot) throws PatternSyntaxException {
        Pattern pattern = Pattern.compile("[A-Za-zÀ-ÿ]{3,}(-[A-Za-zÀ-ÿ]{2,})*");
        Matcher matcher = pattern.matcher(mot);
        
        return matcher.matches();
    }
     
         //dans le jeu les mots doivent être de longueur 2n + 2, où n = niveau, avec 1 <= n <= 5
    private Boolean isValidWordForDico(double niveau, String mot) {    
        
        //test si on a le bon nombre de lettre
        if ((niveau >= 1 && niveau <= 5) && mot.length() == (2 * niveau + 2)) {
            
            if (!isValidWord(mot)) {
                System.err.println("Erreur, isValidWord() : " + mot + " n'est pas considéré comme un mot.");
            }
            
            return isValidWord(mot); //test si le mot est valide
        } else {
            
            System.err.println("Erreur, isValidWordForDico() : Le mot n'est pas de longueur 2n +2");
            
            return false;
        }
    }
}
