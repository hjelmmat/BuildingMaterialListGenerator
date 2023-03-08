import UI.MainFrame;
import main.controllers.MainController;

public class Main {
    public static void main(String[] args) {
        MainFrame view = new MainFrame();
        MainController myController = new MainController(view);
    }
}