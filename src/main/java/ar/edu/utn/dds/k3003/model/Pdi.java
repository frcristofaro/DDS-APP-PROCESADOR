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

    @Column(nullable = false)
    private String contenido;

    private String descripcion;
    private String lugar;

    private LocalDateTime momento;

    @ElementCollection
    private List<String> etiquetas;

    private LocalDateTime process_dt;

    public Pdi() {}

    public Pdi(String hechoId, String contenido, String descripcion, String lugar, LocalDateTime momento, List<String> etiquetas) {

        if(hechoId == null || contenido == null || momento == null) {
            throw new IllegalArgumentException("Hay campos críticos vacíos!");
        }

        this.hechoId = hechoId;
        this.contenido = contenido;
        this.lugar = lugar;
        this.momento = momento;
        this.descripcion = descripcion;
        this.etiquetas = etiquetas != null ? etiquetas : new ArrayList<>();
        this.process_dt = LocalDateTime.now();
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

    public LocalDateTime getProcessDt() { return process_dt; }

    public void setProcessDt(LocalDateTime process_dt) { this.process_dt = process_dt; }

    public void setId(Long id) { this.id = id; }
}
