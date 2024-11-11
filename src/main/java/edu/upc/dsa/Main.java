package edu.upc.dsa;

import edu.upc.dsa.exceptions.PuntoDeInteresNoExisteException;
import edu.upc.dsa.exceptions.UsuarioNoEncontradoException;
import edu.upc.dsa.exceptions.UsuarioNoExisteException;
import edu.upc.dsa.util.PuntoDeInteres;
import edu.upc.dsa.util.Usuario;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jersey.listing.ApiListingResourceJSON;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import edu.upc.dsa.util.Fachada;  // Importa la fachada

/**
 * Main class.
 *
 */
public class Main {


    /*** Main method.
     * @param args
     * @throws IOException */
    public static void main(String[] args) throws IOException {

        // Obtener la instancia única de la fachada
        Fachada fachada = Fachada.getInstance();

        // Crear y agregar usuarios a la fachada
        fachada.registrarUsuario(new Usuario(1, "Juan", "Pérez", "juan.perez@example.com", new Date(90, 4, 23)));
        fachada.registrarUsuario(new Usuario(2, "Ana", "Gómez", "ana.gomez@example.com", new Date(85, 6, 15)));
        fachada.registrarUsuario(new Usuario(3, "Luis", "Martínez", "luis.martinez@example.com", new Date(92, 10, 2)));

        // Ejemplo de creación de puntos de interés
        fachada.registrarPuntoDeInteres(new PuntoDeInteres("Puerta Norte", 10.5, 20.3, PuntoDeInteres.ElementType.DOOR));
        fachada.registrarPuntoDeInteres(new PuntoDeInteres("Arbol Viejo", 15.2, 30.1, PuntoDeInteres.ElementType.TREE));

        // Obtener la lista de usuarios ordenados alfabéticamente
        List<Usuario> usuariosOrdenados = fachada.listarUsuariosAlfabeticamente(fachada.getUsuarios());

        // Consultar usuario por identificador
        try {
            Usuario usuario = fachada.consultarUsuarioPorId(fachada.getUsuarios(), 2);
        } catch (UsuarioNoEncontradoException e) {
            System.err.println(e.getMessage());
        }

        // Registrar paso por un punto de interes
        try {
            fachada.registrarPasoPorPuntoDeInteres(1, 10.5, 20.3); // Ejemplo exitoso
            fachada.registrarPasoPorPuntoDeInteres(2, 15.2, 30.1); // Usuario no existe
        } catch (UsuarioNoExisteException | PuntoDeInteresNoExisteException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Consultar los puntos visitados por el usuario con ID 1 y obtener el resultado como lista
        List<String> resultado = fachada.consultarPuntosVisitados(1);
        resultado.forEach(System.out::println);

        // Consultar los usuarios que han pasado por el punto de interés en las coordenadas dadas
        List<String> usuariosPorPunto = fachada.consultarUsuariosPorPuntoDeInteres(10.5, 20.3);
        usuariosPorPunto.forEach(System.out::println);

        // Consultar puntos de interés de tipo "TREE"
        List<String> puntosDeTipo = fachada.consultarPuntosPorTipo(PuntoDeInteres.ElementType.TREE);
        puntosDeTipo.forEach(System.out::println);
    }
}





