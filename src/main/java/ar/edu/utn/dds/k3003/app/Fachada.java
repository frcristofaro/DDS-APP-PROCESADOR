package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;   //REVISAR
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;     //REVISAR
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;            //REVISAR
import ar.edu.utn.dds.k3003.app.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.model.Coleccion;
import ar.edu.utn.dds.k3003.model.Pdi;
import ar.edu.utn.dds.k3003.service.PdIService;
import ar.edu.utn.dds.k3003.service.SolicitudesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Fachada implements FachadaProcesadorPdI {

    private final PdIService pdiService;
    private final SolicitudesService solicitudesService;

    @Autowired
    private final ConexionHTTP conexionHTTP;

    private final List<Coleccion> colecciones = new ArrayList<>();


    public Fachada(PdIService pdiService, SolicitudesService solicitudesService, ConexionHTTP conexionHTTP){
        this.pdiService = pdiService;
        this.solicitudesService = solicitudesService;
        this.conexionHTTP = conexionHTTP;
    }


    @Override
    public PdIDTO procesar(PdIDTO pdi) {

//        if (estaActivoHecho(pdi.hechoId())) {
//            throw new IllegalStateException(
//                    "El hecho con ID " + pdi.hechoId() + " está inactivo o no tiene solicitudes activas"
//            );
//        }

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

//    public PdIDTO procesarHechoRemoto(String hechoId) {
//        Optional<HechoDTO> hechoOpt = conexionHTTP.obtenerHechoID(hechoId);
//
//        if (hechoOpt.isEmpty()) {
//            throw new NoSuchElementException("El hecho remoto no existe: " + hechoId);
//        }
//
//        HechoDTO hecho = hechoOpt.get();
//
//        // Convertimos HechoDTO a PdIDTO para procesar
//        PdIDTO pdi = new PdIDTO(
//                null,
//                hecho.id(),
//                hecho.titulo(), // descripción del hecho
//                hecho.ubicacion(),
//                hecho.fecha(),
//                null, // contenido opcional
//                hecho.etiquetas() != null ? hecho.etiquetas() : List.of()
//        );
//
//        return procesar(pdi); // llama al método procesar de esta fachada
//    }

//    public boolean estaActivoHecho(String hechoId) {
//        return solicitudesService.validarEstado(hechoId);
//    }

//    public boolean estaActivo(String hechoId) {
//        Optional<SolicitudDTO[]> solicitudesOpt = conexionHTTP.obtenerSolicitudesID(hechoId);
//        if (solicitudesOpt.isEmpty() || solicitudesOpt.get().length == 0) {
//            throw new NoSuchElementException("No hay solicitudes con Hecho ID: " + hechoId);
//        }
//        SolicitudDTO[] solicitudes = solicitudesOpt.get();
//        for (SolicitudDTO solicitud : solicitudes) {
//            if ("CREADA".equalsIgnoreCase(solicitud.estado())) {
//                return true; // al menos una activa
//            }
//        }
//
//        return false; // ninguna activa
//    }

}
