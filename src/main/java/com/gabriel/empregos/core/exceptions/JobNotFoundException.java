package com.gabriel.empregos.core.exceptions;

public class JobNotFoundException extends ModelNotFoundException {

    public JobNotFoundException(String message) {
        super(message);
    }


    public JobNotFoundException(Long id) {
        super("Job not found with id: " + id);
    }

}
