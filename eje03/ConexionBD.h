#ifndef CONEXIONBD_H
#define CONEXIONBD_H

#include <iostream>
#include <thread>
#include <chrono>

class ConexionBD {
private:
    static ConexionBD* instancia;
    bool conectado;
    int intentos;

    ConexionBD() : conectado(false), intentos(0) {}

    ConexionBD(const ConexionBD&) = delete;
    ConexionBD& operator=(const ConexionBD&) = delete;

public:
    static ConexionBD* getInstancia() {
        if (instancia == nullptr) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    void conectar() {
        intentos++;
        if (!conectado) {
            std::cout << "Intentando conectar a la BD...\n";
            std::this_thread::sleep_for(std::chrono::milliseconds(500));
            conectado = true;
            std::cout << "Conexión establecida.\n";
        } else {
            std::cout << "Ya existe una conexión activa.\n";
        }
    }

    void desconectar() {
        if (conectado) {
            conectado = false;
            std::cout << "Conexión cerrada.\n";
        } else {
            std::cout << "No hay conexión activa.\n";
        }
    }

    void estado() const {
        std::cout << "Estado: " << (conectado ? "Conectado" : "Desconectado")
                << " | Intentos: " << intentos << "\n";
    }
};

#endif
