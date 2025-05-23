package org.psionicgeek.linkedin.connection_service.service;


import lombok.RequiredArgsConstructor;
import org.psionicgeek.linkedin.connection_service.entity.Person;
import org.psionicgeek.linkedin.connection_service.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ConnectionService {

    private final PersonRepository connectionRepository;

    public List<Person> getFirstDegreeConnections(Long userId) {
        return connectionRepository.getFirstDegreeConnections(userId);
    }
}
