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
            // Tag exacto (pero solo para acelerar la búsqueda — NO para el filtrado final)
            resPage = pdiBusquedaRepository.buscarPorTextoYTag(texto, tag, pageable);
            System.out.println("Buscando por texto: " + texto + " y tag EXACTO (pre-filtro): " + tag);
        } else {
            resPage = pdiBusquedaRepository.buscarPorTexto(texto, pageable);
            System.out.println("Buscando por texto: " + texto);
        }

        // 1) Eliminar duplicados por hecho_id (ANTES de filtrar y paginar)
        List<PdiBusquedaDocument> sinDuplicados = eliminarDuplicadosPorHecho(resPage.getContent());

        // 2) Aplicar filtro EXTRA por tag (aunque ya lo haga el repo)
        List<PdiBusquedaDocument> filtrados = sinDuplicados.stream()
                .filter(p -> tag == null || tag.isBlank() || p.getEtiquetas().contains(tag))
                .toList();

        // 3) Recalcular totales después del filtro
        int totalFiltrados = filtrados.size();

        // 4) Paginar manualmente
        int from = page * size;
        int to = Math.min(from + size, totalFiltrados);

        List<PdiBusquedaDocument> pagina =
                from >= totalFiltrados ? List.of() : filtrados.subList(from, to);

        int totalPages = (int) Math.ceil((double) totalFiltrados / size);

        // 5) Construir Response REAL
        return new BusquedaResponse(
                pagina,
                page,
                size,
                totalFiltrados,
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
