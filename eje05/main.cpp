#include <iostream>
#include <thread>
#include <vector>
#include "ConexionBD.h"

void tarea(int num) {
    // Todas las tareas intentan obtener la misma instancia
    ConexionBD* c = ConexionBD::getInstancia();

    // Mostrar dirección de memoria (prueba de que es la misma)
    if (num == 0) {
        c->mostrarID();
    }

    c->conectar();
}

int main() {
    std::cout << "=== Prueba de Singleton Thread-Safe ===\n\n";

    const int NUM_HILOS = 8;
    std::vector<std::thread> hilos;

    for (int i = 0; i < NUM_HILOS; i++) {
        hilos.emplace_back(tarea, i);
    }

    for (auto& h : hilos) {
        h.join();
    }

    std::cout << "\n";
    ConexionBD::getInstancia()->estado();

    return 0;
}