<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet 
    xmlns:tux="http://myGame/tux"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
>
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/"> 
        <!-- Définie du template pour la racine -->
        <!-- recupere l'element racine et applique les traitements pour afficher les parties d'un joueur -->
        
      
        <html>
            
            <head>
                <title>Profil</title>
                <link rel="stylesheet" type="text/css" href="../css/style.css"/>
                <meta charset="utf-8"/>
            </head>
            <body>
                
                <div id="content">
                    <header>
                        
                        <div id="headContent">
                        
                              <xsl:element name="img">
                                <xsl:attribute name="alt">tux </xsl:attribute>
                                  <xsl:attribute name="src">
                                      <xsl:value-of select="string('../img/linux-tux.gif')"/>
                                  </xsl:attribute>
                            </xsl:element>
                            
                                <h1>Tux Letter Game</h1>
                        </div>
                      
                        <hr/>



                        <div class="flex-container-1">

                        <div>
                             
                            
                            
                            
                            <!-- image qui correspond à l'avatar du joueur -->
                            <xsl:element name="img">
                                <xsl:attribute name="alt">avatar</xsl:attribute>
                                  <xsl:attribute name="src">
                                      <xsl:value-of select="concat('../img/', tux:profil/tux:avatar)"/>
                                  </xsl:attribute>
                                  <xsl:attribute name="class">rounded logo</xsl:attribute>
                            </xsl:element>
                        </div>
                        <!-- informations du joueur -->
                        <div>    
                            <ul>
                                <li>
                                    <b>Nom</b>: <xsl:value-of select="tux:profil/tux:nom"/>
                                </li>
                                <li>
                                    <b>Naissance</b>:<xsl:value-of select="tux:profil/tux:anniversaire"/>  
                                </li>
                            </ul>                  
                        </div>

                        </div>
                        <hr/>
                    </header>
                    <main>
                        <!-- affichage de l'ensemble des parties -->
                        <h2>Parties :</h2>
                        
                        
                        <!-- tableau des parties -->
                        <table>
                            <thead>
                                <tr>
                                    <th scope="col">numéro partie</th>
                                    <th scope="col">date</th>
                                    <th scope="col">mot</th>
                                    <th scope="col">niveau</th>
                                    <th scope="col">trouvé</th>
                                    <th scope="col">temps (s)</th>
                                    <th scope="col">score</th>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- TEMPLATE PARTIE 
                                  ici on fait fait appel au template qui recupere chaque partie
                                  on affiche chaque partie sur une ligne du tableau 
                                -->
                                <xsl:apply-templates select="/tux:profil/tux:parties/tux:partie">
                                     <xsl:sort select="./@date" order="ascending"/>
                                </xsl:apply-templates>
                                
                            </tbody>
                        </table>
                       
                    </main>
                </div>
                
            </body>
        </html>
    </xsl:template>
    
 
 
    <xsl:template match="tux:partie">
        
        <!-- 
                Template partie qui appelé pour chaque partie selectionnée 
                affichage toutes les informations relative a une partie dans une ligne de tableau
        -->
        
        <xsl:variable name="noeudCourant" select="."/>
        
        
        <xsl:variable name="trouvé" select="./@trouvé"/> 
        
        <tr>
            <td>
                <xsl:value-of select="position()"/> 
            </td>
            <td>
                <xsl:value-of select="./@date"/>
            </td>
            <td>
               <xsl:value-of select="./tux:mot"/>
            </td>
            <td>
               <xsl:value-of select="./tux:mot/@niveau"/>
            </td>
            <td>
               <xsl:value-of select="./@trouvé"/>
            </td>
            <td>
                <xsl:value-of select="./tux:temps"/>
            </td>
            <td>
                <xsl:if test='$trouvé = "100"'>
                    
                    <xsl:variable name="niveau" select="./tux:mot/@niveau"/> 
                    <xsl:variable name="temps" select="./tux:temps"/> 
                    <xsl:variable name="score" select="($niveau * 100) div $temps"/>
                     
                       <xsl:value-of select='format-number($score, "#.0")'/>  <!-- score avec un chiffre apres la virgule -->
                     
                       <!-- 
                       aprés la virgule :
                        # => un chiffre sauf 0
                        0 => un chiffre y compris 0
                        
                        doc : https://www.w3schools.com/XML/func_formatnumber.asp
                        
                      -->
                    
                </xsl:if>
                <xsl:if test='$trouvé != "100"'>
                    0
                </xsl:if>
            </td>
        </tr>
    </xsl:template>
    

    
</xsl:stylesheet>
