package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.facades.dtos.CategoriaHechoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.model.Pdi;
import ar.edu.utn.dds.k3003.repository.PdIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PdIService {

    private final PdIRepository repo;

    @Autowired
    public PdIService(PdIRepository repo) {
        this.repo = repo;
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

        // Si ya existe, traigo el que tengo en bd
        Optional<Pdi> existente = pdi.id() != null
                ? repo.findById(Long.parseLong(pdi.id()))
                : Optional.empty();

        // Si existe, actualizo fecha process_dt
        if (existente.isPresent()) {
            Pdi pdiExistente = existente.get();
            pdiExistente.setProcessDt(LocalDateTime.now());
            return existente.get();
        }

        // Genero etiquetas
        List<String> etiquetas = generarEtiquetas(pdi);

        // Lo guardo en bd
        Pdi nuevaPdI = new Pdi(
                pdi.hechoId(),
                pdi.contenido(),
                pdi.descripcion(),
                pdi.lugar(),
                pdi.momento(),
                etiquetas
        );

        return repo.save(nuevaPdI);

    }

    private static final Map<CategoriaHechoEnum, List<String>> VOCABULARIO = Map.of(
            CategoriaHechoEnum.ENTRETENIMIENTO, List.of("cine", "música", "teatro", "concierto", "festival"),
            CategoriaHechoEnum.EDUCACIONAL, List.of("escuela", "universidad", "examen", "curso", "clase"),
            CategoriaHechoEnum.POLITICO, List.of("elección", "gobierno", "senado", "partido", "ministro"),
            CategoriaHechoEnum.DESASTRE, List.of("terremoto", "inundación", "incendio", "huracán", "explosión"),
            CategoriaHechoEnum.OTRO, List.of("general", "varios")
    );

    private String normalizar(String texto) {
        if (texto == null) return "";
        return texto
                .toLowerCase()
                .replaceAll("[áàäâ]", "a")
                .replaceAll("[éèëê]", "e")
                .replaceAll("[íìïî]", "i")
                .replaceAll("[óòöô]", "o")
                .replaceAll("[úùüû]", "u")
                .replaceAll("[^a-z0-9 ]", " "); // quita símbolos raros
    }

    private List<String> generarEtiquetas(PdIDTO pdi) {

        Set<String> etiquetas = new HashSet<>();

        String texto = normalizar(
                (pdi.contenido() != null ? pdi.contenido() : "") + " " +
                        (pdi.descripcion() != null ? pdi.descripcion() : "")
        );

        for (var entry : VOCABULARIO.entrySet()) {
            String categoria = entry.getKey().name();
            List<String> palabras = entry.getValue();

            for (String palabra : palabras) {
                if (texto.contains(palabra)) {
                    etiquetas.add(categoria.toLowerCase()); // etiqueta por categoría
                    etiquetas.add(palabra); // etiqueta por palabra clave
                }
            }
        }

        if (etiquetas.isEmpty()) {
            etiquetas.add("sin_categoria");
        }

        return new ArrayList<>(etiquetas);

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
