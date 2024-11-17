package edu.upc.dsa.util;

//import org.w3c.dom.stylesheets.LinkStyle;

import java.util.Date;
import java.util.List;

public class Usuario {
    private int identificador;
    private String nombre;
    private String apellidos;
    private String correoElectronico;
    private Date fechaNacimiento;

    private List<PuntoDeInteres> puntosVisitados;

    // Constructor
    public Usuario(int identificador, String nombre, String apellidos, String correoElectronico, Date fechaNacimiento) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correoElectronico = correoElectronico;
        this.fechaNacimiento = fechaNacimiento;
    }

    public void agregarPuntoVisitado(PuntoDeInteres punto) {
        puntosVisitados.add(punto);
    }

    // Método para consultar los puntos de interés visitados en orden
    public List<PuntoDeInteres> consultarPuntosVisitados() {
        return puntosVisitados;
    }

    // Getters y Setters
    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    // Método para mostrar la información del usuario
    @Override
    public String toString() {
        return "Usuario{" +
                "identificador=" + identificador +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                '}';
    }
}