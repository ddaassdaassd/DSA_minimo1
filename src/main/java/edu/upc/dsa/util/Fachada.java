package edu.upc.dsa.util;

import edu.upc.dsa.exceptions.PuntoDeInteresNoExisteException;
import edu.upc.dsa.exceptions.UsuarioNoEncontradoException;
import edu.upc.dsa.exceptions.UsuarioNoExisteException;
import edu.upc.dsa.util.IFachada;
import edu.upc.dsa.util.PuntoDeInteres;
import edu.upc.dsa.util.Usuario;
import org.apache.log4j.Logger;

import java.util.*;

public class Fachada implements IFachada {

    private static final Logger logger = Logger.getLogger(Fachada.class);

    // Instancia única del Singleton
    private static Fachada instance;

    // Listas de usuarios y puntos de interés (pueden ser reemplazadas por una base de datos)
    private List<Usuario> usuarios = new ArrayList<>();
    private List<PuntoDeInteres> puntosDeInteres = new ArrayList<>();

    // Constructor privado para el Singleton
    private Fachada() {}

    // Método para obtener la instancia única (Singleton)
    public static Fachada getInstance() {
        if (instance == null) {
            instance = new Fachada();
        }
        return instance;
    }

    @Override
    public void registrarPuntoDeInteres(PuntoDeInteres punto) {
        logger.info("Iniciando registro del punto de interés: " + punto.getNombre());
        try {
            puntosDeInteres.add(punto);  // Registrar el punto de interés en la lista
            logger.info("Punto de interés registrado: " + punto.getNombre());
        } catch (Exception e) {
            logger.error("Error al registrar el punto de interés: " + punto.getNombre(), e);
        }
    }

    @Override
    public void registrarUsuario(Usuario usuario) {
        // Implementación del método
        logger.info("Iniciando registro de usuario: " + usuario.getNombre());
        try {
            usuarios.add(usuario);  // Registrar el usuario en la lista
            logger.info("Usuario registrado: " + usuario.getNombre());
        } catch (Exception e) {
            logger.error("Error al registrar el usuario: " + usuario.getNombre(), e);
        }
    }

    @Override
    public List<Usuario> listarUsuariosAlfabeticamente(List<Usuario> usuarios) {
        logger.info("Ordenando usuarios alfabéticamente.");
        List<Usuario> usuariosOrdenados = new ArrayList<>(usuarios);
        usuariosOrdenados.sort(Comparator.comparing(Usuario::getApellidos).thenComparing(Usuario::getNombre));
        return usuariosOrdenados;
    }

    @Override
    public Usuario consultarUsuarioPorId(List<Usuario> usuarios, int id) throws UsuarioNoEncontradoException {
        logger.info("Consultando usuario por ID: " + id);
        for (Usuario usuario : usuarios) {
            if (usuario.getIdentificador() == id) {
                return usuario;  // Si lo encontramos, lo devolvemos
            }
        }
        logger.error("El usuario con ID " + id + " no existe.");
        throw new UsuarioNoEncontradoException("El usuario con ID " + id + " no existe.");  // Lanzar la excepción
    }

    @Override
    public List<Usuario> getUsuarios() {
        return this.usuarios;
    }

    @Override
    public void registrarPasoPorPuntoDeInteres(int userId, double coordenadaHorizontal, double coordenadaVertical)
            throws UsuarioNoExisteException, PuntoDeInteresNoExisteException {
        logger.info("Registrando paso por el punto de interés: Usuario ID = " + userId
                + ", Coordenadas = (" + coordenadaHorizontal + ", " + coordenadaVertical + ")");

        Usuario usuario = buscarUsuarioPorId(userId);
        if (usuario == null) {
            logger.error("El usuario con ID " + userId + " no existe.");
            throw new UsuarioNoExisteException("El usuario con ID " + userId + " no existe.");
        }

        PuntoDeInteres punto = buscarPuntoPorCoordenadas(coordenadaHorizontal, coordenadaVertical);
        if (punto == null) {
            logger.error("No se encontró un punto de interés en las coordenadas (" + coordenadaHorizontal + ", " + coordenadaVertical + ")");
            throw new PuntoDeInteresNoExisteException("No existe un punto de interés en las coordenadas ("
                    + coordenadaHorizontal + ", " + coordenadaVertical + ").");
        }

        usuario.agregarPuntoVisitado(punto);
        logger.info("El usuario " + usuario.getNombre() + " ha pasado por el punto de interés " + punto.getNombre());
    }

