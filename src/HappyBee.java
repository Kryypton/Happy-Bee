import extensions.CSVFile;

class HappyBee extends Program{
    
    //Pour toutes les parties
    final double PROBA_TULIPE = 0.15;
    final double PROBA_TOURNESOL = 0.30;
    final double PROBA_COQUELICO = 0.45;
    final double PROBA_FRAISE = 0.60;
    final double PROBA_VIOLETTE = 0.75;
    final double PROBA_MARGERITTE = 0.90;
    final double PROBA_ROSE = 1.00;

    final double PROBA_GRENOUILLE = 0.33;
    final double PROBA_RONCE = 0.66;
    final double PROBA_BRUME = 1.00;
    //Dès le début du jeu
    final int POINT_DE_VIE = 3;
    final int POLLEN = 0;

    //Niveau 1
    final int TAILLE_1 = 5;
    final int POS_X_ABEILLE = TAILLE_1-1;
    final int POS_Y_ABEILLE = TAILLE_1-1;
    final double PROBA_HOSTILE_1 = 0.00;
    final double PROBA_FLEUR_1 = 0.02;
    
    Entitee newEntitee(Entites type, String symbole, int x, int y, int pollen, int pv, int degats){
        Entitee e = new Entitee();
        e.type = type;
        e.symbole = symbole;
        e.x = x; 
        e.y = y;
        e.pollen = pollen;
        e.pv = pv;
        e.degats = degats; 
        return e;
    }

    Entitee creerFleurAleatoire(int x, int y){
        double alea = random();
        if (alea>0 && alea<=PROBA_TULIPE){
            return newEntitee(Entites.TULIPE, "🌼", x, y, 1, 0, 0);
        } else if (alea >PROBA_TULIPE && alea<=PROBA_TOURNESOL){
            return newEntitee(Entites.TOURNESOL, "🌻", x, y, 1, 0, 0);
        } else if (alea<PROBA_TOURNESOL && alea<=PROBA_COQUELICO){
            return newEntitee(Entites.COQUELICO, "🥀", x, y, 1, 0, 0);
        } else if (alea<PROBA_COQUELICO && alea<=PROBA_FRAISE){
            return newEntitee(Entites.FRAISE, "🍓", x, y, 1, 0, 0);
        } else if (alea<PROBA_FRAISE && alea<=PROBA_VIOLETTE){
            return newEntitee(Entites.VIOLETTE, "💐", x, y, 1, 0, 0);
        } else if (alea<PROBA_VIOLETTE && alea<=PROBA_MARGERITTE){
            return newEntitee(Entites.MARGUERITTE, "🌼", x, y, 1, 0, 0);
        } else {
            return newEntitee(Entites.ROSE, "🌹", x, y, 1, 0, 0);
        }
    }

    Entitee creerHostileAleatoire(int x, int y){
        double alea = random();
        if (alea>0 && alea<=PROBA_GRENOUILLE) {
            return newEntitee(Entites.GRENOUILLE, "🐸",x,y,0,1,1);
        } else if (alea >PROBA_GRENOUILLE && alea<=PROBA_RONCE) {
            return newEntitee(Entites.RONCE, "棘",x,y,0,1,1);
        } else {
            return newEntitee(Entites.BRUME, "▒",x,y,0,1,1);
        }
    }


    Entitee[][] creerNiveauUn(double probaHostile, double probaFleur, int taille, int xAbeille,int yAbeille){
        Entitee[][] carte = new Entitee[taille][taille];
        for (int longX = 0; longX<taille;longX++){
            for (int longY = 0; longY<taille;longY++){
                double alea = random();
                if (alea<probaFleur){
                    carte[longX][longY] = creerFleurAleatoire(longX,longY);
                } else if (alea < (probaFleur + probaHostile)) {
                    carte[longX][longY] = creerHostileAleatoire(longX,longY);
                } else {
                    carte[longX][longY] = creerVide(longX,longY);
                }
            }
        }
        placerRucheAbeille(carte, xAbeille, yAbeille);
        return carte;
    }

