package tarea2;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "inventario")
public class Inventario {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "material")
    private List<Material> materiales = new ArrayList<>();

    // Constructor vacío obligatorio
    public Inventario() {
    }

    public Inventario(List<Material> materiales) {
        this.materiales = materiales;
    }

    public List<Material> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<Material> materiales) {
        this.materiales = materiales;
    }
}
