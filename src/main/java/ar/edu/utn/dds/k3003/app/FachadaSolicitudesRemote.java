package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.NoSuchElementException;


public class FachadaSolicitudesRemote implements FachadaSolicitudes {

    private RestTemplate restTemplate;
    private String endpoint;

    public FachadaSolicitudesRemote(RestTemplate restTemplate, String endpoint) {
        this.restTemplate = restTemplate;
        this.endpoint = endpoint;
    }


    @Override
    public SolicitudDTO agregar(SolicitudDTO solicitudDTO) {
        return null;
    }

    @Override
    public SolicitudDTO modificar(String solicitudId, EstadoSolicitudBorradoEnum esta, String descripcion) throws NoSuchElementException {
        return null;
    }

    @Override
    public List<SolicitudDTO> buscarSolicitudXHecho(String hechoId) {
        return List.of();
    }

    @Override
    public SolicitudDTO buscarSolicitudXId(String solicitudId) {
        return null;
    }

    @Override
    public boolean estaActivo(String unHechoId) {
        return restTemplate.getForObject(endpoint + "/solicitudes/activo/" + unHechoId, Boolean.class);
    }

    @Override
    public void setFachadaFuente(FachadaFuente fuente) {

    }


}
