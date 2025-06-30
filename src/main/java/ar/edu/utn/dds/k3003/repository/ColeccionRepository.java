package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColeccionRepository extends JpaRepository<Coleccion, String> {

  Optional<Coleccion> findById(String id);

}
