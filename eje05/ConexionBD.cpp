#include "ConexionBD.h"

ConexionBD* ConexionBD::instancia = nullptr;
std::mutex ConexionBD::mtx;
