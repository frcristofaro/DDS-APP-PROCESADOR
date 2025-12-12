package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pdis")
public class Pdi {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(nullable = false)
    private String hechoId;

    @Getter
    @Column(nullable = false)
    private String contenido;

    @Setter
    @Getter
    private String descripcion;

    @Setter
    @Getter
    private String lugar;

    @Setter
    @Getter
    private String momento;

    @Getter
    private String urlImagen;
    @Setter
    @Getter
    private String ocrResultado;

    @Setter
    @Getter
    @ElementCollection
    @CollectionTable(name = "pdi_etiquetas", joinColumns = @JoinColumn(name = "pdi_id"))
    private List<String> etiquetas = new ArrayList<>();

    private LocalDateTime process_dt;

    public Pdi() {}

    public Pdi(String hechoId, String contenido, String descripcion, String lugar, String momento, String urlImagen, String ocrResultado, List<String> etiquetas) {

        if(hechoId == null || contenido == null || momento == null) {
            throw new IllegalArgumentException("Hay campos críticos vacíos!");
        }
        this.hechoId = hechoId;
        this.descripcion = descripcion;
        this.lugar = lugar;
        this.momento = momento;
        this.contenido = contenido;
        this.urlImagen = urlImagen;
        this.ocrResultado = ocrResultado;
        this.etiquetas = etiquetas != null ? etiquetas : new ArrayList<>();
        this.process_dt = LocalDateTime.now();
    }

    public LocalDateTime getProcessDt() { return process_dt; }

    public void setProcessDt(LocalDateTime process_dt) { this.process_dt = process_dt; }

    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }

}
