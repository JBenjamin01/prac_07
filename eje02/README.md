# Ejercicio 02: Singleton con Recursos Compartidos

<h2 align="center">
  Ejercicio 02 (Intermedio): Singleton con recursos compartidos
  <p align="center">
    <a href="#">
      <img src="https://img.shields.io/badge/C%2B%2B-11-blue?logo=c%2B%2B&logoColor=white">
    </a>
    <a href="#">
      <img src="https://img.shields.io/badge/Actividad%20Académica-UNSA-red?style=flat">
    </a>
  </p>
</h4>

<h3>Explicación breve del código</h3>

<p align="justify">
  El programa implementa un <b>patrón Singleton</b> en <b>C++</b> para crear un 
  <b>logger</b> (registrador de eventos) que escribe mensajes en un archivo llamado 
  <code>bitacora.log</code>. Este patrón garantiza que <b>solo exista una única instancia</b> 
  de la clase <code>Logger</code> durante toda la ejecución del programa.
</p>

<ul>
  <li><b>static Logger* instancia:</b> almacena la única instancia del logger.</li>
  <li><b>getInstance():</b> crea la instancia si no existe y devuelve siempre la misma.</li>
  <li><b>log():</b> agrega una línea al archivo con la <b>fecha, hora y mensaje</b>.</li>
  <li><b>funcionA()</b> y <b>funcionB()</b> usan el mismo logger para escribir mensajes, demostrando que comparten el mismo recurso.</li>
</ul>

<p><b>Resultado</b></p>

<h1 align="center">
  <br>
  <a href="#"><img src="eje2.png" width="1200"></a>
  <br>
</h1>
