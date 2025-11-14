package ar.edu.utn.dds.k3003.app.dtos;

import ar.edu.utn.dds.k3003.model.PdiBusquedaDocument;

import java.util.List;

public class BusquedaResponse {
    private List<PdiBusquedaDocument> resultados;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    // constructor
    public BusquedaResponse(List<PdiBusquedaDocument> resultados, int page, int size, long totalElements, int totalPages) {
        this.resultados = resultados;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<PdiBusquedaDocument> getResultados() {
        return resultados;
    }

    public void setResultados(List<PdiBusquedaDocument> resultados) {
        this.resultados = resultados;
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

