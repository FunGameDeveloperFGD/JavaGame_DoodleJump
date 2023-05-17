import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        JFrame app = new JFrame("Doodle Game!");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.add(game);
        app.pack();
        app.setLocationRelativeTo(null);
        app.setVisible(true);
        game.requestFocus();
    }

}