public class Equipe implements Comparable<Equipe> {
    private final String nom;
    private int pts = 0, bp = 0, bc = 0;

    public Equipe(String nom) { this.nom = nom; }

    public void ajouterResultat(int bp, int bc) {
        this.bp += bp; this.bc += bc;
        if (bp > bc) pts += 3;
        else if (bp == bc) pts += 1;
    }
    public int getDiff() { return bp - bc; }

    @Override
    public int compareTo(Equipe o) {
        int c = Integer.compare(o.pts, this.pts);
        return (c != 0) ? c : Integer.compare(o.getDiff(), this.getDiff());
    }
    @Override
    public String toString() {
        return String.format("%-15s %2d pts  %2d-%2d  diff %+d",
                nom, pts, bp, bc, getDiff());
    }
    public String getNom() { return nom; }
    public int getBp() { return bp; }
    public int getBc() { return bc; }
    public int getPts() { return pts; }
}