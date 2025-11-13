package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.config.RabbitConfig;
import ar.edu.utn.dds.k3003.model.Pdi;
import ar.edu.utn.dds.k3003.repository.PdIRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PdiWorker {

    private final PdIRepository repo;
    private BusquedaService busquedaService;
    private final OCRService ocrService;
    private final EtiquetadorService etiquetadorService;

    @Autowired
    public PdiWorker(PdIRepository repo, BusquedaService busquedaService, OCRService ocrService, EtiquetadorService etiquetadorService) {
        this.repo = repo;
        this.busquedaService = busquedaService;
        this.ocrService = ocrService;
        this.etiquetadorService = etiquetadorService;
    }

    @RabbitListener(queues = RabbitConfig.COLA_PDI)
    public void procesarPdi(String pdiId) {
        System.out.println("Worker procesando PDI: " + pdiId);
        System.out.println("Procesando PDI en instancia: " + System.getProperty("server.port"));

        repo.findById(Long.parseLong(pdiId)).ifPresent(pdi -> {
            try {
                String ocrResultado = ocrService.procesarImagen(pdi.getUrlImagen());
                List<Map<String, Object>> etiquetasRaw = etiquetadorService.etiquetarImagen(pdi.getUrlImagen());

                List<String> etiquetas = etiquetasRaw.stream()
                        .map(e -> e.get("label").toString())
                        .toList();

                pdi.setOcrResultado(ocrResultado);
                pdi.setEtiquetas(etiquetas);
                pdi.setProcessDt(LocalDateTime.now());

                Pdi guardado = repo.save(pdi);
                busquedaService.indexar(guardado);
                System.out.println("PDI procesado exitosamente: " + pdi.getId());
            } catch (Exception e) {
                System.err.println("Error procesando PDI " + pdiId + ": " + e.getMessage());
            }
        });
    }
}