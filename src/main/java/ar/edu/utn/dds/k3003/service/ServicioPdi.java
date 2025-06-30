package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.model.Pdi;
import ar.edu.utn.dds.k3003.repository.RepositorioPdIs;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioPdi {

    private final RepositorioPdIs repo;

    public ServicioPdi(RepositorioPdIs repo) {
        this.repo = repo;
    }

    public void guardar(Pdi pdi) {
        repo.save(pdi);
    }

    public List<Pdi> listarPorHecho(String hechoId) {
        return repo.findByHechoId(hechoId);
    }

    public Optional<Pdi> buscar(String id) {
        try {
            return repo.findById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public List<Pdi> listarTodos() {
        return repo.findAll();
    }

}
