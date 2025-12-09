#ifndef CONEXIONBD_H
#define CONEXIONBD_H

#include <iostream>
#include <thread>
#include <chrono>
#include <mutex>
#include <random>

class ConexionBD {
private:
    static ConexionBD* instancia;    // La única instancia
    static std::mutex mtx;           // Es para proteger creación (thread-safe)
    static std::mutex printMtx;

    bool conectado;
    int intentos;

    // Constructor privado
    ConexionBD() : conectado(false), intentos(0) {}

    ConexionBD(const ConexionBD&) = delete;
    ConexionBD& operator=(const ConexionBD&) = delete;

public:
    // --- Singleton Thread-Safe (Double-Checked Locking) ---
    static ConexionBD* getInstancia() {
        if (instancia == nullptr) {                  
            std::lock_guard<std::mutex> lock(mtx);   
            if (instancia == nullptr) {              
                instancia = new ConexionBD();
            }
        }
        return instancia;
    }

    // Para mostrar la dirección (útil en las pruebas multihilo)
    void mostrarID() const {
        std::lock_guard<std::mutex> lock(printMtx);
        std::cout << "ID de instancia Singleton: " << this << "\n";
    }

    void conectar() {
        // Lo uso para simular retardo aleatorio
        std::this_thread::sleep_for(std::chrono::milliseconds(100 + rand() % 200));

        intentos++;

        std::lock_guard<std::mutex> lock(printMtx);
        if (!conectado) {
            std::cout << "[" << std::this_thread::get_id() << "] Intentando conectar...\n";
            conectado = true;
            std::cout << "[" << std::this_thread::get_id() << "] Conexión establecida.\n";
        } else {
            std::cout << "[" << std::this_thread::get_id() << "] Ya estaba conectado.\n";
        }
    }

    void estado() const {
        std::lock_guard<std::mutex> lock(printMtx);
        std::cout << "Estado final → " << (conectado ? "Conectado" : "Desconectado")
                << " | Intentos totales: " << intentos << "\n";
    }
};

#endif