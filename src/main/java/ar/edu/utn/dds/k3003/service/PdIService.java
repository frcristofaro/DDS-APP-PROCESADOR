package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.app.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.config.RabbitConfig;
import ar.edu.utn.dds.k3003.model.Pdi;
import ar.edu.utn.dds.k3003.repository.PdIRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PdIService {

    private final PdIRepository repo;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private BusquedaService busquedaService;

    @Autowired
    public PdIService(PdIRepository repo, RabbitTemplate rabbitTemplate) {
        this.repo = repo;
        this.rabbitTemplate = rabbitTemplate;
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

    public Pdi procesarPdI(PdIDTO pdi) {

        System.out.println("Iniciando procesarPdI");
        System.out.println("DTO recibido: " + pdi);

        // Crear nueva PdI
        Pdi nuevaPdI = new Pdi(
                pdi.hechoId(),
                pdi.descripcion(),
                pdi.lugar(),
                pdi.momento(),
                pdi.contenido(),
                pdi.urlImagen(),
                null,
                new ArrayList<>()
        );

        System.out.println("Nueva PdI antes de persistir: " + nuevaPdI);

        Pdi guardado = repo.save(nuevaPdI);

        rabbitTemplate.convertAndSend(RabbitConfig.COLA_PDI, guardado.getId().toString());
        System.out.println("Se envió PDI a cola: " + guardado.getId());

        return guardado;

    }

    public boolean eliminarPorId(String pdiId) {
        Long id = Long.parseLong(pdiId);
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

}
