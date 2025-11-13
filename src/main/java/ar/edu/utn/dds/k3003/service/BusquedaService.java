package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.model.Pdi;
import ar.edu.utn.dds.k3003.nosql.PdiBusquedaDocument;
import ar.edu.utn.dds.k3003.nosql.PdiBusquedaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusquedaService {

    private final PdiBusquedaRepository pdiBusquedaRepository;

    @Autowired
    public BusquedaService(PdiBusquedaRepository pdiBusquedaRepository) {
        this.pdiBusquedaRepository = pdiBusquedaRepository;
    }

    public void indexar(Pdi pdi) {
        PdiBusquedaDocument doc = new PdiBusquedaDocument(
                String.valueOf(pdi.getId()),
                pdi.getHechoId(),
                pdi.getDescripcion(),
                pdi.getLugar(),
                pdi.getMomento(),
                pdi.getContenido(),
                pdi.getUrlImagen(),
                pdi.getOcrResultado(),
                pdi.getEtiquetas()
        );
        pdiBusquedaRepository.save(doc);
        System.out.println("PDI indexado en Mongo: " + pdi.getId());
    }

    public List<PdiBusquedaDocument> buscar(String texto, String tag, int page, int size) {
        if (texto == null || texto.isBlank()) return List.of();

        List<PdiBusquedaDocument> res;

        if (tag != null && !tag.isBlank()) {
            res = pdiBusquedaRepository.buscarPorTextoYTag(texto, tag);
        } else {
            res = pdiBusquedaRepository.buscarPorTexto(texto);
        }

        return eliminarDuplicadosPorHecho(res);
    }

    private List<PdiBusquedaDocument> eliminarDuplicadosPorHecho(List<PdiBusquedaDocument> resultados) {
        Map<String, PdiBusquedaDocument> unicos = new LinkedHashMap<>();

        for (PdiBusquedaDocument doc : resultados) {
            String clave = doc.getHechoId() != null ? doc.getHechoId() : doc.getContenido();
            unicos.putIfAbsent(clave, doc);
        }

        return new ArrayList<>(unicos.values());
    }

}
