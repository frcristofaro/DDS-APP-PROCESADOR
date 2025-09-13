package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.app.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Getter
@Setter
@Component
public class ConexionHTTP {

    private String url;
    private RestTemplate restTemplate;

    public ConexionHTTP() {
        this.url = "https://dds-app-solicitud.onrender.com";
        this.restTemplate = new RestTemplate();
    }

    public Optional<SolicitudDTO[]> obtenerSolicitudesID(String id) {
        String fullUrl = this.url + "/api/solicitudes?hecho=" + id;
        try {
            ResponseEntity<SolicitudDTO[]> response = restTemplate.getForEntity(fullUrl, SolicitudDTO[].class);
            SolicitudDTO[] solicitudes = response.getBody();
            return Optional.ofNullable(solicitudes);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException("Error al consultar el hecho: " + e.getStatusCode(), e);
        }
    }


}