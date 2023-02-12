package game;
import game.Dico;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NiveauHandler extends DefaultHandler{
    private Dico dictionnaire;
    private List<Niveau> dico;
    private Niveau niveau;
    private boolean inDico, inNiveau, inMot;
    private StringBuffer buffer;
    private String tousLesMotsDuNiveau ="";
    private int difficultee;

    public NiveauHandler(Dico dictionnaire) {
        super();
        
        this.dictionnaire = dictionnaire;
    }

    @Override
    public void startElement(String uri, String localName, 
			String qName, Attributes attributes) throws SAXException{ 
		if(qName.equals("dictionnaire")){ 
			dico = new LinkedList<Niveau>(); 
			inDico = true; 
		}else if(qName.equals("niveau")){ 
			niveau = new Niveau(); 
			try{ 
				difficultee = Integer.parseInt(attributes.getValue("difficultee")); 
				niveau.setDifficultee(difficultee);; 
			}catch(Exception e){ 
				//erreur, le contenu de id n'est pas un entier 
				throw new SAXException(e); 
			} 
			inNiveau = true; 
		}else if(qName.equals("mot")) { 
			buffer = new StringBuffer(); 
			inMot = true;  
			}else{ 
				//erreur, on peut lever une exception 
				throw new SAXException("Balise "+qName+" inconnue."); 
		} 
		 
	} 
	//détection fin de balise 
    @Override
	public void endElement(String uri, String localName, String qName) 
			throws SAXException{ 
		if(qName.equals("dictionnaire")){ 
			inDico = false; 
		}else if(qName.equals("niveau")){ 
			dico.add(niveau); 
			niveau = null; 
			inDico = false; 
			//System.out.println("\n");
		}else if(qName.equals("mot")){ 
			niveau.setDifficultee(difficultee);
			niveau.setMot(buffer.toString());
			//System.out.println("Niveau :" + difficultee +"   -> " + buffer);
                        dictionnaire.ajouteMotADico(difficultee, buffer.toString());
			buffer = null; 
			inMot = false; 
		}else{ 
			//erreur, on peut lever une exception 
			throw new SAXException("Balise "+qName+" inconnue."); 
		}           
	} 
	//détection de caractères 
    @Override
	public void characters(char[] ch,int start, int length) 
			throws SAXException{ 
		String lecture = new String(ch,start,length); 
		if(buffer != null) buffer.append(lecture);
		//tousLesMotsDuNiveau.append(buffer);     
	} 
	//début du parsing 
    @Override
	public void startDocument() throws SAXException { 
		//System.out.println("Début du parsing"); 
	} 
	//fin du parsing 
    @Override
	public void endDocument() throws SAXException { 
		//System.out.println("Fin du parsing");
		
	} 
}