    String toStr(Entitee[][] carte, int xAbeille, int yAbeille){
        String carteAffichage = "";
        Entitee e;
        for (int longX = 0; longX<length(carte,1);longX++){
            for (int longY = 0; longY<length(carte,2)*1.5;longY++){
                carteAffichage = carteAffichage + "██";
            }
            carteAffichage = carteAffichage + "\n";
            for (int longY = 0; longY<length(carte,2);longY++){
                e = carte[longX][longY];
                carteAffichage = carteAffichage + e.symbole + '█';
            }
            carteAffichage = carteAffichage + "\n";
            
            
            
        }
        for (int longY = 0; longY<length(carte,2)*1.5;longY++){
                carteAffichage = carteAffichage + "██";
        }
        carteAffichage = carteAffichage + "\n il vous reste : " + carte[yAbeille][xAbeille].pv + "points de vie ❤️";
        return carteAffichage;
    }

    Entitee creerVide(int x, int y){
        return newEntitee(Entites.VIDE, "  ", x, y, 0, 0, 0);
    }

    Entitee creerRuche(int x, int y) {
        return newEntitee(Entites.RUCHE, "🏠", x, y, 0, 0, 0);
    }

    Entitee creerAbeille(int x, int y) {
        return newEntitee(Entites.ABEILLE, "🐝", x, y, 0, 3, 0);
    }
    
    void placerRucheAbeille(Entitee[][] carte,int AbeillePosX, int AbeillePosY) {
        carte[length(carte)-1][0] = creerRuche(length(carte)-2,0);
        carte[AbeillePosX][AbeillePosY] = creerAbeille(AbeillePosX,AbeillePosY);
    }

    boolean contientUneFleur(Entitee[][] carte) {
        return (estPresent(carte,Entites.TOURNESOL) || estPresent(carte,Entites.COQUELICO) || estPresent(carte,Entites.FRAISE) || estPresent(carte,Entites.VIOLETTE) || estPresent(carte,Entites.MARGUERITTE) || estPresent(carte,Entites.ROSE) || estPresent(carte,Entites.TULIPE));
    }

    boolean estPresent(Entitee[][] carte, Entites type) {
        for (int Xidx = 0 ; Xidx<length(carte); Xidx=Xidx+1){
            for (int Yidx = 0 ; Yidx<length(carte); Yidx=Yidx+1){
                if (carte[Xidx][Yidx].type == type) {
                    return true;
                }
            }
        }
        return false;
    }

    void testEstPresent(){
        Entitee[][] carteAvecFleurs = new Entitee[3][3];
        for (int x = 0; x<length(carteAvecFleurs); x=x+1){
            for (int y = 0; y<length(carteAvecFleurs); y=y+1){
                carteAvecFleurs[y][x] = creerVide(x,y);
            }
        }
        Entitee[][] carteSansFleurs = carteAvecFleurs;
        carteAvecFleurs[2][2] = newEntitee(Entites.TULIPE, "🌼", 2, 2, 1, 0, 0);
        carteAvecFleurs[1][2] = newEntitee(Entites.TULIPE, "🌼", 2, 2, 1, 0, 0);
        assertEquals(true,estPresent(carteAvecFleurs,Entites.TULIPE));
        assertEquals(false,estPresent(carteSansFleurs,Entites.COQUELICO));
    }

    void testContientUneFleur(){
        Entitee[][] carteSansFleurs = new Entitee[3][3];
        for (int x = 0; x<length(carteSansFleurs,1); x=x+1){
            for (int y = 0; y<length(carteSansFleurs,2); y=y+1){
                carteSansFleurs[y][x] = creerVide(x,y);
            }
        }
        Entitee[][] carteAvecFleurs = new Entitee[3][3];
        for (int x = 0; x<length(carteAvecFleurs,1); x=x+1){
            for (int y = 0; y<length(carteAvecFleurs,2); y=y+1){
                carteAvecFleurs[y][x] = creerVide(x,y);
            }
        }
        carteAvecFleurs[2][2] = newEntitee(Entites.TULIPE, "🌼", 2, 2, 1, 0, 0);
        carteAvecFleurs[1][2] = newEntitee(Entites.COQUELICO, "🌼", 2, 2, 1, 0, 0);
        assertEquals(true,contientUneFleur(carteAvecFleurs));
        assertEquals(false,contientUneFleur(carteSansFleurs));
    }

    int totalPollenCarte(Entitee[][] carte){
        int nbPollen= 0;
        for (int x = 0; x<length(carte);x++){
            for (int y = 0; y<length(carte);y++){
                nbPollen = nbPollen + carte[x][y].pollen;
            }
        }
        return nbPollen;
    }

