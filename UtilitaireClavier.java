import java.util.Scanner;

public class UtilitaireClavier {
    private static final Scanner SC = new Scanner(System.in);
    public static int lireInt() {
        while (!SC.hasNextInt()) {
            System.out.print("Entier requis : ");
            SC.next();
        }
        int v = SC.nextInt();
        SC.nextLine();
        return v;
    }
}