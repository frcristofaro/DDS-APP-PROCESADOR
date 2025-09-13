package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;   //REVISAR
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;     //REVISAR
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;            //REVISAR
import ar.edu.utn.dds.k3003.model.Coleccion;
import ar.edu.utn.dds.k3003.model.Pdi;
import ar.edu.utn.dds.k3003.service.PdIService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class Fachada implements FachadaProcesadorPdI {

    private final PdIService pdiService;
    private final List<Coleccion> colecciones = new ArrayList<>();


    public Fachada(PdIService pdiService){
        this.pdiService = pdiService;
    }


    @Override
    public PdIDTO procesar(PdIDTO pdi) {

        Pdi procesado = pdiService.procesarPdI(pdi);

        return toDTO(procesado);
    }


    @Override
    public PdIDTO buscarPdIPorId(String pdiId) throws NoSuchElementException {
        Pdi pdi = pdiService.buscar(pdiId)
                .orElseThrow(() -> new NoSuchElementException("PdI no encontrada"));
        return toDTO(pdi);
    }

    @Override
    public List<PdIDTO> buscarPorHecho(String hechoId){
        List<Pdi> lista = pdiService.listarPorHecho(hechoId);
        return lista.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void setFachadaSolicitudes(FachadaSolicitudes fachadaSolicitudes) {

    }

    private PdIDTO toDTO(Pdi pdi) {
        return new PdIDTO(
                String.valueOf(pdi.getId()),
                pdi.getHechoId(),
                pdi.getDescripcion(),
                pdi.getLugar(),
                pdi.getMomento(),
                pdi.getContenido(),
                pdi.getEtiquetas() != null ? pdi.getEtiquetas() : Collections.emptyList()
        );
    }

    public Coleccion crearColeccion(Coleccion coleccion) {
        colecciones.add(coleccion);
        return coleccion;
    }

    public List<Coleccion> obtenerColecciones() {
        return colecciones;
    }

    public void eliminarPdIPorId(String pdiId) throws NoSuchElementException {
        boolean eliminado = pdiService.eliminarPorId(pdiId);
        if (!eliminado) {
            throw new NoSuchElementException("No existe un PdI con id " + pdiId);
        }
    }


//    public List<PdIDTO> listarTodosLosPdi() {
//        return this.repoPdi.findAll()
//                .stream()
//                .map(this::toDTO)
//                .collect(Collectors.toList());
//    }

}
