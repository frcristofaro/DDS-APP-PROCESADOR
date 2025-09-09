package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pdis")
public class Pdi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String hechoId;
    private String contenido;
    private String descripcion;
    private String lugar;
    private LocalDateTime momento;

    @ElementCollection
    private List<String> etiquetas;

    public Pdi() {}

    public Pdi(String hechoId, String contenido, String descripcion, String lugar, LocalDateTime momento, List<String> etiquetas) {
        this.hechoId = hechoId;
        this.contenido = contenido;
        this.etiquetas = etiquetas != null ? etiquetas : new ArrayList<>();
        this.lugar = lugar;
        this.momento = momento;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }

    public String getHechoId() {
        return hechoId;
    }

    public String getContenido() {
        return contenido;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getLugar() { return lugar; }

    public void setLugar(String lugar) { this.lugar = lugar; }

    public LocalDateTime getMomento() { return momento; }

    public void setMomento(LocalDateTime momento) { this.momento = momento; }

    public void setId(Long id) { this.id = id; }
}
