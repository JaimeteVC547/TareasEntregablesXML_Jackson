package tarea2;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        File origenXml = new File("datos/inventario.xml");
        File salidaXml = new File("datos/inventario_actualizado.xml");
        File salidaCsv = new File("datos/inventario.csv");

        // 1. Leer y mostrar el inventario original (Apartado 2)
        System.out.println("=== 1. INVENTARIO ORIGINAL ===");
        Inventario inventario = leerYMostrarInventario(origenXml);

        if (inventario == null) {
            return; // Termina si no se pudo leer el fichero de entrada
        }

        // 2. Buscar y actualizar unidades en memoria (Apartado 3)
        System.out.println("\n=== 2. ACTUALIZACIÓN DE MATERIAL ===");
        boolean modificado = buscarYModificarMaterial(inventario);

        // Si el material no existía, se interrumpe sin generar los ficheros de salida
        if (!modificado) {
            return;
        }

        // 3. Guardar y verificar el XML actualizado (Apartado 4)
        System.out.println("\n=== 3. GUARDAR Y VERIFICAR XML ===");
        Inventario inventarioActualizado = guardarYVerificarInventario(inventario, salidaXml);

        // 4. Convertir el inventario actualizado a CSV (Apartado 5)
        if (inventarioActualizado != null) {
            System.out.println("\n=== 4. EXPORTACIÓN A CSV ===");
            exportarACSV(inventarioActualizado, salidaCsv);
        }
    }

    /**
     * Apartado 2: Leer el XML mediante XmlMapper.readValue() y mostrar sus datos y totales.
     */
    public static Inventario leerYMostrarInventario(File archivoXml) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            Inventario inventario = xmlMapper.readValue(archivoXml, Inventario.class);

            int totalUnidades = 0;
            for (Material mat : inventario.getMateriales()) {
                System.out.println(mat);
                totalUnidades += mat.getUnidades();
            }

            System.out.println("Materiales registrados: " + inventario.getMateriales().size());
            System.out.println("Unidades totales: " + totalUnidades);

            return inventario;
        } catch (Exception e) {
            System.out.println("Error al leer el fichero de entrada: " + e.getMessage());
            return null;
        }
    }

    /**
     * Apartado 3: Buscar coincidencia exacta por código y actualizar las unidades con el setter.
     */
    public static boolean buscarYModificarMaterial(Inventario inventario) {
        Scanner teclado = new Scanner(System.in);
        System.out.print("Introduce el código del material a modificar (ej. M02): ");
        String codigoBuscado = teclado.nextLine().trim();

        Material materialEncontrado = null;
        for (Material mat : inventario.getMateriales()) {
            if (mat.getCodigo().equals(codigoBuscado)) {
                materialEncontrado = mat;
                break;
            }
        }

        if (materialEncontrado == null) {
            System.out.println("Material no encontrado");
            return false;
        }

        System.out.println("\nMaterial encontrado: " + materialEncontrado);

        int nuevasUnidades = -1;
        while (nuevasUnidades < 0) {
            System.out.print("Introduce la nueva cantidad de unidades: ");
            String entrada = teclado.nextLine().trim();
            try {
                int valor = Integer.parseInt(entrada);
                if (valor >= 0) {
                    nuevasUnidades = valor;
                } else {
                    System.out.println("Aviso: Las unidades deben ser mayores o iguales a 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Aviso: Debes introducir un número entero válido.");
            }
        }

        int unidadesAnteriores = materialEncontrado.getUnidades();
        materialEncontrado.setUnidades(nuevasUnidades);

        System.out.println("\nCambio realizado:");
        System.out.println("Nombre: " + materialEncontrado.getNombre() + 
                           " | Unidades anteriores: " + unidadesAnteriores + 
                           " | Nuevas unidades: " + nuevasUnidades);

        return true;
    }

    /**
     * Apartado 4: Guardar el objeto con XmlMapper.writeValue() y deserializarlo en una nueva instancia.
     */
    public static Inventario guardarYVerificarInventario(Inventario inventario, File archivoSalida) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT); // Formato legible (indentado)

            // Guardar objeto en fichero XML
            xmlMapper.writeValue(archivoSalida, inventario);
            System.out.println("Fichero guardado en: " + archivoSalida.getPath());

            // Deserializar en una nueva instancia de Inventario para verificar
            Inventario nuevoInventario = xmlMapper.readValue(archivoSalida, Inventario.class);

            int totalUnidadesNuevas = 0;
            for (Material mat : nuevoInventario.getMateriales()) {
                totalUnidadesNuevas += mat.getUnidades();
            }

            System.out.println("Verificación - Suma total de unidades en fichero guardado: " + totalUnidadesNuevas);
            return nuevoInventario;

        } catch (Exception e) {
            System.out.println("Error al guardar/recuperar el XML actualizado: " + e.getMessage());
            return null;
        }
    }

    /**
     * Apartado 5: Convertir la información de los objetos a un fichero CSV con cabecera.
     */
    public static void exportarACSV(Inventario inventario, File archivoCsv) {
        try (BufferedWriter writer = Files.newBufferedWriter(archivoCsv.toPath(), StandardCharsets.UTF_8)) {
            // Escribir cabecera separada por punto y coma
            writer.write("codigo; nombre; unidades");
            writer.newLine();

            int filasExportadas = 0;
            for (Material mat : inventario.getMateriales()) {
                String linea = mat.getCodigo() + "; " + mat.getNombre() + "; " + mat.getUnidades();
                writer.write(linea);
                writer.newLine();
                filasExportadas++;
            }

            System.out.println("Fichero CSV generado en: " + archivoCsv.getPath());
            System.out.println("Filas de datos exportadas: " + filasExportadas);

        } catch (Exception e) {
            System.out.println("Error al exportar a CSV: " + e.getMessage());
        }
    }
}
