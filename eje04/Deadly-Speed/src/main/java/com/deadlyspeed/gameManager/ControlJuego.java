package com.deadlyspeed.gameManager;

public class ControlJuego {
  private static ControlJuego instance;
  
  private int nivelActual;
  private int puntajeJugadorAzul;
  private int puntajeJugadorRojo;
  private int vidasJugadorAzul;
  private int vidasJugadorRojo;
  private String nombreJugadorAzul;
  private String nombreJugadorRojo;
  
  private ControlJuego() {
    this.nivelActual = 1;
    this.puntajeJugadorAzul = 0;
    this.puntajeJugadorRojo = 0;
    this.vidasJugadorAzul = 0;
    this.vidasJugadorRojo = 0;
    this.nombreJugadorAzul = "";
    this.nombreJugadorRojo = "";
  }
  
  public static synchronized ControlJuego getInstance() {
    if (instance == null) {
      instance = new ControlJuego();
    }
    return instance;
  }
  
  public void iniciarJuego(String jugadorAzul, String jugadorRojo) {
    this.nombreJugadorAzul = jugadorAzul;
    this.nombreJugadorRojo = jugadorRojo;
    this.nivelActual = 1;
    this.puntajeJugadorAzul = 0;
    this.puntajeJugadorRojo = 0;
    this.vidasJugadorAzul = 0;
    this.vidasJugadorRojo = 0;
  }
  
  public void reiniciarJuego() {
    this.nivelActual = 1;
    this.puntajeJugadorAzul = 0;
    this.puntajeJugadorRojo = 0;
    this.vidasJugadorAzul = 0;
    this.vidasJugadorRojo = 0;
  }
  
  public void incrementarNivel() {
    this.nivelActual++;
  }
  
  public void agregarPuntaje(String jugador, int puntos) {
    if (jugador.equals(nombreJugadorAzul)) {
      this.puntajeJugadorAzul += puntos;
    } else if (jugador.equals(nombreJugadorRojo)) {
      this.puntajeJugadorRojo += puntos;
    }
  }
  
  public void setVidas(String jugador, int vidas) {
    if (jugador.equals(nombreJugadorAzul)) {
      this.vidasJugadorAzul = vidas;
    } else if (jugador.equals(nombreJugadorRojo)) {
      this.vidasJugadorRojo = vidas;
    }
  }
  
  public void reducirVida(String jugador) {
    if (jugador.equals(nombreJugadorAzul) && vidasJugadorAzul > 0) {
      this.vidasJugadorAzul--;
    } else if (jugador.equals(nombreJugadorRojo) && vidasJugadorRojo > 0) {
      this.vidasJugadorRojo--;
    }
  }
  
  public int getNivelActual() {
    return nivelActual;
  }
  
  public int getPuntajeJugadorAzul() {
    return puntajeJugadorAzul;
  }
  
  public int getPuntajeJugadorRojo() {
    return puntajeJugadorRojo;
  }
  
  public int getVidasJugadorAzul() {
    return vidasJugadorAzul;
  }
  
  public int getVidasJugadorRojo() {
    return vidasJugadorRojo;
  }
  
  public String getNombreJugadorAzul() {
    return nombreJugadorAzul;
  }
  
  public String getNombreJugadorRojo() {
    return nombreJugadorRojo;
  }
  
  public String getEstadoJuego() {
    return String.format(
      "=== ESTADO DEL JUEGO ===\n" +
      "Nivel: %d\n" +
      "%s - Puntaje: %d | Vidas: %d\n" +
      "%s - Puntaje: %d | Vidas: %d",
      nivelActual,
      nombreJugadorAzul, puntajeJugadorAzul, vidasJugadorAzul,
      nombreJugadorRojo, puntajeJugadorRojo, vidasJugadorRojo
    );
  }
}