    void testTotalPollenCarte(){
        Entitee[][] carte = new Entitee[3][3];
        for (int x = 0; x<length(carte); x=x+1){
            for (int y = 0; y<3; y=y+1){
                carte[y][x] = creerVide(x,y);
            }
        }
        carte[2][2] = newEntitee(Entites.TULIPE, "🌼", 2, 2, 1, 0, 0);
        carte[1][2] = newEntitee(Entites.TULIPE, "🌼", 2, 2, 1, 0, 0);
        assertEquals(2,totalPollenCarte(carte));
    }

    String inverserChaine(String chaine) {
        String chaineInverse = "";
        for (int idx = length(chaine)-1; idx>=0 ; idx = idx-1){
            chaineInverse = chaineInverse + charAt(chaine,idx);
        }
        return chaineInverse;
    }

    void testInverserChaine(){
        String chaine = "test";
        String chaineInverse = "tset";
        assertEquals(chaineInverse,inverserChaine(chaine));
    }

    boolean verifChaine(String chaine) {
        int taille = length(chaine)-1;
        while (taille>=0) {
            if ((charAt(chaine, taille) == 'N') || (charAt(chaine, taille) == 'S') || (charAt(chaine, taille) == 'E') || (charAt(chaine, taille) == 'O')){
                taille = taille-1;
            } else {
                return false;
            }
        }
        return true;
    }

    void testVerifChaine(){
        String chaineOK = "NNSSEEOO";
        String chainePasOK = "NNSDS";
        assertEquals(true,verifChaine(chaineOK));
        assertEquals(false,verifChaine(chainePasOK));
    }

