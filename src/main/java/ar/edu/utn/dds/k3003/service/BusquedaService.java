package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.model.Pdi;
import ar.edu.utn.dds.k3003.nosql.PdiBusquedaDocument;
import ar.edu.utn.dds.k3003.nosql.PdiBusquedaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusquedaService {

    private final PdiBusquedaRepository searchRepo;

    @Autowired
    public BusquedaService(PdiBusquedaRepository searchRepo) {
        this.searchRepo = searchRepo;
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
        searchRepo.save(doc);
        System.out.println("PDI indexado en Mongo: " + pdi.getId());
    }

    public List<PdiBusquedaDocument> buscar(String texto, String tag, int page, int size) {
        if (texto == null || texto.isBlank()) return List.of();

        if (tag != null && !tag.isBlank()) {
            return searchRepo.buscarPorTextoYTag(texto, tag);
        } else {
            return searchRepo.buscarPorTexto(texto);
        }
    }

}
