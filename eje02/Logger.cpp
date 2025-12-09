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

    // metodo para registrar los mensajes en el logger
    void log(const string& mensaje) {
        time_t ahora = time(nullptr);
        tm* t= localtime(&ahora);

        char buffer[80];
        strftime(buffer, sizeof(buffer), "%Y-%m-%d %H:%M:%S", t);

        archivo << "[" << buffer << "] " << mensaje << endl;
    }
    

Logger* Logger::instancia = nullptr;

void funcionA() {
    Logger::getInstance()->log("Mensaje desde funcionA()");
}

void funcionB() {
    Logger::getInstance()->log("Mensaje desde funcionB()");
}

int main() {
    Logger::getInstance()->log("Inicio del programa");
    funcionA();
    funcionB();
    Logger::getInstance()->log("Fin del programa");

    cout << "Logs escritos en bitacora.log" << endl;
    return 0;
}

};