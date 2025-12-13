package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.HechoBusquedaDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface HechoBusquedaRepository extends MongoRepository<HechoBusquedaDocument, String> {

    @Query("{ $or: [ { 'titulo': { $regex: ?0, $options: 'i' } } ] }")
    List<HechoBusquedaDocument> buscarHechoPorTexto(String texto);

}
