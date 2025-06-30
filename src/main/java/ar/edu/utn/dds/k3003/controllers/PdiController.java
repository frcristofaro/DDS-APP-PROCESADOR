package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/pdis")
public class PdiController {

    private final FachadaProcesadorPdI fachada;

    @Autowired
    public PdiController(FachadaProcesadorPdI fachada) {
        this.fachada = fachada;
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
}
