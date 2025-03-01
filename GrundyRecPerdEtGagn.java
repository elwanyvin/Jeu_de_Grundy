import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Grundy game with integrated computer AI
 * This program contains the methods to test jouerGagnant() and the efficiency test testEstGagnanteEfficacite()
 * This version contains everything in the previous version plus the ability to remember winning situations
 * as to not have to calculate them again after.
 * 
 * @author E.Thevenin et E.Yvin 
 */
class GrundyRecPerdEtGagn {

    /** Counter of elementary operations */
    long cpt = 0;

    /** List of losing positions */
    ArrayList<ArrayList<Integer>> posPerdantes = new ArrayList<ArrayList<Integer>>();

    /** List of winning positions */
    ArrayList<ArrayList<Integer>> posGagnantes = new ArrayList<ArrayList<Integer>>();


    /**
     * Main method of the program
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

    }

    /**
     * Game loop for the Grundy game
     * Player and computer take turns playing until one has won
     * The user is free to choose who starts first
     * The game ends if one player can no longer make a move.
     * The winner is the last one to move.
     */
    void leJeu(){
        System.out.println("--------GRUNDY V2--------\n");
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
                if (jeuGagnant==false) { //if there are no winning moves, computer plays a random legal move
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
    * Displays the game board
    *
    * @param tabCoeff game board to display ( containing the number of matches per heap)
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
     * Tests method 'afficherJeu'
     * 
     */
    void testAfficherJeu() {
        System.out.println("testafficherJeu() : \n");

        //Visual tests
    
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
        afficherJeu(tabCoeff3); // empty game board
    
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
     * Plays the winning move if it exists
     *
     * @param jeu game board
     * @return true if a winning move exists, false otherwise
     */
    boolean jouerGagnant(ArrayList<Integer> jeu) {

        boolean gagnant = false;

        if (jeu == null) {
            System.err.println("jouerGagnant(): le parametre jeu est null");
        }else if(estConnueGagnante(jeu)){
            gagnant = true;
        } else {
            ArrayList<Integer> essai = new ArrayList<Integer>();

            // a first decomposition is tried
            // which is stored in the "essai" array
            // tas is the index of the heap where separation occured
            int tas = premier(jeu, essai);

            // Implementation of rule number 2 
            // a situation is winning for the machine if and only if at least one decomposition exists
            // that is losing for the opponent
            // this decomposition will then be the one played by the machine
            while (tas != -1 && !gagnant) {
                // estPerdante is recursive
                if (estPerdante(essai)) {
                    // estPerdante(essai) == true -> a winning decomposition has been found 
                    jeu.clear();
                    gagnant = true;
                    // essai is copied back into jeu ( as it's the game board situation after computer played its winning move)
                    for (int i = 0; i < essai.size(); i++) {
                        jeu.add(essai.get(i));
                    }
                } else {
                    // estPerdante(essai) == false -> decomposition tried is not winning, so the computer tries the next possible one with suivant
                    // If tas == -1 after "suivant" -> no winning decomposition has been found after trying every possible one
                    tas = suivant(jeu, essai, tas);
                }
            }
        }
        return gagnant;
    }

    /**
     * RECURSIVE method indicating is given board state is losing
     * Used to find if a tried decomposition is losing for the opponent
     *
     * @param jeu game board
     * @return true if given game state is losing, false otherwise
     */
    boolean estPerdante(ArrayList<Integer> jeu) {
        boolean ret = true; // by default game state is losing
        boolean connuePerdante = estConnuePerdante(jeu); // check if game state is already known as losing
        boolean connueGagnante = estConnueGagnante(jeu); // check if game state is already known as winning

        // Creation of a copy of the game to try all possible decompositions
        ArrayList<Integer> essai = new ArrayList<Integer>(); 

        int tas = 0;
        if (jeu == null) {
            System.err.println("estPerdante(): le paramÃ¨tre jeu est null");
        }else if (connuePerdante) {
            ret = true;
        }else if (connueGagnante) {
            ret = false;
        }else {
            // if only heaps of 1 or 2 matches remain
            // game state is losing -> END of recursivity
            if ( !estPossible(jeu) ) {
                ret = true;
            }
            else {

                // a first decomposition is tried
                // which is stored in the "essai" array
                // tas is the index of the heap where separation occured
                tas = premier(jeu, essai);
                while ( (tas != -1) && ret) {

                    // Implementation of rule number 1
                    // A situation is losing IF and ONLY IF each of its possible decompositions is winning for the opponent
                    // estPerdante is a RECURSIVE method
                    // if "estPerdante(essai)" == true, it means that a losing decomposition (for the opponent) has been found
                    // so game state is not losing so we get out of the while loop and return false
                    if (estPerdante(essai) == true) {
                        ret = false;
                        //Add game state to winning positions array
                        ArrayList<Integer> copy = new ArrayList<Integer>(jeu); //ccopy of the game board
                        //Normalization
                        copy.sort(null);
                        copy.removeIf(n->n<3);
                        //added to the array
                        posGagnantes.add(copy);
                    } else {
                        // Iterates the next possible decomposition
                        // if tas == -1 no more decompositions are possible
                        tas = suivant(jeu, essai, tas);
                    }
                    cpt++;
                }
            }
        }

        if(tas==-1){
            //essai is losing so we add it to the losing positions array
            //Normalization of the tried decomposition
            essai.sort(null);
            essai.removeIf(n->n<3);

            //Added to the losing positions array
            posPerdantes.add(essai);
        }
        return ret;
        
    }

    /**
     * Checks if game state is already known as losing
     *
     * @param jeu game board
     * @return true if state is known as losing, false otherwise
     */
    boolean estConnuePerdante(ArrayList<Integer> jeu) {
        //Copy of the game board as to not mess it up
        ArrayList<Integer> copy = new ArrayList<>(jeu);

        //Normalization of the copy
        copy.sort(null);
        copy.removeIf(n->n<3);

        if (posPerdantes.contains(copy)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests estConnuePerdante()
     */
    void testEstConnuePerdante() {
        System.out.println("testEstConnuePerdante()");
    
        // Initialization of the losing positions array
        posPerdantes = new ArrayList<>();
    
        ArrayList<Integer> perdante1 = new ArrayList<>();
        perdante1.add(3);
        perdante1.add(5);
        posPerdantes.add(perdante1);
    
        ArrayList<Integer> perdante2 = new ArrayList<>();
        perdante2.add(4);
        perdante2.add(6);
        perdante2.add(7);
        posPerdantes.add(perdante2);
    
        System.out.println("Test cas normaux");
    
        ArrayList<Integer> test1 = new ArrayList<>();
        test1.add(3);
        test1.add(5);
        testCasEstConnuePerdante(test1, true);
    
        ArrayList<Integer> test2 = new ArrayList<>();
        test2.add(1);
        test2.add(2);
        test2.add(4);
        testCasEstConnuePerdante(test2, false);
    
        System.out.println("Test cas limites");
    
        ArrayList<Integer> test3 = new ArrayList<>();
        test3.add(2); 
        test3.add(3);
        test3.add(5);
        testCasEstConnuePerdante(test3, true);
    
        ArrayList<Integer> test4 = new ArrayList<>();
        testCasEstConnuePerdante(test4, false);
    
        ArrayList<Integer> test5 = new ArrayList<>();
        test5.add(1);
        test5.add(2);
        testCasEstConnuePerdante(test5, false);
    
    }
    
    /**
     * Tests a specific case of the estConnuePerdante method.
     *
     * @param jeu the game state to test
     * @param attendu the expected result, true if the configuration is known to be losing, false otherwise
     */
    void testCasEstConnuePerdante(ArrayList<Integer> jeu, boolean attendu) {
        System.out.print("estConnuePerdante(" + jeu + ") : -> ");
        boolean obtenu = estConnuePerdante(jeu);
        if (attendu == obtenu) {
            System.out.println("OK");
        } else {
            System.err.println("ERREUR, obtenu : " + obtenu + " attendu : " + attendu);
        }
    }
 
    /**
     * Checks if game state is already known as winning
     *
     * @param jeu game board
     * @return true if state is known as winning, false otherwise
     */
    boolean estConnueGagnante(ArrayList<Integer> jeu) {
        //Copy of the game board as to not mess it up
        ArrayList<Integer> copy = new ArrayList<>(jeu);

        //Normalization of the copy
        copy.sort(null);
        copy.removeIf(n->n<3);

        if (posGagnantes.contains(copy)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests estConnueGagnante()
     */
    void testEstConnueGagnante() {
        System.out.println("testEstConnueGagnante()");
    
        // Winning positions initialization
        posGagnantes = new ArrayList<>();
    
        // Adding winning positions
        ArrayList<Integer> gagnante1 = new ArrayList<>();
        gagnante1.add(3);
        gagnante1.add(5);
        posGagnantes.add(gagnante1);
    
        ArrayList<Integer> gagnante2 = new ArrayList<>();
        gagnante2.add(4);
        gagnante2.add(6);
        gagnante2.add(7);
        posGagnantes.add(gagnante2);
    
        System.out.println("Test cas normaux");
    
        ArrayList<Integer> test1 = new ArrayList<>();
        test1.add(3);
        test1.add(5);
        testCasEstConnueGagnante(test1, true);
    
        ArrayList<Integer> test2 = new ArrayList<>();
        test2.add(1);
        test2.add(2);
        test2.add(4);
        testCasEstConnueGagnante(test2, false);
    
        System.out.println("Test cas limites");
    
        ArrayList<Integer> test3 = new ArrayList<>();
        test3.add(2);
        test3.add(3);
        test3.add(5);
        testCasEstConnueGagnante(test3, true);
    
        ArrayList<Integer> test4 = new ArrayList<>();
        testCasEstConnueGagnante(test4, false);
    
        ArrayList<Integer> test5 = new ArrayList<>();
        test5.add(1);
        test5.add(2);
        testCasEstConnueGagnante(test5, false);
    }
    
    /**
     * Tests a specific case of the estConnueGagnante method.
     *
     * @param jeu the game state to test
     * @param attendu the expected result, true if the configuration is known to be winning, false otherwise
     */
    void testCasEstConnueGagnante(ArrayList<Integer> jeu, boolean attendu) {
        System.out.print("estConnueGagnante(" + jeu + ") : -> ");
        boolean obtenu = estConnueGagnante(jeu);
        if (attendu == obtenu) {
            System.out.println("OK");
        } else {
            System.err.println("ERREUR, obtenu : " + obtenu + " attendu : " + attendu);
        }
    }
    
    /**
     * Indicates if current game state is winning.
     * This method just calls "estPerdante(jeu)" and returns the opposite.
     * This method is only used in the efficiency tests.
     *
     * @param jeu game board
     * @return true if game state is winning, false otherwise
     */
    boolean estGagnante(ArrayList<Integer> jeu) {
        boolean ret = false;
        if (jeu == null) {
            System.err.println("estGagnante(): le parametre jeu est null");
        } else {
            ret = !estPerdante(jeu);
        }
        return ret;
    }

    /**
     * Efficiency test method for "estGagnante"
     * Method calls "estGagnante" for increasing game board sizes
     * and displays for each size:
     *   - Number of elementary operations done by the computer ( cpt counter )
     *   - Execution time in nanoseconds ( measured with System.nanoTime()).
     */
    void testEstGagnanteEfficacite() {
        System.out.println("testEstGagnanteEfficacite()");
        
        ArrayList<Integer> jeu;
        long debut, fin, diffT;
        diffT = 0;
        int n =3;
        while (diffT<500000000) { //We try until execution time exceeds 500 milliseconds
            posPerdantes.clear(); //Reset of the array of losing positions
            posGagnantes.clear(); //Reset of the array of winning positions
            jeu = new ArrayList<Integer>(); //Reset of the game board
            jeu.add(n); // tas containing n matches
    
            cpt = 0; // Reset of the global counter
            debut = System.nanoTime(); // Start of the chronometer
    
            estGagnante(jeu); // Call of the main method
    
            fin = System.nanoTime(); // End of the chronometer
            diffT = (fin - debut); // Difference of time
    
            // Display of the results
            System.out.println("n = " + n + "; Nb Operations = " + cpt + "; Temps (nanosecondes) = " + diffT);

            n++;
        }
    }
    
    /**
     * Tests the method "jouerGagnant"
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
     * Tests a single case of method "jouerGagnant"
     *
     * @param jeu game board
     * @param resJeu game board after the winning move
     * @param res expected result
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
            System.err.println("ERREUR\n");
        }
    }

    /**
     * Divides a given heap on the game board by removing nb matches from it
     * The new heap formed is then put at the end of the game board
     * Divided heap loses matches equal to the number removed from it
     *
     * @param jeu   game board
     * @param tas   index of heap to divide
     * @param nb    number of matcbes to be REMOVED from heap of index tas
     */
    void enlever ( ArrayList<Integer> jeu, int tas, int nb ) {
        // error cases
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
            // new heap added at the end of the game board containing nb matches
            jeu.add(nb);
            // nb matches are removed from heap of index tas
            jeu.set ( tas, (jeu.get(tas) - nb) );
        }
    }

    /**
     * Tests if a legal move is possible
     *
     * @param jeu  game board
     * @return  true if there is at least one heap with at least 3 matches, false otherwise
     */
    boolean estPossible(ArrayList<Integer> jeu) {
        boolean ret = false;
        if (jeu == null) {
            System.err.println("estPossible(): le paramÃ¨tre jeu est null");
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
     * Creates the first possible decomposition of the game board
     *
     * @param jeu      game board.
     * @param jeuEssai ArrayList where decomposition will be stored.
     * @return Index of separated heap, -1 if no legal move is possible
     */
    int premier(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai) {

        int numTas = -1; // by default no decompositions possible
        int i;

        if (jeu == null) {
            System.err.println("premier(): le parametre jeu est null");
        } else if (!estPossible((jeu)) ){
            System.err.println("premier(): aucun tas n'est divisible");
        } else if (jeuEssai == null) {
            System.err.println("premier(): le parametre jeuEssai est null");
        } else {
            // Reset jeuEssai before filling it for safety.
            jeuEssai.clear(); 
            i = 0;

            // Copies jeu into jeuEssai heap by heap
            // jeuEssai is currently a perfect copy of jeu
            while (i < jeu.size()) {
                jeuEssai.add(jeu.get(i));
                i = i + 1;
            }

            i = 0;
            // looks for the first heap with size >= 3
            boolean trouve = false;
            while ( (i < jeu.size()) && !trouve) {

                // if such heap is found
                if ( jeuEssai.get(i) >= 3 ) {
                    trouve = true;
                    numTas = i;
                }

                i = i + 1;
            }

            // Removes a single match from the heap found in the while loop from jeuEssai
            // if one has been found.
            if ( numTas != -1 ) enlever ( jeuEssai, numTas, 1 );
        }

        return numTas;
    }

    /**
     * Tests method "premier()"
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
     * Tests a case of method "premier()"
     * @param jeu game board
     * @param tas expected index of separated heap
     * @param res expected result of jeuEssai
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
     * Generates the next possible decomposition given a game board
     *
     * @param jeu      game board
     * @param jeuEssai game board after the move was played
     * @param tas    index of last divided heap
     * @return index of the divided heap in the move, -1 if none were possible
     */
    int suivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int tas) {

        // System.out.println("suivant(" + jeu.toString() + ", " +jeuEssai.toString() +
        // ", " + tas + ") = ");

        int numTas = -1; // by default no possible moves

        int i = 0;
        // error cases
        if (jeu == null) {
            System.err.println("suivant(): le paramÃ¨tre jeu est null");
        } else if (jeuEssai == null) {
            System.err.println("suivant() : le paramÃ¨tre jeuEssai est null");
        } else if (tas >= jeu.size()) {
            System.err.println("suivant(): le paramÃ¨tre tas est trop grand");
        }

        else {

            int nbAllumEntas = jeuEssai.get(tas);
            int nbAllDernCase = jeuEssai.get(jeuEssai.size() - 1);

            // if one can still remove matches from the same heap (given in parameter),
            // i.e. if the difference between the number of allumettes on this heap and
            // the number of allumettes in the last heap is > 2, then we can still remove
            // 1 match from this heap and add 1 match to the last heap
            if ( (nbAllumEntas - nbAllDernCase) > 2 ) {
                jeuEssai.set ( tas, (nbAllumEntas - 1) );
                jeuEssai.set ( jeuEssai.size() - 1, (nbAllDernCase + 1) );
                numTas = tas;
            }

            // else we need to look at the next heap to divide it if possible
            else {
                // reset jeuEssai and copy the game board into it
                jeuEssai.clear();
                for (i = 0; i < jeu.size(); i++) {
                    jeuEssai.add(jeu.get(i));
                }

                boolean separation = false;
                i = tas + 1; // next heap
                // if there is still a valid heap (>=3 matches)
                // we divide it by removing one match
                while ( i < jeuEssai.size() && !separation ) {
                    // check that heap size >=3
                    if ( jeu.get(i) > 2 ) {
                        separation = true;
                        // we take off one match
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
     * Tests the method "suivant()"
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
     * Tests a case of the method "suivant()"
     *
     * @param jeu game board
     * @param jeuEssai game board obtained after dividing a heap
     * @param tas index of last divided heap
     * @param resJeu expected game board ( expected jeuEssai )
     * @param restas expected index of separated heap
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