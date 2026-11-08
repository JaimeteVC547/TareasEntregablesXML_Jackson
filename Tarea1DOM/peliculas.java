package Tarea1DOM;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.Scanner;

public class peliculas {

    public static void main(String[] args) {
        File origenXml = new File("datos/peliculas.xml");
        File salidaXml = new File("datos/peliculas_actualizadas.xml");

        // Apartado 1: Leer y mostrar catálogo inicial
        System.out.println(" CATALOGO ORIGINAL ");
        leerYMostrarCatalogo(origenXml);

        // Apartado 2: Buscar película y modificar copias en RAM
        System.out.println("\n ACTUALIZACIÓN DE COPIAS ");
        // Para pasar la ID al apartado 4, podemos pedirla o guardarla al actualizar
        Document docModificado = buscarPeliculaYActualizarCopias(origenXml);

        // Apartado 3 y 4: Solo se ejecutan si la película existía y se modificó
        if (docModificado != null) {
            // Apartado 3: Guardar en peliculas_actualizadas.xml
            boolean guardadoOK = guardarCatalogoActualizado(docModificado, salidaXml);

            if (guardadoOK) {
                // Apartado 4: Volver a leer el fichero de salida generado
                // (puedes pasarle el id buscado o hacer una verificación general)
                volverALeerSalida(salidaXml, "P02"); // Ejemplo con P02
            }
        }
    }

    
    // Apartado 1: Leer y mostrar el catálogo de películas junto con los totales.
    public static void leerYMostrarCatalogo(File archivoXml) {
        try {
            DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factoria.newDocumentBuilder();
            Document documento = builder.parse(archivoXml);

            NodeList peliculas = documento.getElementsByTagName("pelicula");
            int totalCopias = 0;

            for (int i = 0; i < peliculas.getLength(); i++) {
                Node pelicula = peliculas.item(i);
                
                if (pelicula.getNodeType() == Node.ELEMENT_NODE) {
                    Element peliculaElement = (Element) pelicula;

                    String id = peliculaElement.getAttribute("id");

                    // Extraer texto de los elementos hijos
                    String titulo = peliculaElement.getElementsByTagName("titulo").item(0).getTextContent();
                    String genero = peliculaElement.getElementsByTagName("genero").item(0).getTextContent();
                    String copiasTexto = peliculaElement.getElementsByTagName("copias").item(0).getTextContent();

                    // Convertir copias a número para poder sumarlas
                    int copias = Integer.parseInt(copiasTexto);
                    totalCopias += copias;

                    // Mostrar la información de la película
                    System.out.println(id + " | " + titulo + " | " + genero + " | " + copias + " copias");
                }
            }

            // Muestra de los datos que nos piden en el enunciado
            System.out.println("Películas registradas: " + peliculas.getLength());
            System.out.println("Copias totales: " + totalCopias);

        } catch (Exception ex) {
            System.getLogger(peliculas.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public static Document buscarPeliculaYActualizarCopias(File archivoXml) {
        try {
            Scanner teclado = new Scanner(System.in);

            // 1. Solicitar identificador limpiando espacios al principio y al final
            System.out.print("Introduce el identificador de la película a modificar (ej. P02): ");
            String idBuscado = teclado.nextLine().trim();

            // 2. Cargar el documento XML en memoria
            DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factoria.newDocumentBuilder();
            Document documento = builder.parse(archivoXml);

            NodeList peliculas = documento.getElementsByTagName("pelicula");
            Element peliculaEncontrada = null;

            // 3. Buscar la película por el atributo "id"
            for (int i = 0; i < peliculas.getLength(); i++) {
                Node pelicula = peliculas.item(i);
                if (pelicula.getNodeType() == Node.ELEMENT_NODE) {
                    Element peliculaElement = (Element) pelicula;
                    if (peliculaElement.getAttribute("id").equals(idBuscado)) {
                        peliculaEncontrada = peliculaElement;
                        break;
                    }
                }
            }

            // 4. Si no existe, mostrar mensaje y terminar finalizando el proceso
            if (peliculaEncontrada == null) {
                System.out.println("Película no encontrada");
                return null;
            }

            // 5. Si existe, mostrar datos actuales
            String titulo = peliculaEncontrada.getElementsByTagName("titulo").item(0).getTextContent();
            String genero = peliculaEncontrada.getElementsByTagName("genero").item(0).getTextContent();
            String copiasAnterioresTexto = peliculaEncontrada.getElementsByTagName("copias").item(0).getTextContent();
            int copiasAnteriores = Integer.parseInt(copiasAnterioresTexto);

            System.out.println("\nPelícula encontrada:");
            System.out.println(idBuscado + " | " + titulo + " | " + genero + " | " + copiasAnteriores + " copias");

            // 6. Solicitar y validar la nueva cantidad 
            int nuevasCopias = -1;
            while (nuevasCopias < 0) {
                System.out.print("Introduce el nuevo número de copias: ");
                String entrada = teclado.nextLine().trim();

                try {
                    int valor = Integer.parseInt(entrada);
                    if (valor >= 0) {
                        nuevasCopias = valor;
                    } else {
                        System.out.println("Aviso: El número de copias debe ser mayor o igual a 0.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Aviso: Debes introducir un número entero válido.");
                }
            }

            // 7. Cambiar únicamente el contenido del elemento <copias> en el DOM
            peliculaEncontrada.getElementsByTagName("copias").item(0).setTextContent(String.valueOf(nuevasCopias));

            // 8. Mostrar el resultado de la modificación
            System.out.println("\nCambio realizado:");
            System.out.println("Título: " + titulo + " | Copias anteriores: " + copiasAnteriores + " | Nuevas copias: " + nuevasCopias);

            // Devuelve el Document modificado porque luego lo utlizaremos en el apartado 3
            return documento;

        } catch (Exception ex) {
            System.getLogger(peliculas.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return null;
        }
    }

    public static boolean guardarCatalogoActualizado(Document documento, File archivoSalida) {
        if (documento == null) {
            return false;
        }

        try {
            // 1. Crear la factoría y el objeto Transformer
            TransformerFactory factoria = TransformerFactory.newInstance();
            Transformer transformer = factoria.newTransformer();

            // 2. Configuramos la codificación UTF-8 y activar la indentación
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // 3. Asignamos el origen y el destino
            DOMSource fuente = new DOMSource(documento);
            StreamResult resultado = new StreamResult(archivoSalida);

            // 4. Escribir los cambios en el disco duro
            transformer.transform(fuente, resultado);

            System.out.println("\nFichero actualizado guardado correctamente en: " + archivoSalida.getPath());
            return true;

        } catch (Exception ex) {
            System.getLogger(peliculas.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

        
     // Apartado 4: Volver a leer la salida cargando datos/peliculas_actualizadas.xml 
     // en un nuevo Document para verificar los datos guardados en disco.
    public static void volverALeerSalida(File archivoSalida, String idModificado) {
        try {
            DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factoria.newDocumentBuilder();
            
            // Cargar obligatoriamente el nuevo fichero guardado
            Document documentoActualizado = builder.parse(archivoSalida);

            NodeList peliculas = documentoActualizado.getElementsByTagName("pelicula");
            int sumaCopiasGuardadas = 0;

            System.out.println("\n=== COMPROBACIÓN DE SALIDA (SEGUNDA LECTURA DESDE DISCO) ===");

            for (int i = 0; i < peliculas.getLength(); i++) {
                Node pelicula = peliculas.item(i);

                if (pelicula.getNodeType() == Node.ELEMENT_NODE) {
                    Element peliculaElement = (Element) pelicula;
                    String id = peliculaElement.getAttribute("id");
                    int copias = Integer.parseInt(peliculaElement.getElementsByTagName("copias").item(0).getTextContent());

                    sumaCopiasGuardadas += copias;

                    // Muestra únicamente las copias de la película modificada
                    if (id.equals(idModificado)) {
                        String titulo = peliculaElement.getElementsByTagName("titulo").item(0).getTextContent();
                        System.out.println("Película modificada en archivo: " + id + " | " + titulo + " | Copias guardadas: " + copias);
                    }
                }
            }

            System.out.println("Suma total de copias en el fichero guardado: " + sumaCopiasGuardadas);

        } catch (Exception ex) {
            System.getLogger(peliculas.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
