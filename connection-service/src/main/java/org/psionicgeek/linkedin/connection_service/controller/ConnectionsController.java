package org.psionicgeek.linkedin.connection_service.controller;


import lombok.RequiredArgsConstructor;
import org.psionicgeek.linkedin.connection_service.entity.Person;
import org.psionicgeek.linkedin.connection_service.service.ConnectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionsController {
    private final ConnectionService connectionService;

    @GetMapping("/first-degree")
    public ResponseEntity<List<Person>> getFirstDegreeConnections() {
        List<Person> connections = connectionService.getFirstDegreeConnections();
        return new ResponseEntity<>(connections, HttpStatus.OK);
    }

    @PostMapping("/request/{userId}")
    public ResponseEntity<Boolean> sendConnectionRequest(@PathVariable Long userId) {

        return ResponseEntity.ok(connectionService.sendConnectionRequest(userId));
    }

    @PostMapping("/accept/{userId}")
    public ResponseEntity<Boolean> acceptConnectionRequest(@PathVariable Long userId) {

        return ResponseEntity.ok(connectionService.acceptConnectionRequest(userId));
    }

    @PostMapping("/reject/{userId}")
    public ResponseEntity<Boolean> rejectConnectionRequest(@PathVariable Long userId) {

        return ResponseEntity.ok(connectionService.rejectConnectionRequest(userId));
    }
}