    void deplacementSud(Entitee[][] carte, int posYAbeille, int posXAbeille){
        if (posYAbeille+1>=length(carte,2)){
            println("Déplacement hors carte !");
        } else {
            if ((carte[posYAbeille+1][posXAbeille].degats)>=1){
                carte[posYAbeille][posXAbeille].pv = carte[posYAbeille][posXAbeille].pv - carte[posYAbeille+1][posXAbeille].degats;
                println("Vous avez perdu une vie, il vous reste : " + carte[posYAbeille][posXAbeille].pv + " points de vie. Le niveau recommence.");
                carte[posYAbeille][posXAbeille].pollen = 0;
                carte[posYAbeille][posXAbeille] = creerVide(posYAbeille,posXAbeille);
                carte[POS_Y_ABEILLE][POS_X_ABEILLE] = carte[posYAbeille][posXAbeille];
            } else if (carte[posYAbeille+1][posXAbeille].pollen>=1) {
                carte[posYAbeille][posXAbeille].pollen = carte[posYAbeille][posXAbeille].pollen + carte[posYAbeille+1][posXAbeille].pollen;
                println("+1 pollen !" + carte[posYAbeille][posXAbeille].pollen);
                carte[posYAbeille+1][posXAbeille] = carte[posYAbeille][posXAbeille];
                carte[posYAbeille][posXAbeille] = creerVide(posYAbeille, posXAbeille);
            } else {
                carte[posYAbeille+1][posXAbeille] = carte[posYAbeille][posXAbeille];
                carte[posYAbeille][posXAbeille] = creerVide(posYAbeille,posXAbeille);
            }
        }
    }
    void deplacementNord(Entitee[][] carte, int posYAbeille, int posXAbeille){
        if (posYAbeille-1<0){
            println("Déplacement hors carte !");
        } else {
            if ((carte[posYAbeille-1][posXAbeille].degats)>=1){
                carte[posYAbeille][posXAbeille].pv = carte[posYAbeille][posXAbeille].pv - carte[posYAbeille-1][posXAbeille].degats;
                println("Vous avez perdu une vie, il vous reste : " + carte[posYAbeille][posXAbeille].pv + " points de vie. Le niveau recommence.");
                carte[posYAbeille][posXAbeille].pollen = 0;
                carte[posYAbeille][posXAbeille] = creerVide(posYAbeille,posXAbeille);
                carte[POS_Y_ABEILLE][POS_X_ABEILLE] = carte[posYAbeille][posXAbeille];
            } else if (carte[posYAbeille-1][posXAbeille].pollen>=1) {
                carte[posYAbeille][posXAbeille].pollen = carte[posYAbeille][posXAbeille].pollen + carte[posYAbeille-1][posXAbeille].pollen;
                println("+1 pollen !"+ carte[posYAbeille][posXAbeille].pollen);
                carte[posYAbeille-1][posXAbeille] = carte[posYAbeille][posXAbeille];
                carte[posYAbeille][posXAbeille] = creerVide(posYAbeille, posXAbeille);
            } else {
                carte[posYAbeille-1][posXAbeille] = carte[posYAbeille][posXAbeille];
                carte[posYAbeille][posXAbeille] = creerVide(posYAbeille,posXAbeille);
            }
        }
    }
    void deplacementOuest(Entitee[][] carte, int posYAbeille, int posXAbeille){
        if (posXAbeille-1<0){
            println("Déplacement hors carte !");
        } else {
            if ((carte[posYAbeille][posXAbeille-1].degats)>=1){
                carte[posYAbeille][posXAbeille].pv = carte[posYAbeille][posXAbeille].pv - carte[posYAbeille][posXAbeille-1].degats;
                println("Vous avez perdu une vie, il vous reste : " + carte[posYAbeille][posXAbeille].pv + " points de vie. Le niveau recommence.");
                carte[posYAbeille][posXAbeille].pollen = 0;
                carte[posYAbeille][posXAbeille] = creerVide(posYAbeille,posXAbeille);
                carte[POS_Y_ABEILLE][POS_X_ABEILLE] = carte[posYAbeille][posXAbeille];

            } else if (carte[posYAbeille][posXAbeille-1].pollen>=1) {
                carte[posYAbeille][posXAbeille].pollen = carte[posYAbeille][posXAbeille].pollen + carte[posYAbeille][posXAbeille-1].pollen;
                println("+1 pollen !"+ carte[posYAbeille][posXAbeille].pollen);
                carte[posYAbeille][posXAbeille-1] = carte[posYAbeille][posXAbeille];
                carte[posYAbeille][posXAbeille] = creerVide(posYAbeille, posXAbeille);

            } else {
                carte[posYAbeille][posXAbeille-1] = carte[posYAbeille][posXAbeille];
                carte[posYAbeille][posXAbeille] = creerVide(posYAbeille,posXAbeille);
            }
        }
    }
    void deplacementEst(Entitee[][] carte, int posYAbeille, int posXAbeille){
        if (posXAbeille+1<=length(carte,1)){
            println("déplacement hors carte !");
        } else {
            if ((carte[posYAbeille][posXAbeille+1].degats)>=1){
                carte[posYAbeille][posXAbeille].pv = carte[posYAbeille][posXAbeille].pv - carte[posYAbeille][posXAbeille+1].degats;
                println("Vous avez perdu une vie, il vous reste : " + carte[posYAbeille][posXAbeille].pv + " points de vie. Le niveau recommence.");
                carte[posYAbeille][posXAbeille].pollen = 0;
                carte[posYAbeille][posXAbeille] = creerVide(posYAbeille,posXAbeille);
                carte[POS_Y_ABEILLE][POS_X_ABEILLE] = carte[posYAbeille][posXAbeille];
            } else if (carte[posYAbeille][posXAbeille+1].pollen>=1) {
                carte[posYAbeille][posXAbeille].pollen = carte[posYAbeille][posXAbeille].pollen + carte[posYAbeille][posXAbeille+1].pollen;
                println("+1 pollen !"+ carte[posYAbeille][posXAbeille].pollen);
                carte[posYAbeille][posXAbeille+1] = carte[posYAbeille][posXAbeille];
                carte[posYAbeille][posXAbeille] = creerVide(posYAbeille, posXAbeille);
            } else {
                carte[posYAbeille][posXAbeille+1] = carte[posYAbeille][posXAbeille];
                carte[posYAbeille][posXAbeille] = creerVide(posYAbeille,posXAbeille);
            }
        }
    }

    //Globalement les fonctions de déplacements sont toutes les mêmes pratiquement. Par lisibilité, je fais qu'un seul test pour les 4 fonction deplacements
    /*void testDeplacements(){
        Entitee[][] carte = new Entitee[3][3];
        for (int x = 0; x<length(carte); x=x+1){
            for (int y = 0; y<3; y=y+1){
                carte[y][x] = creerVide(x,y);
            }
        }
        carte[1][0] = newEntitee(Entites.TULIPE, "🌼", 2, 2, 1, 0, 0);
        placerRucheAbeille(carte,0,0);

        Entitee[][] carte = new Entitee[3][3];
        for (int x = 0; x<length(carte); x=x+1){
            for (int y = 0; y<3; y=y+1){
                carte[y][x] = creerVide(x,y);
            }
        }
        carte[1][0] = newEntitee(Entites.TULIPE, "🌼", 2, 2, 1, 0, 0);
        placerRucheAbeille(carte,0,0);

    }*/

