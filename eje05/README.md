<h2 align="center">
  Ejercicio 05 (Desafío): Singleton seguro en entornos multihilo
  <p align="center">
    <a href="#">
      <img src="https://img.shields.io/badge/C%2B%2B-17-blue?logo=c%2B%2B&logoColor=white">
    </a>
    <a href="#">
      <img src="https://img.shields.io/badge/Actividad%20Académica-UNSA-red?style=flat">
    </a>
  </p>
</h2>

<h3>Descripción del ejercicio</h3>
<p align="justify">
  Este programa implementa una versión mejorada del patrón <b>Singleton</b> usando mecanismos de sincronización para hacerlo <b>seguro en entornos multihilo</b>. 
  La clase <code>ConexionBD</code> simula una conexión a una base de datos y garantiza que 
  <b>solo una única instancia</b> pueda existir, incluso cuando varios hilos intentan crearla simultáneamente.
</p>

<h3>¿Qué lo hace diferente?</h3>
<p align="justify">
  A diferencia de un Singleton tradicional, esta versión utiliza <b>double-checked locking</b> junto con un 
  <code>std::mutex</code> para evitar condiciones de carrera. También incluye un mutex adicional para mantener ordenada 
  la salida en consola cuando múltiples hilos imprimen información al mismo tiempo.
</p>

<ul>
  <li><b>static ConexionBD* instancia:</b> almacena la única instancia global.</li>
  <li><b>static std::mutex mtx:</b> protege la creación de la instancia.</li>
  <li><b>getInstancia():</b> aplica <i>double-checked locking</i> para asegurar thread-safety.</li>
  <li><b>conectar():</b> simula el intento de conexión usando el mismo objeto desde varios hilos.</li>
  <li><b>mostrarID():</b> muestra la dirección de la instancia (útil para la prueba multihilo).</li>
</ul>

<h3>Comportamiento del programa</h3>
<p align="justify">
  El <code>main</code> lanza varios hilos simultáneamente. Cada uno intenta obtener la instancia del Singleton y llamar a 
  <code>conectar()</code>. Como resultado:
</p>

<ul>
  <li>Se crea <b>una sola instancia</b> sin importar cuántos hilos compitan.</li>
  <li>Solo un hilo establece la conexión; los demás detectan que ya existe.</li>
  <li>La salida por consola se sincroniza gracias al mutex adicional.</li>
  <li>El número de intentos coincide con la cantidad de hilos ejecutados.</li>
</ul>

<h3>Resultado esperado</h3>
<h1 align="center">
  <br>
  <a href="#"><img src="eje05.png" width="1200"></a>
  <br>
</h1>

<p align="justify">
  Se observa que todos los hilos comparten la misma dirección de instancia y solo uno establece la conexión, 
  demostrando que el Singleton thread-safe funciona correctamente.
</p>