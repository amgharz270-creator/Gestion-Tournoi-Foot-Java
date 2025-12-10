import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Match {
    private final Equipe e1, e2;
    private int s1, s2;
    private boolean joue = false;
    private final LocalDateTime dateHeure;

    public Match(Equipe e1, Equipe e2, LocalDateTime dh) {
        this.e1 = e1; this.e2 = e2; this.dateHeure = dh;
    }
    public LocalDateTime getDateHeure() { return dateHeure; }

    public void saisirScore() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.printf("Score %s - %s (%s) : ", e1.getNom(), e2.getNom(), dateHeure.format(fmt));
        s1 = UtilitaireClavier.lireInt();
        s2 = UtilitaireClavier.lireInt();
        e1.ajouterResultat(s1, s2);
        e2.ajouterResultat(s2, s1);
        joue = true;
    }
    public void forcerScore(int s1, int s2) {
        this.s1 = s1; this.s2 = s2;
        e1.ajouterResultat(s1, s2);
        e2.ajouterResultat(s2, s1);
        joue = true;
    }
    public boolean estJoue() { return joue; }
    public Equipe getE1() { return e1; }
    public Equipe getE2() { return e2; }
    public int getS1() { return s1; }
    public int getS2() { return s2; }
    public void afficher() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.printf("%-15s vs %-15s   %s   %s%n",
                e1.getNom(), e2.getNom(),
                joue ? s1 + "-" + s2 : "à jouer",
                dateHeure.format(fmt));
    }
}