package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Coleccion;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryColeccionRepo {

  private List<Coleccion> almacenamiento = new ArrayList<>();

  public Coleccion guardar(Coleccion c) {
    almacenamiento.add(c);
    return c;
  }

  public List<Coleccion> listar() {
    return new ArrayList<>(almacenamiento);
  }

  public Optional<Coleccion> buscarPorId(Long id) {
    return almacenamiento.stream().filter(c -> c.getId().equals(id)).findFirst();
  }
}