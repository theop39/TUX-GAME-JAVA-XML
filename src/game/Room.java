/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import aleatoire.Random;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Room {
    private int depth;
    private int height;
    private int width;
    private String textureBottom;
    private String textureNorth;
    private String textureEast;
    private String textureWest;
    private String textureTop;
    private String textureSouth;
    
    
    public Room() {
        this.depth = 100;
        this.width = 100;
        this.height = 60;
    }
    
    public Room(String path, String filename) {
        //try ici
        try {
            this.lirePlateau(path, filename);
        }    
        catch(Exception ex) {
            Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
            this.depth = 100;
            this.width = 100;
            this.height = 60;
            this.textureBottom = "textures/fence1.png";
            this.textureNorth = "textures/fence0.png";
            this.textureEast = "textures/fence0.png";
            this.textureWest = "textures/fence0.png";
        } 
       
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getTextureBottom() {
        return textureBottom;
    }

    public void setTextureBottom(String textureBottom) {
        this.textureBottom = textureBottom;
    }

    public String getTextureNorth() {
        return textureNorth;
    }

    public void setTextureNorth(String textureNorth) {
        this.textureNorth = textureNorth;
    }

    public String getTextureEast() {
        return textureEast;
    }

    public void setTextureEast(String textureEast) {
        this.textureEast = textureEast;
    }

    public String getTextureWest() {
        return textureWest;
    }

    public void setTextureWest(String textureWest) {
        this.textureWest = textureWest;
    }

    public String getTextureTop() {
        return textureTop;
    }

    public void setTextureTop(String textureTop) {
        this.textureTop = textureTop;
    }

    public String getTextureSouth() {
        return textureSouth;
    }

    public void setTextureSouth(String textureSouth) {
        this.textureSouth = textureSouth;
    }
    
    public void lirePlateau(String path, String filename) throws SAXException, IOException {
        // crée un parser de type DOM   
        DOMParser parser = new DOMParser();

        //parse le document XML correspondant au fichier filename dans le chemin path
        parser.parse(path + filename);
        Document doc = parser.getDocument();
        
        
        //NodeList listeNoeuds = doc.getElementsByTagName("dimensions");
        
        
       //on recupere les dimensions 
        
        NodeList listeNoeuds;
        
        listeNoeuds = doc.getElementsByTagName("height");
        Element heightNode = (Element) listeNoeuds.item(0);        
        
        listeNoeuds = doc.getElementsByTagName("width");
        Element widthNode = (Element) listeNoeuds.item(0);        
        
        listeNoeuds = doc.getElementsByTagName("depth");
        Element depthNode = (Element) listeNoeuds.item(0);        
      

       //on recupere les textures 
       
       
       listeNoeuds = doc.getElementsByTagName("textureBottom");
       Element textureBottomNode = (Element) listeNoeuds.item(0);
       
       listeNoeuds = doc.getElementsByTagName("textureEast");
       Element textureEastNode = (Element) listeNoeuds.item(0);
       
       listeNoeuds = doc.getElementsByTagName("textureWest");
       Element textureWestNode = (Element) listeNoeuds.item(0);
       
       listeNoeuds = doc.getElementsByTagName("textureNorth");
       Element textureNorthNode = (Element) listeNoeuds.item(0); 
       
       //on set les attributs de la room avec ce qu'on a parsé :

       
       this.height = Integer.parseInt(heightNode.getTextContent());
       this.width = Integer.parseInt(widthNode.getTextContent());
       this.depth = Integer.parseInt(depthNode.getTextContent());
       
       this.textureBottom = textureBottomNode.getTextContent();
       this.textureEast = textureEastNode.getTextContent();
       this.textureWest = textureWestNode.getTextContent();
       this.textureNorth = textureNorthNode.getTextContent();
   } 
    
    
}
