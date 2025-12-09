#include "Configuracion.h"

int main() {
    auto* cfg1 = Configuracion::getInstancia();
    cfg1->mostrarConfiguracion();

    cfg1->setIdioma("EN");
    cfg1->setZonaHoraria("UTC");

    auto* cfg2 = Configuracion::getInstancia();
    cfg2->mostrarConfiguracion();

    std::cout << "Es la misma instancia? "
            << (cfg1 == cfg2 ? "Sí" : "No") << "\n";
}