# rest-example

GitHub: https://github.com/ddaassdaassd/DSA_minimo1.git

clase usuario: tiene identificador, nombre apellido, correo electronco, fecha de nacimiento, una lista de puntos de interés i un método para agregar puntos a esta lista (son puntos que ha visitado el usuario)

clase PuntoDeInteres: tiene nombre, coordenadas horizontal i vertical i un tipo de la lsta, door, wall, bridge,...

test: prueba todas las funciones:
- registrar usuario
- listar usuario por orden alfabetico
- consltar informacon de un usuario por su id
- añadir puntos de interés en el mapa
- registrar que un usuario para por un punto de interés
- consultas puntos de interés por los que pasa un usuario
- listar usuarios que han pasado por un punt de interés
- consultar puntos de interés de un tipo

el servicio api rest hace las mismas funciones

- registrar usuario------------------------------------------ POST /usuarios
- listar usuario por orden alfabetico------------------------ GET /usuarios
- consltar informacon de un usuario por su id---------------- GET /usuarios/{id}
- añadir puntos de interés en el mapa------------------------ POST /puntosinteres
- registrar que un usuario para por un punto de interés------ POST /registro
- consultas puntos de interés por los que pasa un usuario---- GET /puntosvisitados/{id}
- listar usuarios que han pasado por un punt de interés------ GET /usuarios/punto
- consultar puntos de interés de un tipo--------------------- GET /puntosinteres/tipo