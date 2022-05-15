Práctica 2. Creación a la interconexión de microservicios
75.587 - Ing. del software de componentes y sistemas distribuidos aula 2
alumno: Carlos Cañado Moya
--------------------------------------------------------------------------------

--- Instrucciones para poner en marcha la infraestructura ---

### Docker Desktop / Docker Compose
Tener instalado Docker según se propone en las instrucciones de preparación del proyecto.

### Clonar el repositorio con el esqueleto del proyecto y ambos microservicios
Aunque las buenas prácticas recomiendan un repositorio por cada microservicio. Por simplificar he gestionado la totalidad del codigo desde un solo repositio.
Todo mi código se puede descargar de mi GitHub del repo: https://github.com/ccanado/epcsd-spring
En reste repositorio, además del código de ambos servicios, se encuentran todas los entregables y, como e lógico, todo el historico de commits

### Arrancar los contenedores proporcionados
Desda la raíz del directorio, donde se encuentra el docker-compose.yml, será necesaario arrancar los contenedores mediante: docker-compose up
Deberían arrancarse los contenedores:
* epcsd-spring_adminer_1 - adminer, un cliente SQL
* epcsd-spring_kafka_1 - el servidor de kafka
* epcsd-spring_db_1 - la bbdd postgresql
* epcsd-spring_zookeeper_1 - kafka zookeeper

### Contenidos a ejecutar
Ya sea conando el repositorio o desde el zip subido como entraga. Los archivos importantes para chequeo son:
* /deliverable/readme.txt >> Este archivo .txt con la información del proyecto
* /deliverable/epcsd.postman_collection.json >> json para importar la colección de Postman con todas las peticiones necesarias a las API.
* /deliverable/notification-0.0.1-SNAPSHOT.jar >> .jar del Microservicio de Noticiacion ejecutable mediante un: java -jar notification-0.0.1-SNAPSHOT.jar
* /deliverable/showcatalog-0.0.1-SNAPSHOT.jar >> .jar del Microservicio de ShowCatalog ejecutable mediante un: java -jar showcatalog-0.0.1-SNAPSHOT.jar

### Ejecutables corriendo
Una vez que se levanten ambos servicios. Podremos acceder a las siguientes URL:
* Panel _Adminer_ en http://localhost:18080/ para BBDD PostgreSQL
  - Motor: PostgreSQL
  - Servidor: db
  - Usuario: epcsd
  - Contraseña: epcsd
  - Esquema: epcsd
* Especificación API de MS ShowCatalog: http://localhost:18081/swagger-ui/index.html
* Especificación API de MS Notificacion: http://localhost:18082/swagger-ui/index.html

### Comprobación Funciones
Desde el propio Postman y usando la colección provista se podrá hacer las siguientes acciones:
* [POST] Create Category - {{showcatalogUrl}}/categories - Creará una category con id:1
* [POST] Create Show - {{showcatalogUrl}}/shows - Creará un show en la category id:1 con un id:1
- En la creación del show habrá una comunicación vía brokes de pub/sub entres los MS showcatalog y notification que lanzará las notificaciones (chequeables en los logs de los MS)
* [PUT] Add Performance - {{showcatalogUrl}}/shows/1/performances - Añadirá una performance al show con id:1
* [PATCH] Open Show - {{showcatalogUrl}}/shows/1/open - Se habrirá el show 1 cambiando su estado y el de sus performance
* [PATCH] Cancel Show - {{showcatalogUrl}}/shows/1/cancel - Se cancelará el show 1 cambiando su estado y el de sus performance
- Si se intenta de nuevo open o cancela un el show ya cancelado se podrá ver la gestión de la excepción pues en el estado actual no está permitodo
* [DEL] Delete Category - {{showcatalogUrl}}/categories/1 - En este punto no se permitirá eliminar la category puesto que tiene un show relacionado
* [DEL] Delete Show - {{showcatalogUrl}}/shows/1 - Eliminará el show y todas las performances relacionadas
- en este punto será posible volver a lanzar del 'Delete Category' y ahora si permitirá eliminar la category
- para las siguientes acciones será necesario crear de nuevo alguna category con shows y performance para poder ver el resultado
* [GET] Get All Categories - {{showcatalogUrl}}/categories - Devolverá un array con todas las categorías añadidas
* [GET] Get Shows by Name - {{showcatalogUrl}}/shows/findByName/Velas - Devolverá un array de {id,name} de todos los shows que contienen el texto pasado en la petición
* [GET] Get Shows by Category - {{showcatalogUrl}}/shows/findByCategory/1 - Devolverá un array de {id,name} de todos los shows de la category con id:1
* [GET] Get Show - {{showcatalogUrl}}/shows/1 - Obtendrá el detalle completo del show con id:1
* [GET] Get Show Performances - {{showcatalogUrl}}/shows/1/performances - Obtendrá un array con todas las performances del show con id:1
* [GET] Send Show Notification - {{notificationUrl}}/notifications/show/1 - lanzará la notificación a usuarios que tengan como favorita la category del show id:1

### Considraciones adicionales
Con este archivo de texto y el enlace al repositorio de código se debería tener todo tanto para chequear el código, como para ejecutar los jar como para probar las llamadas con Postman
De cualquier forma adjuntaré también todos los archivos en el entregable e incluiré un video explicativo.

--

Carlos Cañado