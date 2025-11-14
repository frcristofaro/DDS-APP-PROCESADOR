package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.app.ConexionHTTP;
import ar.edu.utn.dds.k3003.app.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.app.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.app.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.model.PdiBusquedaDocument;
import ar.edu.utn.dds.k3003.service.BusquedaService;
import ar.edu.utn.dds.k3003.service.OCRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/pdis")
public class PdiController {

    private final FachadaProcesadorPdI fachada;
    private final ConexionHTTP conexionHTTP;
    private final OCRService ocrService;

    @Autowired
    private BusquedaService busquedaService;

    @Autowired
    public PdiController(FachadaProcesadorPdI fachada, ConexionHTTP conexionHTTP, OCRService ocrService) {
        this.fachada = fachada;
        this.conexionHTTP = conexionHTTP;
        this.ocrService = ocrService;
    }

    @PostMapping
    public ResponseEntity<PdIDTO> crearPdi(@RequestBody PdIDTO dto) {
        try {
            System.out.println("CONTROLLER: Creando PDI");
            PdIDTO creado = fachada.procesar(dto);
            System.out.println("CONTROLLER: Listo");
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PdIDTO> obtenerPdiPorId(@PathVariable String id) {
        try {
            PdIDTO pdi = fachada.buscarPdIPorId(id);
            return new ResponseEntity<>(pdi, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<PdIDTO>> obtenerPdisPorHecho(
            @RequestParam(value = "hecho", required = false) String hechoId) {
        if (hechoId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<PdIDTO> lista = fachada.buscarPorHecho(hechoId);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHecho(@PathVariable String id) {
        fachada.eliminarPdIPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estado-solicitud/{hechoId}")
    public ResponseEntity<Boolean> procesarHechoRemoto(@PathVariable String hechoId) {
        Optional<SolicitudDTO[]> solicitudesOpt = conexionHTTP.obtenerSolicitudesID(hechoId);

        if (solicitudesOpt.isEmpty() || solicitudesOpt.get().length == 0) {
            throw new NoSuchElementException("No hay solicitudes con Hecho ID: " + hechoId);
        }

        SolicitudDTO[] solicitudes = solicitudesOpt.get();

        for (SolicitudDTO solicitud : solicitudes) {
            if ("ACEPTADA".equalsIgnoreCase(solicitud.estado())) {

                //Existen solicitudes inactivas
                return ResponseEntity.ok(false);
            }
        }

        //Todas las solicitudes están activas
        return ResponseEntity.ok(true);
    }


    @GetMapping("/test-ocr")
    public ResponseEntity<String> testOcr() {
        //String urlTest = "https://cdn.pixabay.com/photo/2015/03/24/23/58/nobody-is-perfect-688370_1280.jpg";
        String urlTest = "https://cdn.pixabay.com/photo/2016/01/12/22/37/accomplish-1136863_1280.jpg";
        String resultado = ocrService.procesarImagen(urlTest);
        System.out.println("Resultado OCR: " + resultado);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<PdiBusquedaDocument>> buscarPdis(
            @RequestParam String texto,
            @RequestParam(required = false) String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        List<PdiBusquedaDocument> resultados = busquedaService.buscar(texto, tag, page, size);
        return ResponseEntity.ok(resultados);
    }

}
