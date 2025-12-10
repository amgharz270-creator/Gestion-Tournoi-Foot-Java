import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Poule {
    private final List<Equipe> equipes = new ArrayList<>();
    private final List<Match>  matchs  = new ArrayList<>();

    public void ajouter(Equipe e) { equipes.add(e); }
    public int getNbMatchs() { return matchs.size(); }

    public void genererMatchs() {
        matchs.clear();
        LocalDateTime dh = LocalDateTime.now(); // date + heure actuelle
        for (int i = 0; i < equipes.size(); i++)
            for (int j = i + 1; j < equipes.size(); j++) {
                matchs.add(new Match(equipes.get(i), equipes.get(j), dh));
                dh = dh.plusDays(1).plusHours(2); // +1 jour & +2h
            }
    }
    public void afficherClassement() {
        Collections.sort(equipes);
        System.out.println("\n----- CLASSEMENT -----");
        int pos = 1;
        for (Equipe e : equipes) System.out.println((pos++) + ". " + e);
        System.out.println("----------------------\n");
    }
    public List<Equipe> getEquipes() { return equipes; }
    public List<Match>  getMatchs()  { return matchs; }
    public List<Match> getMatchsNonJoues() {
        return matchs.stream().filter(m -> !m.estJoue()).collect(Collectors.toList());
    }
    public void resetScore(Match m, int newS1, int newS2) {
        m.getE1().ajouterResultat(-m.getS1(), -m.getS2());
        m.getE2().ajouterResultat(-m.getS2(), -m.getS1());
        m.forcerScore(newS1, newS2);
    }
    public int getTotalButs() {
        return equipes.stream().mapToInt(Equipe::getBp).sum();
    }
    public int getMaxDiff() {
        return equipes.stream().mapToInt(Equipe::getDiff).max().orElse(0);
    }
}