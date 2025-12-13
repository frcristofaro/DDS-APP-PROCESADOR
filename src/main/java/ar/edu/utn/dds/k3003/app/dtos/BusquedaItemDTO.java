package ar.edu.utn.dds.k3003.app.dtos;

public class BusquedaItemDTO {

    public enum Tipo {
        PDI,
        HECHO
    }

    private Tipo tipo;
    private Object data;

    public BusquedaItemDTO(Tipo tipo, Object data) {
        this.tipo = tipo;
        this.data = data;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Object getData() {
        return data;
    }
}
