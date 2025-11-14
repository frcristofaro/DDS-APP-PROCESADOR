package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.PdiBusquedaDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PdiBusquedaRepository  extends MongoRepository<PdiBusquedaDocument, String> {

        // Búsqueda por texto y etiqueta
        @Query("{ $and: [ " +
                "{ $or: [ " +
                "  { 'descripcion': { $regex: ?0, $options: 'i' } }, " +
                "  { 'contenido': { $regex: ?0, $options: 'i' } }, " +
                "  { 'ocrResultado': { $regex: ?0, $options: 'i' } } " +
                "] }, " +
                "{ 'etiquetas': ?1 } " +
                "] }")
        Page<PdiBusquedaDocument> buscarPorTextoYTag(String texto, String tag, Pageable pageable);

        // Si no hay tag, buscamos solo por texto
        @Query("{ $or: [ " +
                "{ 'descripcion': { $regex: ?0, $options: 'i' } }, " +
                "{ 'contenido': { $regex: ?0, $options: 'i' } }, " +
                "{ 'ocrResultado': { $regex: ?0, $options: 'i' } } " +
                "] }")
        Page<PdiBusquedaDocument> buscarPorTexto(String texto, Pageable pageable);
    }
