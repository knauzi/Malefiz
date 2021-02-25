package main;

import controller.gui.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

public class MalefizGUI extends Application {

    @Override
    public void start(Stage stage) throws Exception
    {
        Controller controller = new Controller(stage);

        stage.setResizable(false);
        stage.setScene(controller.getConfigView().getScene());
        stage.setTitle("Malefiz");
        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
