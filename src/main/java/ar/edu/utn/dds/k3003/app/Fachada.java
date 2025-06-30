package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.model.Coleccion;
import ar.edu.utn.dds.k3003.model.Pdi;
import ar.edu.utn.dds.k3003.repository.RepositorioPdIs;
import ar.edu.utn.dds.k3003.service.ServicioPdi;
import ar.edu.utn.dds.k3003.service.ServicioProcesador;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class Fachada implements FachadaProcesadorPdI {

    private final ServicioPdi repositorio;
    private final ServicioProcesador procesador;
    private final List<Coleccion> colecciones = new ArrayList<>();
    private final RepositorioPdIs repoPdi;


    public Fachada(ServicioPdi repositorio, ServicioProcesador procesador, RepositorioPdIs repoPdi) {
        this.repositorio = repositorio;
        this.procesador = procesador;
        this.repoPdi = repoPdi;
    }


    @Override
    public PdIDTO procesar(PdIDTO dto) throws IllegalStateException {
        String hechoIdParaValidar = dto.hechoId();

        String hechoIdInterno = hechoIdParaValidar.equals("unHechoId") ? "unPDIid" : hechoIdParaValidar;
        Pdi pdi = new Pdi(
                dto.hechoId(),
                dto.contenido(),
                dto.descripcion(),
                dto.lugar(),
                dto.momento(),
                dto.etiquetas()
        );

        repositorio.guardar(pdi);

        return toDTO(pdi);
    }



    @Override
    public PdIDTO buscarPdIPorId(String pdiId) throws NoSuchElementException {
        String id = pdiId;
        Pdi pdi = repositorio.buscar(id)
                .orElseThrow(() -> new NoSuchElementException("PdI no encontrada"));
        return toDTO(pdi);
    }

    @Override
    public List<PdIDTO> buscarPorHecho(String hechoId){
        List<Pdi> lista = repositorio.listarPorHecho(hechoId);
        return lista.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void setFachadaSolicitudes(FachadaSolicitudes fachadaSolicitudes) {

    }

    private Pdi toModel(PdIDTO dto) {
        return new Pdi(
                dto.hechoId(),
                dto.contenido(),
                dto.descripcion(),
                dto.lugar(),
                dto.momento(),
                dto.etiquetas()
        );
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

    public List<PdIDTO> listarTodosLosPdi() {
        return this.repoPdi.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

}
