package ar.edu.utn.dds.k3003.service;

import java.util.List;
import java.util.Map;

public interface EtiquetadorImagenes {
    List<Map<String, Object>> etiquetarImagen(String imageUrl);
}
