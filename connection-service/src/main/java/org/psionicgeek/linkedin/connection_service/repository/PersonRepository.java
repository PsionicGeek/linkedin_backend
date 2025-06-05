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



    @Query("OPTIONAL MATCH (sender:Person)-[r:REQUESTED_TO]->(receiver:Person) " +
           "WHERE sender.userId = $senderId AND receiver.userId = $receiverId " +
           "RETURN COUNT(r) > 0")
    boolean connectionRequestExists(Long senderId, Long receiverId);

    @Query("OPTIONAL MATCH (sender:Person)-[r:CONNECTED_TO]-(receiver:Person) " +
           "WHERE sender.userId = $senderId AND receiver.userId = $receiverId " +
           "RETURN COUNT(r) > 0")
    boolean alreadyConnected(Long senderId, Long receiverId);

    @Query("MATCH (sender:Person), (receiver:Person) " +
           "WHERE sender.userId = $senderId AND receiver.userId = $receiverId " +
           "CREATE (sender)-[:REQUESTED_TO]->(receiver)")
    void addConnectionRequest(Long senderId, Long receiverId);


    @Query("MATCH (sender:Person)-[r:REQUESTED_TO]->(receiver:Person) " +
           "WHERE sender.userId = $senderId AND receiver.userId = $receiverId " +
           "DELETE r")
    void deleteConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (sender:Person)-[r:REQUESTED_TO]->(receiver:Person) " +
            "WHERE sender.userId = $senderId AND receiver.userId = $receiverId " +
            "DELETE r "+
            "CREATE (sender)-[:CONNECTED_TO]->(receiver)"
    )
    void addConnection(Long senderId, Long receiverId);
}
