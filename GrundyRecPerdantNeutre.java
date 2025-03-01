import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Jeu de Grundy avec IA pour la machine
 * Ce programme contient les méthodes permettant de tester jouerGagnant() et le test d'efficacité testEstGagnanteEfficacite()
 * Cette version contient le programme fonctionnel amélioré pour se souvenir des situations perdantes et gagnantes et utiliser le théorème 3.4
 *
 * @author E.Thevenin et E.Yvin
 */
class GrundyRecPerdantNeutre {

    /** Compteur d'opérations élémentaires */
    long cpt = 0;

    /** Liste des situations perdantes */
    ArrayList<ArrayList<Integer>> posPerdantes = new ArrayList<ArrayList<Integer>>();

    /** Liste des situations gagnantes */
    ArrayList<ArrayList<Integer>> posGagnantes = new ArrayList<ArrayList<Integer>>();


    /**
     * Méthode principale du programme
     */
    void principal() {

        // leJeu();
        testEstGagnanteEfficacite();
        testAfficherJeu();
        testJouerGagnant();
        testPremier();
        testSuivant();
        testEstConnuePerdante();
        testEstConnueGagnante();
        testSupprimePerdantes();

    }

    /**
     * Démarre le jeu de Grundy.
     * Le jeu se déroule en alternance entre la machine et le joueur.
     * La machine commence si l'utilisateur le souhaite, sinon c'est le joueur qui commence.
     * Le jeu se termine si l'un des joueurs ne peut plus jouer.
     * Le gagnant est celui qui peut jouer en dernier.
     */
    void leJeu(){
        System.out.println("--------GRUNDY V3--------\n");
        ArrayList<Integer> jeu = new ArrayList<Integer>();
        int nbAllumettes = SimpleInput.getInt("Choisissez le nombre d'allumettes de depart : ");
        jeu.add(nbAllumettes);
        System.out.print("Jeu : ");
        afficherJeu(jeu);

        boolean jeuPossible = true;
        boolean jeuGagnant = true;
        int tas = -1;
        int nbAllumettesJoueur;

        int turncpt = SimpleInput.getInt("Qui commence ? 0 pour la machine, 1 pour le joueur : ");
        while (turncpt != 0 && turncpt != 1) {
            turncpt = SimpleInput.getInt("Nombre non valide. Qui commence ? 0 pour la machine, 1 pour le joueur : ");
        }
        while (jeuPossible) {
            if(turncpt == 0){
                turncpt=1;
                System.out.println("--------Tour de la machine--------\n");
                jeuGagnant=jouerGagnant(jeu);
                if (jeuGagnant==false) { //si il n'y a pas de coup gagnant, on joue au hasard
                    int taille = jeu.size();

                    tas = (int) (Math.random()*taille);
                    while (tas < 0 || tas >= jeu.size() || jeu.get(tas) <3 ) {
                        tas = (int) (Math.random()*taille);
                    }

                    nbAllumettesJoueur = (int) (Math.random()*jeu.get(tas)-1)+1;
                    while (nbAllumettesJoueur <= 0 || nbAllumettesJoueur >= jeu.get(tas) || 2*nbAllumettesJoueur == jeu.get(tas)) {
                        nbAllumettesJoueur = (int) (Math.random()*jeu.get(tas)-1)+1;
                    }
                    enlever(jeu, tas, nbAllumettesJoueur);
                }
                afficherJeu(jeu);
            }else{
                turncpt=0;
                System.out.println("--------Tour du joueur--------\n");

                tas = SimpleInput.getInt("Choisissez le tas (indice dans le tableau): ");

                while (tas < 0 || tas >= jeu.size() || jeu.get(tas) <3 ) {
                    tas = SimpleInput.getInt("Tas non valide. Choisissez le tas (indice dans le tableau): ");
                }

                nbAllumettesJoueur = SimpleInput.getInt("Choisissez le nombre d'allumettes a retirer : ");
                while (nbAllumettesJoueur <= 0 || nbAllumettesJoueur >= jeu.get(tas) || 2*nbAllumettesJoueur == jeu.get(tas)) {
                    nbAllumettesJoueur = SimpleInput.getInt("Nombre d'allumettes non valide. Choisissez le nombre d'allumettes a retirer : ");

                }

                enlever(jeu, tas, nbAllumettesJoueur);
                afficherJeu(jeu);

            }
            
            jeuPossible = estPossible(jeu);
        }

        if(turncpt == 0){            
            System.out.println("--------GAGNANT : JOUEUR--------" );
        }else{
        System.out.println("--------GAGNANT : MACHINE--------" );
        }
    }
    
