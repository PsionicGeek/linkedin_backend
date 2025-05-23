package org.psionicgeek.linkedin.connection_service.repository;

import org.psionicgeek.linkedin.connection_service.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Optional<Person> getByName(Long userId);

    @Query("MATCH (n:Person)-[r:CONNECTED_TO]-(m:Person) WHERE n.userId = $userId RETURN m")
    List<Person> getFirstDegreeConnections(Long userId);
}
