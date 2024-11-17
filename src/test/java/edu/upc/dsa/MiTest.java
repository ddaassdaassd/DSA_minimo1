package edu.upc.dsa;

import edu.upc.dsa.exceptions.PuntoDeInteresNoExisteException;
import edu.upc.dsa.exceptions.UsuarioNoEncontradoException;
import edu.upc.dsa.exceptions.UsuarioNoExisteException;
import edu.upc.dsa.util.Fachada;
import edu.upc.dsa.util.PuntoDeInteres;
import edu.upc.dsa.util.Usuario;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MiTest {
    Fachada fachada;

    @Before
    public void setUp() {
        this.fachada = Fachada.getInstance();
        this.fachada.registrarUsuario(new Usuario(1, "Juan", "Pérez", "juan.perez@example.com", new java.util.Date(90, 4, 23)));
        this.fachada.registrarUsuario(new Usuario(2, "Ana", "Gómez", "ana.gomez@example.com", new java.util.Date(85, 6, 15)));
        this.fachada.registrarPuntoDeInteres(new PuntoDeInteres("Puerta Norte", 10.5, 20.3, PuntoDeInteres.ElementType.DOOR));
        this.fachada.registrarPuntoDeInteres(new PuntoDeInteres("Arbol Viejo", 15.2, 30.1, PuntoDeInteres.ElementType.TREE));
    }

    @After
    public void tearDown() {
        // Limpiar los datos si es necesario
        this.fachada.getUsuarios().clear();
        this.fachada.getPuntosDeInteres().clear();
    }

    @Test
    public void testRegistrarUsuario() {
        Assert.assertEquals(2, fachada.getUsuarios().size());

        this.fachada.registrarUsuario(new Usuario(3, "Carlos", "Martínez", "carlos.martinez@example.com", new java.util.Date(80, 3, 12)));
        Assert.assertEquals(3, fachada.getUsuarios().size());
    }

    @Test
    public void testListarUsuariosAlfabeticamente() {
        // Crear una lista desordenada de usuarios
        List<Usuario> usuariosDesordenados = new ArrayList<>();
        usuariosDesordenados.add(new Usuario(3, "Luis", "Zapata", "luis.zapata@example.com", new java.util.Date(95, 1, 20)));
        usuariosDesordenados.add(new Usuario(2, "Ana", "Gómez", "ana.gomez@example.com", new java.util.Date(85, 6, 15)));
        usuariosDesordenados.add(new Usuario(1, "Juan", "Pérez", "juan.perez@example.com", new java.util.Date(90, 4, 23)));
        usuariosDesordenados.add(new Usuario(4, "Ana", "López", "ana.lopez@example.com", new java.util.Date(92, 2, 12)));

        // Llamar a la función para ordenar
        List<Usuario> usuariosOrdenados = fachada.listarUsuariosAlfabeticamente(usuariosDesordenados);

        // Verificar el orden esperado
        Assert.assertEquals(4, usuariosOrdenados.size());
        Assert.assertEquals("Gómez", usuariosOrdenados.get(0).getApellidos());
        Assert.assertEquals("Ana", usuariosOrdenados.get(0).getNombre());

        Assert.assertEquals("López", usuariosOrdenados.get(1).getApellidos());
        Assert.assertEquals("Ana", usuariosOrdenados.get(1).getNombre());

        Assert.assertEquals("Pérez", usuariosOrdenados.get(2).getApellidos());
        Assert.assertEquals("Juan", usuariosOrdenados.get(2).getNombre());

        Assert.assertEquals("Zapata", usuariosOrdenados.get(3).getApellidos());
        Assert.assertEquals("Luis", usuariosOrdenados.get(3).getNombre());
    }

    @Test
    public void testConsultarUsuarioPorId() throws UsuarioNoEncontradoException {
        Usuario usuario = fachada.consultarUsuarioPorId(fachada.getUsuarios(), 1);
        Assert.assertNotNull(usuario);
        Assert.assertEquals("Juan", usuario.getNombre());

        Assert.assertThrows(UsuarioNoEncontradoException.class, () ->
                fachada.consultarUsuarioPorId(fachada.getUsuarios(), 999));  // ID no existente
    }

    @Test
    public void testRegistrarPuntoDeInteres() {
        Assert.assertEquals(2, fachada.getPuntosDeInteres().size());
        this.fachada.registrarPuntoDeInteres(new PuntoDeInteres("Nuevo Punto", 25.4, 50.3, PuntoDeInteres.ElementType.TREE));
        Assert.assertEquals(3, fachada.getPuntosDeInteres().size());
    }

    @Test
    public void testRegistrarPasoPorPuntoDeInteresExitoso() throws UsuarioNoExisteException, PuntoDeInteresNoExisteException {
        // Verificar que inicialmente el usuario no haya visitado ningún punto
        Usuario usuario = fachada.getUsuarios().get(0); // Usuario con ID 1 (Juan Pérez)
        Assert.assertTrue(usuario.consultarPuntosVisitados().isEmpty());

        // Registrar el paso por un punto de interés
        fachada.registrarPasoPorPuntoDeInteres(1, 10.5, 20.3);

        // Verificar que el usuario haya visitado el punto de interés
        List<PuntoDeInteres> puntosVisitados = usuario.consultarPuntosVisitados();
        Assert.assertEquals(1, puntosVisitados.size());
        Assert.assertEquals("Puerta Norte", puntosVisitados.get(0).getNombre());
    }

    @Test
    public void testRegistrarPasoPorPuntoDeInteresUsuarioNoExiste() {
        // Intentar registrar el paso de un usuario inexistente
        Assert.assertThrows(UsuarioNoExisteException.class, () -> {
            fachada.registrarPasoPorPuntoDeInteres(999, 10.5, 20.3); // Usuario con ID 999 no existe
        });
    }

    @Test
    public void testRegistrarPasoPorPuntoDeInteresPuntoNoExiste() {
        // Intentar registrar el paso por un punto de interés inexistente
        Assert.assertThrows(PuntoDeInteresNoExisteException.class, () -> {
            fachada.registrarPasoPorPuntoDeInteres(1, 99.9, 99.9); // Coordenadas no existentes
        });
    }

    @Test
    public void testConsultarPuntosVisitadosExitoso() throws UsuarioNoExisteException, PuntoDeInteresNoExisteException {
        // Registrar que el usuario 1 ha visitado un punto de interés
        fachada.registrarPasoPorPuntoDeInteres(1, 10.5, 20.3); // Puerta Norte

        // Consultar los puntos visitados por el usuario
        List<String> puntosVisitados = fachada.consultarPuntosVisitados(1);

        // Verificar la información retornada
        Assert.assertFalse(puntosVisitados.isEmpty());
        Assert.assertEquals("Puntos de interés visitados por Juan:", puntosVisitados.get(0));
        Assert.assertTrue(puntosVisitados.contains("- Puerta Norte (Coordenadas: 10.5, 20.3)"));
    }

    @Test
    public void testConsultarPuntosVisitadosUsuarioNoHaVisitado() throws UsuarioNoExisteException {
        // Consultar los puntos visitados por un usuario que no ha visitado ningún punto
        List<String> puntosVisitados = fachada.consultarPuntosVisitados(2); // Ana no ha visitado puntos

        // Verificar la información retornada
        Assert.assertFalse(puntosVisitados.isEmpty());
        Assert.assertEquals("El usuario Ana no ha visitado ningún punto de interés.", puntosVisitados.get(0));
    }

    @Test
    public void testConsultarPuntosVisitadosUsuarioNoExiste() throws UsuarioNoExisteException {
        // Consultar los puntos visitados por un usuario inexistente
        List<String> puntosVisitados = fachada.consultarPuntosVisitados(999); // Usuario no registrado

        // Verificar la información retornada
        Assert.assertFalse(puntosVisitados.isEmpty());
        Assert.assertEquals("Error: Usuario con ID 999 no encontrado.", puntosVisitados.get(0));
    }

    @Test
    public void testConsultarUsuariosPorPuntoDeInteresExitoso() throws UsuarioNoExisteException, PuntoDeInteresNoExisteException {
        // Registrar que el usuario 1 ha pasado por el punto
        fachada.registrarPasoPorPuntoDeInteres(1, 10.5, 20.3); // Puerta Norte

        // Consultar usuarios que pasaron por el punto
        List<String> usuariosPorPunto = fachada.consultarUsuariosPorPuntoDeInteres(10.5, 20.3);

        // Verificar los resultados
        Assert.assertFalse(usuariosPorPunto.isEmpty());
        Assert.assertEquals("Usuario: Juan Pérez", usuariosPorPunto.get(0));
    }

    @Test
    public void testConsultarUsuariosPorPuntoDeInteresSinUsuarios() {
        // Consultar un punto donde no ha pasado ningún usuario
        List<String> usuariosPorPunto = fachada.consultarUsuariosPorPuntoDeInteres(15.2, 30.1); // Arbol Viejo

        // Verificar el mensaje retornado
        Assert.assertFalse(usuariosPorPunto.isEmpty());
        Assert.assertEquals("No hay usuarios que hayan pasado por este punto.", usuariosPorPunto.get(0));
    }

    @Test
    public void testConsultarUsuariosPorPuntoDeInteresNoExiste() {
        // Consultar un punto que no existe
        List<String> usuariosPorPunto = fachada.consultarUsuariosPorPuntoDeInteres(99.9, 88.8);

        // Verificar el mensaje retornado
        Assert.assertFalse(usuariosPorPunto.isEmpty());
        Assert.assertEquals("Error: No existe un punto de interés en las coordenadas (99.9, 88.8).", usuariosPorPunto.get(0));
    }
    @Test
    public void testConsultarPuntosPorTipoConPuntosExistentes() {
        // Registrar puntos de interés de diferentes tipos
        fachada.registrarPuntoDeInteres(new PuntoDeInteres("Puerta Norte", 10.5, 20.3, PuntoDeInteres.ElementType.DOOR));
        fachada.registrarPuntoDeInteres(new PuntoDeInteres("Arbol Viejo", 15.2, 30.1, PuntoDeInteres.ElementType.TREE));

        // Consultar puntos de tipo DOOR
        List<String> puntosPorTipo = fachada.consultarPuntosPorTipo(PuntoDeInteres.ElementType.DOOR);

        // Verificar que el punto Puerta Norte esté presente
        Assert.assertFalse(puntosPorTipo.isEmpty());
        Assert.assertTrue(puntosPorTipo.contains("Punto: Puerta Norte (Coordenadas: 10.5, 20.3)"));
    }

    @Test
    public void testConsultarPuntosPorTipoConVariosPuntos() {
        // Registrar varios puntos de interés con diferentes tipos
        fachada.registrarPuntoDeInteres(new PuntoDeInteres("Puerta Norte", 10.5, 20.3, PuntoDeInteres.ElementType.DOOR));
        fachada.registrarPuntoDeInteres(new PuntoDeInteres("Arbol Viejo", 15.2, 30.1, PuntoDeInteres.ElementType.TREE));
        fachada.registrarPuntoDeInteres(new PuntoDeInteres("Puerta Sur", 25.5, 40.3, PuntoDeInteres.ElementType.DOOR));

        // Consultar puntos de tipo DOOR
        List<String> puntosPorTipo = fachada.consultarPuntosPorTipo(PuntoDeInteres.ElementType.DOOR);

        // Verificar que los puntos Puerta Norte y Puerta Sur estén presentes
        Assert.assertFalse(puntosPorTipo.isEmpty());
        Assert.assertTrue(puntosPorTipo.contains("Punto: Puerta Norte (Coordenadas: 10.5, 20.3)"));
        Assert.assertTrue(puntosPorTipo.contains("Punto: Puerta Sur (Coordenadas: 25.5, 40.3)"));
    }

    @Test
    public void testConsultarPuntosPorTipoTipoNoRegistrado() {
        // Consultar puntos de tipo TREE sin que haya puntos registrados de ese tipo
        List<String> puntosPorTipo = fachada.consultarPuntosPorTipo(PuntoDeInteres.ElementType.TREE);

        // Verificar que el mensaje indique que no hay puntos de ese tipo
        Assert.assertTrue(puntosPorTipo.isEmpty());
    }



}