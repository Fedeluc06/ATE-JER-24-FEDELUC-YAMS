package src;

import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;

public class main {

    // Constante de dé
    public static final int NOMBRE_DE_DES = 5;
    public static final int NOMBRE_DE_FACES = 6;
    // Constante de point
    public static final int NBRE_DE_1 = 1;
    public static final int NBRE_DE_2 = 2;
    public static final int NBRE_DE_3 = 3;
    public static final int NBRE_DE_4 = 4;
    public static final int NBRE_DE_5 = 5;
    public static final int NBRE_DE_6 = 6;
    public static final int POINT_YAHTZEE = 50;
    public static final int POINT_FULL_HOUSE = 25;
    public static final int POINT_PETITE_SUITE = 30;
    public static final int CONSECUTIF_PETITE_SUITE = 4;
    public static final int POINT_GRANDE_SUITE = 40;
    public static final int CONSECUTIF_GRANDE_SUITE = 5;
    public static final int CONSECUTIF_BRELAN = 3;
    public static final int CONSECUTIF_CARRE = 4;
    // Constante de couleur de caractères
    public static final String RESET = "\u001B[0m";
    public static final String ROUGE = "\u001B[31m";
    public static final String VERT = "\u001B[32m";
    public static final String JAUNE = "\u001B[33m";
    public static final String BLEU = "\u001B[34m";
    public static final String BOLD = "\u001B[1m";
    // Constante d'icone
    public static final String DE = "\uD83C\uDFB2 ";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int[] des = new int[NOMBRE_DE_DES];
        boolean[] combinaison = new boolean[7];

        int pointTotaux = 0;
        int relances = 2;
        int compteurManches = 1;


        boolean reponseRelances = true;
        // Intro du jeu
        System.out.println(ROUGE + " |Y A H T Z E E| " + RESET);

        System.out.println(" " + DE + DE + DE + DE + DE);
        System.out.println(BLEU + "| BY Luca Fedele |" + RESET);

        System.out.println("Appuyez sur" + JAUNE + " Entrée " + RESET + "pour lancer le jeu...");
        scanner.nextLine();

