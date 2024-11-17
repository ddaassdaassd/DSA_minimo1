package edu.upc.dsa.util;

import edu.upc.dsa.exceptions.PuntoDeInteresNoExisteException;
import edu.upc.dsa.exceptions.UsuarioNoEncontradoException;
import edu.upc.dsa.exceptions.UsuarioNoExisteException;

import java.util.List;

public interface IFachada {
    void registrarPuntoDeInteres(PuntoDeInteres punto);
    void registrarPasoPorPuntoDeInteres(int userId, double coordenadaHorizontal, double coordenadaVertical)
            throws UsuarioNoExisteException, PuntoDeInteresNoExisteException;
    void registrarUsuario(Usuario usuario);
    List<Usuario> listarUsuariosAlfabeticamente(List<Usuario> usuarios);
    List<String> consultarPuntosVisitados(int userId) throws UsuarioNoExisteException;
    List<String> consultarUsuariosPorPuntoDeInteres(double coordenadaHorizontal, double coordenadaVertical);
    List<String> consultarPuntosPorTipo(PuntoDeInteres.ElementType tipo);
    List<Usuario> getUsuarios();
    List<PuntoDeInteres> getPuntosDeInteres();
    Usuario consultarUsuarioPorId(List<Usuario> usuarios, int id) throws UsuarioNoEncontradoException;
    PuntoDeInteres buscarPuntoPorCoordenadas(double coordenadaHorizontal, double coordenadaVertical);
}