    void perteVie(Entitee e,int nbPVPerdu){
        e.pv = e.pv - nbPVPerdu;
    }

    void credit(){
        println("═════════════════════════════════════════");
        println("HappyBee - 18/12/2022");
        println("═════════════════════════════════════════");
        delay(1000);
        println("Réalisé par : DORNY Nathan");
        delay(1000);
        println("IUT A de Lille - Villeneuve d'Ascq");
        delay(1000);
        println("Contact : 0606060606 | mail : nathan.dorny.etu@univ-lille.fr");
        delay(1000);
        println("HappyBee - Tout Droits réservés");
        println("═════════════════════════════════════════");
    }
    
    boolean niveauUn(String cheminAParcourrir, Entitee[][] carteNiveauUn, int xAbeille, int yAbeille, int xRuche, int yRuche, int pollenNiveauUn){
        boolean niveauValide = false;
            while (length(cheminAParcourrir)>0) {
                    if ((charAt(cheminAParcourrir,0)=='N') && (length(cheminAParcourrir)>0)) {
                        cheminAParcourrir = substring(cheminAParcourrir,1,length(cheminAParcourrir));
                        deplacementNord(carteNiveauUn, yAbeille,xAbeille);
                        yAbeille = yAbeille-1;
                    } 
                    else if ((charAt(cheminAParcourrir,0)=='S') && (length(cheminAParcourrir)>0)) {
                        cheminAParcourrir = substring(cheminAParcourrir,1,length(cheminAParcourrir));
                        deplacementSud(carteNiveauUn,yAbeille, xAbeille);
                        yAbeille = yAbeille+1;
                    } 
                    else if ((charAt(cheminAParcourrir,0)=='E') && (length(cheminAParcourrir)>0)) {
                        cheminAParcourrir = substring(cheminAParcourrir,1,length(cheminAParcourrir));
                        deplacementEst(carteNiveauUn,yAbeille,xAbeille);
                        xAbeille = xAbeille+1;
                    } 
                    else if ((charAt(cheminAParcourrir,0)=='O') && (length(cheminAParcourrir)>0)) {
                        cheminAParcourrir = substring(cheminAParcourrir,1,length(cheminAParcourrir));
                        deplacementOuest(carteNiveauUn,yAbeille, xAbeille);
                        xAbeille = xAbeille-1;
                    }
                    println(toStr(carteNiveauUn, xAbeille, yAbeille));
                    delay(1000);
            }
            if ((xAbeille == xRuche) && (yAbeille == yRuche) && (pollenNiveauUn==carteNiveauUn[yAbeille][xAbeille].pollen)) {
                /*println(xAbeille + " devrait être égal à " + xRuche);
                println(yAbeille + " devrait être égal à " + yRuche);*/
                return true;
            } else {
                /*println(xAbeille + " devrait être égal à " + xRuche);
                println(yAbeille + " devrait être égal à " + yRuche);*/
                return false;
            }
    }

