package aut.ap;
import milou.*;

public class Main {
    public static void main(String[] args) {
        Services services = new Services();
        GraphicalUI gui = new GraphicalUI(services);
        gui.start();
    }
}