package controller.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import model.agents.Agent;
import model.game.Game;
import view.gui.ConfigView;
import view.gui.GameView;

public class Controller implements EventHandler<ActionEvent>
{
    /* views */
    private final Stage stage;
    private final ConfigView configView;
    private GameView gameView;

    /* model */
    private Game game;

    public Controller(Stage stage)
    {
        this.stage = stage;
        this.configView = new ConfigView(this);  // when fist started there is no game instance -> created from
                                                 // input to config view
    }

    @Override
    public void handle(ActionEvent event)
    {
        if (event.getSource() instanceof Button)
        {
            if ((((Button) event.getSource()).getText()).equals("Starte Spiel"))
            {
                startGame();
            }
        }
        // TODO handle all other events
    }

    private void registerGameView(GameView gameView)
    {
        this.gameView = gameView;
    }

    private Agent[] createAgentsFromConfigData()
    {
        // TODO
        return null;
    }

    private void startGame()
    {
        System.out.println("Spiel wird gestartet...");

        // retrieve and save selection from forms
        Agent[] agents = createAgentsFromConfigData();
        //game = new Game(agents);

        gameView = new GameView(null, this);

        // change to game scene
        stage.setScene(gameView.getScene());
        stage.centerOnScreen();
    }

    public Stage getStage()
    {
        return stage;
    }

    public ConfigView getConfigView()
    {
        return configView;
    }

    public GameView getGameView()
    {
        return gameView;
    }
}
