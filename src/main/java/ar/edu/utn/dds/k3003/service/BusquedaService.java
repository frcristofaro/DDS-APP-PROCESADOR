package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.app.dtos.BusquedaResponse;
import ar.edu.utn.dds.k3003.model.Pdi;
import ar.edu.utn.dds.k3003.model.PdiBusquedaDocument;
import ar.edu.utn.dds.k3003.repository.PdiBusquedaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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


    public BusquedaResponse buscar(String texto, String tag, int page, int size) {

        if (texto == null || texto.isBlank()) {
            return new BusquedaResponse(List.of(), page, size, 0, 0);
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<PdiBusquedaDocument> resPage;

        if (tag != null && !tag.isBlank()) {
            // Tag exacto
            resPage = pdiBusquedaRepository.buscarPorTextoYTag(texto, tag, pageable);
            System.out.println("Buscando por texto: " + texto + " y tag EXACTO: " + tag);
        } else {
            resPage = pdiBusquedaRepository.buscarPorTexto(texto, pageable);
            System.out.println("Buscando por texto: " + texto);
        }

        // Eliminamos duplicados por hecho_id
        List<PdiBusquedaDocument> unicos = eliminarDuplicadosPorHecho(resPage.getContent());

        return new BusquedaResponse(
                unicos,
                page,
                size,
                resPage.getTotalElements(),
                resPage.getTotalPages()
        );
    }

    private List<PdiBusquedaDocument> eliminarDuplicadosPorHecho(List<PdiBusquedaDocument> docs) {
        return docs.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                PdiBusquedaDocument::getHechoId, // clave
                                d -> d, // valor
                                (d1, d2) -> d1 // si repite hechoId, uso el primero
                        ),
                        m -> new ArrayList<>(m.values())
                ));
    }

}
