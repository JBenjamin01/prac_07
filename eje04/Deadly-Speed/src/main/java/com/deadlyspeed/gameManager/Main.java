package com.deadlyspeed.gameManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
  public static void main(String[] args) {
    // DEMOSTRACIÓN DEL PATRÓN SINGLETON
    System.out.println("=== DEMOSTRACIÓN PATRÓN SINGLETON - ControlJuego ===\n");
    
    // Módulo 1: Inicialización del juego
    System.out.println("Módulo 1 - Inicialización del juego:");
    ControlJuego controlJuego1 = ControlJuego.getInstance();
    controlJuego1.iniciarJuego("Jugador Azul", "Jugador Rojo");
    System.out.println(controlJuego1.getEstadoJuego());
    System.out.println();
    
    // Módulo 2: Simulación de enemigos (tropas rojas) actualizando vidas
    System.out.println("Módulo 2 - Enemigos establecen vidas iniciales:");
    ControlJuego controlJuego2 = ControlJuego.getInstance();
    controlJuego2.setVidas("Jugador Azul", 10);
    controlJuego2.setVidas("Jugador Rojo", 10);
    System.out.println(controlJuego2.getEstadoJuego());
    System.out.println();
    
    // Módulo 3: Jugador gana puntos
    System.out.println("Módulo 3 - Jugador Azul suma puntos:");
    ControlJuego controlJuego3 = ControlJuego.getInstance();
    controlJuego3.agregarPuntaje("Jugador Azul", 100);
    controlJuego3.agregarPuntaje("Jugador Azul", 50);
    System.out.println(controlJuego3.getEstadoJuego());
    System.out.println();
    
    // Módulo 4: Interfaz reduce vidas en batalla
    System.out.println("Módulo 4 - Batalla reduce vidas:");
    ControlJuego controlJuego4 = ControlJuego.getInstance();
    controlJuego4.reducirVida("Jugador Rojo");
    controlJuego4.reducirVida("Jugador Azul");
    controlJuego4.reducirVida("Jugador Rojo");
    System.out.println(controlJuego4.getEstadoJuego());
    System.out.println();
    
    // Módulo 5: Sistema incrementa nivel
    System.out.println("Módulo 5 - Sistema incrementa nivel:");
    ControlJuego controlJuego5 = ControlJuego.getInstance();
    controlJuego5.incrementarNivel();
    controlJuego5.agregarPuntaje("Jugador Rojo", 75);
    System.out.println(controlJuego5.getEstadoJuego());
    System.out.println();
    
    // Verificar que todas las instancias son la misma
    System.out.println("=== VERIFICACIÓN DE SINGLETON ===");
    System.out.println("controlJuego1 == controlJuego2: " + (controlJuego1 == controlJuego2));
    System.out.println("controlJuego2 == controlJuego3: " + (controlJuego2 == controlJuego3));
    System.out.println("controlJuego3 == controlJuego4: " + (controlJuego3 == controlJuego4));
    System.out.println("controlJuego4 == controlJuego5: " + (controlJuego4 == controlJuego5));
    System.out.println("\n¡Todas las referencias apuntan a la MISMA instancia!");
    System.out.println("\n=== INICIANDO INTERFAZ GRÁFICA ===\n");
    
    launch(args);
  }
}
