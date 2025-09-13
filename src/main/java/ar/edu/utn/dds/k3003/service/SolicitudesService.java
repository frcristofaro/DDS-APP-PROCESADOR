package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.app.ConexionHTTP;
import ar.edu.utn.dds.k3003.app.dtos.SolicitudDTO;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SolicitudesService {

    private final ConexionHTTP conexionHTTP;

    public SolicitudesService(ConexionHTTP conexionHTTP) {
        this.conexionHTTP = conexionHTTP;
    }

    //Verifico qe todas las solicitudes de un hecho estén ACEPTADAS
    public boolean validarEstado(String hechoId) {
        Optional<SolicitudDTO[]> solicitudesOpt = conexionHTTP.obtenerSolicitudesID(hechoId);

        if (solicitudesOpt.isEmpty() || solicitudesOpt.get().length == 0) {
            throw new NoSuchElementException("No hay solicitudes con Hecho ID: " + hechoId);
        }

        SolicitudDTO[] solicitudes = solicitudesOpt.get();

        for (SolicitudDTO solicitud : solicitudes) {
            if (!"ACEPTADA".equalsIgnoreCase(solicitud.estado())) {
                return false;
            }
        }

        return true;
    }

}
