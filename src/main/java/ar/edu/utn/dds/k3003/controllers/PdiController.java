package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.app.ConexionHTTP;
import ar.edu.utn.dds.k3003.app.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.app.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.app.dtos.PdIDTO;
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

    @Autowired
    public PdiController(FachadaProcesadorPdI fachada, ConexionHTTP conexionHTTP) {
        this.fachada = fachada;
        this.conexionHTTP = conexionHTTP;
    }

    @PostMapping
    public ResponseEntity<PdIDTO> crearPdi(@RequestBody PdIDTO dto) {
        try {
            PdIDTO creado = fachada.procesar(dto);
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




}
