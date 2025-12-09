package com.deadlyspeed.gameManager;

import com.deadlyspeed.field.RandomBattle;
import com.deadlyspeed.gameLogic.RandomPlay;
import com.deadlyspeed.connection.ConnectionDB;
import com.deadlyspeed.connection.Player;
import com.deadlyspeed.connection.StatePlayer;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.application.Platform;

public class Game {
  private Player playerBlue, playerRed;

  public Game(String playerBlue, String playerRed, Stage stage) {
    // Inicializar el estado del juego con Singleton
    ControlJuego.getInstance().iniciarJuego(playerBlue, playerRed);
    
    // Creamos el juego aleatorio que hemos implementado
    RandomPlay randomPlay = new RandomPlay(playerBlue, playerRed);
    RandomBattle randomBattle = new RandomBattle();
    Scene game = randomPlay.game(randomBattle);
    randomBattle.getExit().setOnAction(e -> stage.setScene(Lobby.getInstance(stage).getMainLobby()));
    game.getStylesheets().add("/styles/game.css");
    stage.setScene(game);

    // Hilo para verificar si alguna lista está vacía; si esto ocurre, entonces hay un jugador ganador
    Thread gameOver = new Thread(() -> {
      while (!randomPlay.gameOver()) {
        try {
          Thread.sleep(1000);     
        } catch(InterruptedException e) {
          e.printStackTrace();
        }
      }

      String winner = randomPlay.getPlayerCurrent();

      Platform.runLater(() -> showVictory(winner, stage));
    });

    gameOver.setDaemon(true);
    gameOver.start();
  }

  public Game(Player playerBlue, Player playerRed, Stage stage) {
    this(playerBlue.getName(), playerRed.getName(), stage);
    this.playerBlue = playerBlue;
    this.playerRed = playerRed;
  }

  private void showVictory(String winner, Stage stage) {
    StackPane background = new StackPane();

    Text win = new Text("You won, " + winner + "!");

    // Mostrar el estado del juego desde el Singleton
    Text gameState = new Text(ControlJuego.getInstance().getEstadoJuego());
    gameState.getStyleClass().add("report");

    Text isLeague = new Text("Venture into the champions league");
    isLeague.getStyleClass().add("report");

    if(playerBlue != null &&  playerRed != null)
      isLeague.setText(leagueVictory(winner));

    // Tenemos la opcion de jugar otra partida
    Button newGame = new Button("New game");
    newGame.setOnAction(e -> {
      ControlJuego.getInstance().reiniciarJuego();
      stage.setScene(Lobby.getInstance(stage).getMainLobby());
    });

    // Tambien podemos abandonar el juego
    Button quit = new Button("Exit game");
    quit.setOnAction(e -> stage.close());

    VBox vbox = new VBox();
    vbox.getChildren().addAll(win, gameState, isLeague, newGame, quit);
    vbox.setAlignment(Pos.CENTER);

    // Crear la escena de victoria
    background.getChildren().addAll(vbox);

    // Crear la escena de presentación
    Scene victory = new Scene(background, 1500, 1000);
    victory.getStylesheets().add("/styles/victory.css");
    stage.setScene(victory);
  }

  private String leagueVictory(String winner) {
    StatePlayer state = null;
    if(winner.equals(playerBlue.getName())) {
      state = ConnectionDB.getInstance().saveGame(playerBlue.getId(), playerRed.getId());
      if(state == StatePlayer.SUCCESS)
        return "The game was successfully saved";
      return "Unexpected error occurred while saving the game";
    }

    state = ConnectionDB.getInstance().saveGame(playerRed.getId(), playerBlue.getId());
    if(state == StatePlayer.SUCCESS)
      return "The game was successfully saved";
    return "Unexpected error occurred while saving the game";
  }
}
