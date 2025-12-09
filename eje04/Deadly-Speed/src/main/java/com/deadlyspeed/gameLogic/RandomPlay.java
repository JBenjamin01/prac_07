package com.deadlyspeed.gameLogic;

import com.deadlyspeed.randomTroop.TroopFactory;
import com.deadlyspeed.randomTroop.fighter.*;
import com.deadlyspeed.field.*;

import java.util.List;
import java.util.LinkedList;
import java.util.Random;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

public class RandomPlay implements Play<RandomBattle, Scene> {

  // Tipo de batalla actual
  private RandomBattle battle;

  // Abstrae el turno de las tropas con su jugador correspondiente
  private List<Fighter<ImageView>> troopCurrent;
  private String playerCurrent;

  // Campos para asistencia del campo de batalla
  private String player1, player2;

  // Campo que almacena las skins de los guerreros en pantalla
  private List<ImageView> skins = new LinkedList<>();

  // Control de modo de colisión/choque y ganancia de velocidad
  private boolean clash;

  // Campos importantes del guerrero actual
  private Fighter<ImageView> selWarrior, victim, attacker;

  // Posibles respuestas del guerrero víctima
  private enum Answer {COUNTERATTACK, ESCAPE, BOTH};
  private Answer answer;

  public RandomPlay(String player1, String player2) {
    this.player1 = player1;
    this.player2 = player2;
  }

  /**
   * @param battle tipo de batalla que se generará; aquí implementamos generación aleatoria
   * @return representación gráfica de la batalla (escena de JavaFX)
   */
  public Scene game(RandomBattle battle) {
    this.battle = battle.createField().putFighters();

    // Por defecto la tropa azul (jugador 1) empieza
    troopCurrent = this.battle.getTroopBlue();
    playerCurrent = player1;

    // Inicializar vidas en el Singleton ControlJuego
    com.deadlyspeed.gameManager.ControlJuego.getInstance().setVidas(player1, this.battle.getTroopBlue().size());
    com.deadlyspeed.gameManager.ControlJuego.getInstance().setVidas(player2, this.battle.getTroopRed().size());

    // Buscar las skins dentro del GridPane y guardarlas
    setSkins();

    this.battle.getMap().setOnMouseClicked(event -> {
      /**
       * x = número de fila calculado dividiendo la coordenada Y entre el tamaño de cada celda (100px)
       * y = número de columna calculado dividiendo la coordenada X entre el tamaño de cada celda (100px)
       */
      int x = (int) (event.getY() / 100);
      int y = (int) (event.getX() / 100);

      if(!clash) {
        if(event.getButton() == MouseButton.PRIMARY)
          selectWarriorOf(x, y);
        if(event.getButton() == MouseButton.SECONDARY)
          moveSelWarriorTo(x, y);
      } else {
        if(event.getButton() == MouseButton.PRIMARY)
          selectVictimOf(x, y);
        if(event.getButton() == MouseButton.SECONDARY)
          moveSelVictimTo(x, y);
      }
    });

    return new Scene(this.battle.getContainer(), 1500, 1000);
  }

  /**
   * Método llamado cuando se hace clic izquierdo.
   * Seleccionar un guerrero implica:
   * - Asegurar que no haya un guerrero previamente seleccionado
   * - Verificar que realmente haya un guerrero en esa celda
   * - Verificar que pertenezca a la tropa del jugador actual
   * - Reportar si alguna condición no se cumple
   */
  private void selectWarriorOf(int x, int y) {
    if(selWarrior != null)
      selWarrior = null;

    // Buscar la skin en el campo de skins
    ImageView skin = searchSkin(x, y);

    if(skin != null) {
      // Buscar al guerrero dentro de la tropa actual
      selWarrior = TroopFactory.getWarrior(troopCurrent, x, y);

      if(selWarrior == null) {
        change();
        battle.getMessage().setText(playerCurrent + ", no es tu turno");
        change();
        return;
      }

      battle.getMessage().setText(playerCurrent + ", gana velocidad o ataca");

      // Mostrar información del guerrero seleccionado
      battle.getNameLabel().setText(selWarrior.getName());
      battle.getSkinView().setImage(selWarrior.getSkin().getImage());
      battle.getHpBar().setProgress(selWarrior.getHp() * 1.0 / 12);
      battle.getSpeedBar().setProgress(selWarrior.getSpeed() * 1.0 / 20);
      battle.getAttackBar().setProgress(selWarrior.getAttack() * 1.0 / 13);
      battle.getDefenseBar().setProgress(selWarrior.getDefense() * 1.0 / 10);

    } else {
      battle.getMessage().setText(playerCurrent + ", no estás seleccionando un guerrero");
    }
  }

