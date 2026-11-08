package tarea2;

public class Material {
    private String codigo;
    private String nombre;
    private int unidades;

    // Constructor vacío obligatorio para que Jackson pueda deserializar
    public Material() {
    }

    // Constructor opcional con parámetros
    public Material(String codigo, String nombre, int unidades) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.unidades = unidades;
    }

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    @Override
    public String toString() {
        return codigo + " | " + nombre + " | " + unidades + " unidades";
    }
}