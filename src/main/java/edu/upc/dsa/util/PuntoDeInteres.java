package edu.upc.dsa.util;

public class PuntoDeInteres {

    // Enum para los tipos de elementos
    public enum ElementType {
        DOOR, WALL, BRIDGE, POTION, SWORD, COIN, GRASS, TREE
    }

    // Atributos de la clase PuntoDeInteres
    private String nombre;
    private double coordenadaHorizontal;
    private double coordenadaVertical;
    private ElementType tipo;

    // Constructor
    public PuntoDeInteres(String nombre, double coordenadaHorizontal, double coordenadaVertical, ElementType tipo) {
        this.nombre = nombre;
        this.coordenadaHorizontal = coordenadaHorizontal;
        this.coordenadaVertical = coordenadaVertical;
        this.tipo = tipo;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCoordenadaHorizontal() {
        return coordenadaHorizontal;
    }

    public void setCoordenadaHorizontal(double coordenadaHorizontal) {
        this.coordenadaHorizontal = coordenadaHorizontal;
    }

    public double getCoordenadaVertical() {
        return coordenadaVertical;
    }

    public void setCoordenadaVertical(double coordenadaVertical) {
        this.coordenadaVertical = coordenadaVertical;
    }

    public ElementType getTipo() {
        return tipo;
    }

    public void setTipo(ElementType tipo) {
        this.tipo = tipo;
    }

    // Método para mostrar la información del PuntoDeInteres
    @Override
    public String toString() {
        return "PuntoDeInteres{" +
                "nombre='" + nombre + '\'' +
                ", coordenadaHorizontal=" + coordenadaHorizontal +
                ", coordenadaVertical=" + coordenadaVertical +
                ", tipo=" + tipo +
                '}';
    }
}