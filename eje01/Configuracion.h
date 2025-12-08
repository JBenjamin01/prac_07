#ifndef CONFIGURACION_H
#define CONFIGURACION_H

#include <string>
#include <iostream>
#include <set>

class Configuracion {
private:
    static Configuracion* instancia;
    std::string idioma;
    std::string zonaHoraria;

    // Es la lista válida de idiomas
    const std::set<std::string> idiomasValidos { "ES", "EN", "PT", "FR" };

    // Constructor privado
    Configuracion() : idioma("ES"), zonaHoraria("GMT-5") {}

public:
    // Mi método principal del Singleton
    static Configuracion* getInstancia() {
        if (instancia == nullptr) {
            instancia = new Configuracion();
        }
        return instancia;
    }

    void mostrarConfiguracion() const {
        std::cout << "\n---- CONFIGURACIÓN DEL SISTEMA ----\n";
        std::cout << "Idioma: " << idioma << "\n";
        std::cout << "Zona horaria: " << zonaHoraria << "\n";
        std::cout << "Dirección en memoria: " << this << "\n";
        std::cout << "-----------------------------------\n";
    }

    // Nueva característica: validación de idioma
    bool setIdioma(const std::string& idi) {
        if (idiomasValidos.count(idi)) {
            idioma = idi;
            return true;
        }
        std::cout << "Error: idioma no permitido.\n";
        return false;
    }

    void setZonaHoraria(const std::string& zona) { zonaHoraria = zona; }

    // Método útil para pruebas
    static void reset() {
        delete instancia;
        instancia = nullptr;
    }
};

#endif