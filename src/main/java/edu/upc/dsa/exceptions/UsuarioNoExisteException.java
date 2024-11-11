package edu.upc.dsa.exceptions;

public class UsuarioNoExisteException extends Exception {
    public UsuarioNoExisteException(String mensaje) {
        super(mensaje);
    }
}