    @Override
    public List<String> consultarPuntosVisitados(int userId) {
        logger.info("Consultando puntos visitados por el usuario con ID: " + userId);
        List<String> puntosVisitadosInfo = new ArrayList<>();
        try {
            Usuario usuario = buscarUsuarioPorId(userId);

            if (usuario == null) {
                puntosVisitadosInfo.add("Error: Usuario con ID " + userId + " no encontrado.");
                return puntosVisitadosInfo;
            }

            List<PuntoDeInteres> puntosVisitados = usuario.consultarPuntosVisitados();
            if (puntosVisitados.isEmpty()) {
                puntosVisitadosInfo.add("El usuario " + usuario.getNombre() + " no ha visitado ningún punto de interés.");
            } else {
                puntosVisitadosInfo.add("Puntos de interés visitados por " + usuario.getNombre() + ":");
                for (PuntoDeInteres punto : puntosVisitados) {
                    puntosVisitadosInfo.add("- " + punto.getNombre() + " (Coordenadas: "
                            + punto.getCoordenadaHorizontal() + ", "
                            + punto.getCoordenadaVertical() + ")");
                }
            }
        } catch (Exception e) {
            logger.error("Error al consultar los puntos visitados por el usuario con ID " + userId, e);
        }
        return puntosVisitadosInfo;
    }

    @Override
    public List<String> consultarUsuariosPorPuntoDeInteres(double coordenadaHorizontal, double coordenadaVertical) {
        logger.info("Consultando usuarios por el punto de interés en coordenadas: (" + coordenadaHorizontal + ", " + coordenadaVertical + ")");
        List<String> usuariosPorPunto = new ArrayList<>();
        try {
            PuntoDeInteres punto = buscarPuntoPorCoordenadas(coordenadaHorizontal, coordenadaVertical);
            if (punto == null) {
                usuariosPorPunto.add("Error: No existe un punto de interés en las coordenadas (" + coordenadaHorizontal + ", " + coordenadaVertical + ").");
                return usuariosPorPunto;
            }

            for (Usuario usuario : usuarios) {
                for (PuntoDeInteres puntoVisitado : usuario.consultarPuntosVisitados()) {
                    if (puntoVisitado.getCoordenadaHorizontal() == coordenadaHorizontal
                            && puntoVisitado.getCoordenadaVertical() == coordenadaVertical) {
                        usuariosPorPunto.add("Usuario: " + usuario.getNombre() + " " + usuario.getApellidos());
                        break;
                    }
                }
            }

            if (usuariosPorPunto.isEmpty()) {
                usuariosPorPunto.add("No hay usuarios que hayan pasado por este punto.");
            }
        } catch (Exception e) {
            logger.error("Error al consultar los usuarios por el punto de interés.", e);
        }
        return usuariosPorPunto;
    }

    @Override
    public List<String> consultarPuntosPorTipo(PuntoDeInteres.ElementType tipo) {
        logger.info("Consultando puntos de interés de tipo: " + tipo);
        List<String> puntosFiltrados = new ArrayList<>();
        try {
            for (PuntoDeInteres punto : puntosDeInteres) {
                if (punto.getTipo() == tipo) {
                    puntosFiltrados.add("Punto: " + punto.getNombre() + " (Coordenadas: "
                            + punto.getCoordenadaHorizontal() + ", "
                            + punto.getCoordenadaVertical() + ")");
                }
            }

            if (puntosFiltrados.isEmpty()) {
                puntosFiltrados.add("No existen puntos de interés de tipo " + tipo);
            }
        } catch (Exception e) {
            logger.error("Error al consultar los puntos de interés por tipo.", e);
        }
        return puntosFiltrados;
    }

    // Métodos privados para la búsqueda de usuario y punto de interés
    private Usuario buscarUsuarioPorId(int id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getIdentificador() == id) {
                return usuario;
            }
        }
        return null;
    }

    private PuntoDeInteres buscarPuntoPorCoordenadas(double coordenadaHorizontal, double coordenadaVertical) {
        for (PuntoDeInteres punto : puntosDeInteres) {
            if (punto.getCoordenadaHorizontal() == coordenadaHorizontal && punto.getCoordenadaVertical() == coordenadaVertical) {
                return punto;
            }
        }
        return null;
    }
}