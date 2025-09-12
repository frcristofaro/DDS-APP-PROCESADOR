package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO; //REVISAR
import ar.edu.utn.dds.k3003.app.FachadaSolicitudesRemote;
//import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
import ar.edu.utn.dds.k3003.model.Pdi;
import ar.edu.utn.dds.k3003.repository.PdIRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PdIService {

    private final PdIRepository repo;
    //private final FachadaSolicitudesRemote fachadaSolicitudes;


    public PdIService(PdIRepository repo) { //, FachadaSolicitudesRemote fachadaSolicitudes) {
        this.repo = repo;
        //this.fachadaSolicitudes = fachadaSolicitudes;
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

        // Valido hecho con Solicitudes
//        if(!fachadaSolicitudes.estaActivo(pdi.hechoId())) {
//            throw new IllegalStateException("El hecho fue censurado o está inactivo");
//        }

        // Valido si ya existe
        Optional<Pdi> existente = pdi.id() != null
                ? repo.findById(Long.parseLong(pdi.id()))
                : Optional.empty();

        if (existente.isPresent()) return existente.get();

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

    private static final Map<String, List<String>> VOCABULARIO = Map.of(
            "ENTRETENIMIENTO", List.of("cine", "música", "teatro", "concierto", "festival"),
            "EDUCACIONAL", List.of("escuela", "universidad", "examen", "curso", "clase"),
            "POLITICO", List.of("elección", "gobierno", "senado", "partido", "ministro"),
            "DESASTRE", List.of("terremoto", "inundación", "incendio", "huracán", "explosión"),
            "OTRO", List.of("general", "varios")
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

    //TODO
    private List<String> generarEtiquetas(PdIDTO pdi) {

        Set<String> etiquetas = new HashSet<>();

        String texto = normalizar(
                (pdi.contenido() != null ? pdi.contenido() : "") + " " +
                        (pdi.descripcion() != null ? pdi.descripcion() : "")
        );

        for (var entry : VOCABULARIO.entrySet()) {
            String categoria = entry.getKey();
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


//    TODO
//    public List<Pdi> listarTodos() {
//        return repo.findAll();
//    }

//    TODO
//    public void guardar(Pdi pdi) {
//        repo.save(pdi);
//    }

}
