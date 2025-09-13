package ar.edu.utn.dds.k3003.app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SolicitudDTO(
        @JsonProperty("id") String id,
        @JsonProperty("descripcion") String descripcion,
        @JsonProperty("estado") String estado,
        @JsonProperty("hechoId") String hechoId  // Coincide con el JSON recibido
) {}
