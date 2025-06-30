package ar.edu.utn.dds.k3003.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioProcesador {

    public List<String> etiquetar(String contenido) {

        List<String> etiquetas = new ArrayList<>();
        if (contenido.toLowerCase().contains("fuego")) etiquetas.add("incendio");
        if (contenido.toLowerCase().contains("basura")) etiquetas.add("contaminación");
        if (contenido.isEmpty()) etiquetas.add("general");
        return etiquetas;

    }

}