    /**
     * Affiche le jeu en format visuel
     *
     * @param tabCoeff tableau des coefficients des tas
     */
    void afficherJeu(ArrayList<Integer> tabCoeff) {
        String[] jeu = new String [tabCoeff.size()];

        for (int i=0; i<tabCoeff.size();i++){
            int coeff = tabCoeff.get(i);
            jeu[i]=i+" : ";

            for(int j=0; j<coeff;j++){                
                jeu[i] = jeu[i]+"|";

            }
        }
        System.out.println("\n----------------------------------------------------------------------------");
        for(int k=0; k<jeu.length;k++){
            System.out.print("  " + jeu[k] + "  ");
        }
        System.out.println();
        System.out.println("----------------------------------------------------------------------------\n");
    }

    /**
     * Tests de la méthode afficherJeu()
     * 
     */
    void testAfficherJeu() {
        System.out.println("testafficherJeu() : \n");

        //Test visuels
    
        System.out.println(" Cas normaux \n");
    
        ArrayList<Integer> tabCoeff1 = new ArrayList<>();
        tabCoeff1.add(3);

        System.out.println("afficherJeu("+tabCoeff1.toString()+")");
        afficherJeu(tabCoeff1);
    
        ArrayList<Integer> tabCoeff2 = new ArrayList<>();
        tabCoeff2.add(1);
        tabCoeff2.add(4);
        tabCoeff2.add(2);
        System.out.println("afficherJeu("+tabCoeff2.toString()+")");
        afficherJeu(tabCoeff2);
    
        System.out.println("\n Cas limites \n");
    
        ArrayList<Integer> tabCoeff3 = new ArrayList<>();
        System.out.println("afficherJeu("+tabCoeff3.toString()+")");
        afficherJeu(tabCoeff3); // Liste vide
    
        ArrayList<Integer> tabCoeff4 = new ArrayList<>();
        tabCoeff4.add(0);
        tabCoeff4.add(0);
        tabCoeff4.add(3);
        System.out.println("afficherJeu("+tabCoeff4.toString()+")");
        afficherJeu(tabCoeff4);
    
        ArrayList<Integer> tabCoeff5 = new ArrayList<>();
        tabCoeff5.add(0);
        tabCoeff5.add(2);
        tabCoeff5.add(5);
        tabCoeff5.add(0);
        System.out.println("afficherJeu("+tabCoeff5.toString()+")");
        afficherJeu(tabCoeff5);
    
    }

