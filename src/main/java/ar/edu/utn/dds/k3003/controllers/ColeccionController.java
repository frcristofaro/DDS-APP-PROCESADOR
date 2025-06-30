package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.model.Coleccion;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/colecciones")
public class ColeccionController {

    private final Fachada fachada;

    public ColeccionController(Fachada fachada) {
        this.fachada = fachada;
    }

    @PostMapping
    public Coleccion create(@RequestBody Coleccion coleccion) {
        return fachada.crearColeccion(coleccion);
    }

    @GetMapping
    public List<Coleccion> getAll() {
        return fachada.obtenerColecciones();
    }

}
