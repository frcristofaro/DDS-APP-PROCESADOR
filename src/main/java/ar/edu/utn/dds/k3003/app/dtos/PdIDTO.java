package ar.edu.utn.dds.k3003.app.dtos;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public record PdIDTO(
        String id,
        String hechoId,
        String descripcion,
        String lugar,
        String momento,
        String contenido,
        List<String> etiquetas
        ) {

    public PdIDTO(String id,String hechoId) {
        this(id,hechoId, null, null, null, null, Collections.emptyList());
    }
}
