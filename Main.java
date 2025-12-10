import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private static final Scanner SC = new Scanner(System.in);
    private static final GestionPoules gp = new GestionPoules();

    public static void main(String[] args) {
        while (true) {
            afficherMenu();
            int choix = lireChoix(1, 15);
            Poule poule = choisirPoule();
            if (poule == null && choix != 15) continue;
            switch (choix) {
                case 1 -> creerPoule(poule);
                case 2 -> { poule.genererMatchs(); System.out.println(poule.getNbMatchs() + " matchs générés."); }
                case 3 -> saisirUnResultat(poule);
                case 4 -> poule.afficherClassement();
                case 5 -> remplirAuto(poule);
                case 6 -> afficherCalendrier(poule);
                case 7 -> modifierScore(poule);
                case 8 -> meilleurButeur(poule);
                case 9 -> lancerPhaseFinaleDepuis(poule);
                case 10 -> sauvegarder(poule);
                case 11 -> charger(poule);
                case 12 -> stats(poule);
                case 13 -> rechercherEquipe(poule);
                case 14 -> exporterClassement(poule);
                case 15 -> { System.out.println("Bye !"); return; }
            }
        }
    }

    private static void afficherMenu() {
        System.out.println("""
                \n======== MENU TOURNOI ========
                1.  Créer / ré-init la poule
                2.  Générer les matchs
                3.  Saisir un résultat
                4.  Afficher le classement
                5.  Remplissage auto (aléatoire)
                6.  Afficher le calendrier
                7.  Modifier un score
                8.  Meilleur buteur
                9.  Phase finale
                10. Sauvegarder
                11. Charger
                12. Statistiques
                13. Rechercher une équipe
                14. Exporter classement
                15. Quitter
                ==============================
                Choix : """);
    }

    private static Poule choisirPoule() {
        if (gp.groupes().size() == 1) return gp.first();
        List<String> grp = new ArrayList<>(gp.groupes());
        for (int i = 0; i < grp.size(); i++) System.out.println((i + 1) + ". " + grp.get(i));
        System.out.print("Choix poule : ");
        int idx = lireChoix(1, grp.size()) - 1;
        return gp.get(grp.get(idx));
    }

    private static int lireChoix(int min, int max) {
        int c;
        do {
            c = UtilitaireClavier.lireInt();
            if (c < min || c > max) System.out.print("Valeur entre " + min + " et " + max + " : ");
        } while (c < min || c > max);
        return c;
    }

    private static void creerPoule(Poule poule) {
        poule.getEquipes().clear(); poule.getMatchs().clear();
        System.out.print("Nombre d’équipes : ");
        int n = lireChoix(2, 16);
        for (int i = 1; i <= n; i++) {
            System.out.print("Nom équipe " + i + " : ");
            String nom = new Scanner(System.in).nextLine().trim();
            poule.ajouter(new Equipe(nom));
        }
        System.out.println("Poule créée.");
    }

    private static void saisirUnResultat(Poule poule) {
        List<Match> lst = poule.getMatchsNonJoues();
        if (lst.isEmpty()) { System.out.println("Aucun match à jouer."); return; }
        for (int i = 0; i < lst.size(); i++) System.out.println((i + 1) + ". " + lst.get(i).getE1().getNom() + " - " + lst.get(i).getE2().getNom());
        System.out.print("Numéro du match : ");
        int idx = lireChoix(1, lst.size()) - 1;
        lst.get(idx).saisirScore();
    }

    private static void remplirAuto(Poule poule) {
        Random r = new Random();
        for (Match m : poule.getMatchsNonJoues()) m.forcerScore(r.nextInt(5), r.nextInt(5));
        System.out.println("Scores aléatoires attribués.");
    }

    private static void afficherCalendrier(Poule poule) {
        List<Match> m = poule.getMatchs();
        if (m.isEmpty()) { System.out.println("Pas de matchs."); return; }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (int i = 0; i < m.size(); i++) {
            Match mt = m.get(i);
            System.out.printf("%d. %-15s vs %-15s   %s   %s%n",
                    i + 1, mt.getE1().getNom(), mt.getE2().getNom(),
                    mt.estJoue() ? mt.getS1() + "-" + mt.getS2() : "à jouer",
                    mt.getDateHeure().format(fmt));
        }
    }

    private static void modifierScore(Poule poule) {
        afficherCalendrier(poule);
        List<Match> m = poule.getMatchs();
        if (m.isEmpty()) return;
        System.out.print("Numéro du match à modifier : ");
        int idx = lireChoix(1, m.size()) - 1;
        Match mt = m.get(idx);
        if (!mt.estJoue()) { System.out.println("Match pas encore joué."); return; }
        System.out.print("Nouveau score équipe 1 : ");
        int ns1 = UtilitaireClavier.lireInt();
        System.out.print("Nouveau score équipe 2 : ");
        int ns2 = UtilitaireClavier.lireInt();
        poule.resetScore(mt, ns1, ns2);
        System.out.println("Score modifié.");
    }

    private static void meilleurButeur(Poule poule) {
        Equipe e = poule.getEquipes().stream().max(Comparator.comparingInt(Equipe::getBp)).orElse(null);
        if (e == null) System.out.println("Pas d'équipe.");
        else System.out.println("Meilleure attaque : " + e.getNom() + " (" + e.getBp() + " buts)");
    }

    private static void lancerPhaseFinaleDepuis(Poule poule) {
        if (poule.getEquipes().size() < 4) {
            System.out.println("Il faut au moins 4 équipes."); return;
        }
        List<Equipe> eq = new ArrayList<>(poule.getEquipes());
        Collections.sort(eq);
        System.out.println("Phase finale (scores aléatoires) :");
        Equipe q1 = PhaseFinale.quart(poule, 1, 4);
        Equipe q2 = PhaseFinale.quart(poule, 2, 3);
        Equipe f1 = PhaseFinale.demi(q1, q2);
        Equipe champion = PhaseFinale.finale(f1, f1);
        System.out.println("CHAMPION : " + champion.getNom());
    }

    private static void stats(Poule poule) {
        System.out.println("Buts total : " + poule.getTotalButs());
        System.out.println("Plus gros écart : " + poule.getMaxDiff());
    }

    private static void rechercherEquipe(Poule poule) {
        System.out.print("Nom équipe à rechercher : ");
        String nom = new Scanner(System.in).nextLine().trim();
        poule.getEquipes().stream()
                .filter(e -> e.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .ifPresentOrElse(e -> {
                    System.out.println(e);
                    poule.getMatchs().stream()
                            .filter(m -> m.getE1() == e || m.getE2() == e)
                            .forEach(m -> System.out.println("  " + m.getE1().getNom() + " " + m.getS1() + "-" + m.getS2() + " " + m.getE2().getNom()));
                }, () -> System.out.println("Introuvable."));
    }

    private static void sauvegarder(Poule poule) {
        try (PrintWriter pw = new PrintWriter("tournoi.save")) {
            for (Equipe e : poule.getEquipes()) pw.println("EQ:" + e.getNom() + ":" + e.getPts() + ":" + e.getBp() + ":" + e.getBc());
            for (Match m : poule.getMatchs()) pw.println("MA:" + m.getE1().getNom() + ":" + m.getE2().getNom() + ":" + m.getS1() + ":" + m.getS2() + ":" + m.estJoue());
            System.out.println("Sauvegardé dans tournoi.save");
        } catch (IOException ex) { System.out.println("Erreur sauvegarde"); }
    }

    private static void charger(Poule poule) {
        poule.getEquipes().clear(); poule.getMatchs().clear();
        try (BufferedReader br = Files.newBufferedReader(Paths.get("tournoi.save"))) {
            String l;
            while ((l = br.readLine()) != null) {
                String[] p = l.split(":");
                if (p[0].equals("EQ")) {
                    Equipe e = new Equipe(p[1]);
                    e.ajouterResultat(Integer.parseInt(p[3]), Integer.parseInt(p[4]));
                    poule.ajouter(e);
                }
            }
            System.out.println("Chargé depuis tournoi.save");
        } catch (IOException ex) { System.out.println("Fichier absent ou erroné."); }
    }

    private static void exporterClassement(Poule poule) {
        try (PrintWriter pw = new PrintWriter("classement.txt")) {
            List<Equipe> eq = new ArrayList<>(poule.getEquipes());
            Collections.sort(eq);
            int pos = 1;
            for (Equipe e : eq) pw.println((pos++) + ". " + e);
            System.out.println("Classement exporté dans classement.txt");
        } catch (IOException ex) { System.out.println("Erreur export"); }
    }
}