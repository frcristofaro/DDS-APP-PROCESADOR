package ar.edu.utn.dds.k3003.app.dtos;

import ar.edu.utn.dds.k3003.model.HechoBusquedaDocument;
import ar.edu.utn.dds.k3003.model.PdiBusquedaDocument;
import org.glassfish.jersey.internal.guava.Lists;

import java.util.ArrayList;
import java.util.List;

public class BusquedaItemDTO {

//    public enum Tipo {
//        PDI,
//        HECHO
//    }

//    private Tipo tipo;
    private List<Object> items = new ArrayList<>();
    private List<HechoBusquedaDocument> hecho = new ArrayList<>();
    private List<PdiBusquedaDocument> pdi = new ArrayList<>();

    public BusquedaItemDTO(List<HechoBusquedaDocument> hecho, List<PdiBusquedaDocument> pdi) {
        this.hecho = hecho;
        this.pdi = pdi;
    }

    public BusquedaItemDTO() {}

    //    public Tipo getTipo() {
//        return tipo;
//    }
//
//    public void setTipo(Tipo tipo) {
//        this.tipo = tipo;
//    }


    public List<HechoBusquedaDocument> getHecho() {
        return hecho;
    }

    public void setHecho(List<HechoBusquedaDocument> hecho) {
        this.hecho = hecho;
    }

    public List<PdiBusquedaDocument> getPdi() {
        return pdi;
    }

    public void setPdi(List<PdiBusquedaDocument> pdi) {
        this.pdi = pdi;
    }

    public List<Object> getItems() {
        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }
}
