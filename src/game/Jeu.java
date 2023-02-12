/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import aleatoire.Random;
import env3d.Env;
import org.lwjgl.input.Keyboard;
import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.SAXException;


public abstract class Jeu {

    enum MENU_VAL {
        MENU_SORTIE, MENU_CONTINUE, MENU_JOUE
    }

    protected Env env;
    private  Room mainRoom;
    private Profil profil;
    private Tux tux;
    private ArrayList<Letter> lettres;
    private  Dico dico;
    protected String mot;
    protected int niveau;
    private Room menuRoom;
    protected EnvTextMap menuText;//textes a afficher dans les menus         
    private Letter letter;
    protected boolean partieTerminee;
    
    public Jeu() {
        
        // Crée un nouvel environnement
        this.env = new Env();

        //Instancie une Room
        this.mainRoom = new Room("src/xml/","plateau.xml");

        // Instancie une autre Room pour les menus
     
         this.menuRoom = new Room();
     
        
        //textures des menus 
        
        menuRoom.setTextureEast("textures/black.png");
        menuRoom.setTextureWest("textures/black.png");
        menuRoom.setTextureNorth("textures/black.png");
        menuRoom.setTextureBottom("textures/black.png");

        // Règle la camera
        env.setCameraXYZ(50, 60, 175);
        env.setCameraPitch(-20);

        // Désactive les contrôles par défaut
        env.setDefaultControl(false);
        
        // Dictionnaire
        this.dico = new Dico("dico.xml"); //creation nouveau dictionnaire

        //on parse le fichier XML pour recupérer les mots du dictionnaire
        
        //Méthode via DOM
        /*
        try {
            this.dico.lireDictionnaireDOM("src/xml/");
        } catch(IOException ex) {
            Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
        } */
        
        //Méthode via Sax
        
        this.dico.lireDictionnaireSAX();
        
       
        // instancie le menuText
        menuText = new EnvTextMap(env);
        
        // Textes affichés à l'écran
        menuText.addText("Le profil a été correctement chargé", "profilChargé", 140, 300);
        menuText.addText("Aucun profil correspondant n'a été trouvé", "profilInexistant", 140, 300);
        menuText.addText("Appuyez sur <Enter> pour revenir au menu principal", "appuyezEntrée", 140, 280);
            menuText.addText("Appuyez sur <Enter> pour accéder à votre profil", "accesProfil", 140, 280);
        menuText.addText("Le mot a bien été ajouté", "motAjouté", 140, 300);
        menuText.addText("Le mot n'a pas pu être ajouté", "motNonAjouté", 140, 300);
        menuText.addText("Voulez vous ?", "Question", 200, 300);
        menuText.addText("1. Commencer une nouvelle partie ?", "Jeu1", 250, 280);
        menuText.addText("2. Revenir au menu principal", "Jeu3", 250, 260);
        menuText.addText("3. Quitter le jeu ?", "Jeu4", 250, 240);
        menuText.addText("Choisissez un nom de joueur : ", "NomJoueur", 200, 300);
        menuText.addText("Choisissez un mot à ajouter au dictionnaire :", "nouveauMot", 50, 300);
        menuText.addText("1. Charger un profil de joueur existant ?", "Principal1", 250, 280);
        menuText.addText("2. Créer un nouveau joueur ?", "Principal2", 250, 260);
        menuText.addText("3. Ajouter un mot dans le dictionnaire de jeu","Principal3" ,250, 240);
        menuText.addText("4. Quitter le jeu ?", "Principal4", 250, 220);
        menuText.addText("Saisir le niveau (entre 1 et 5): ", "choixNiveau", 200, 250);
        menuText.addText("Erreur de saisie, le niveau doit être compris entre 1 et 5", "erreurSaisieNiveau", 200, 230);
        menuText.addText("", "afficherMotDebutPartie", 50, 80);
   
        
    }

    /**
     * Gère le menu principal
     *
     */
    public void execute() {

        MENU_VAL mainLoop;
        mainLoop = MENU_VAL.MENU_SORTIE;
      
        do {
            mainLoop = menuPrincipal();
        } while (mainLoop != MENU_VAL.MENU_SORTIE);
        
        this.env.setDisplayStr("Au revoir !", 300, 30);
        
        env.exit();
    }

