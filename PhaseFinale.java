import java.util.*;
import java.time.LocalDateTime;
public class PhaseFinale {
    public static Equipe quart(Poule poule, int rank1, int rank2) {
        List<Equipe> eq = new ArrayList<>(poule.getEquipes());
        if (eq.size() < 4) throw new IllegalStateException("Pas assez d'équipes");
        Collections.sort(eq);
        Equipe e1 = eq.get(rank1 - 1), e2 = eq.get(rank2 - 1);
        Match m = new Match(e1, e2, LocalDateTime.now()); // heure actuelle
        m.forcerScore(rand4(), rand4());
        m.afficher();
        return (m.getS1() >= m.getS2()) ? e1 : e2;
    }
    public static Equipe demi(Equipe q1, Equipe q2) {
        Match m = new Match(q1, q2, LocalDateTime.now());
        m.forcerScore(rand4(), rand4());
        m.afficher();
        return (m.getS1() >= m.getS2()) ? q1 : q2;
    }
    public static Equipe finale(Equipe d1, Equipe d2) {
        Match m = new Match(d1, d2, LocalDateTime.now());
        m.forcerScore(rand4(), rand4());
        m.afficher();
        return (m.getS1() >= m.getS2()) ? d1 : d2;
    }
    private static int rand4() { return new Random().nextInt(5); }
}