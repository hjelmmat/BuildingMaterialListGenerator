import UI.MainFrame;
import Models.Buildable.House;
import controllers.MainController;

public class Main {
    public static void main(String[] args) {
        MainFrame view = new MainFrame();
        House house = new House();
        MainController myController = new MainController(view, house);
    }
}