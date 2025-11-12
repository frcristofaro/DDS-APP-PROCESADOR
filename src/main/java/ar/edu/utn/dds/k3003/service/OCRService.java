package ar.edu.utn.dds.k3003.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class OCRService implements OCRProcesador {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = "K86883722888957";

    public String procesarImagen(String urlImagen) {
        try {
            String endpoint = "https://api.ocr.space/parse/imageurl";

            // Detectar extensión
            String lowerUrl = urlImagen.toLowerCase();
            String fileType = null;
            if (lowerUrl.endsWith(".png")) {
                fileType = "PNG";
            } else if (lowerUrl.endsWith(".jpg") || lowerUrl.endsWith(".jpeg")) {
                fileType = "JPG";
            }

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpoint)
                    .queryParam("apikey", apiKey)
                    .queryParam("url", urlImagen);

            // Si no es PNG o JPG, pasar filetype manual
            if (fileType == null) {
                builder.queryParam("filetype", "PNG"); // asumimos PNG por defecto
            }

            ResponseEntity<Map> response = restTemplate.getForEntity(builder.toUriString(), Map.class);

            System.out.println("OCR API RESPONSE");
            System.out.println(response);

            if (response.getBody() != null) {
                System.out.println("OCR API BODY");
                System.out.println(response.getBody());
            } else {
                System.out.println("El body es NULL");
            }

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List parsedResults = (List) response.getBody().get("ParsedResults");
                if (parsedResults != null && !parsedResults.isEmpty()) {
                    Map firstResult = (Map) parsedResults.get(0);
                    return (String) firstResult.get("ParsedText");
                }
            }

        } catch (Exception e) {
            System.err.println("Error al procesar OCR: " + e.getMessage());
        }

        return "";
    }

}
