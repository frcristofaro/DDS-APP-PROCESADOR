//package ar.edu.utn.dds.k3003.app;
//
//import ar.edu.utn.dds.k3003.facades.FachadaFuente;
//import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
//import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
//import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//
//@Service
//public class FachadaSolicitudesRemote {
//
//    private final ConexionHTTP conexionHTTP;
//
//    public FachadaSolicitudesRemote() {
//        this.conexionHTTP = new ConexionHTTP();
//    }
//
//    @Override
//    public SolicitudDTO agregar(SolicitudDTO solicitudDTO) {
//        // Podrías implementar un POST usando RestTemplate
//        throw new UnsupportedOperationException("Método no implementado aún");
//    }
//
//    @Override
//    public SolicitudDTO modificar(String solicitudId, Object estado, String descripcion) {
//        throw new UnsupportedOperationException("Método no implementado aún");
//    }
//
//    @Override
//    public List<SolicitudDTO> buscarSolicitudXHecho(String hechoId) {
//        throw new UnsupportedOperationException("Método no implementado aún");
//    }
//
//    @Override
//    public SolicitudDTO buscarSolicitudXId(String solicitudId) {
//        throw new UnsupportedOperationException("Método no implementado aún");
//    }
//
//    @Override
//    public boolean estaActivo(String unHechoId) {
//        return conexionHTTP.obtenerHechoID(unHechoId).isPresent();
//    }
//
//    @Override
//    public void setFachadaFuente(Object fuente) {
//        // No aplica en la versión remota
//    }
//
//
//}