    /**
     * Joue le coup gagnant s'il existe
     *
     * @param jeu plateau de jeu
     * @return vrai s'il y a un coup gagnant, faux sinon
     */
    boolean jouerGagnant(ArrayList<Integer> jeu) {

        boolean gagnant = false;

        if (jeu == null) {
            System.err.println("jouerGagnant(): le parametre jeu est null");
        }else if(estConnueGagnante(jeu)){
            gagnant = true;
        } else {
            ArrayList<Integer> essai = new ArrayList<Integer>();

            // Une toute première décomposition est effectuée à partir de jeu.
            // Cette première décomposition du jeu est enregistrée dans essai.
            // tas est le numéro de la case du tableau ArrayList (qui commence à zéro) qui
            // mémorise le tas (nbre d'allumettes) qui a été décomposé
            int tas = premier(jeu, essai);

            // mise en oeuvre de la règle numéro2
            // Une situation (ou position) est dite gagnante pour la machine, s’il existe AU MOINS UNE décomposition
            // (c-à-d UNE action qui consiste à décomposer un tas en 2 tas inégaux) perdante pour l’adversaire. C'est
            // évidemment cette décomposition perdante qui sera choisie par la machine.
            while (tas != -1 && !gagnant) {
                if (estPerdante(essai)) { // estPerdante est récursif
                    // estPerdante (pour l'adversaire) à true ===> Bingo essai est la décomposition choisie par la machine qui est alors
                    // certaine de gagner !!
                    jeu.clear();
                    gagnant = true;
                    // essai est recopié dans jeu car essai est la nouvelle situation de jeu après que la machine ait joué (gagnant)
                    for (int i = 0; i < essai.size(); i++) {
                        jeu.add(essai.get(i));
                    }
                } else {
                    // estPerdante à false ===> la machine essaye une autre décomposition en faisant appel à "suivant".
                    // Si, après exécution dex suivant, tas est à (-1) alors il n'y a plus de décomposition possible à partir de jeu (et on sort du while).
                    // En d'autres mots : la machine n'a PAS trouvé à partir de jeu UNE décomposition gagnante.

                    //on essaie une autre decomposition
                    tas = suivant(jeu, essai, tas);
                }
            }


        }

        return gagnant;
    }

    /**
     * Méthode RECURSIVE qui indique si la configuration (du jeu actuel ou jeu d'essai) est perdante.
     * Cette méthode est utilisée par la machine pour savoir si l'adversaire peut perdre (à 100%).
     *
     * @param jeu plateau de jeu actuel (l'état du jeu à un certain moment au cours de la partie)
     * @return vrai si la configuration (du jeu) est perdante, faux sinon
     */
    boolean estPerdante(ArrayList<Integer> jeuNonTrim) {
        boolean ret = true; // par défaut la configuration est perdante

        ArrayList<Integer> jeu = supprimePerdantes(jeuNonTrim); // on supprime les configurations perdantes
        boolean connuePerdante = estConnuePerdante(jeu);
        boolean connueGagnante = estConnueGagnante(jeu);
        
        int tas = 0;
        ArrayList<Integer> essai = new ArrayList<Integer>(); // size = 0 !

        if (jeu == null) {
            System.err.println("estPerdante(): le paramètre jeu est null");
        }else if (connuePerdante) {
            ret = true;
        }else if (connueGagnante) {
            ret = false;
        }else if (jeu.size()==0) { // si le jeu est vide, alors la situation est forcemente perdante
            ret = true;
        }else {
            // si il n'y a plus que des tas de 1 ou 2 allumettes dans le plateau de jeu
            // alors la situation est forcément perdante (ret=true) = FIN de la récursivité
            if ( !estPossible(jeu) ) {
                ret = true;
            }else {
                // création d'un jeu d'essais qui va examiner toutes les décompositions
                // possibles à partir de jeu

                // toute première décomposition : enlever 1 allumette au premier tas qui possède
                // au moins 3 allumettes, tas = -1 signifie qu'il n'y a plus de tas d'au moins 3 allumettes
                tas = premier(jeu, essai);
                while ( (tas != -1) && ret) {
                    // mise en oeuvre de la règle numéro1
                    // Une situation (ou position) est dite perdante si et seulement si TOUTES ses décompositions possibles
                    // (c-à-d TOUTES les actions qui consistent à décomposer un tas en 2 tas inégaux) sont TOUTES gagnantes
                    // (pour l’adversaire).
                    // L'appel à "estPerdante" est RECURSIF.
                    // Si "estPerdante(essai)" est true c'est équivalent à "estGagnante" est false, la décomposition
                    // essai n'est donc pas gagnante, on sort du while et on renvoie false.
                    if (estPerdante(essai) == true) {
                        // Si UNE SEULE décomposition (à partir du jeu) est perdante (pour l'adversaire) alors le jeu n'EST PAS perdant.
                        // On renverra donc false : la situation (jeu) n'est PAS perdante.
                        ret = false;

                        //On ajoute le jeu aux gagnantes
                        ArrayList<Integer> copy = supprimePerdantes(jeuNonTrim);
                        //Normalisation
                        copy.sort(null);
                        copy.removeIf(n->n<3);
                        //ajout aux gagnantes
                        posGagnantes.add(copy);
                    } else {
                        // génère la configuration d'essai suivante (c'est-à-dire UNE décomposition possible)
                        // à partir du jeu, si tas = -1 il n'y a plus de décomposition possible
                        tas = suivant(jeu, essai, tas);
                    }
                    cpt++;
                }
            }
        }
        if(tas==-1){
            //essai est une configuration perdante
            //Normalisation de l'essai
            essai.sort(null);
            essai.removeIf(n->n<3);

            //On ajoute l'essai aux perdantes
            posPerdantes.add(essai);
        }
        return ret;
    }

