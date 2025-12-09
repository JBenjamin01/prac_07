<h2 align="center">
  Ejercicio 03 (Aplicado): Simulación de conexión a una base de datos usando Singleton
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
  Este programa implementa una clase <code>ConexionBD</code> que simula de forma sencilla la conexión a una base de
  datos. Se utiliza el patrón <b>Singleton</b> para garantizar que <b>solo exista una única conexión activa</b>
  durante toda la ejecución del programa.
</p>

<p align="justify">
  La instancia es accesible mediante el método <code>getInstancia()</code>, que siempre devuelve la misma dirección de
  memoria, incluso si diferentes partes del programa intentan crear nuevas conexiones.
</p>

<h3>Características principales</h3>
<ul>
  <li><b>static ConexionBD* instancia:</b> almacena la única instancia permitida.</li>
  <li><b>getInstancia():</b> crea la instancia al inicio y luego siempre la reutiliza.</li>
  <li><b>conectar():</b> simula un intento de conexión y evita reconectar si ya está activa.</li>
  <li><b>desconectar():</b> cierra la conexión si está activa.</li>
  <li><b>estado():</b> muestra si está conectado y cuántos intentos se hicieron.</li>
  <li><b>intentos:</b> contador simple que permite ver cuántas veces se llamó a <code>conectar()</code>.</li>
</ul>

<h3>Explicación del comportamiento</h3>
<p align="justify">
  En el <code>main</code>, se crean dos punteros (<code>c1</code> y <code>c2</code>) llamando a <code>getInstancia()</code>.
  Ambos apuntan a la <b>misma dirección de memoria</b>, lo que demuestra que la conexión es única. Luego se llama a
  <code>conectar()</code>, <code>estado()</code> y <code>desconectar()</code> desde ambos punteros, mostrando que todos
  operan sobre la misma instancia global.
</p>

<h3>Resultado esperado</h3>
<p align="justify">El programa debe mostrar:</p>
<ul>
  <li>Que <code>c1</code> y <code>c2</code> tienen la misma dirección (prueba del Singleton).</li>
  <li>Que solo se puede establecer una conexión activa.</li>
  <li>Que los métodos llamados desde <code>c1</code> y <code>c2</code> afectan al mismo objeto.</li>
  <li>Mensajes informativos según el estado: conectado, desconectado o ya conectado.</li>
</ul>

<h1 align="center">
  <br>
  <a href="#"><img src="eje03.png" width="1200"></a>
  <br>
</h1>

<p align="justify">
  Con esto se verifica el funcionamiento del patrón Singleton en un contexto práctico de simulación de conexión a base
  de datos.
</p>