    //saisir un nom de joueur dans le menu
    
    private String getUserInput(String key) {
        String input;
        menuText.getText(key).display();
        input = menuText.getText(key).lire(true);
        menuText.getText(key).clean();
    
        return input;
    }
    
    private MENU_VAL menuJeu() {//MENU pour un profil chargé ou nouveau profil qui vient d'être créé  

        MENU_VAL playTheGame;
        playTheGame = MENU_VAL.MENU_JOUE;
        
        Partie partie;
        do {
            // restaure la room du menu
            env.setRoom(menuRoom);
         
            // affiche menu
            menuText.getText("Question").display();
            menuText.getText("Jeu1").display();
            //menuText.getText("Jeu2").display();
            menuText.getText("Jeu3").display();
            menuText.getText("Jeu4").display();
            
            // vérifie qu'une touche 1, 2, 3  est pressée
            int touche = 0;
            while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_3 || touche == Keyboard.KEY_4)) {
                touche = env.getKey();
                env.advanceOneFrame();
            }

            // nettoie l'environnement du texte
            menuText.getText("Question").clean();
            menuText.getText("Jeu1").clean();
           //menuText.getText("Jeu2").clean();
            menuText.getText("Jeu3").clean();
            menuText.getText("Jeu4").clean();


            // et décide quoi faire en fonction de la touche pressée
            switch (touche) {
                // -----------------------------------------
                // Touche 1 : Commencer une nouvelle partie
                // -----------------------------------------                
                case Keyboard.KEY_1: // choisi un niveau et charge un mot depuis le dico
                    // .......... dico.******
               

                    this.niveau = 0;
                    String niveauStr;//choix du niveau sous forme de chaine de caractere 
                    boolean erreurSaisie = false;
                    
                    do {

                         niveauStr = getUserInput("choixNiveau");

                         try {
                             this.niveau = Integer.parseInt(niveauStr); //on recupere le niveau sous forme en int
                         } catch(NumberFormatException e) {
                             System.out.println("Erreur saisie");
                             this.menuText.getText("erreurSaisieNiveau").display();
                             erreurSaisie = true;
                         }

                         //menuText.getText("Question").display();

                    }while (this.niveau < 1 || this.niveau > 5 );//on recommence tant que la saisie n'est pas bonne

                    if (erreurSaisie) { //message à l'utilisateur en cas d'erreur de saisie
                        this.menuText.getText("erreurSaisieNiveau").clean();
                    }
                    
                     env.setRoom(mainRoom);

                     this.mot = this.dico.getMotDepuisListeNiveaux(niveau); //on récupere un mot pour la partie 
        
                     this.mot = this.mot.toLowerCase(Locale.ITALY); //on met tout en minuscule au cas où

                     this.lettres = new ArrayList<Letter>();//contient l'ensemble des lettres

                     ArrayList <Coordonnée> cL = new ArrayList<Coordonnée>();//va contenir l'ensemble des coordonnées des lettres

                     //initialise la liste des coordonnées de manière à former un cercle
                     cL = this.coordonnéesLettres(2.0 * niveau+2.0,  mainRoom.getWidth()/2.0, mainRoom.getDepth()/2.0, 35.0);

                     String motShuffled = Random.shuffle(mot); //on mélange les lettres pour pas que ce soit trop facile

                     for (int i = 0; i < motShuffled .length(); i++) {
                        Coordonnée c = cL.get(i);//récupere la coordonnée de la lettre

                        //on construit une nouvelle lettre à partir des coordonnées qui ont  été généré
                        Letter lettre = new Letter (env, mainRoom, c.getX() , c.getY(), motShuffled.charAt(i));

                        //rotation selon l'axe y aléatoire 
                        lettre.setRotateY(Random.getRandomNumber(0, 180));

                        this.lettres.add(lettre);//on ajoute la lettre à la liste des lettres
                     }

                    //date d'aujourd'hui
                    String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

                    partie = new Partie(date, this.mot, this.niveau);
                    // joue
                    joue(partie);
                    
                    //on affiche le profil du joueur avant de commencer la partie 
            
                    
                    this.profil.ajouterPartie(partie);
                    //on sauvegarde la partie
                    try {
                          profil.sauvegarder(); //enregistre les resultats dans le fichier nom_profil.xml 
                    } catch(Exception e) {
                        System.out.println("Erreur lors de la sauvegarde.");
                    }                   
                    
                    profil.affiche();//affiche les informations du profil du joueur sur la console 
                    
                    // .......... profil.****** 


                    playTheGame = MENU_VAL.MENU_JOUE;
                    break;
                // -----------------------------------------                
                case Keyboard.KEY_2:
                    playTheGame = MENU_VAL.MENU_CONTINUE;
                    break;

                // -----------------------------------------
                // Touche 4 : Quitter le jeu
                // -----------------------------------------                
                case Keyboard.KEY_3:
                    
                    playTheGame = MENU_VAL.MENU_SORTIE;

            }
        } while (playTheGame == MENU_VAL.MENU_JOUE);
        return playTheGame;
    }
    

    private MENU_VAL menuPrincipal() {//Menu principal  
        MENU_VAL choix = MENU_VAL.MENU_CONTINUE;
        String nomJoueur;
       
        // restaure la room du menu
        env.setRoom(menuRoom);

        menuText.getText("Question").display();
        menuText.getText("Principal1").display();
        menuText.getText("Principal2").display();
        menuText.getText("Principal3").display();
        menuText.getText("Principal4").display();

               
        // vérifie qu'une touche 1, 2 ou 3 est pressée
        int touche = 0;
        while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_3 || touche == Keyboard.KEY_4)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }

        menuText.getText("Question").clean();
        menuText.getText("Principal1").clean();
        menuText.getText("Principal2").clean();
        menuText.getText("Principal3").clean();
        menuText.getText("Principal4").clean();
        
        // et décide quoi faire en fonction de la touche pressée
        
        switch (touche) {
            
          
            // -------------------------------------
            // Touche 1 : Charger un profil existant
            // -------------------------------------
             case Keyboard.KEY_1: 
                // demande le nom du joueur existant
                nomJoueur = getUserInput("NomJoueur");
                
                //nom fichier pour un profil donné = nom_profil.xml ex: alban_profil.xml pour Alban
                
                
                String nomFichier = nomJoueur.concat("_profil.xml");
                boolean profilChargé = false;
                
                try {
                    profil = new Profil(nomFichier);
                    
                    System.out.println("Chargement du profil de " + nomJoueur + " réussi.");
                    profilChargé = true;
                
                } 
                catch(Exception ex) {//impossible de charger le profil du joueur 
                     System.out.println("Le profil du joueur " + nomJoueur + " n'a pas été trouvé.");
                     choix = MENU_VAL.MENU_CONTINUE;//retour au menu principal 
                }
                
                if (profilChargé) {
                    menuText.getText("profilChargé").display();
                    menuText.getText("accesProfil").display();
                } else {
                    menuText.getText("appuyezEntrée").display();
                    menuText.getText("profilInexistant").display();
                }        
                
                while (!(touche == Keyboard.KEY_RETURN)) {
                    touche = env.getKey();
                    env.advanceOneFrame();
                }
              
                if (profilChargé) {
                    menuText.getText("accesProfil").clean();
                    menuText.getText("profilChargé").clean();
                    choix = menuJeu();//on est redirigié vers le menu du jeu (Ne sera pas atteint si exception avant => fichier qui n'existe pas)
                } else {
                    menuText.getText("appuyezEntrée").clean();
                    menuText.getText("profilInexistant").clean();
                }
                
                
                
                break; 

            // -------------------------------------
            // Touche 2 : Créer un nouveau joueur
            // -------------------------------------
            case Keyboard.KEY_2:
                // demande le nom du nouveau joueur
                nomJoueur = getUserInput("NomJoueur");
                
                Boolean nouveauJoueur = true;
                
                File f = new File("src/xml/" + nomJoueur + "_profil.xml");
                    if(f.exists() && !f.isDirectory()) { 
                    nouveauJoueur = false;
                }
                
                if (nouveauJoueur){//on test si c'est un nouveau joueur (nomNouveauJoueur_profil.xml ne doit pas exister)

                    //on met la date d'aujourd'hui pour la date de naissance comme valeur par defaut

                    String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

                    profil = new Profil(nomJoueur, date); 

                    try {
                           profil.nouveauJoueur();//creer le fichier xml associé au nouveau joueur 
                    } catch(Exception e) {
                       System.out.println(e); //erreur lors de la création lors du nouveau profil
                    }
                    
                    choix = menuJeu();
                } else  {
                     System.out.println("Erreur le joueur " + nomJoueur + " existe déja, retour au menu principal");
                     choix = MENU_VAL.MENU_CONTINUE; //retour menu principal car le joueur existe déja
                }          
                
                break;
            // -------------------------------------
            // Touche 3 : ajouter un mot dans le dictionnaire du jeu
            // -------------------------------------
            case Keyboard.KEY_3:
                String nouveauMot = getUserInput("nouveauMot");
                //strlen(str) == 2n + 2 <=> n = strlen(str)/2 - 1
                double niveau = (double) nouveauMot.length()/2 - 1;
                //ajouter ici
                EditeurDico editeur = new EditeurDico();
                Boolean motAjoute = false;
                
                motAjoute = editeur.ajouterMot(nouveauMot, niveau);
                
                if (motAjoute) {
                    menuText.getText("motAjouté").display();
                    System.out.println("Le mot " + nouveauMot + " a bien été ajouté au dictionnaire du jeu");
                } else {
                     menuText.getText("motNonAjouté").display();
                }
                
                menuText.getText("nouveauMot").clean();
                menuText.getText("appuyezEntrée").display();
                
                while (!(touche == Keyboard.KEY_RETURN)) {
                    touche = env.getKey();
                    env.advanceOneFrame();
                }
                
               menuText.getText("appuyezEntrée").clean();
               
               if (motAjoute) {
                   menuText.getText("motAjouté").clean();
               } else {
                   menuText.getText("motNonAjouté").clean();
               }
              
             
                choix = MENU_VAL.MENU_CONTINUE;
                break;    
                
            // -------------------------------------
            // Touche 4 : Sortir du jeu
            // -------------------------------------
            case Keyboard.KEY_4:
                choix = MENU_VAL.MENU_SORTIE;
                break;
        }
        return choix;
    }

    public void joue(Partie partie) {
       
        // Instancie un Tux
        tux = new Tux(env, mainRoom);
        env.addObject(tux);
        
         for (Letter lettre : lettres) {
            env.addObject(lettre); //ajout de chaque lettre à l'environnement
        }      

        // Ici, on peut initialiser des valeurs pour une nouvelle partie
        démarrePartie(partie);

        // Boucle de jeu
        Boolean finished;
        finished = false;
       
        this.partieTerminee = false;   
               
        Chronometre chronoMotAffiche = new Chronometre(4);
        this.menuText.getText("afficherMotDebutPartie").setSize(2.0);
        this.menuText.getText("afficherMotDebutPartie").modifyTextAndDisplay("Trouve ce mot :".concat(this.mot));
        chronoMotAffiche.start();
        
        while (!finished) {
            
           
            finished = this.partieTerminee;//a modifier
            
            // Contrôles globaux du jeu (sortie, ...)
            //1 is for escape key         
      
            //on enleve le mot affiché en debut de partie dans les cas suivats : 
            if (!chronoMotAffiche.remainsTime() || env.getKey() == 1 || finished) {
                //!chronoMotAffiche.remainsTime() <=> on a dépassé les 5 secondes 
                //env.getKey() <=> 1 joueur appuie sur échape 
                //finished <=> la partie est terminée 
                
                chronoMotAffiche.stop();
                this.menuText.getText("afficherMotDebutPartie").clean();
            }
            
            
            if (env.getKey() == 1) {
                finished = true;
            } 

            // Contrôles des déplacements de Tux (gauche, droite, ...)
            tux.deplace((JeuDevineLeMotOrdre) this);  //on transmet le Jeu a Tux pour qu'il sait ou sont les lettres 

            // Ici, on applique les regles
            appliqueRegles(partie);

            // Fait avancer le moteur de jeu (mise à jour de l'affichage, de l'écoute des événements clavier...)
            env.advanceOneFrame();
        }
       
        // Ici on peut calculer des valeurs lorsque la partie est terminée
        terminePartie(partie);

    }

    protected abstract void démarrePartie(Partie partie);

    protected abstract void appliqueRegles(Partie partie);

    protected abstract void terminePartie(Partie partie);
    
    protected abstract void tuxTrouveLettre(Letter lettre);
    
    
    ///////////////////////
    
     protected double distance (Letter letter){
        
        //coordonnées de la lettre en question
        
        double xL = letter.getX();
        double yL = letter.getY();
        double zL = letter.getZ();
        
        // coordonnées de tux au prochain déplacement
        
        double xT = tux.getX() + 2;
        double yT = tux.getY() + 2;
        double zT = tux.getZ() + 2;
        
        //calcul de la distance
        
        //opérandes pour le calcul
        double op1 = (xL - xT) * (xL - xT); 
        double op2 = (yL - yT) * (yL - yT);
        double op3 = (zL - zT) * (zL - zT);
        
        double somme = op1 + op2 + op3;
      
        return Math.sqrt(somme);
    }
     
     //determine si tux est en collision avec une lettre donée 
      protected Boolean collision(Letter letter)  {
        Boolean collision = (distance(letter) < letter.getScale() + tux.getScale());
        
        if (collision) {
            this.tuxTrouveLettre(letter);
            
        }
        
        return collision;
    }   
      
      //determine si tux est en collision avec au moins une des toutes les lettres 
      public Boolean collisionAvecUneDesLettre() {
        Boolean collision = false;
        
        Letter lettre;
        int i = 0;
        
        while (!collision && i < this.getLettres().size()) {
            lettre = this.getLettres().get(i);
            i++;
            collision = collision(lettre);
        }
        
        return collision;
    }
      
    protected ArrayList<Letter> getLettres() {
        return lettres;
    } 
    
    
    //retourne la liste des coordonnées des lettres pour former un cercle 
    /* parametres
        n = nombre de lettres
        xC = abscisse du centre du cercle
        yC = ordonnée du centre du cercle
        r = rayon du cercle
    */
    public static ArrayList<Coordonnée> coordonnéesLettres(double n, double xC, double yC, double r){
        
        ArrayList<Coordonnée> coordonnéeDesLettres = new  ArrayList<Coordonnée>();
        
        
        //vecteurs utilisés pour calcul du produit scalaire pour déterminer l'angle 
        double [] v1 = new double[2]; 
        double [] v2 = new double[2];
        
        double [] cCercle = new double [2];
        double rayon = r;
        
        //cercle centre
        cCercle[0] = xC;
        cCercle[1] = yC;
        
        double xMin = cCercle[0] - rayon; //abscisse min
        double xMax = xMin + 2.0*rayon;//abscisse max
        double step = 0.25;//pas utilisé pour générer les points du cercle 
        double y1 = 0.0, y2 = 0.0;
        double theta;//angle formés var les vecteurs (v1, v2)
        double delta;//discriminant
        double longueurArcCourant = 0.0;//longueur de l'arc de cercle par rapport au dernier point 
        double p = 2.0 * Math.PI * rayon; 
         p = Math.floor(p * 100.0) / 100.0;
            
        double nbCubes = n;
        double compteur = 1.0;
        
        v1[0] = 2;
        v1[1] = 0;
        
        Coordonnée c = new Coordonnée(xC + rayon, yC,0);
        
        coordonnéeDesLettres.add(c);
        
        //generation de l'arc de cercle supérieur 
        for (double x = xMax; x >= xMin; x-= step) {
            
            //calcul des coordonnées de y en fonction de X 
            
            //calcul du discriminant
            delta = (-2.0 * cCercle[1]) * (-2.0 * cCercle[1]);
            
            double t =-4.0 * (-1.0*rayon*rayon + x*x -2.0 * x * cCercle[0] + cCercle[0]* cCercle[0] + cCercle[1] * cCercle[1]);
            delta += t;
            
            //on determine y en fonction du discriminant
            if (delta == 0.0) {
                y1 = (2.0 * cCercle[1])/(2.0);
                
            } else if (delta > 0.0) {
                y1 =  (2.0*cCercle[1] + Math.sqrt(delta))/(2.0) ;
            }
           
            //y1 = Math.sqrt((rayon * rayon) - (x*x));
            y1 =  Math.floor(y1 * 100.0) / 100.0;
          
            v2[0] = x - cCercle[0];
            v2[1] = y1 - cCercle[1];
          
            //calcul produit scalaire des deux vecteurs  
            
            double operande1 = v1[0] * v2[0] + v1[1] * v2[1];
            
            //produits des normes des vecteurs 
            
            double operande2 = Math.sqrt(Math.abs(v1[0])*Math.abs(v1[0]) + Math.abs(v1[1])+Math.abs(v1[1])) * Math.sqrt(Math.abs(v2[0])*Math.abs(v2[0])  +  Math.abs(v2[1])*Math.abs(v2[1]));     
            
            double operande3 = operande1 / operande2;
            
            //calcul de l'angle (v1, v2) via les operandes
            
            theta = Math.acos(operande3);
            theta = Math.toDegrees(theta);
            theta = Math.floor(theta * 100.0) / 100.0;
            
            //calcul longueur arc de cercle courant 
            longueurArcCourant = (theta/360.0) * 2.0 * Math.PI * rayon;
            longueurArcCourant = Math.floor(longueurArcCourant * 100.0) / 100.0;
            
          
            //determine si le dernier point généré doit etre celui d'une lettre
            //=> lettre equidistante sur le cercle 
            if (longueurArcCourant >= ((compteur * p)/nbCubes)) {
                 c = new Coordonnée(x, y1, 0); //=> la derniere coordonnée générée doit etre celle d'une lettre
                 coordonnéeDesLettres.add(c);    
                 
                 
                 //System.out.println("(" + x + "," + y1 + "), theta = " + theta + " arc courant = " + longueurArcCourant + " p = " + p);
                 compteur++;
            }
            
        }
        
        //generation de l'arc de cercle inferieur 
        
        for (double x = xMin; x <= xMax; x+= step) {
            
             delta = (-2.0 * cCercle[1]) * (-2.0 * cCercle[1]);
            
            double t =-4.0 * (-1.0*rayon*rayon + x*x -2.0 * x * cCercle[0] + cCercle[0]* cCercle[0] + cCercle[1] * cCercle[1]);
            delta += t;
            
            if (delta > 0.0) {
                y2 =  (2.0*cCercle[1] - Math.sqrt(delta))/(2.0) ;
            
                   y2 = Math.floor(y2 * 100.0) / 100.0;
             
            v2[0] = x - cCercle[0];
            v2[1] = y2 - cCercle[1];
            
            double operande1 = v1[0] * v2[0] + v1[1] * v2[1];
            double operande2 = Math.sqrt(Math.abs(v1[0])*Math.abs(v1[0]) + Math.abs(v1[1])+Math.abs(v1[1])) * Math.sqrt(Math.abs(v2[0])*Math.abs(v2[0])  +  Math.abs(v2[1])*Math.abs(v2[1]));        
            
            double operande3 = operande1 / operande2;
            
            //calcul de l'angle
            
            theta = Math.acos(operande3);
            theta = 360.0 - Math.toDegrees(theta);
            theta = Math.floor(theta * 100.0) / 100.0;
            
            longueurArcCourant = (theta/360.0) * 2.0 * Math.PI * rayon;
            longueurArcCourant = Math.floor(longueurArcCourant * 100.0) / 100.0;
             
            if (longueurArcCourant >= ((compteur * p)/nbCubes)) {
                 c = new Coordonnée(x, y2, 0);
                 coordonnéeDesLettres.add(c);
                
         
                 //System.out.println("(" + x + "," + y2 + "), theta = " + theta + " delta = " + delta);
                 compteur++;
            }
            
            
            } 
        } 
        
        
        //on genere l'ensemble des coordonées du cercle + l'angle associé a chaque fois
        
        return coordonnéeDesLettres;
    }
    
}
