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



public class Profil {
    private String nom;
    private String dateNaissance;
    private String avatar;
    private ArrayList<Partie> parties;
    public Document doc;
    

    public Profil(String nom, String dateNaissance) {
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.avatar = "conan.jpg";
        this.parties = new ArrayList<Partie>();
    }
    
    public Profil(String filename) throws SAXException, IOException {
       this.doc = fromXML(filename);//initialise doc 
       
       this.parties = new ArrayList<Partie>();
       
       setAll(); //fetch tous les attributs 
    }
    
    private void setAll() {//initialise tous les attributs via le parsing de doc
        NodeList listeNoeuds;
        Element nom;
        Element dateNaissance;
        Element avatar;
        
        //traitement pour le nom
        listeNoeuds = this.doc.getElementsByTagName("nom");
        
        nom = (Element) listeNoeuds.item(0);
        this.nom = nom.getTextContent();
        
        //traitement pour date de naissance
        
        listeNoeuds = this.doc.getElementsByTagName("anniversaire");
        dateNaissance = (Element) listeNoeuds.item(0);
        this.dateNaissance = xmlDateToProfileDate(dateNaissance.getTextContent());
        
        //traitement avatar
        
        listeNoeuds = this.doc.getElementsByTagName("avatar");
        avatar = (Element) listeNoeuds.item(0);
        this.avatar = avatar.getTextContent();
        
        
        //recupere les parties 
        
        NodeList partiesListe = doc.getElementsByTagName("partie");
        
        for (int i = 0; i < partiesListe.getLength(); i++) {
            Element partieNode = (Element) partiesListe.item(i);
            
            //on declare une partie
            Partie partie;
       
            String date;
            Element motNode;
            String niveau;
            String trouve;
            Element tempsNode;
            
            //on recupere la date
            
  
            date = partieNode.getAttribute("date");
            date = xmlDateToProfileDate(date);
            
           //on recupere le mot

            
           listeNoeuds = partieNode.getElementsByTagName("mot");
           motNode = (Element) listeNoeuds.item(0);
           
           
           //on recupere le niveau
           niveau = motNode.getAttribute("niveau");           
           
           //on recupere trouv??

           trouve = partieNode.getAttribute("trouv??"); 
              
          //on recupere temps
          listeNoeuds = partieNode.getElementsByTagName("temps");
          tempsNode = (Element) listeNoeuds.item(0);

          partie = new Partie(date, motNode.getTextContent(), Integer.parseInt(niveau));
          
          partie.setTemps(Integer.parseInt(tempsNode.getTextContent()));
          partie.setTrouv??(Integer.parseInt(trouve));
          
          //on ajoute la partie 
          
          this.ajouterPartie(partie); 
        }
    }
    
    
    //creer un nouveau XML associ?? au profil du nouveau joueur joueur (un profil sans partie mais valide par rapport au schema XSD de profil) 
    public void nouveauJoueur() throws ParserConfigurationException, TransformerException { 
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        
        //cr??ation de la racine
        Document document = docBuilder.newDocument();
        Element rootElement = document.createElement("profil");
        
        //attributs de la racine
        rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.setAttribute("xmlns", "http://myGame/tux");
        rootElement.setAttribute("xsi:schemaLocation", "http://myGame/tux ../xsd/profil.xsd");
        
        //cr??ation de l'??lement nom
        Element nomNode = document.createElement("nom");      
        nomNode.setTextContent(this.nom);
        
        
        //cr??ation de avatar
        Element avatarNode = document.createElement("avatar");
        avatarNode.setTextContent(this.avatar);
        
        //creation de l'??lement aniversaire
        Element anniversaireNode = document.createElement("anniversaire");
        anniversaireNode.setTextContent(profileDateToXmlDate(this.dateNaissance));
        
        //creation de l'??l??ment parties
        Element PartiesNode = document.createElement("parties"); 
        
        
        document.appendChild(rootElement);
        
        rootElement.appendChild(nomNode);
        rootElement.appendChild(avatarNode);
        rootElement.appendChild(anniversaireNode);
        rootElement.appendChild(PartiesNode);
        
        try { //ecriture de ce qu'il y a en m??moire dans le fichier
            FileOutputStream output =  new FileOutputStream("src/xml/" + this.nom + "_profil.xml");
            writeXml(document, output);
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    
    //sauvegarde le profil dans son fichier profil associ?? 
    //le nom du fichier est toujours nomJoueur_profil.xml
    //methode appell??e apr??s chaque partie
    //met a jour le profil avec la derniere partie et ??crit le resultat dans le fichier qui correspond au profil du joueur
    public void sauvegarder() throws SAXException, IOException, TransformerException {
        
        this.doc = fromXML (this.nom + "_profil.xml");
        
        //recupere la derniere partie que le joueur vient de jouer
        Partie dernierePartie = this.parties.get(this.parties.size() - 1);
        
        NodeList listeNoeuds = doc.getElementsByTagName("parties");
        
        Element partiesNode = (Element) listeNoeuds.item(0);
        
        //on cr??er la derniere partie sous forme d'??l??ment DOM
        Element dernierePartieNode = (Element) doc.createElement("partie");
        
        
        dernierePartieNode.setAttribute("date", profileDateToXmlDate(dernierePartie.getDate()));
        dernierePartieNode.setAttribute("trouv??", Integer.toString(dernierePartie.getTrouv??()));
        
        Element tempsNode = doc.createElement("temps");
        
        tempsNode.setTextContent(Integer.toString(dernierePartie.getTemps()));
        
        Element motNode = doc.createElement("mot");
        
        motNode.setAttribute("niveau", Integer.toString(dernierePartie.getNiveau()));
        motNode.setTextContent(dernierePartie.getMot());
        
        dernierePartieNode.appendChild(tempsNode);
        dernierePartieNode.appendChild(motNode);
        
        //on ajoute la derniere partie 
        partiesNode.appendChild(dernierePartieNode);
        
        
        
          try { 
            FileOutputStream output =  new FileOutputStream("src/xml/" + this.nom + "_profil.xml");
            writeXml(this.doc, output);
            System.out.println("Sauvegarde r??ussi");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("nouveauJoueur() : Erreur lors de la cr??ation du profil du nouveau joueur");
        }

    }
    
    //ecrit le XML en m??moire (doc), dans un fichier 
    private static void writeXml(Document doc, OutputStream output) throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);
    }
    