  /**
   * Método llamado cuando se hace clic derecho.
   * Mover un guerrero implica:
   * - Verificar que haya un guerrero seleccionado
   * - Comprobar si ganará velocidad o atacará
   * - Si ataca: comprobar velocidad suficiente y que el objetivo sea enemigo
   * - Si gana velocidad: moverlo, actualizar posición y cambiar turno
   */
  private void moveSelWarriorTo(int x, int y) {
    if(selWarrior == null) {
      battle.getMessage().setText(playerCurrent + ", no has seleccionado a ninguno de tus guerreros");
      return;
    }

    ImageView skin = searchSkin(x, y);

    if(skin != null) {
      // Cambiar a la tropa de la víctima para inicializar y reportar ataque
      change();
      victim = TroopFactory.getWarrior(troopCurrent, x, y);
      change();

      if(victim == null) {
        battle.getMessage().setText(playerCurrent + ", no puedes atacar a tus propios guerreros");
        return;
      }

      if(!selWarrior.attackTo(x, y)) {
        battle.getMessage().setText(playerCurrent + ", no tienes suficiente velocidad para atacar");
        return;
      }

      attacker = selWarrior;

      change();
      battle.getMessage().setText(playerCurrent + ", estás siendo atacado/a" + victim.beAttackedBy(attacker));

      // Definir la respuesta de la víctima
      optionBeAttacked(x, y);

    } else {
      selWarrior.gainSpeed(x, y);

      // Actualizar posición
      battle.getMap().getChildren().remove(selWarrior.getSkin());
      battle.getMap().add(selWarrior.getSkin(), y, x);
      battle.getMessage().setText(playerCurrent + ", ganaste velocidad");
      setSkins();

      change();
    }

    selWarrior = null;
  }

  /**
   * Simula el cambio de turno entre jugadores.
   * También ayuda en el modo de choque a cambiar de tropas temporalmente.
   */
  private void change() {
    if(troopCurrent == battle.getTroopBlue()) {
      troopCurrent = battle.getTroopRed();
      playerCurrent = player2;
    } else {
      troopCurrent = battle.getTroopBlue();
      playerCurrent = player1;
    }
  }

  /**
   * Lógica al ser atacado:
   * - Si la víctima no puede escapar ni contraatacar → muere
   * - Si ambos tienen misma probabilidad de ganar → mueren ambos
   * - Si no, se genera un choque para que la víctima decida
   */
  public void optionBeAttacked(int x, int y) {
    double win = victim.probabilityWinning(attacker);
    int speed = victim.getSpeed();

    battle.getMap().getChildren().remove(attacker.getSkin());
    battle.getMap().getChildren().remove(victim.getSkin());

    if(win < 50.00 && speed < 2) {
      troopCurrent.remove(victim);
      com.deadlyspeed.gameManager.ControlJuego.getInstance().reducirVida(playerCurrent);
      battle.getMessage().setText(playerCurrent + ", tu guerrero murió con honor");
      battle.getMap().add(attacker.getSkin(), y, x);

      setSkins();
      victim = attacker = null;
      return;
    }

    if(win == 50.00) {
      troopCurrent.remove(victim);
      com.deadlyspeed.gameManager.ControlJuego.getInstance().reducirVida(playerCurrent);

      change();
      troopCurrent.remove(attacker);
      com.deadlyspeed.gameManager.ControlJuego.getInstance().reducirVida(playerCurrent);
      change();

      battle.getMessage().setText("Batalla épica: ambos guerreros murieron con honor");

      setSkins();
      victim = attacker = null;
      return;
    }

    // Crear la vista temporal de choque
    HBox oneOnOne = new HBox();
    oneOnOne.setAlignment(Pos.CENTER);
    oneOnOne.getChildren().addAll(victim.getSkin(), attacker.getSkin());
    battle.getMap().add(oneOnOne, y, x);

    // Entrar a modo choque
    clash = true;
  }

  // Filtra las skins dentro del GridPane y actualiza el campo "skins"
  private void setSkins() {
    battle.getMap().getChildren()
          .filtered(node -> node instanceof ImageView)
          .forEach(node -> skins.add((ImageView) node));
  }

  // Busca una skin en el campo "skins"
  private ImageView searchSkin(int x, int y) {
    for(ImageView skin : skins)
      if(GridPane.getRowIndex(skin) == x && GridPane.getColumnIndex(skin) == y)
        return skin;
    return null;
  }

  // ##############################
  // ##    LÓGICA DEL CHOQUE    ##
  // ##############################

  /**
   * La víctima debe responder sin mover otro guerrero, por ello:
   * - Se verifica que se haya seleccionado al guerrero dentro del choque
   * - Se determina el tipo de respuesta disponible (ESCAPE, COUNTERATTACK, BOTH)
   */
  private void selectVictimOf(int x, int y) {
    HBox oneOnOne = searchClash(x, y);

    if(oneOnOne == null) {
      battle.getMessage().setText(playerCurrent + ", solo puedes mover al guerrero que está siendo atacado");
      return;
    }
    
    // Mostrar información del guerrero víctima
    battle.getNameLabel().setText(victim.getName());
    battle.getSkinView().setImage(victim.getSkin().getImage());
    battle.getHpBar().setProgress(victim.getHp() * 1.0 / 12);
    battle.getSpeedBar().setProgress(victim.getSpeed() * 1.0 / 20);
    battle.getAttackBar().setProgress(victim.getAttack() * 1.0 / 13);
    battle.getDefenseBar().setProgress(victim.getDefense() * 1.0 / 10);

    double win = victim.probabilityWinning(attacker);
    int speed = victim.getSpeed();

    if(win < 50.00) {
      battle.getMessage().setText("Solo puedes escapar, asegúrate de alejarte lo más que puedas");
      answer = Answer.ESCAPE;
    } else if(speed < 2) {
      battle.getMessage().setText("Solo puedes enfrentarlo o contraatacar");
      answer = Answer.COUNTERATTACK;
    } else {
      battle.getMessage().setText("Puedes enfrentarlo o escapar, decide sabiamente");
      answer = Answer.BOTH;
    }
  }

