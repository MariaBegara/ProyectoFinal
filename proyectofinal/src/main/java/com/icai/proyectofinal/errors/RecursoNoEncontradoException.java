package com.icai.proyectofinal.errors;

public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String usuarioNoEncontrado) {

        super(usuarioNoEncontrado);
    }
}
