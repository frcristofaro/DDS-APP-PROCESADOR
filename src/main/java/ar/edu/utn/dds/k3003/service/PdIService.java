package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.app.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.model.Pdi;
import ar.edu.utn.dds.k3003.repository.PdIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PdIService {

    private final PdIRepository repo;
    private final OCRProcesador ocrService;
    private final EtiquetadorImagenes etiquetadorService;

    @Autowired
    public PdIService(PdIRepository repo, OCRService ocrService, EtiquetadorService etiquetadorService) {
        this.repo = repo;
        this.ocrService = ocrService;
        this.etiquetadorService = etiquetadorService;
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

        // Si ya existe, traigo el que tengo en la base
        Optional<Pdi> existente = pdi.id() != null
                ? repo.findById(Long.parseLong(pdi.id()))
                : Optional.empty();

        if (existente.isPresent()) {
            Pdi pdiExistente = existente.get();
            pdiExistente.setProcessDt(LocalDateTime.now());
            System.out.println("Actualizando PdI existente: " + pdiExistente);
            return repo.save(pdiExistente);
        }

        String ocrResultado = null;
        List<String> etiquetas = new ArrayList<>();

        if (pdi.urlImagen() != null && !pdi.urlImagen().isBlank()) {
            try {
                System.out.println("Procesando OCR para URL: " + pdi.urlImagen());
                ocrResultado = ocrService.procesarImagen(pdi.urlImagen());
                System.out.println("Resultado OCR: " + ocrResultado);
            } catch (Exception e) {
                System.err.println("Error al procesar OCR: " + e.getMessage());
            }

            try {
                System.out.println("Etiquetando imagen...");

                List<Map<String, Object>> etiquetasRaw = etiquetadorService.etiquetarImagen(pdi.urlImagen());

                for (Map<String, Object> etiquetaMap : etiquetasRaw) {
                    Object label = etiquetaMap.get("label");
                    if (label != null) {
                        etiquetas.add(label.toString());
                    }
                }
                System.out.println("Etiquetas obtenidas: " + etiquetas);
            } catch (Exception e) {
                System.err.println("Error al etiquetar imagen: " + e.getMessage());
            }
        }

        // Crear nueva PdI
        Pdi nuevaPdI = new Pdi(
                pdi.hechoId(),
                pdi.descripcion(),
                pdi.lugar(),
                pdi.momento(),
                pdi.contenido(),
                pdi.urlImagen(),
                ocrResultado,
                etiquetas
        );

        System.out.println("Nueva PdI antes de persistir: " + nuevaPdI);

        return repo.save(nuevaPdI);

    }


    /*
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
    */

    public boolean eliminarPorId(String pdiId) {
        Long id = Long.parseLong(pdiId);
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

}
