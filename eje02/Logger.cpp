#include <iostream>
#include <fstream>
#include <string>
#include <ctime>
using namespace std;

class Logger {
private:
    static Logger* instancia;  // Instancia
    ofstream archivo;         

    Logger() {
        archivo.open("bitacora.log", ios::app);
    }

public:
    // metodo estático que verifica que aun no se haya creado una instancia
    static Logger* getInstance() {
        if (instancia == nullptr)
            instancia = new Logger();
        return instancia;
    }

};