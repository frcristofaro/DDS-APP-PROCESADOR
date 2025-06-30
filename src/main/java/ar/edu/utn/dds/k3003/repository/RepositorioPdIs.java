package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Pdi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioPdIs extends JpaRepository<Pdi, Long> {

    List<Pdi> findByHechoId(String hechoId);

}