    /**
     * Supprime les éléments perdants d'une situation.
     * Pour cela, on itère sur chaque élément du jeu et on vérifie si la situation qui en résulte est connue comme perdante.
     * Si c'est le cas, on supprime cet élément.
     * La méthode renvoie la liste des éléments restants.
     *
     * @param jeu plateau de jeu actuel
     * @return la liste des éléments qui ne sont pas perdants
     */
    ArrayList<Integer> supprimePerdantes(ArrayList<Integer> jeu) {
        ArrayList<Integer> ret = new ArrayList<Integer>(jeu); //copie du jeu
        boolean perdante = estConnuePerdante(jeu); //si la situation est connue comme perdante

        int count = 0; //compte le nombre de tas perdants

        for (int i = 0; i < jeu.size() && !perdante; i++) {
            ArrayList<Integer> essai = new ArrayList<Integer>(); //
            essai.add(jeu.get(i));
            if (estConnuePerdante(essai)) { //si l'essai est connu comme perdant
                ret.remove(i-count); //on supprime le tas perdant
                count++; //on incrémente le nombre de tas perdants
                perdante=estConnuePerdante(jeu);
            }
        }

        return ret;
    }

    /**
     * Tests supprimePerdantes().
     */
    void testSupprimePerdantes() {
        System.out.println("testSupprimePerdantes()");
    
        //Loosing positions initialization
        posPerdantes = new ArrayList<>();
    
        // Addition of losing positions
        ArrayList<Integer> perdante1 = new ArrayList<>();
        perdante1.add(3);
        posPerdantes.add(perdante1);
    
        ArrayList<Integer> perdante2 = new ArrayList<>();
        perdante2.add(5);
        posPerdantes.add(perdante2);
    
        System.out.println("Test cas normaux");
    
        ArrayList<Integer> jeu1 = new ArrayList<>();
        jeu1.add(4); 
        jeu1.add(6); 
        ArrayList<Integer> attendu1 = new ArrayList<>(jeu1);
        testCasSupprimePerdantes(jeu1, attendu1);
    
        ArrayList<Integer> jeu2 = new ArrayList<>();
        jeu2.add(3); 
        jeu2.add(4); 
        ArrayList<Integer> attendu2 = new ArrayList<>();
        attendu2.add(4); 
        testCasSupprimePerdantes(jeu2, attendu2);
    
        System.out.println("Test cas limites");
    
        ArrayList<Integer> jeu3 = new ArrayList<>();
        jeu3.add(3); 
        jeu3.add(5); 
        ArrayList<Integer> attendu3 = new ArrayList<>(); 
        testCasSupprimePerdantes(jeu3, attendu3);
    
        ArrayList<Integer> jeu4 = new ArrayList<>();
        ArrayList<Integer> attendu4 = new ArrayList<>(); 
        testCasSupprimePerdantes(jeu4, attendu4);
    
        ArrayList<Integer> jeu5 = new ArrayList<>();
        jeu5.add(6); 
        jeu5.add(8); 
        ArrayList<Integer> attendu5 = new ArrayList<>(jeu5); 
        testCasSupprimePerdantes(jeu5, attendu5);
    }
    