    private Document fromXML (String filename) throws SAXException, IOException {
        // Cree un DOM ?? partir d'un fichier XML
        
            DOMParser parser = new DOMParser();
            
            parser.parse("src/xml/" + filename);  
            Document doc = parser.getDocument();
    
            return doc;
    }
    
    void ajouterPartie(Partie p) {
        if (p != null) {
            this.parties.add(p);
        }
    }
    
    public int getDernierNiveau() {
        return 0;
    }
    
    @Override
    public String toString() {
        
        return "nom: " + this.nom + " ,avatar: " + this.avatar + " ,naissance:" + this.dateNaissance + " ,nombre de parties: " + parties.size(); 
    }
    
    //affiche des informations sur le profil de l'utilisateur dans la console
    public void affiche() {
        System.out.println("---------------------------Profil de " + this.nom + "----------------------------");
        
        System.out.println(this);
        
        for (Partie p : parties) {
            System.out.println(p);
        }
        
    }

    /// Takes a date in XML format (i.e. ????-??-??) and returns a date
    /// in profile format: dd/mm/yyyy
    public static String xmlDateToProfileDate(String xmlDate) {
        String date;
        // r??cup??rer le jour
        date = xmlDate.substring(xmlDate.lastIndexOf("-") + 1, xmlDate.length());
        date += "/";
        // r??cup??rer le mois
        date += xmlDate.substring(xmlDate.indexOf("-") + 1, xmlDate.lastIndexOf("-"));
        date += "/";
        // r??cup??rer l'ann??e
        date += xmlDate.substring(0, xmlDate.indexOf("-"));

        return date;
    }

    /// Takes a date in profile format: dd/mm/yyyy and returns a date
    /// in XML format (i.e. ????-??-??)
    public static String profileDateToXmlDate(String profileDate) {
        String date;
        // R??cup??rer l'ann??e
        date = profileDate.substring(profileDate.lastIndexOf("/") + 1, profileDate.length());
        date += "-";
        // R??cup??rer  le mois
        date += profileDate.substring(profileDate.indexOf("/") + 1, profileDate.lastIndexOf("/"));
        date += "-";
        // R??cup??rer le jour
        date += profileDate.substring(0, profileDate.indexOf("/"));

        return date;
    }
   
    
}
