package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
import ar.edu.utn.dds.k3003.app.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.model.Pdi;
import ar.edu.utn.dds.k3003.service.PdIService;
import ar.edu.utn.dds.k3003.service.SolicitudesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FachadaProcesadorPdI {

    private final PdIService pdiService;
    private final SolicitudesService solicitudesService;

    @Autowired
    private final ConexionHTTP conexionHTTP;


    public FachadaProcesadorPdI(PdIService pdiService, SolicitudesService solicitudesService, ConexionHTTP conexionHTTP){
        this.pdiService = pdiService;
        this.solicitudesService = solicitudesService;
        this.conexionHTTP = conexionHTTP;
    }



    public PdIDTO procesar(PdIDTO pdi) {

        if (estaActivoHecho(pdi.hechoId())) {
            throw new IllegalStateException(
                    "El hecho con ID " + pdi.hechoId() + " está inactivo o no tiene solicitudes activas"
            );
        }

        Pdi procesado = pdiService.procesarPdI(pdi);

        return toDTO(procesado);
    }



    public PdIDTO buscarPdIPorId(String pdiId) throws NoSuchElementException {
        Pdi pdi = pdiService.buscar(pdiId)
                .orElseThrow(() -> new NoSuchElementException("PdI no encontrada"));
        return toDTO(pdi);
    }


    public List<PdIDTO> buscarPorHecho(String hechoId){
        List<Pdi> lista = pdiService.listarPorHecho(hechoId);
        return lista.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void setFachadaSolicitudes(FachadaSolicitudes fachadaSolicitudes) {}

    private PdIDTO toDTO(Pdi pdi) {
        return new PdIDTO(
                String.valueOf(pdi.getId()),
                pdi.getHechoId(),
                pdi.getDescripcion(),
                pdi.getLugar(),
                pdi.getMomento(),
                pdi.getContenido(),
                pdi.getUrlImagen(),
                pdi.getOcrResultado(),
                pdi.getEtiquetas() != null ? pdi.getEtiquetas() : Collections.emptyList()
        );
    }

    public void eliminarPdIPorId(String pdiId) throws NoSuchElementException {
        boolean eliminado = pdiService.eliminarPorId(pdiId);
        if (!eliminado) {
            throw new NoSuchElementException("No existe un PdI con id " + pdiId);
        }
    }

    public boolean estaActivoHecho(String hechoId) {
        return solicitudesService.validarEstado(hechoId);
    }


}
