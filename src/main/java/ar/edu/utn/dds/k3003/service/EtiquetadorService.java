package ar.edu.utn.dds.k3003.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EtiquetadorService implements EtiquetadorImagenes {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = "mtgFwYM0Fh0hLxgNG8ozPTDoyrch975Q";

    public List<Map<String, Object>> etiquetarImagen(String imageUrl) {
        String fullUrl = "https://api.apilayer.com/image_labeling/url?url=" + imageUrl;

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", apiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(fullUrl, HttpMethod.GET, entity, List.class);

        return response.getBody() != null ? response.getBody() : List.of();
    }
}
