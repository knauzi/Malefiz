package view.gui;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import controller.gui.Controller;
import model.game.Game;
import model.utils.Color;

import java.util.ArrayList;

public class ConfigView implements View
{
    private Scene scene;
    private final Stage stage;

    /* config selections (data) */
    public static final int NUM_AGENTS = 4;
    private final ArrayList<TextField> names = new ArrayList<>(NUM_AGENTS);
    private final ArrayList<ChoiceBox<String>> types = new ArrayList<>(NUM_AGENTS);
    private Button startButton;

    public ConfigView(Controller controller)
    {
        this.stage = controller.getStage();
        buildConfigView(controller);
    }

    // TODO this is bullshit crap fuck -> revise !!
    public void buildConfigView(Controller controller)
    {
        final HBox[] hbs = new HBox[NUM_AGENTS];
        final String[] allTypes = new String[]{ "-", "Mensch", "Zufall-KI", "Einfache-KI", "TDL-KI" };

        // column labels
        HBox topRow = new HBox();
        topRow.setAlignment(Pos.BASELINE_RIGHT);
        Label typeCol = new Label("Typ");
        Label nameCol = new Label("Name\t\t\t\t\t    ");
        topRow.setSpacing(107);
        topRow.getChildren().addAll(typeCol, nameCol);

        // forms for all player (specify type and name)
        for (int i = 0; i < NUM_AGENTS; i++)
        {
            Label label = new Label(Color.getColorAsStringById(i) + ": ");

            ChoiceBox<String> cb = new ChoiceBox<>();
            cb.getItems().addAll(allTypes);
            cb.getSelectionModel().selectFirst();
            types.add(cb);

            TextField name = new TextField();
            names.add(name);

            HBox hb = hbs[i] = new HBox();
            hb.setSpacing(10);
            hb.setAlignment(Pos.CENTER);
            hb.getChildren().addAll(label, cb, name);
        }

        // start button
        startButton = new Button("Starte Spiel");
        startButton.addEventHandler(ActionEvent.ACTION, controller);

        // overall layout of the scene
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(10);
        layout.getChildren().add(topRow);
        layout.getChildren().addAll(hbs);
        layout.getChildren().add(startButton);

        scene = new Scene(layout, 400, 250);
    }

    @Override
    public void update()
    {
        // TODO
    }

    public Button getStartButton()
    {
        return startButton;
    }

    public Scene getScene()
    {
        return scene;
    }

    public Stage getStage()
    {
        return stage;
    }

    public ArrayList<TextField> getNames()
    {
        return names;
    }

    public ArrayList<ChoiceBox<String>> getTypes()
    {
        return types;
    }
}
