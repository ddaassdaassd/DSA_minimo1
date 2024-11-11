package edu.upc.dsa;

import edu.upc.dsa.exceptions.PuntoDeInteresNoExisteException;
import edu.upc.dsa.exceptions.UsuarioNoEncontradoException;
import edu.upc.dsa.util.Fachada;
import edu.upc.dsa.util.PuntoDeInteres;
import edu.upc.dsa.util.Usuario;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class FachadaTest {
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
    public void testConsultarUsuarioPorId() throws UsuarioNoEncontradoException {
        Usuario usuario = fachada.consultarUsuarioPorId(fachada.getUsuarios(), 1);
        Assert.assertNotNull(usuario);
        Assert.assertEquals("Juan", usuario.getNombre());

        Assert.assertThrows(UsuarioNoEncontradoException.class, () ->
                fachada.consultarUsuarioPorId(fachada.getUsuarios(), 999));  // ID no existente
    }

    @Test
    public void testConsultarUsuariosPorPuntoDeInteres() throws UsuarioNoEncontradoException, PuntoDeInteresNoExisteException {
        // Registrar que el usuario 1 ha pasado por el punto
        fachada.registrarPasoPorPuntoDeInteres(1, 10.5, 20.3);

        List<String> usuariosQuePasaron = fachada.consultarUsuariosPorPuntoDeInteres(10.5, 20.3);
        Assert.assertFalse(usuariosQuePasaron.isEmpty());
        Assert.assertTrue(usuariosQuePasaron.contains("Usuario: Juan Pérez"));
    }

    @Test
    public void testRegistrarPuntoDeInteres() {
        Assert.assertEquals(2, fachada.getPuntosDeInteres().size());
        this.fachada.registrarPuntoDeInteres(new PuntoDeInteres("Nuevo Punto", 25.4, 50.3, PuntoDeInteres.ElementType.TREE));
        Assert.assertEquals(3, fachada.getPuntosDeInteres().size());
    }

    @Test
    public void testConsultarPuntoDeInteres() throws PuntoDeInteresNoExisteException {
        PuntoDeInteres punto = fachada.consultarPuntoDeInteres(10.5, 20.3);
        Assert.assertNotNull(punto);
        Assert.assertEquals("Puerta Norte", punto.getNombre());

        Assert.assertThrows(PuntoDeInteresNoExisteException.class, () ->
                fachada.consultarPuntoDeInteres(999.9, 999.9));  // Coordenadas no existentes
    }

    @Test
    public void testActualizarUsuario() throws UsuarioNoEncontradoException {
        Usuario usuario = fachada.consultarUsuarioPorId(fachada.getUsuarios(), 1);
        Assert.assertEquals("Juan", usuario.getNombre());

        usuario.setNombre("Juan Carlos");
        fachada.actualizarUsuario(usuario);

        usuario = fachada.consultarUsuarioPorId(fachada.getUsuarios(), 1);
        Assert.assertEquals("Juan Carlos", usuario.getNombre());
    }

    @Test
    public void testEliminarUsuario() throws UsuarioNoEncontradoException {
        Assert.assertEquals(2, fachada.getUsuarios().size());
        this.fachada.eliminarUsuario(1);
        Assert.assertEquals(1, fachada.getUsuarios().size());

        Assert.assertThrows(UsuarioNoEncontradoException.class, () ->
                fachada.consultarUsuarioPorId(fachada.getUsuarios(), 1));  // Usuario eliminado
    }

    @Test
    public void testEliminarPuntoDeInteres() throws PuntoDeInteresNoExisteException {
        Assert.assertEquals(2, fachada.getPuntosDeInteres().size());
        this.fachada.eliminarPuntoDeInteres(15.2, 30.1);
        Assert.assertEquals(1, fachada.getPuntosDeInteres().size());

        Assert.assertThrows(PuntoDeInteresNoExisteException.class, () ->
                fachada.consultarPuntoDeInteres(15.2, 30.1));  // Punto eliminado
    }
}