package ar.edu.utn.dds.k3003.app.dtos;

import ar.edu.utn.dds.k3003.model.HechoBusquedaDocument;
import ar.edu.utn.dds.k3003.model.PdiBusquedaDocument;

import java.util.List;

public class BusquedaResponse {
    private List<HechoBusquedaDocument> resHechos;
    private List<PdiBusquedaDocument> resPDI;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    // constructor
    public BusquedaResponse(List<HechoBusquedaDocument> resHechos, List<PdiBusquedaDocument> resPDI, int page, int size, long totalElements, int totalPages) {
        this.resHechos = resHechos;
        this.resPDI = resPDI;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<HechoBusquedaDocument> getResHechos() {
        return resHechos;
    }

    public void setResHechos(List<HechoBusquedaDocument> resHechos) {
        this.resHechos = resHechos;
    }

    public List<PdiBusquedaDocument> getResPDI() {
        return resPDI;
    }

    public void setResPDI(List<PdiBusquedaDocument> resPDI) {
        this.resPDI = resPDI;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}

