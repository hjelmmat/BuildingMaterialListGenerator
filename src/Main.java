import UI.MainFrame;
import main.Models.Buildable.House;
import main.controllers.MainController;

public class Main {
    public static void main(String[] args) {
        MainFrame view = new MainFrame();
        House house = new House();
        MainController myController = new MainController(view, house);
    }
}