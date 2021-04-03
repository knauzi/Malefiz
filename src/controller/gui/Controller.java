package controller.gui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import model.agents.Agent;
import model.agents.Human;
import model.agents.RandomAI;
import model.agents.SimpleAI;
import model.game.*;
import model.utils.Color;
import tdl_stuff.net.NeuralNetwork;
import tdl_stuff.tdl.TDLAgent;
import view.gui.ConfigView;
import view.gui.FigureGUI;
import view.gui.GameBoard;
import view.gui.GameView;
import view.gui.utils.Highlight;

import java.io.IOException;
import java.util.ArrayList;

public class Controller implements EventHandler<Event>
{
    /* views */
    private final Stage stage;
    private final ConfigView configView;
    private GameView gameView;

    /* model */
    private Game game;

    /* move data */
    private Figure selectedFigure;
    private Tile selectedTile;
    private Tile selectedBlockTile;

    public Controller(Stage stage)
    {
        this.stage = stage;
        this.configView = new ConfigView(this);  // when fist started there is no game instance -> created from
                                                 // input to config view
    }

    @Override
    public void handle(Event event)
    {
        if(game != null && game.getPhase() == Game.Phase.GAME_OVER)
        {
            event.consume();
            return;
        }
        if (event.getSource() instanceof Button)
        {
            if ((((Button) event.getSource()).getText()).equals("Starte Spiel"))
            {
                startGame();
            }
            else if ((((Button) event.getSource()).getId()).equals("roll"))
            {
                if (game.getPhase() == Game.Phase.WAITING_FOR_ROLL)
                {
                    game.rollDice();
                    game.setPhase(Game.Phase.FIGURE_SELECTION);
                    gameView.setDiceResult(game.getDiceResult());
                }
            }
            event.consume();
        }
        else if (event.getSource() instanceof Highlight &&
                event.getEventType().equals(MouseEvent.MOUSE_CLICKED) &&
                game.getPhase() == Game.Phase.TARGET_SELECTION)
        {
            // System.out.println("Clicked Target");
            Highlight highlight = (Highlight) event.getSource();
            if (highlight.getTile().getState() != Tile.State.BLOCKED)
            {
                setSelectedTile(highlight.getTile());
                Move move = null;
                switch (highlight.getTile().getState())
                {
                    case EMPTY           -> move = new Move(selectedFigure, selectedTile, game.getDiceResult());
                    case OCCUPIED_RED    -> move = new Move(selectedFigure, selectedTile, game.getDiceResult(), Color.RED);
                    case OCCUPIED_GREEN  -> move = new Move(selectedFigure, selectedTile, game.getDiceResult(), Color.GREEN);
                    case OCCUPIED_YELLOW -> move = new Move(selectedFigure, selectedTile, game.getDiceResult(), Color.YELLOW);
                    case OCCUPIED_BLUE   -> move = new Move(selectedFigure, selectedTile, game.getDiceResult(), Color.BLUE);
                    default              -> System.out.println("Unknown state of tile!");
                }
                assert move != null;
                GameLogic.makeMoveOnGame(game, move);
                gameView.getGameBoard().setHighlightTiles(null);
                setSelectedFigure(null);
                setSelectedTile(null);
                setSelectedBlockTile(null);
                game.advanceToNextAgent();
                gameView.getGameBoard().redraw(game, this);

                if(game.isOver())
                {
                    game.setPhase(Game.Phase.GAME_OVER);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Spieler " + game.getActiveAgent() + " hat gewonnen!\n\nSpiel neustarten?");
                    alert.showAndWait()
                            .filter(response -> response == ButtonType.OK)
                            .ifPresent(response -> game.resetGame());
                }
            }
            else // block at target tile
            {
                setSelectedTile(highlight.getTile());
                ArrayList<Tile> blockTargets = GameLogic.getPossibleTargetTilesOfBlock(game.getBoard(), selectedTile, selectedFigure);
                gameView.getGameBoard().setHighlightTiles(blockTargets);
                game.setPhase(Game.Phase.MOVING_BLOCK);
                gameView.getGameBoard().redraw(game, this);
            }
            event.consume();
        }
        else if (event.getSource() instanceof Highlight &&
                event.getEventType().equals(MouseEvent.MOUSE_CLICKED) &&
                game.getPhase() == Game.Phase.MOVING_BLOCK)
        {
            // System.out.println("Clicked Block Target");
            Highlight highlight = (Highlight) event.getSource();
            setSelectedBlockTile(highlight.getTile());
            Move move = new Move(selectedFigure, selectedTile, game.getDiceResult(), selectedBlockTile);
            GameLogic.makeMoveOnGame(game, move);
            gameView.getGameBoard().setHighlightTiles(null);
            setSelectedFigure(null);
            setSelectedTile(null);
            setSelectedBlockTile(null);
            game.advanceToNextAgent();
            gameView.getGameBoard().redraw(game, this);
            event.consume();
        }
        else if (event.getSource() instanceof FigureGUI &&
                 event.getEventType().equals(MouseEvent.MOUSE_CLICKED) &&
                 (game.getPhase() == Game.Phase.FIGURE_SELECTION || game.getPhase() == Game.Phase.TARGET_SELECTION))
        {
            // System.out.println("Clicked Figure");
            FigureGUI figureGUI = (FigureGUI) event.getSource();
            if (figureGUI.getFigure().getColor().ordinal() == game.getActiveAgent())
            {
                ArrayList<Tile> possibleTargets = GameLogic.getPossibleTargetTilesOfFigure(figureGUI.getFigure(), game.getDiceResult());
                gameView.getGameBoard().setHighlightTiles(possibleTargets);
                setSelectedFigure(figureGUI.getFigure());
                game.setPhase(Game.Phase.TARGET_SELECTION);
                gameView.getGameBoard().redraw(game, this);
            }
            event.consume();
        }
        // reset selection
        else if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED) &&
                 event.getSource() instanceof GameBoard &&
                 !(game.getPhase() == Game.Phase.WAITING_FOR_ROLL))
        {
            // System.out.println("Clicked");
            gameView.getGameBoard().setHighlightTiles(null);
            setSelectedFigure(null);
            setSelectedTile(null);
            setSelectedBlockTile(null);
            game.setPhase(Game.Phase.FIGURE_SELECTION);
            gameView.getGameBoard().redraw(game, this);
            event.consume();
        }
    }

    private void registerGameView(GameView gameView)
    {
        this.gameView = gameView;
    }

    private Agent[] createAgentsFromConfigData()
    {
        ArrayList<ChoiceBox<String>> types = configView.getTypes();
        Agent[] agents = new Agent[ConfigView.NUM_AGENTS];
        for (int i = 0; i < ConfigView.NUM_AGENTS; i++)
        {
            switch (types.get(i).getValue())
            {
                case "Mensch"       -> agents[i] = new Human(Color.getColorById(i), game.getBoard());
                case "Zufall-KI"    -> agents[i] = new RandomAI(Color.getColorById(i), game.getBoard());
                case "Einfache-KI"  -> agents[i] = new SimpleAI(Color.getColorById(i), game.getBoard());
                case "TDL-KI"       -> {
                    try {
                        agents[i] = new TDLAgent(Color.getColorById(i), game.getBoard(), NeuralNetwork.readFrom("src/tdl_stuff/models/SavedNN_2"));
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                default             -> agents[i] = null;
            }
        }

        return agents;
    }

    private void startGame()
    {
        System.out.println("Spiel wird gestartet...");

        game = new Game();

        Agent[] agents = createAgentsFromConfigData();
        game.setAgents(agents);

        gameView = new GameView(game, this);
        game.addObserver(gameView);

        stage.setScene(gameView.getScene());
        stage.centerOnScreen();
        stage.show();

        game.start();
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

    private void setSelectedFigure(Figure figure)
    {
        selectedFigure = figure;
    }

    private void setSelectedTile(Tile tile)
    {
        selectedTile = tile;
    }

    private void setSelectedBlockTile(Tile tile)
    {
        selectedBlockTile = tile;
    }
}