        // Lancement du jeu
        System.out.println("Bienvenue dans le jeu du yahtzee ! ");
        while (compteurManches <= 5) {
            System.out.println(JAUNE + "Manche :" + compteurManches + RESET);
            lancerDes(des, random);

            afficherResultatNombreDeDes(des);
            while (relances > 0) {
                {
                    do {
                        System.out.print("Voulez-vous relancer les dés ? (y/n) : ");
                        String reponse = scanner.nextLine().trim().toLowerCase();
                        if (reponse.equals("y")) {
                            reponseRelances = true;
                        } else if (reponse.equals("n")) {
                            reponseRelances = true;
                            relances = 0;
                        } else {
                            reponseRelances = false;
                        }
                    } while (!reponseRelances);
                }
                if (relances > 0) {
                    System.out.print("Entrez les numéros des dés à relancer (1-5, séparés par des espaces) : ");
                    String[] desARelancer = scanner.nextLine().trim().split(" ");
                    choisirXdeEnString(desARelancer, des, random);

                    afficherResultatNombreDeDes(des);
                }
                relances--;
            }
            controlePoint(des, combinaison, pointTotaux);
            pointTotaux += retourneLeNombreDePointTotaux(des, combinaison, pointTotaux);
            System.out.println(pointTotaux);
            relances = 2;
            compteurManches++;
        }
        // Fin du jeu
        System.out.println("Fin du jeu.");
        scanner.close();
    }

    /////////////////////////////////////// Lancement de dé ///////////////////////////////////////

    /**
     * Lance le nombre de dés contenu dans le tableau de dés
     *
     * @param des    tableau de dés à lancer
     * @param random génère un nombre aléatoire (1-6)
     */
    private static void lancerDes(int[] des, Random random) {
        for (int i = 0; i < des.length; i++) {
            des[i] = random.nextInt(NOMBRE_DE_FACES) + 1;
        }
    }

    /////////////////////////////////////// Affichage dés, sommes des dés et résultat ///////////////////////////////////////

    /**
     * Afficher le tableau des dés lancés
     *
     * @param des tableau des dés lancés
     */
    private static void afficherDes(int[] des) {
        int numeroDe = 1;
        System.out.println("Résultat des dés : ");
        for (int de : des) {
            System.out.println(BLEU + DE + "N°" + numeroDe + RESET + " : " + BOLD + de + RESET + " ");
            numeroDe++;
        }
        System.out.println();
    }

    /**
     * Additione les numeros des 5 dés
     *
     * @param des              tableau des dés lancés
     * @param compteurSommeDes nombre de point effectués
     * @return la somme des dés lancés
     */
    private static int retournerSommeDes(int[] des, int compteurSommeDes) {
        for (int de : des) {
            compteurSommeDes += de;
        }
        return compteurSommeDes;
    }

    /**
     * Affiche le résultat sur la console
     *
     * @param des tableau des dés lancés
     */
    private static void afficherResultatNombreDeDes(int[] des) {
        afficherDes(des);
        System.out.println(VERT + "Nombre de 1 : " + RESET + BOLD + compterLeNombreDe1(des) + RESET);
        System.out.println(VERT + "Nombre de 2 : " + RESET + BOLD + compterLeNombreDe2(des) + RESET);
        System.out.println(VERT + "Nombre de 3 : " + RESET + BOLD + compterLeNombreDe3(des) + RESET);
        System.out.println(VERT + "Nombre de 4 : " + RESET + BOLD + compterLeNombreDe4(des) + RESET);
        System.out.println(VERT + "Nombre de 5 : " + RESET + BOLD + compterLeNombreDe5(des) + RESET);
        System.out.println(VERT + "Nombre de 6 : " + RESET + BOLD + compterLeNombreDe6(des) + RESET);
    }

    /////////////////////////////////////// Chois des dés à relancer ///////////////////////////////////////

    /**
     * Prend en compte le choix de relance de l'utilisateur et convertit sa réponse String en Integer pour sélectionner le bon index à changer
     *
     * @param desARelancer tableau de chaine de caractères qui désignent quels dé il faut relancer
     * @param des          tableau des dés déjà lancés
     * @param random       regénère un nombre aléatoire (1-6) sur l'index du dé que l'on veut relancer
     */
    private static void choisirXdeEnString(String[] desARelancer, int[] des, Random random) {
        for (String numero : desARelancer) {
            int index = Integer.parseInt(numero) - 1;
            if (index >= 0 && index < NOMBRE_DE_DES) {
                des[index] = random.nextInt(NOMBRE_DE_FACES) + 1;
            }
        }
    }

    /////////////////////////////////////// Compter le nombre total par dé ///////////////////////////////////////

    /**
     * Détecte quel nombre apparaît 3 fois
     *
     * @param des tableau des dés lancés
     * @return
     */
    private static int compterLeNombreDeDes3Fois(int[] des) {
        Arrays.sort(des);
        int nombreTroisFois = 0;
        int countConsecutive = 1;
        for (int i = 0; i < des.length - 1; i++) {
            if (des[i] == des[i + 1]) {
                countConsecutive++;
            } else {
                countConsecutive = 1;
            }
            if (countConsecutive == CONSECUTIF_BRELAN) {
                nombreTroisFois = des[i];
                return nombreTroisFois;
            }
        }
        return nombreTroisFois;
    }

    /**
     * @param des
     * @return
     */
    private static int compterLeNombreDeDes4Fois(int[] des) {
        Arrays.sort(des);
        int nombreQuatreFois = 0;
        int countConsecutive = 1;
        for (int i = 0; i < des.length - 1; i++) {
            if (des[i] == des[i + 1]) {
                countConsecutive++;
            } else {
                countConsecutive = 1;
            }
            if (countConsecutive == CONSECUTIF_CARRE) {
                nombreQuatreFois = des[i];
            }
        }
        return nombreQuatreFois;
    }

    /**
     * Calcul le nombre de 1 dans un lancer
     *
     * @param des tableau des dés lancés
     * @return le nombre de 1 dans le tableau
     */
    private static int compterLeNombreDe1(int[] des) {
        int compteur = 0;
        for (int index = 0; index < des.length; index++) {
            if (des[index] == NBRE_DE_1) {
                compteur++;
            }
        }
        return compteur;
    }

    /**
     * Calcul le nombre de 2 dans un lancer
     *
     * @param des tableau des dés lancés
     * @return le nombre de 2 dans le tableau
     */
    private static int compterLeNombreDe2(int[] des) {
        int compteur = 0;
        for (int index = 0; index < des.length; index++) {
            if (des[index] == NBRE_DE_2) {
                compteur++;
            }
        }
        return compteur;
    }

    /**
     * Calcul le nombre de 3 dans un lancer
     *
     * @param des tableau des dés lancés
     * @return le nombre de 3 dans le tableau
     */
    private static int compterLeNombreDe3(int[] des) {
        int compteur = 0;
        for (int index = 0; index < des.length; index++) {
            if (des[index] == NBRE_DE_3) {
                compteur++;
            }
        }
        return compteur;
    }

    /**
     * Calcul le nombre de 4 dans un lancer
     *
     * @param des tableau des dés lancés
     * @return le nombre de 4 dans le tableau
     */
    private static int compterLeNombreDe4(int[] des) {
        int compteur = 0;
        for (int index = 0; index < des.length; index++) {
            if (des[index] == NBRE_DE_4) {
                compteur++;
            }
        }
        return compteur;
    }

    /**
     * Calcul le nombre de 5 dans un lancer
     *
     * @param des tableau des dés lancés
     * @return le nombre de 5 dans le tableau
     */
    private static int compterLeNombreDe5(int[] des) {
        int compteur = 0;
        for (int index = 0; index < des.length; index++) {
            if (des[index] == NBRE_DE_5) {
                compteur++;
            }
        }
        return compteur;
    }

    /**
     * Calcul le nombre de 6 dans un lancer
     *
     * @param des tableau des dés lancés
     * @return le nombre de 6 dans le tableau
     */
    private static int compterLeNombreDe6(int[] des) {
        int compteur = 0;
        for (int index = 0; index < des.length; index++) {
            if (des[index] == NBRE_DE_6) {
                compteur++;
            }
        }
        return compteur;
    }

    //////////////////////////////// Détecter les séries de point ///////////////////////////////////////

    /**
     * Calcul si le lancer de dé donne un Yahtzee
     *
     * @param des tableau des dés lancés
     * @return si c'est un Yahtzee ou non
     */
    private static boolean estYahtzee(int[] des) {
        boolean yahtzee = false;
        if (des[0] == des[1] && des[1] == des[2] && des[2] == des[3] && des[3] == des[4]) {
            yahtzee = true;
        }
        return yahtzee;
    }

    /**
     * Calcul si le lancer de dé donne une Petite Suite
     *
     * @param des tableau des dés lancés
     * @return si c'est une Petite Suite ou non
     */
    private static boolean estPetiteSuite(int[] des) {
        Arrays.sort(des);

        // Compter le nombre de valeurs consécutives distinctes
        int countConsecutif = 1;

        // Parcourir les dés triés avec une boucle for
        for (int i = 0; i < des.length - 1; i++) {
            if (des[i] == des[i + 1] - 1) {
                countConsecutif++;
            } else if (des[i] != des[i + 1]) {
                countConsecutif = 1;
            }
            if (countConsecutif == CONSECUTIF_PETITE_SUITE) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calcul si le lancer de dé donne un Carre
     *
     * @param des tableau des dés lancés
     * @return si c'est un Carre ou non
     */
    private static boolean estGrandeSuite(int[] des) {
        Arrays.sort(des);

        // Compter le nombre de valeurs consécutives distinctes
        int countConsecutif = 1;

        // Parcourir les dés triés avec une boucle for
        for (int i = 0; i < des.length - 1; i++) {
            if (des[i] == des[i + 1] - 1) {
                countConsecutif++;
            } else if (des[i] != des[i + 1]) {
                countConsecutif = 1;
            }
            if (countConsecutif == CONSECUTIF_GRANDE_SUITE) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param des
     * @return
     */
    private static boolean estFullHouse(int[] des) {
        boolean fullHouse = false;
        int nbreDe1 = compterLeNombreDe1(des);
        int nbreDe2 = compterLeNombreDe2(des);
        int nbreDe3 = compterLeNombreDe3(des);
        int nbreDe4 = compterLeNombreDe4(des);
        int nbreDe5 = compterLeNombreDe5(des);
        int nbreDe6 = compterLeNombreDe6(des);

        if ((nbreDe1 == 3 || nbreDe2 == 3 || nbreDe3 == 3 || nbreDe4 == 3 || nbreDe5 == 3 || nbreDe6 == 3) && (nbreDe1 == 2 || nbreDe2 == 2 || nbreDe3 == 2 || nbreDe4 == 2 || nbreDe5 == 2 || nbreDe6 == 2)) {
            fullHouse = true;
        }
        return fullHouse;
    }

    /**
     * @param des
     * @return
     */
    private static boolean estCarre(int[] des) {
        Arrays.sort(des);
        // Compter le nombre de valeurs consécutives distinctes
        int countConsecutif = 1;

        // Parcourir les dés triés avec une boucle for
        for (int i = 0; i < des.length - 1; i++) {
            if (des[i] == des[i + 1]) {
                countConsecutif++;
            } else if (des[i] != des[i + 1]) {
                countConsecutif = 1;
            }
            if (countConsecutif == CONSECUTIF_CARRE) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calcul si le lancer de dé donne un Brelan
     *
     * @param des tableau des dés lancés
     * @return si c'est un Brelan ou non
     */
    private static boolean estBrelan(int[] des) {
        Arrays.sort(des);

        // Compter le nombre de valeurs consécutives distinctes
        int countConsecutif = 1;

        // Parcourir les dés triés avec une boucle for
        for (int i = 0; i < des.length - 1; i++) {
            if (des[i] == des[i + 1]) {
                countConsecutif++;
            } else if (des[i] != des[i + 1]) {
                countConsecutif = 1;
            }
            if (countConsecutif == CONSECUTIF_BRELAN) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne le nombre de point selon la combinaison détectée
     *
     * @param des tableau des dés lancés
     * @return le nombre de points par combinaison
     */
    private static int retourneLeNombreDePointTotaux(int[] des, boolean[] combinaison, int pointTotaux) {

        int compteurSommesDes = 0;
        boolean yahtzee = estYahtzee(des);
        boolean grandeSuite = estGrandeSuite(des);
        boolean petiteSuite = estPetiteSuite(des);
        boolean estFullHouse = estFullHouse(des);
        boolean carre = estCarre(des);
        int pointCarre = compterLeNombreDeDes4Fois(des) * CONSECUTIF_CARRE;
        boolean brelan = estBrelan(des);
        int pointBrelan = compterLeNombreDeDes3Fois(des) * CONSECUTIF_BRELAN;

        if (yahtzee && !combinaison[0]) {
            pointTotaux += POINT_YAHTZEE;
            combinaison[0] = true;
        } else if (grandeSuite && !combinaison[1]) {
            pointTotaux += POINT_GRANDE_SUITE;
            combinaison[1] = true;
        } else if (petiteSuite && !combinaison[2]) {
            pointTotaux += POINT_PETITE_SUITE;
            combinaison[2] = true;
        } else if (estFullHouse && !combinaison[3]) {
            pointTotaux += POINT_FULL_HOUSE;
            combinaison[3] = true;
        } else if (carre && !combinaison[4]) {
            pointTotaux += pointCarre;
            combinaison[4] = true;
        } else if (brelan && !combinaison[5]) {
            pointTotaux += pointBrelan;
            combinaison[5] = true;
        }else if (!combinaison[6]) {
            pointTotaux += retournerSommeDes(des,compteurSommesDes);
            combinaison[6] = true;
        } else {
            pointTotaux += 0;
        }
        return pointTotaux;
    }

    /**
     * Détecte la bonne combinaisons de point et l'affiche
     *
     * @param des tableau des dés lancés
     */
    private static void controlePoint(int[] des, boolean[] combinaisons, int pointTotaux) {

        boolean yahtzee = estYahtzee(des);
        boolean grandeSuite = estGrandeSuite(des);
        boolean petiteSuite = estPetiteSuite(des);
        boolean estFullHouse = estFullHouse(des);
        boolean carre = estCarre(des);
        boolean brelan = estBrelan(des);


        if (yahtzee && !combinaisons[0]) {
            System.out.println("Yahtzee !");
        } else if (grandeSuite &&!combinaisons[1]) {
            System.out.println("Grande Suite !");
        } else if (petiteSuite && !combinaisons[2]) {
            System.out.println("Petite Suite !");
        } else if (estFullHouse && !combinaisons[3]) {
            System.out.println("FullHouse !");
        } else if (carre && !combinaisons[4]) {
            System.out.println("Carre !");
        } else if (brelan && !combinaisons[5]) {
            System.out.println("Brelan !");
        } else if (!combinaisons[6]) {
            System.out.println("Chance !");
        } else {
            System.out.println("Vous n'avez pas obtenu de combinaison ");
        }
    }
}
