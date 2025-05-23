package org.psionicgeek.linkedin.postservice.exception;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RuntimeConflictException extends RuntimeException {

    public RuntimeConflictException(String message) {
        super(message);
    }
}
