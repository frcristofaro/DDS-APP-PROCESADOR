package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.app.dtos.BusquedaItemDTO;
import ar.edu.utn.dds.k3003.app.dtos.BusquedaResponse;
import ar.edu.utn.dds.k3003.model.HechoBusquedaDocument;
import ar.edu.utn.dds.k3003.model.Pdi;
import ar.edu.utn.dds.k3003.model.PdiBusquedaDocument;
import ar.edu.utn.dds.k3003.repository.HechoBusquedaRepository;
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

        private final HechoBusquedaRepository hechoBusquedaRepository;
        private final PdiBusquedaRepository pdiBusquedaRepository;

        @Autowired
        public BusquedaService(PdiBusquedaRepository pdiBusquedaRepository, HechoBusquedaRepository hechoBusquedaRepository) {
            this.pdiBusquedaRepository = pdiBusquedaRepository;
            this.hechoBusquedaRepository = hechoBusquedaRepository;
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
                    pdi.getEtiquetas(),
                    null
            );
            pdiBusquedaRepository.save(doc);
            System.out.println("PDI indexado en Mongo: " + pdi.getId());
        }


    public BusquedaResponse buscar(String texto, String tag, int page, int size) {

        if (texto == null || texto.isBlank()) {
            return new BusquedaResponse(List.of(), page, size, 0, 0);
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<PdiBusquedaDocument> resPDI;
        Page<HechoBusquedaDocument> resHechos;

        if (tag != null && !tag.isBlank()) {
            // Tag exacto (pero solo para acelerar la búsqueda — NO para el filtrado final)
            resPDI = pdiBusquedaRepository.buscarPDIPorTextoYTag(texto, tag, pageable);
            resHechos = hechoBusquedaRepository.buscarHechoPorTexto(texto, pageable);
            System.out.println("Buscando por texto: " + texto + " y tag EXACTO (pre-filtro): " + tag);
        } else {
            resPDI = pdiBusquedaRepository.buscarPDIPorTexto(texto, pageable);
            resHechos = hechoBusquedaRepository.buscarHechoPorTexto(texto, pageable);
            System.out.println("Buscando por texto: " + texto);
        }

        List<HechoBusquedaDocument> res_hechos = resHechos.getContent();

        // 1) Eliminar duplicados por hecho_id (ANTES de filtrar y paginar)
        List<PdiBusquedaDocument> sinDuplicados = eliminarDuplicadosPorHecho(resPDI.getContent());

        // 2) Aplicar filtro EXTRA por tag (aunque ya lo haga el repo)
        List<PdiBusquedaDocument> res_pdis = sinDuplicados.stream()
                .filter(p -> tag == null || tag.isBlank() || p.getEtiquetas().contains(tag))
                .toList();

        List<BusquedaItemDTO> items = new ArrayList<>();

        // PDIs
        for (PdiBusquedaDocument pdi : res_pdis) {
            items.add(new BusquedaItemDTO(
                    BusquedaItemDTO.Tipo.PDI,
                    pdi
            ));
        }

        // Hechos
        for (HechoBusquedaDocument hecho : res_hechos) {
            items.add(new BusquedaItemDTO(
                    BusquedaItemDTO.Tipo.HECHO,
                    hecho
            ));
        }

        int total = items.size();

        int from = page * size;
        int to = Math.min(from + size, total);

        List<BusquedaItemDTO> pagina =
                from >= total ? List.of() : items.subList(from, to);

        int totalPages = (int) Math.ceil((double) total / size);

        return new BusquedaResponse(
                pagina,
                page,
                size,
                total,
                totalPages
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
