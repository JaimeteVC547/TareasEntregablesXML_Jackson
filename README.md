Este repositorio contiene la resolución de las dos tareas de lectura, modificación y exportación de archivos XML en Java.

Contenido de las Tareas

Tarea 1: Procesamiento XML con DOM (tarea1)
Lectura del archivo inventario.xml usando el parser DOM (DocumentBuilder).
Modificación de las unidades de un producto en memoria.
Guardado del XML actualizado y exportación de datos a formato CSV.

Tarea 2: Procesamiento XML con Jackson (tarea2)
Mapeo de XML a objetos Java (Inventario, Material) usando XmlMapper de Jackson.
Búsqueda y actualización de stock por consola.
Serialización del nuevo inventario a inventario_actualizado.xml y exportación a inventario.csv.

Librerías Necesarias (lib/)
Para la ejecución de la Tarea 2 en VS Code (sin Maven) se requieren los siguientes ficheros .jar en la carpeta lib/:
jackson-core-2.17.2.jar
jackson-databind-2.17.2.jar
jackson-annotations-2.17.2.jar
jackson-dataformat-xml-2.17.2.jar
stax2-api-4.2.2.jar
woodstox-core-6.7.0.jar

Cómo Ejecutar el Proyecto
Abrir la carpeta raíz del proyecto en Visual Studio Code.
Navegar hasta la clase Main.java de la tarea que desees probar (tarea1 o tarea2).
Hacer clic sobre el botón Run que aparece sobre el método main.

Preguntas del enunciado
¿qué diferencia hay entre modificar el `Document` en memoria y guardar el resultado con
`Transformer`?
Modificar el objeto Document consiste únicamente en alterar la representación en RAM del árbol de nodos Java. Durante este paso, el archivo físico del disco duro no sufre ningún cambio. La función del Transformer es realizar la serialización de ese árbol DOM modificado: toma la estructura de nodos en memoria, la traduce de nuevo a la sintaxis de texto propia de XML y la escribe físicamente en el fichero de destino (inventario_actualizado.xml).

¿qué operaciones realizan `readValue()` y `writeValue()`?
En la librería Jackson, la función readValue() ejecuta el proceso de deserialización, leyendo el contenido del fichero XML e interpretando sus etiquetas para instanciar y rellenar automáticamente los objetos Java (Inventario y Material). Por su parte, writeValue() lleva a cabo la serialización, realizando el camino inverso: toma los datos almacenados en las instancias de los objetos Java y los escribe como un documento estructurado bajo el formato XML.

¿Por qué la exportación a CSV es una conversión de formato?
Se considera una conversión de formato porque cambia la arquitectura y el modelo conceptual de los datos. El formato XML es jerárquico y estructurado en árbol mediante etiquetas anidadas, mientras que el CSV es un formato plano y tabular compuesto por filas de texto separadas por un delimitador. Exportar de XML a CSV implica aplanar la información y transformar una estructura en árbol a una matriz lineal bidimensional.