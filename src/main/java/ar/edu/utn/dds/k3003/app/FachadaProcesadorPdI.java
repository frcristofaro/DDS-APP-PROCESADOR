package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.app.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.model.Pdi;
import ar.edu.utn.dds.k3003.service.PdIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FachadaProcesadorPdI {

    private final PdIService pdiService;

    @Autowired
    private final ConexionHTTP conexionHTTP;


    public FachadaProcesadorPdI(PdIService pdiService, ConexionHTTP conexionHTTP){ //SolicitudesService solicitudesService, ConexionHTTP conexionHTTP){
        this.pdiService = pdiService;
        this.conexionHTTP = conexionHTTP;
    }



    public PdIDTO procesar(PdIDTO pdi) {

        System.out.println("FACHADA: Voy a procesar pdi");

//        if (estaActivoHecho(pdi.hechoId())) {
//            throw new IllegalStateException(
//                    "El hecho con ID " + pdi.hechoId() + " está inactivo o no tiene solicitudes activas"
//            );
//        }

        System.out.println("FACHADA: Pdi no está Activo -> Lo Proceso");
        Pdi procesado = pdiService.procesarPdI(pdi);
        System.out.println(procesado.getId().toString());

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


//    public boolean estaActivoHecho(String hechoId) {
//        Optional<SolicitudDTO[]> solicitudesOpt = conexionHTTP.obtenerSolicitudesID(hechoId);
//        System.out.println("FACHADA: Estoy viendo si está ACTIVADA");
//        if (solicitudesOpt.isEmpty() || solicitudesOpt.get().length == 0) {
//            throw new NoSuchElementException("No hay solicitudes con Hecho ID: " + hechoId);
//        }
//
//        SolicitudDTO[] solicitudes = solicitudesOpt.get();
//
//        for (SolicitudDTO solicitud : solicitudes) {
//            if ("ACEPTADA".equalsIgnoreCase(solicitud.estado())) {
//                return false;
//            }
//        }
//
//        return true;
//    }


}
