package ar.edu.utn.dds.k3003.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "Colecciones")
public class Coleccion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nombre;
  private String descripcion;
  private LocalDateTime fechaModificacion;

  public Coleccion() {}

  public Coleccion(String nombre, String descripcion) {
    this.nombre = nombre;
    this.descripcion = descripcion;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public LocalDateTime getFechaModificacion() {
    return fechaModificacion;
  }

  public void setFechaModificacion(LocalDateTime fechaModificacion) {
    this.fechaModificacion = fechaModificacion;
  }
}
