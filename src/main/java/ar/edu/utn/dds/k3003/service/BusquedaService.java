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
            return new BusquedaResponse(
                    List.of(),
                    List.of(),
                    page,
                    size,
                    0,
                    0
            );
        }

        // ===============================
        // 1️⃣ Buscar SIN paginar en Mongo
        // ===============================

        List<PdiBusquedaDocument> pdis;
        List<HechoBusquedaDocument> hechos;

        if (tag != null && !tag.isBlank()) {
            // Tag se usa solo para PDIs
            pdis = pdiBusquedaRepository.buscarPDIPorTextoYTag(texto, tag);
            hechos = List.of(); // no se buscan hechos con tag
            System.out.println("Buscando PDIs por texto y tag: " + texto + " / " + tag);
        } else {
            pdis = pdiBusquedaRepository.buscarPDIPorTexto(texto);
            hechos = hechoBusquedaRepository.buscarHechoPorTexto(texto);
            System.out.println("Buscando PDIs y Hechos por texto: " + texto);
        }

        // ===============================
        // 2️⃣ Limpiar y filtrar PDIs
        // ===============================

        // Eliminar duplicados por hecho_id
        List<PdiBusquedaDocument> pdisSinDuplicados =
                eliminarDuplicadosPorHecho(pdis);

        // Filtro extra por tag (defensivo)
        List<PdiBusquedaDocument> pdisFinales = pdisSinDuplicados.stream()
                .filter(p -> tag == null || tag.isBlank() || p.getEtiquetas().contains(tag))
                .toList();

        // ===============================
        // 3️⃣ Totales GLOBALES
        // ===============================

        int totalPdis = pdisFinales.size();
        int totalHechos = hechos.size();
        int totalItems = totalPdis + totalHechos;

        int totalPages = (int) Math.ceil((double) totalItems / size);

        // ===============================
        // 4️⃣ Paginado MANUAL
        // ===============================

        int from = page * size;
        int to = Math.min(from + size, totalItems);

        List<PdiBusquedaDocument> paginaPdis = List.of();
        List<HechoBusquedaDocument> paginaHechos = List.of();

        if (from < totalPdis) {
            // La página empieza en PDIs
            int toPdi = Math.min(to, totalPdis);
            paginaPdis = pdisFinales.subList(from, toPdi);

            int restantes = size - paginaPdis.size();
            if (restantes > 0 && totalHechos > 0) {
                int hechosFrom = Math.max(0, from - totalPdis);
                paginaHechos = hechos.subList(
                        hechosFrom,
                        Math.min(hechosFrom + restantes, totalHechos)
                );
            }
        } else {
            // La página empieza directamente en Hechos
            int hechosFrom = from - totalPdis;
            int hechosTo = Math.min(to - totalPdis, totalHechos);

            if (hechosFrom < totalHechos) {
                paginaHechos = hechos.subList(hechosFrom, hechosTo);
            }
        }

        // ===============================
        // 5️⃣ Response FINAL
        // ===============================

        return new BusquedaResponse(
                paginaHechos,
                paginaPdis,
                page,
                size,
                totalItems,
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
