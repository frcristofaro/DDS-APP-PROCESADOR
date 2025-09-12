package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Pdi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PdIRepository extends JpaRepository<Pdi, Long> {

    List<Pdi> findByHechoId(String hechoId);

}