  /**
   * Ejecuta la acción elegida por la víctima:
   * - ESCAPE
   * - COUNTERATTACK
   * - BOTH (se decide según la posición seleccionada)
   */
  private void moveSelVictimTo(int x, int y) {
    if(answer == null) {
      battle.getMessage().setText(playerCurrent + ", selecciona a tu guerrero víctima");
      return;
    }

    int currentX = victim.getPosition()[0];
    int currentY = victim.getPosition()[1];
    HBox oneOnOne = searchClash(x, y);

    switch(answer) {
      case ESCAPE:
        escapeNow(currentX, currentY, x, y, oneOnOne);
        break;
      case COUNTERATTACK:
        counterattack(currentX, currentY, x, y, oneOnOne);
        break;
      default:
        if(currentX != x && currentY != y)
          escapeNow(currentX, currentY, x, y, oneOnOne);
        else
          counterattack(currentX, currentY, x, y, oneOnOne);
        break;
    }
  }

  /**
   * Acciones al escapar:
   * - Verificar que no está intentando contraatacar
   * - Verificar que tenga velocidad suficiente para llegar al destino
   * - Si escapa, regresar al modo normal
   */
  private void escapeNow(int currentX, int currentY, int x, int y, HBox oneOnOne) {
    answer = null;

    if(currentX == x && currentY == y) {
      battle.getMessage().setText("No puedes enfrentarlo, solo escapar");
      return;
    }

    if(victim.escapeTo(x, y)) {
      battle.getMap().getChildren().remove(oneOnOne);
      battle.getMap().add(victim.getSkin(), y, x);
      battle.getMap().add(attacker.getSkin(), currentY, currentX);
      victim = attacker = null;
      clash = false;
      change();
    } else {
      battle.getMessage().setText(playerCurrent + ", no tienes suficiente velocidad para escapar");
    }
  }

  /**
   * Contraataque:
   * - Quitar el HBox temporal
   * - Verificar si el atacante puede escapar; si no, muere
   * - Regresar al modo normal
   */
  private void counterattack(int currentX, int currentY, int x, int y, HBox oneOnOne) {
    answer = null;

    if(currentX != x || currentY != y) {
      battle.getMessage().setText("No puedes escapar, solo contraatacar");
      return;
    }

    battle.getMap().getChildren().remove(oneOnOne);

    change();

    if(attacker.getSpeed() < 2) {
      troopCurrent.remove(attacker);
      com.deadlyspeed.gameManager.ControlJuego.getInstance().reducirVida(playerCurrent);

      battle.getMap().add(victim.getSkin(), currentY, currentX);
      battle.getMessage().setText(playerCurrent + ", tu guerrero ha muerto, analiza mejor a tu enemigo");
    } else {
      scapeRandom(currentX, currentY);
      battle.getMessage().setText(playerCurrent + ", tu guerrero escapó como pudo, elige tus batallas con cuidado");
      change();
    }
    
    clash = false;
    victim = attacker = null;
  }

  // Intenta escapar del atacante a una posición aleatoria válida
  private void scapeRandom(int currentX, int currentY) {
    Random random = new Random();
    int x, y;

    while(true) {
      x = random.nextInt(10) + 1; 
      y = random.nextInt(10) + 1; 
 
      // Comprobar que la casilla está vacía y que tiene velocidad para escapar
      if(searchSkin(x, y) == null && attacker.escapeTo(x, y)) {
        battle.getMap().add(attacker.getSkin(), y, x); 
        battle.getMap().add(victim.getSkin(), currentY, currentX);
        return;
      }
    }
  }

  /**
   * @return el HBox temporal que representa un choque. Si no existe, devuelve null
   */
  private HBox searchClash(int x, int y) {
    for(Node node : battle.getMap().getChildren())
      if(node instanceof HBox && GridPane.getRowIndex(node) == x && GridPane.getColumnIndex(node) == y)
        return (HBox) node;
    return null;
  }

  /**
   * @return true si alguna tropa se quedó sin guerreros
   */
  public boolean gameOver() {
    if(battle.getTroopBlue().isEmpty()) {
      playerCurrent = player2;
      return true;
    }

    if(battle.getTroopRed().isEmpty()) {
      playerCurrent = player1;
      return true;
    }

    return false;
  }

  public String getPlayerCurrent() { return playerCurrent; }

  public String getPlayer1() {return player1; }

  public String getPlayer2() {return player2; }
}
