package edu.upc.dsa.services;

import edu.upc.dsa.util.Fachada;
import edu.upc.dsa.util.PuntoDeInteres;
import edu.upc.dsa.util.Usuario;
import edu.upc.dsa.exceptions.UsuarioNoEncontradoException;
import edu.upc.dsa.exceptions.PuntoDeInteresNoExisteException;
import edu.upc.dsa.exceptions.UsuarioNoExisteException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/usuarios", description = "Endpoint to User List Service")
@Path("/usuarios")
public class MiService {

    private Fachada fachada;

    // Constructor
    public MiService() {
        this.fachada = Fachada.getInstance();
    }

    @POST
    @ApiOperation(value = "Registrar un nuevo usuario", notes = "Registra un nuevo usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuario creado exitosamente", response = Usuario.class),
            @ApiResponse(code = 400, message = "Error en los datos proporcionados")
    })
    @Path("/registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getNombre() == null || usuario.getCorreoElectronico() == null) {
            return Response.status(400).entity("Faltan datos obligatorios para registrar al usuario").build();
        }

        // Llamada al método de la fachada para registrar al usuario
        fachada.registrarUsuario(usuario);

        // Retornar respuesta con el usuario registrado
        return Response.status(201).entity(usuario).build();
    }

    @GET
    @ApiOperation(value = "Listar usuarios ordenados alfabéticamente", notes = "Devuelve la lista de usuarios ordenada por apellidos y nombre")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuarios listados exitosamente", response = Usuario.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error interno del servidor")
    })
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarUsuariosAlfabeticamente() {
        try {
            // Obtener la lista de usuarios ordenados alfabéticamente
            List<Usuario> usuariosOrdenados = fachada.listarUsuariosAlfabeticamente(fachada.getUsuarios());

            // Crear una entidad genérica para la lista de usuarios
            GenericEntity<List<Usuario>> entity = new GenericEntity<List<Usuario>>(usuariosOrdenados) {};

            // Retornar la respuesta con la lista de usuarios
            return Response.status(200).entity(entity).build();
        } catch (Exception e) {
            // Si hay algún error, devolver un código 500
            return Response.status(500).entity("Error al obtener la lista de usuarios").build();
        }
    }
    // Nueva petición: Consultar información de un usuario por su identificador
    @GET
    @ApiOperation(value = "Consultar información de un usuario por su identificador", notes = "Devuelve la información completa de un usuario usando su identificador")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario encontrado", response = Usuario.class),
            @ApiResponse(code = 404, message = "Usuario no encontrado")
    })
    @Path("/consultar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarUsuarioPorId(@PathParam("id") int id) {
        try {
            // Consultar el usuario usando el ID
            Usuario usuario = fachada.consultarUsuarioPorId(fachada.getUsuarios(), id);

            // Retornar respuesta con el usuario encontrado
            return Response.status(200).entity(usuario).build();
        } catch (UsuarioNoEncontradoException e) {
            // Si el usuario no se encuentra, retornar un error 404
            return Response.status(404).entity("Usuario con ID " + id + " no encontrado").build();
        }
    }
    // Nueva petición: Añadir un punto de interés
    @POST
    @ApiOperation(value = "Añadir un punto de interés en el mapa", notes = "Registra un nuevo punto de interés con coordenadas y tipo en el sistema")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Punto de interés registrado exitosamente", response = PuntoDeInteres.class),
            @ApiResponse(code = 400, message = "Error en los datos proporcionados")
    })
    @Path("/registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registrarPuntoDeInteres(PuntoDeInteres punto) {
        try {
            // Registrar el punto de interés en la fachada
            fachada.registrarPuntoDeInteres(punto);
            return Response.status(201).entity(punto).build();  // Responder con el punto creado
        } catch (Exception e) {
            return Response.status(400).entity("Error al registrar el punto de interés").build();  // En caso de error
        }
    }

    // Nueva petición: Registrar que un usuario pasa por un punto de interés
    @POST
    @ApiOperation(value = "Registrar que un usuario pasa por un punto de interés",
            notes = "Registra que un usuario ha pasado por un punto de interés con las coordenadas proporcionadas.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Paso registrado correctamente"),
            @ApiResponse(code = 400, message = "Error al registrar el paso (usuario o punto de interés no válido)"),
            @ApiResponse(code = 404, message = "Usuario o punto de interés no encontrados")
    })
    @Path("/registrarPaso")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registrarPasoPorPuntoDeInteres(@QueryParam("userId") int userId,
                                                   @QueryParam("coordenadaHorizontal") double coordenadaHorizontal,
                                                   @QueryParam("coordenadaVertical") double coordenadaVertical) {
        try {
            // Ejecutar la función registrarPasoPorPuntoDeInteres de la fachada
            fachada.registrarPasoPorPuntoDeInteres(userId, coordenadaHorizontal, coordenadaVertical);
            return Response.status(200).entity("Paso registrado correctamente").build();  // Responder con éxito
        } catch (UsuarioNoExisteException e) {
            return Response.status(404).entity("Error: El usuario con ID " + userId + " no existe.").build();  // Usuario no encontrado
        } catch (PuntoDeInteresNoExisteException e) {
            return Response.status(404).entity("Error: No existe un punto de interés en las coordenadas ("
                    + coordenadaHorizontal + ", " + coordenadaVertical + ").").build();  // Punto de interés no encontrado
        } catch (Exception e) {
            return Response.status(400).entity("Error al registrar el paso: " + e.getMessage()).build();  // Error genérico
        }
    }

    // Nueva petición: Consultar los puntos de interés por los que un usuario ha pasado
    @GET
    @ApiOperation(value = "Consultar los puntos de interés por los que un usuario ha pasado",
            notes = "Devuelve una lista de los puntos de interés que un usuario ha visitado, en el orden en que fueron registrados.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Puntos de interés encontrados"),
            @ApiResponse(code = 404, message = "Usuario no encontrado")
    })
    @Path("/consultarPuntosVisitados")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarPuntosVisitados(@QueryParam("userId") int userId) {
        try {
            // Consultar los puntos visitados por el usuario
            List<String> puntosVisitados = fachada.consultarPuntosVisitados(userId);

            // Si no hay puntos visitados
            if (puntosVisitados.isEmpty()) {
                return Response.status(200).entity("El usuario no ha visitado ningún punto de interés.").build();
            }

            // Retornar los puntos visitados en formato JSON
            return Response.status(200).entity(puntosVisitados).build();
        } catch (UsuarioNoExisteException e) {
            // En caso de que no se encuentre el usuario
            return Response.status(404).entity("Error: El usuario con ID " + userId + " no existe.").build();
        } catch (Exception e) {
            return Response.status(400).entity("Error al consultar los puntos visitados: " + e.getMessage()).build();
        }
    }
    // Petición para consultar los usuarios por un punto de interés
    @GET
    @Path("/usuarios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarUsuariosPorPuntoDeInteres(
            @QueryParam("coordenadaHorizontal") double coordenadaHorizontal,
            @QueryParam("coordenadaVertical") double coordenadaVertical) {

        try {
            // Llamamos a la función que devuelve la lista de usuarios que han pasado por el punto de interés
            List<String> usuarios = fachada.consultarUsuariosPorPuntoDeInteres(coordenadaHorizontal, coordenadaVertical);

            // Si no se encontró el punto de interés, la lista de usuarios será vacía
            if (usuarios.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No hay usuarios que hayan pasado por el punto de interés en las coordenadas proporcionadas.")
                        .build();
            }

            return Response.ok(usuarios).build();
        } catch (Exception e) {
            // Si hay un error, devolvemos un mensaje de error
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al consultar los usuarios por el punto de interés.")
                    .build();
        }
    }

    // Petición para consultar puntos de interés por tipo
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarPuntosPorTipo(@QueryParam("tipo") PuntoDeInteres.ElementType tipo) {
        try {
            // Llamamos a la función que devuelve la lista de puntos de interés de un tipo determinado
            List<String> puntos = fachada.consultarPuntosPorTipo(tipo);

            // Si la lista de puntos está vacía, indicamos que no se encontraron puntos de interés de ese tipo
            if (puntos.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No existen puntos de interés de tipo " + tipo)
                        .build();
            }

            // Si se encuentran puntos de interés, se retornan en formato JSON
            return Response.ok(puntos).build();
        } catch (Exception e) {
            // Si hay un error, devolvemos un mensaje de error
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al consultar los puntos de interés por tipo.")
                    .build();
        }
    }
}
