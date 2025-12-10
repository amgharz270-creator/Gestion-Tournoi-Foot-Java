import java.util.*;

public class GestionPoules {
    private final Map<String, Poule> poules = new LinkedHashMap<>();

    public GestionPoules() { poules.put("Poule unique", new Poule()); }
    public GestionPoules(int nbGroupes) {
        for (char g = 'A'; g < 'A' + nbGroupes; g++) poules.put("Groupe " + g, new Poule());
    }
    public Set<String> groupes() { return poules.keySet(); }
    public Poule get(String g) { return poules.get(g); }
    public Poule first() { return poules.values().iterator().next(); }
}