    /**
     * Tests a specific case of the supprimePerdantes method.
     *
     * @param jeu the initial game state to test
     * @param attendu the expected game state after removing losing configurations
     */
    void testCasSupprimePerdantes(ArrayList<Integer> jeu, ArrayList<Integer> attendu) {
        System.out.print("supprimePerdantes(" + jeu + ") : -> ");
        ArrayList<Integer> obtenu = supprimePerdantes(jeu);
        if (attendu.equals(obtenu)) {
            System.out.println("OK");
        } else {
            System.err.println("ERREUR, obtenu : " + obtenu + " attendu : " + attendu);
        }
    }
    
    /**
     * Vérifie si la configuration donnée du jeu est déjà connue comme perdante.
     *
     * @param jeu plateau de jeu actuel
     * @return vrai si la configuration est répertoriée comme perdante, faux sinon
     */
    boolean estConnuePerdante(ArrayList<Integer> jeu) {
        //On copie le jeu pour ne pas le modifier
        ArrayList<Integer> copy = new ArrayList<>(jeu);

        //Normalisation de la copie
        copy.sort(null);
        copy.removeIf(n->n<3);

        if (posPerdantes.contains(copy)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Vérifie si la configuration donnée du jeu est déjà connue comme gagnante.
     *
     * @param jeu plateau de jeu actuel
     * @return vrai si la configuration est répertoriée comme gagnante, faux sinon
     */
    boolean estConnueGagnante(ArrayList<Integer> jeu) {
        //On copie le jeu pour ne pas le modifier
        ArrayList<Integer> copy = new ArrayList<>(jeu);

        //Normalisation de la copie
        copy.sort(null);
        copy.removeIf(n->n<3);

        if (posGagnantes.contains(copy)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Indique si la configuration est gagnante.
     * Méthode qui appelle simplement "estPerdante".
     *
     * @param jeu plateau de jeu
     * @return vrai si la configuration est gagnante, faux sinon
     */
    boolean estGagnante(ArrayList<Integer> jeu) {
        boolean ret = false;
        if (jeu == null) {
            System.err.println("estGagnante(): le parametre jeu est null");
        } else {
            if(estConnueGagnante(jeu)) {
                ret = true;
            }else{
                ret = !estPerdante(jeu);
            } 
        }
        return ret;
    }

    /**
     * Méthode de test pour mesurer l'efficacité de la méthode estGagnante en fonction de la taille du plateau de jeu.
     * La méthode appelle estGagnante pour des tailles de plateau de jeu croissantes (1 à 20 par exemple)
     * et affiche pour chaque taille :
     *   - le nombre d'opérations élémentaires effectuées par estGagnante (compteur cpt)
     *   - le temps d'exécution de estGagnante en millisecondes (mesuré avec System.nanoTime()).
     */
    void testEstGagnanteEfficacite() {
        System.out.println("testEstGagnanteEfficacite()");
        
        ArrayList<Integer> jeu;
        long debut, fin, diffT;
        diffT = 0;
        int n =3;
        while (diffT<500000000) { 
            posPerdantes.clear(); //Réinitialisation des configurations connues comme perdantes
            posGagnantes.clear(); //Réinitialisation des configurations connues comme gagnantes
            jeu = new ArrayList<Integer>(); //Réinitialisation du jeu
            jeu.add(n); // tas contenant n allumettes
    
            cpt = 0; // Réinitialiser le compteur global
            debut = System.nanoTime(); // Début du chronométrage
    
            estGagnante(jeu); // Appel de la méthode principale
    
            fin = System.nanoTime(); // Fin du chronométrage
            diffT = (fin - debut); // Difference de temps
    
            // Affichage des résultats
            System.out.println("n = " + n + "; Nb Operations = " + cpt + "; Temps (nanosecondes) = " + diffT);

            n++;
        }
    }
    
    /**
     * Tests succincts de la méthode joueurGagnant()
     */
    void testJouerGagnant() {
        System.out.println();
        System.out.println("*** testJouerGagnant() ***");

        System.out.println("Test des cas normaux");
        ArrayList<Integer> jeu1 = new ArrayList<Integer>();
        jeu1.add(6);
        ArrayList<Integer> resJeu1 = new ArrayList<Integer>();
        resJeu1.add(4);
        resJeu1.add(2);

        testCasJouerGagnant(jeu1, resJeu1, true);

    }

    /**
     * Test d'un cas de la méthode jouerGagnant()
     *
     * @param jeu le plateau de jeu
     * @param resJeu le plateau de jeu après avoir joué gagnant
     * @param res le résultat attendu par jouerGagnant
     */
    void testCasJouerGagnant(ArrayList<Integer> jeu, ArrayList<Integer> resJeu, boolean res) {
        // Arrange
        System.out.print("jouerGagnant (" + jeu.toString() + ") : ");

        // Act
        boolean resExec = jouerGagnant(jeu);

        // Assert
        System.out.print(jeu.toString() + " " + resExec + " : ");
        boolean egaliteJeux = jeu.equals(resJeu);
        if (  egaliteJeux && (res == resExec) ) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERREUR, obtenu : " + resExec + " attendu : " + res);
        }
    }

    /**
     * Divise en deux tas les allumettes d'une tas de jeu (1 tas = 1 tas).
     * Le nouveau tas se place nécessairement en fin de tableau.
     * Le tas qui est divisé diminue du nombre d'allumettes enlevées.
     *
     * @param jeu   tableau des allumettes par tas
     * @param tas tas pour lequel les allumettes doivent être séparées
     * @param nb    nombre d'allumettes RETIREE du tas (tas) lors de la séparation
     */
    void enlever ( ArrayList<Integer> jeu, int tas, int nb ) {
        // traitement des erreurs
        if (jeu == null) {
            System.err.println("enlever() : le parametre jeu est null");
        } else if (tas >= jeu.size()) {
            System.err.println("enlever() : le numero de tas est trop grand");
        } else if (tas < 0) {
            System.err.println("enlever() : le numero de tas est trop petit");
        } else if (nb >= jeu.get(tas)) {
            System.err.println("enlever() : le nb d'allumettes a retirer est trop grand");
        } else if (nb <= 0) {
            System.err.println("enlever() : le nb d'allumettes a retirer est trop petit");
        } else if (2 * nb == jeu.get(tas)) {
            System.err.println("enlever() : le nb d'allumettes a retirer est la moitie");
        } else {
            // nouveau tas ajouté au jeu (nécessairement en fin de tableau)
            // ce nouveau tas contient le nbre d'allumettes retirées (nb) du tas à séparer
            jeu.add(nb);
            // le tas restant possède "nb" allumettes en moins
            jeu.set ( tas, (jeu.get(tas) - nb) );
        }
    }

    /**
     * Teste s'il est possible de séparer un des tas
     *
     * @param jeu      plateau de jeu
     * @return vrai s'il existe au moins un tas de 3 allumettes ou plus, faux sinon
     */
    boolean estPossible(ArrayList<Integer> jeu) {
        boolean ret = false;
        if (jeu == null) {
            System.err.println("estPossible(): le paramètre jeu est null");
        } else {
            int i = 0;
            while (i < jeu.size() && !ret) {
                if (jeu.get(i) > 2) {
                    ret = true;
                }
                i = i + 1;
            }
        }
        return ret;
    }

    /**
     * Crée une toute première configuration d'essai à partir du jeu
     *
     * @param jeu      plateau de jeu
     * @param jeuEssai nouvelle configuration du jeu
     * @return le numéro du tas divisé en deux ou (-1) si il n'y a pas de tas d'au moins 3 allumettes
     */
    int premier(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai) {

        int numTas = -1; // pas de tas à séparer par défaut
        int i;

        if (jeu == null) {
            System.err.println("premier(): le parametre jeu est null");
        } else if (!estPossible((jeu)) ){
            System.err.println("premier(): aucun tas n'est divisible");
        } else if (jeuEssai == null) {
            System.err.println("premier(): le parametre jeuEssai est null");
        } else {
            // avant la copie du jeu dans jeuEssai il y a un reset de jeuEssai
            jeuEssai.clear(); // size = 0
            i = 0;

            // recopie case par case de jeu dans jeuEssai
            // jeuEssai est le même que le jeu avant la première configuration d'essai
            while (i < jeu.size()) {
                jeuEssai.add(jeu.get(i));
                i = i + 1;
            }

            i = 0;
            // rechercher un tas d'allumettes d'au moins 3 allumettes dans le jeu
            // sinon numTas = -1
            boolean trouve = false;
            while ( (i < jeu.size()) && !trouve) {

                // si on trouve un tas d'au moins 3 allumettes
                if ( jeuEssai.get(i) >= 3 ) {
                    trouve = true;
                    numTas = i;
                }

                i = i + 1;
            }

            // sépare le tas (case numTas) en un nouveau tas d'UNE SEULE allumette qui vient se placer en fin du tableau
            // le tas en case numTas a diminué d'une allumette (retrait d'une allumette)
            // jeuEssai est le plateau de jeu qui fait apparaître cette séparation
            if ( numTas != -1 ) enlever ( jeuEssai, numTas, 1 );
        }

        return numTas;
    }

    /**
     * Tests succincts de la méthode premier()
     */
    void testPremier() {
        System.out.println();
        System.out.println("*** testPremier()");

        ArrayList<Integer> jeu1 = new ArrayList<Integer>();
        jeu1.add(10);
        jeu1.add(11);
        int tas1 = 0;
        ArrayList<Integer> res1 = new ArrayList<Integer>();
        res1.add(9);
        res1.add(11);
        res1.add(1);
        testCasPremier(jeu1, tas1, res1);
    }

    /**
     * Test un cas de la méthode testPremier
     * @param jeu le plateau de jeu
     * @param tas le numéro du tas séparé en premier
     * @param res le plateau de jeu après une première séparation
     */
    void testCasPremier(ArrayList<Integer> jeu, int tas, ArrayList<Integer> res) {
        // Arrange
        System.out.print("premier (" + jeu.toString() + ") : ");
        ArrayList<Integer> jeuEssai = new ArrayList<Integer>();
        // Act
        int notas = premier(jeu, jeuEssai);
        // Assert
        System.out.println("\nnotas = " + notas + " jeuEssai = " + jeuEssai.toString());
        boolean egaliteJeux = jeuEssai.equals(res);
        if ( egaliteJeux && notas == tas ) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERREUR\n");
        }
    }

    /**
     * Génère la configuration d'essai suivante (c'est-à-dire UNE décomposition possible)
     *
     * @param jeu      plateau de jeu
     * @param jeuEssai configuration d'essai du jeu après séparation
     * @param tas    le numéro du tas qui est le dernier à avoir été séparé
     * @return le numéro du tas divisé en deux pour la nouvelle configuration, -1 si plus aucune décomposition n'est possible
     */
    int suivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int tas) {

        // System.out.println("suivant(" + jeu.toString() + ", " +jeuEssai.toString() +
        // ", " + tas + ") = ");

        int numTas = -1; // par défaut il n'y a plus de décomposition possible

        int i = 0;
        // traitement des erreurs
        if (jeu == null) {
            System.err.println("suivant(): le paramètre jeu est null");
        } else if (jeuEssai == null) {
            System.err.println("suivant() : le paramètre jeuEssai est null");
        } else if (tas >= jeu.size()) {
            System.err.println("suivant(): le paramètre tas est trop grand");
        }

        else {

            int nbAllumEntas = jeuEssai.get(tas);
            int nbAllDernCase = jeuEssai.get(jeuEssai.size() - 1);

            // si sur la même tas (passée en paramètre) on peut encore retirer des allumettes,
            // c-à-d si l'écart entre le nombre d'allumettes sur cette tas et
            // le nombre d'allumettes en fin de tableau est > 2, alors on retire encore
            // 1 allumette sur cette tas et on ajoute 1 allumette en dernière case
            if ( (nbAllumEntas - nbAllDernCase) > 2 ) {
                jeuEssai.set ( tas, (nbAllumEntas - 1) );
                jeuEssai.set ( jeuEssai.size() - 1, (nbAllDernCase + 1) );
                numTas = tas;
            }

            // sinon il faut examiner le tas (tas) suivant du jeu pour éventuellement le décomposer
            // on recrée une nouvelle configuration d'essai identique au plateau de jeu
            else {
                // copie du jeu dans JeuEssai
                jeuEssai.clear();
                for (i = 0; i < jeu.size(); i++) {
                    jeuEssai.add(jeu.get(i));
                }

                boolean separation = false;
                i = tas + 1; // tas suivant
                // si il y a encore un tas et qu'il contient au moins 3 allumettes
                // alors on effectue une première séparation en enlevant 1 allumette
                while ( i < jeuEssai.size() && !separation ) {
                    // le tas doit faire minimum 3 allumettes
                    if ( jeu.get(i) > 2 ) {
                        separation = true;
                        // on commence par enlever 1 allumette à ce tas
                        enlever(jeuEssai, i, 1);
                        numTas = i;
                    } else {
                        i = i + 1;
                    }
                }
            }
        }

        return numTas;
    }

    /**
     * Tests succincts de la méthode suivant()
     */
    void testSuivant() {
        System.out.println();
        System.out.println("*** testSuivant() ****");

        int tas1 = 0;
        int restas1 = 0;
        ArrayList<Integer> jeu1 = new ArrayList<Integer>();
        jeu1.add(10);
        ArrayList<Integer> jeuEssai1 = new ArrayList<Integer>();
        jeuEssai1.add(9);
        jeuEssai1.add(1);
        ArrayList<Integer> res1 = new ArrayList<Integer>();
        res1.add(8);
        res1.add(2);
        testCasSuivant(jeu1, jeuEssai1, tas1, res1, restas1);

        int tas2 = 0;
        int restas2 = -1;
        ArrayList<Integer> jeu2 = new ArrayList<Integer>();
        jeu2.add(10);
        ArrayList<Integer> jeuEssai2 = new ArrayList<Integer>();
        jeuEssai2.add(6);
        jeuEssai2.add(4);
        ArrayList<Integer> res2 = new ArrayList<Integer>();
        res2.add(10);
        testCasSuivant(jeu2, jeuEssai2, tas2, res2, restas2);

        int tas3 = 1;
        int restas3 = 1;
        ArrayList<Integer> jeu3 = new ArrayList<Integer>();
        jeu3.add(4);
        jeu3.add(6);
        jeu3.add(3);
        ArrayList<Integer> jeuEssai3 = new ArrayList<Integer>();
        jeuEssai3.add(4);
        jeuEssai3.add(5);
        jeuEssai3.add(3);
        jeuEssai3.add(1);
        ArrayList<Integer> res3 = new ArrayList<Integer>();
        res3.add(4);
        res3.add(4);
        res3.add(3);
        res3.add(2);
        testCasSuivant(jeu3, jeuEssai3, tas3, res3, restas3);

    }

    /**
     * Test un cas de la méthode suivant
     *
     * @param jeu le plateau de jeu
     * @param jeuEssai le plateau de jeu obtenu après avoir séparé un tas
     * @param tas le numéro du tas qui est le dernier à avoir été séparé
     * @param resJeu est le jeuEssai attendu après séparation
     * @param restas est le numéro attendu du tas qui est séparé
     */
    void testCasSuivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int tas, ArrayList<Integer> resJeu, int restas) {
        // Arrange
        System.out.print("suivant (" + jeu.toString() + ", " + jeuEssai.toString() + ", " + tas + ") : ");
        // Act
        int notas = suivant(jeu, jeuEssai, tas);
        // Assert
        System.out.println("\nnotas = " + notas + " jeuEssai = " + jeuEssai.toString());
        boolean egaliteJeux = jeuEssai.equals(resJeu);
        if ( egaliteJeux && notas == restas ) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERREUR\n");
        }
    }

}