    void algorithm() {
        // Menu d'arrivé, l'utilisateur dispose de 3 choix : 1. [Nouvelle partie] (pour jouer les 3 niveau) | 2. [Aide] | 3. [Crédit]
        println("Bievenue sur HappyBee ! \n Veuillez choisir l'un des choix ci-dessous :");
            println("═════════════════════════════════════════");
            println("1. [Nouvelle Partie]");
            println("2. [AIDE & règles de jeu]");
            println("3. [Crédit]");
            println("4. [Sortir]");
            println("═════════════════════════════════════════");
            int choix = readChar();
            boolean selectionExecutee = false;
        while ((!((int) choix<0) || !((int) choix>=4)) && (selectionExecutee != true) ){
            if (choix == '1'){
                println("═════════════════════════════════════════");
                println("Vous avez choisit de Lancer une partie HappyBee ! Amusez-vous bien !");
                println("═════════════════════════════════════════");

                //Génération de la carte aléatoire
                
                for (double cpt = 0.00; cpt<=0.07; cpt = cpt+0.01){
                    Entitee[][] carteNiveauUn = new Entitee[TAILLE_1][TAILLE_1];
                    int xAbeille = length(carteNiveauUn,1)-1;
                    int yAbeille = length(carteNiveauUn,2)-1;
                    int yRuche = length(carteNiveauUn,1)-1;
                    int xRuche = 0;
                    carteNiveauUn = creerNiveauUn(PROBA_HOSTILE_1+cpt,PROBA_FLEUR_1+cpt,5, xAbeille,yAbeille);
                while (contientUneFleur(carteNiveauUn) != true) {
                    carteNiveauUn = creerNiveauUn(PROBA_HOSTILE_1+cpt,PROBA_FLEUR_1+cpt,5,xAbeille,yAbeille);
                }

                
                
                println(toStr(carteNiveauUn,xAbeille,yAbeille));
                int pollenNiveauUn = totalPollenCarte(carteNiveauUn);
                println("═════════════════════════════════════════");
                println("Vous devez ramasser : " + pollenNiveauUn + " Pollen sur les fleurs. Pour vous déplacer, veuillez remplir la suite d'instruction à l'abeille  N pour se déplacer au nord par exemple");
                println("═════════════════════════════════════════");
                String cheminAParcourrir = readString();
                    println(niveauUn(cheminAParcourrir, carteNiveauUn,xAbeille,yAbeille,xRuche,yRuche,pollenNiveauUn));
                }
                println("Fin ! Merci d'avoir joué, ceci était une version Béta non terminé.");
                

                ///////////////////////////////////
                println("═════════════════════════════════════════");
                println("1. [Nouvelle Partie]");
                println("2. [AIDE & règles de jeu]");
                println("3. [Crédit]");
                println("4. [Sortir]");
                println("═════════════════════════════════════════");
                choix = readChar();
                ///////////////////////////////////
            } else if (choix == '2'){
                println("═════════════════════════════════════════");
                println("Vous avez choisit de consulter les aides");
                println("═════════════════════════════════════════");
                println("Pour déplacer l'abeille dans la carte, il suffis de lister les déplacements de l'abeille avec les lettres N S E O (nord sud est ouest), il faut que l'abeille retourne à sa ruche en ayant ramasser tout le pollen de chaques fleurs en passant dessus.");
                ///////////////////////////////////
                println("═════════════════════════════════════════");
                println("1. [Nouvelle Partie]");
                println("2. [AIDE & règles de jeu]");
                println("3. [Crédit]");
                println("4. [Sortir]");
                println("═════════════════════════════════════════");
                choix = readChar();
                ///////////////////////////////////
            } else if (choix =='3' ) {
                println("═════════════════════════════════════════");
                println("Vous avez choisit les crédits : ");
                println("═════════════════════════════════════════");
                credit();
                ///////////////////////////////////
                println("═════════════════════════════════════════");
                println("1. [Nouvelle Partie]");
                println("2. [AIDE & règles de jeu]");
                println("3. [Crédit]");
                println("4. [Sortir]");
                println("═════════════════════════════════════════");
                choix = readChar();
                ///////////////////////////////////
            } else if (choix == '4'){
                println("═════════════════════════════════════════");
                println("Merci d'avoir joué !");
                println("═════════════════════════════════════════");
                selectionExecutee = true;
            } else {
                println("Entrée Invalide, veuillez entrez le numéro correspondant à votre choix. (indiquez le numéro 1 2 ou 3");
                ///////////////////////////////////
                println("═════════════════════════════════════════");
                println("1. [Nouvelle Partie]");
                println("2. [AIDE & règles de jeu]");
                println("3. [Crédit]");
                println("4. [Sortir]");
                println("═════════════════════════════════════════");
                choix = readChar();
                ///////////////////////////////////
            }
        }
        /* Je viens de "finir" mais je suis extremement frustré de ne pas avoir pus faire tout ce que je voulais faire, 
                je suis assez déprimé de rendre un travail aussi peu aboutis, j'en suis désoler. Je me suis retrouvé tout seul 
                lors de la réalisation de cette SAé, je ne veux aucunement remettre la faute sur qui ce soit, cependant mon binôme
                 m'a abandonné en me laissant les deux dernière SAé sans compté les ratrapage de DS. Même si je sais qu'il sera trop tard, 
                 je vous enverrai une autre version de HappyBee, c'est un projet qui m'a beaucoup plut malgrès tout. Créer un "jeu" a 
                 toujours été un objectif pour moi, en 4e j'ai réalisé sur sccratch une imitation du jeu plante contre zombie. Je suis 
                 assez déçu de voir que je ne peux compter sur personne pour m'aider sur les SAé, y aurait-il moyen de trouver une solution 
                 à l'avenir pour imposer des groupes ? Cela permettera à tous de ne pas faire de mauvaises décisions. 
                 Certaines fonctions n'ont pas de test, sinon je n'aurai pas pus finaliser les niveau*/
    }
}
