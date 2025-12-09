#include <iostream>
#include "ConexionBD.h"

int main() {
    // La misma instancia única
    ConexionBD* c1 = ConexionBD::getInstancia();
    ConexionBD* c2 = ConexionBD::getInstancia();

    std::cout << "Probando Singleton...\n\n";

    // Para demostrar que ambas variables apuntan a la misma instancia
    std::cout << "Direccion de c1: " << c1 << "\n";
    std::cout << "Direccion de c2: " << c2 << "\n\n";

    // Probar conexiones
    c1->estado();
    c1->conectar();
    c1->estado();

    std::cout << "\nProbando llamar conectar() desde c2 (debe decir que ya esta conectado):\n";
    c2->conectar();
    c2->estado();

    std::cout << "\nDesconectando...\n";
    c1->desconectar();
    c1->estado();

    std::cout << "\nIntentando desconectar otra vez:\n";
    c2->desconectar();
    c2->estado();

    return 0;
}