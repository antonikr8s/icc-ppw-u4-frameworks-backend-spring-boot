# Programación y Plataformas Web

![Logo UPS](assets/00-ups-icc.png)

# Spring Boot – Autenticación y Autorización con JWT: Seguridad y Control de Acceso

<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="80">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original.svg" width="80">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/nginx/nginx-original.svg" width="80">
</div>


## Práctica 16: Despliegue portable de Spring Boot con Docker y Nginx en Ubuntu Server

## Autores

**Carlos Antonio Gordillo Tenemaza**
* 📧 Correo: [antoniogordillo.1808@gmail.com](mailto:antoniogordillo.1808@gmail.com)
* 💻 GitHub: [antonikr8s](https://github.com/antonikr8s)
* 💼 LinkedIn: [Carlos Gordillo](https://linkedin.com/in/carlos-antonio-gordillo-tenemaza-828540281/)  

---

## Capturas de Pantalla


### 1. Captura del metodo POST para ingresar cinco productos
**Descripción:** Evidencia de la ejecución exitosa de peticiones HTTP con el método `POST` hacia el endpoint `/api/products` utilizando Postman. Se observa el envío de la estructura en formato JSON (`CreateProductDto`) y la respuesta correcta del servidor con un estado `200 OK` / `201 Created`, retornando el producto persistido con su identificador único (`id`) asignado dinámicamente
![Post](./assets/06-postman.png)

### 2. Captura del DBeaver
**Descripción:** Verificación de la persistencia real en el entorno de base de datos a través de DBeaver. Mediante la ejecución de la consulta sugerida *SELECT * FROM products;*, se comprueba que las cinco entidades fueron almacenadas correctamente en la tabla de PostgreSQL (`devdb`) gestionada dentro del contenedor Docker. Se evidencia la asignación secuencial de las claves primarias y el funcionamiento de los campos de auditoría heredados de `BaseEntity`.
![Dbeaver](assets/07dbeaver.png)

### 3. Validación de Auditoría y Eliminación Lógica en PostgreSQL
**Descripción:** Evidencia del estado final de la base de datos `devdb` en DBeaver tras la ejecución del escenario de pruebas solicitado en clase. Se verifica el correcto funcionamiento del ciclo de vida de los datos gestionados por JPA e Hibernate a través de las siguientes observaciones:
![Update](assets/08-update.png)

---

## Práctica 6 (Spring Boot): Validación de DTOs y Control de Datos de Entrada
<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="55">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="55">
</div>

### Prueba 1: Validar formato erróneo
![Bad](assets/09-Bad400.png)

### Prueba 2: Crear un producto válido
![Okk](assets/10-Ok200.png)

### Prueba 3: Validar regla de negocio - Eliminar el producto
![Delete](assets/11-Delete.png)

### Prueba 4: Validar regla de negocio - Intentar actualizar un producto eliminado
![Internal](assets/12-Internal500.png)

### Prueba 5: Validar regla de negocio - `findAll`
![Get](assets/13-Get.png)

### Verificar en DBeaver
![Update](assets/14-Update.png)

---

## Práctica 7 (Spring Boot): Manejo Global de Errores y Excepciones

<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="55">
</div>

### Prueba 1: Buscar producto inexistente
![notFound](assets/15-notFound.png)

### Prueba 2: Nombre de producto duplicado
![Conflicto](assets/16-Conflicto.png)

### Prueba 3: Validación estructurada de DTO
![BadRequest](assets/17-BadRequest.png)

### Prueba 4: Flujo de Eliminado Lógico a Inexistente
![Error404](assets/18-Error404.png)


### Actualización en DBeaver
![Update-2](assets/19-Update-2.png)

---

## Práctica 8 (Spring Boot): Relaciones ManyToOne, Foreign Keys y Consultas Relacionales

<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="55">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" width="55">
</div>

### 1. Creacion  de una categoria
![PP](assets/52-2Creation.png)

### 2. Creación de un producto con sus relaciones
![PP](assets/53-33-Producto.png)

### 3. Descripción de la tabla products en PostgreSQL
**Descripción:** Para ver el producto creado
![PP](assets/54-4.png)

**Descripción:** Para ver la relación
![PP](assets/55-5.png)

## Preguntas y Respuestas

### Explicación 1: Funcionamiento de las relaciones `@ManyToOne` y `@OneToMany` en JPA

En Spring Data JPA, las relaciones entre las tablas de la base de datos se representan en las clases del programa usando anotaciones. En este caso, se relacionan las entidades **Producto** y **Categoría**.

- **`@ManyToOne` en Producto:** Indica que **muchos productos pueden pertenecer a una sola categoría**. Por ejemplo, varios productos pueden pertenecer a la categoría "Tecnología". `@JoinColumn(name = "category_id")` indica la columna que se utiliza para relacionar el producto con su categoría.

- **`@OneToMany` en Categoría:** Indica que **una categoría puede tener muchos productos**. Por ejemplo, una categoría puede contener varios productos. `mappedBy = "category"` indica que la relación ya está controlada desde la entidad `Producto`.

- **`FetchType.LAZY`:** Indica que los datos relacionados se cargan **solo cuando son necesarios**. Por ejemplo, los productos de una categoría se consultan únicamente cuando el programa necesita acceder a ellos. Esto ayuda a mejorar el rendimiento.

---

## Práctica 9 (Spring Boot): Request Parameters, Consultas Relacionadas y Filtrado con JPA

<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="55">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" width="55">
</div>

### 1: Producto con varias categorías
**Descripción:** Vamos a crear un producto complejo que encaje en varias de las categorías
![n](assets/56-Varias-Cat.png)

### 2. Consulta con filtros por usuario
**Descripción:** Se muestra los productos que creó un usuario específico.
![n](assets/57-Pp.png)

### 3. Consulta con filtros por nombres
**Descripción:** Se muestra los productos por el nombre 'Sensores'.
![n](assets/57-Pp.png)

### 4. Consulta con filtros por categoría
![n](assets/58.png)

## Preguntas y Respuestas

### Explicación 1: Relación `@ManyToMany` y `@JoinTable`

La relación `@ManyToMany` se utiliza cuando **un producto puede tener varias categorías y una categoría puede pertenecer a varios productos**.

Como esta relación conecta muchas categorías con muchos productos, se necesita una **tabla intermedia** en la base de datos para relacionar ambas entidades.

En Spring Boot, esto se configura en la entidad `Product` mediante la anotación `@JoinTable`. En ella se indica el nombre de la tabla intermedia, por ejemplo, `product_categories`, y las columnas que relacionan los productos y las categorías.

- **`joinColumns`:** Indica la columna que contiene el ID del producto.
- **`inverseJoinColumns`:** Indica la columna que contiene el ID de la categoría.

De esta manera, la tabla intermedia permite relacionar fácilmente los productos con sus diferentes categorías.



### Explicación 2: Filtrado dinámico y Repositorios

El filtrado de productos por usuario y categoría se realiza mediante **Spring Data JPA**.

Para buscar los productos de un usuario específico, se pueden crear métodos en el repositorio, como `findByUserId(Long id)`. A partir del nombre del método, Spring Data JPA genera automáticamente la consulta necesaria para buscar los productos que pertenecen a ese usuario.

Estos métodos se utilizan desde la capa de servicio, mientras que el controlador recibe los datos necesarios para realizar el filtro mediante parámetros de consulta (`@RequestParam`) o variables de la URL (`@PathVariable`).

De esta forma, cada capa cumple una función específica y el filtrado se realiza directamente en la base de datos, evitando cargar datos innecesarios.

---

## Práctica 10 (Spring Boot): Paginación de Productos con Page, Slice y Pageable

<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="55">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" width="55">
</div>

### 1. Ejecutar `seed_data.sql` (cargar los datos)
![Power](assets/22-PowerShell.png)

### 2. Captura de respuesta con Page
**Descripción:** `GET` /api/products/page?page=0&size=5
![Page](assets/23-Respuesta-Page.png)

### 3. Captura de respuesta con Slice
**Descripción:** `GET` /api/products/slice?page=0&size=5
![Slice](assets/24-Respuesta-Slice.png)

### 4. Captura de error por paginación inválida
**Descripción:** `GET` /api/products/page?page=-1&size=0
![Paginacion-Invalida](assets/25-Paginacion-invalida.png)

### 5. Captura de endpoint de categoría paginado
**Descripción:** `GET` /api/categories/2/products/page?page=110&size=5
![Categoria-Paginado](assets/26-Categoria-paginado.png)

### 6. Captura de endpoint de categoría paginado
**Descripción:** `GET` /api/categories/2/products/slice?page=10&size=5
![Update](assets/26-Categoria-paginado.png)

### 7. Paginación General con `Page<T>` (`GET /api/products/page`)
**Descripción:** Respuesta esperada: Un objeto JSON que incluye la lista content junto con la estructura de metadatos completa (totalElements, totalPages, number, size).
![notFound](assets/59-1PAg.png)

### 8: Paginación General con `Slice<T>` (`GET /api/products/slice`)
**Descripción:** Respuesta esperada: Un objeto JSON con el listado content y banderas booleanas simplificadas (first, last, hasNext, hasPrevious, numberOfElements), sin incluir totalElements ni totalPages.
![notFound](assets/60-Slice.png)

## Preguntas y Respuestas

### 1. Explicación Técnica: Diferencia entre `Page<T>` y `Slice<T>`

En Spring Data JPA, `Page<T>` y `Slice<T>` permiten dividir grandes cantidades de datos en páginas más pequeñas. La principal diferencia está en la información que devuelven y en la forma en que realizan las consultas a la base de datos.

| Característica | `Page<T>` | `Slice<T>` |
|---|---|---|
| **Consulta `COUNT`** | Realiza una consulta adicional para conocer el total de registros. | No realiza una consulta `COUNT`. Solo consulta los datos necesarios. |
| **Información devuelta** | Proporciona el total de páginas y el total de elementos. | Indica si existe una página siguiente o anterior. |
| **Rendimiento** | Puede tener un mayor costo debido a la consulta adicional de conteo. | Tiene un mejor rendimiento porque evita la consulta de conteo. |
| **Uso ideal** | Cuando se necesita mostrar el total de páginas y registros. | Cuando solo se necesita avanzar o retroceder entre páginas, como en un scroll infinito. |

---

### Explicación 1: ¿Por qué `Slice<T>` realiza la consulta usando `size + 1`?

`Slice<T>` necesita saber si existe una página siguiente, pero sin realizar una consulta `COUNT`.

Para lograrlo, Spring Data JPA solicita **un registro adicional** a la cantidad de elementos que debe mostrar.

Por ejemplo, si la página debe mostrar **10 productos**, se solicitan **11 productos**:

- Si se encuentran **11 productos**, significa que existe una página siguiente. El producto adicional se elimina y `hasNext()` devuelve `true`.
- Si se encuentran **10 productos o menos**, significa que no hay más datos y `hasNext()` devuelve `false`.

De esta manera, `Slice<T>` puede saber si existe una página siguiente sin realizar una consulta adicional para contar todos los registros.

---

## Explicación 2: Impacto en el Rendimiento y Optimización de Consultas

Cuando la base de datos contiene una gran cantidad de registros, realizar una consulta `COUNT(*)` en cada petición puede aumentar el tiempo de respuesta y consumir más recursos.

`Slice<T>` evita esta consulta adicional, ya que solo busca los datos necesarios y un registro extra para comprobar si existe una página siguiente.

Por esta razón, `Slice<T>` puede ofrecer un **mejor rendimiento** cuando no es necesario conocer el número total de páginas o registros.

---

## Práctica 11 (Spring Boot): Autenticación JWT, Autorización por Roles y Protección de Endpoints

<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="55">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" width="55">
</div>

### 1. Registro de usuario nuevo
**Descripción:** Debe responder `201 Created` con un `token`, el `userId`, `name`, `email` y `roles`: `["ROLE_USER"]`.
![UsuarioNuevo](assets/27-Registro-New-User.png)

### 2. Verifica en DBeaver que se guardó bien
**Descripción:** Debe mostrar el usuario con rol `ROLE_USER`.
![VerificaDBeaver](assets/28-Verificacion-DBeaver.png)

### 3. Login con ese usuario
**Descripción:** Debe responder `200 OK` con un nuevo token.
![LoginUser](assets/29-Login-User-Creado.png)

### 4. Login con contraseña incorrecta (caso de error)
**Descripción:** Mismo endpoint, pero con `"password": "incorrecta"`. Debe responder `401` (no `500`).
![LoginPassword](assets/30-Contraseña-incorrecta.png)

### 5. Registro con email duplicado (caso de error)
**Descripción:** Debe responder `409 Conflict` ("El email ya está registrado").
![RegistroEmail](assets/31-Correo-duplicado.png)

### 6. Endpoint protegido SIN token
**Descripción:** Sin ningún header `Authorization`. Debe responder `401 Unauthorized` con el JSON de `ErrorResponse` que armamos en el `JwtAuthenticationEntryPoint` (con `timestamp`, `status`, `error`, `message`, `path`).
![EndPointSinToken](assets/32-Endpoint-SIN-token.png)

### 7. Endpoint protegido CON token
**Descripción:** Ahora debe responder `200 OK` con la lista normal de productos.
![EndPointConToken](assets/33-Endpoint-CON-token.png)

---

## Práctica 12 (Spring Boot): Autenticación JWT, Autorización por Roles y Protección de Endpoints

<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="55">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" width="55">
</div>

### 1. Creacion de con usuario normal
![](assets/34-New-User.png)


### 2. Login con usuario normal
**Descripción:** Debe dar 403 Forbidden con mensaje "No tienes permisos para acceder a este recurso"
![](assets/35-406-Sin-permisos.png)


### 3. Asignar ROLE_ADMIN a un usuario (DBeaver)
**Descripción:** Ajusta el `user_id` al del usuario que quieres volver admin — verifica el id real en la tabla users
![](assets/36-Permisos.png)


### 4. Login con usuario ADMIN
![](assets/37-Autorizacion.png)


### 5. Petición Exitosa usando un usuario con rol `ROLE_ADMIN`
![notFound](assets/61-Token.png)

### 6. Usuario con rol `ROLE_USER` intenta eliminar un registro
**Descripción:** El usuario 11 está intentando borrar el producto del usuario 1,
![notFound](assets/62-2.png)

---

## Práctica 13 (Spring Boot): Validación de Propiedad de Recursos

<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="55">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" width="55">
</div>

### 1. Captura de creación de producto con usuario autenticado
**Descripción:** Crear producto con el `TOKEN` del Usuario X.
![](assets/41-Creacion-Producto.png)


### 2. Captura de bloqueo por producto ajeno
**Descripción:** Usuario Z intenta actualizar el producto de Usuario X. Resultado esperado `403 Forbidden`
![](assets/42-Bloqueo.png)



### 3. Captura de eliminación de producto ajeno bloqueada
**Descripción:** Usuario Z intenta eliminar el producto de Usuario X. `403 Forbidden`
![](assets/43-Eliminacion.png)


### 4. Captura de ADMIN modificando producto ajeno
**Descripción:** En `DBEAVER` se otorga permisos al usuario para tener el `ROLE_ADMIN`.
![](assets/44-ADMIN.png)

Despues de otorgar permisos de `ROLE_ADMIN`, se vuelve a ejecutar el `DELETE`. Resultado esperado `200 OK`.

![](assets/45-DELETE.png)

## Preguntas y Respuestas

### 1. Bloqueo a usuarios estándar (`403 Forbidden`)

Se comprobó que un usuario no puede modificar o eliminar un producto que pertenece a otro usuario.

Cuando el usuario intenta realizar una petición `PUT` o `DELETE` sobre un producto que no le pertenece, el sistema bloquea la operación y devuelve un error `403 Forbidden`.

Esto evita que un usuario pueda modificar o eliminar información de otros usuarios.

### 2. Acceso especial para Administradores

Los usuarios con el rol `ROLE_ADMIN` tienen permisos especiales y pueden ignorar la restricción de propiedad.

Esto permite que un administrador pueda modificar o eliminar cualquier producto del sistema cuando sea necesario, por ejemplo, para realizar tareas de mantenimiento o administración.

---

## Práctica 14 (Spring Boot): Renovación de Access Token con Refresh Token

<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="55">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" width="55">
</div>

### 1. Captura de un usuario creado SIN `Refresh Token`
**Descripción:** Anteriormente, al crear un usuario, el sistema únicamente mostraba el `Access Token`.

![](assets/50-Refresh.png)

### 2. Captura de un usuario creado CON `Refresh Token`
**Descripción:** Ahora, cada nuevo usuario creado obtiene también un `Refresh Token`.

![](assets/63-Tokk.png)

### 3. Login con Refresh Token `Refresh Token`
**Descripción:** La respuesta muestra un código `200 OK` junto con el `Access Token`, el `Refresh Token` y los roles del usuario.

![](assets/64-LoginRefresh.png)

### 4. Refresh Exitoso
**Descripción:** La respuesta muestra un código `200 OK` con un nuevo `Access Token` y un nuevo `Refresh Token`. Esto demuestra que se realizó correctamente la rotación del token.

![](assets/65-RefreshRefresh.png)

### 5. Logout
**Descripción:** Al cerrar sesión, el sistema responde con el código `204 No Content`.

![](assets/66-Logout.png)

### 6. Refresh después de Logout (Error)
**Descripción:** Después de cerrar sesión, se intenta utilizar el `Refresh Token` anterior. El sistema responde con un código `400 Bad Request`, indicando que el token ya no es válido o fue revocado.

![](assets/67-Log-Error.png)

---

## Preguntas y Respuestas

### 1. ¿Cuál es la diferencia entre `Access Token` y `Refresh Token`?

- **Access Token:** Es un token de corta duración. En este sistema tiene una duración de 30 minutos y se utiliza para acceder a los endpoints protegidos mediante la cabecera `Authorization: Bearer <token>`.

- **Refresh Token:** Es un token de mayor duración. En este sistema tiene una duración de 7 días y se almacena en la base de datos. Se utiliza para solicitar nuevos tokens cuando el `Access Token` expira, evitando que el usuario tenga que iniciar sesión nuevamente.

---

### 2. ¿Por qué el `Refresh Token` no debe usarse en `Authorization: Bearer`?

El `Refresh Token` no se utiliza para acceder a los endpoints protegidos. El filtro de seguridad verifica que el token utilizado sea un `Access Token`.

Si se intenta enviar un `Refresh Token` en `Authorization: Bearer`, el sistema lo rechaza y devuelve un error `401 Unauthorized`. Esto evita que el token de renovación se utilice para acceder directamente a los recursos protegidos.

---

### 3. ¿Qué significa rotar un `Refresh Token`?

La rotación significa que, cada vez que se utiliza un `Refresh Token` para obtener nuevos tokens, el sistema invalida el token anterior y genera uno nuevo.

De esta manera, el token anterior ya no puede volver a utilizarse. Esto mejora la seguridad y reduce el riesgo de que un token antiguo pueda ser reutilizado.

---

## Práctica 15 (Spring Boot): Documentación de Endpoints con Swagger, OpenAPI y Seguridad JWT

<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="55">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" width="55">
</div>

### Captura 1. Swagger UI cargado
**Descripción:** Se levanta el servicio y se accede a la ruta `http://localhost:8080/api/swagger-ui/index.html, evidenciando la lista de controladores y los endpoints agrupados por tags.

![](assets/68-SSwa.png)

### Captura 2. JSON OpenAPI
**Descripción:** Se consulta la ruta `/api/v3/api-docs` para mostrar el documento generado con su estructura principal, incluyendo `openapi`, `paths` y `components`.

![](assets/69-Api.png)

### Captura 3. AuthController documentado
**Descripción:** Se expande el controlador de autenticación en Swagger UI, mostrando los endpoints `POST /api/auth/register` y `POST /api/auth/login` junto con sus respectivas descripciones.

![](assets/70auth.png)


### Captura 4. Botón Authorize
**Descripción:** Se abre el botón `Authorize`, mostrando que el esquema de seguridad `bearerAuth` está configurado para recibir tokens JWT.

![](assets/71-B.png)


### Captura 5. Endpoint protegido sin token
**Descripción:** Se intenta acceder al endpoint `GET /api/products/page?page=0&size=5` desde Swagger sin proporcionar un token. 

![](assets/72-Ex.png)

El sistema bloquea la petición y devuelve un error `401 Unauthorized`.

![](assets/73-Res.png)


### Captura 6. Endpoint protegido con token desde Swagger
**Descripción:** Se agrega un token válido en Swagger y se vuelve a ejecutar el endpoint `GET /api/products/page?page=0&size=5`. 

![](assets/74-Add.png)

La petición se realiza correctamente y devuelve una respuesta `200 OK`.

![](assets/75-GET.png)

### Captura 7. Endpoint ADMIN con usuario normal
**Descripción:** Se utiliza el token de un usuario con `ROLE_USER` para acceder al endpoint administrativo `GET /api/products`. El sistema bloquea el acceso porque el usuario no tiene los permisos necesarios y devuelve un error `403 Forbidden`.

![](assets/76-PAgg.png)

### Captura 8. Endpoint ADMIN con usuario administrador

**Descripción:** Se utiliza el token de un usuario con `ROLE_ADMIN` para acceder al mismo endpoint `GET /api/products`. 

![](assets/77-Permisos.png)
El acceso es permitido y se obtiene una respuesta `200 OK`.

![](assets/78-Autorizathed.png)

---

## Preguntas y Respuestas

### 1. ¿Cuál es la diferencia entre Swagger UI y OpenAPI?

**OpenAPI** es el estándar que define la estructura y descripción de una API. **Swagger UI** es una herramienta que utiliza esa documentación para mostrar los endpoints de forma visual y permitir probar las peticiones directamente desde el navegador.


### 2. ¿Por qué Swagger puede ser público pero los endpoints seguir protegidos?

Swagger puede ser público porque su función es mostrar y documentar los endpoints de la API. Sin embargo, esto no significa que todos los endpoints sean públicos.

Los endpoints protegidos siguen utilizando las reglas de seguridad de Spring Security. Por ejemplo, un usuario puede acceder a Swagger, pero necesitará un JWT válido y los permisos correspondientes para consumir un endpoint protegido.


### 3. ¿Cómo se configura Swagger para enviar un JWT en `Authorization: Bearer`?

Se configura un esquema de seguridad `bearerAuth` en OpenAPI indicando que la API utiliza autenticación mediante un token JWT.

Luego, Swagger UI muestra el botón **Authorize**, donde se puede ingresar el token. Al realizar una petición, Swagger lo envía automáticamente en la cabecera:

`Authorization: Bearer <token>`

De esta manera, los endpoints protegidos pueden validar el JWT y determinar si el usuario tiene permiso para acceder al recurso.

---

## Práctica 16: Despliegue portable de Spring Boot con Docker y Nginx en Ubuntu Server

<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="55">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" width="55">
</div>

### 1. Instalacion de Docker en Ubuntu
**Descripción:** Crear la red y el Postgres, dentro de la VM (por `SSH`)

![](assets/46-Docker-VM.png)


### 2. Clonar el repositorio dentro de la VM
**Descripción:** Postgres-dev está corriendo correctamente en el puerto 5433, dentro de app-network

![](assets/47-Git.png)

### 3. Crear el archivo `.env.ubuntu`
**Descripción:** Protege el archivo: `chmod 600 .env.ubuntu` y se confirma con `cat .env.ubuntu`

![](assets/48-Nano.png)


## 4. Swagger UI cargado con configuración personalizada
**Descripción:** Se inicia el servicio y se accede a Swagger UI mediante la ruta `http://192.168.56.103:8080/api/swagger-ui/index.html`.

Se evidencia que Swagger UI y la documentación de OpenAPI son de acceso público. La interfaz muestra la configuración personalizada de la API con el título **"API de Programación y Plataformas Web"**, la versión **"1.0.0"** y el contexto global `/api`.

![](assets/79-Configuracion.png)


## 5. Modal de Autorización (Esquema Bearer JWT)
**Descripción:** Se abre el botón **"Authorize"** en Swagger UI. Se evidencia que está configurado el esquema de seguridad `bearerAuth`, utilizando autenticación de tipo `Bearer` con tokens JWT.

El modal también muestra el mensaje personalizado: **"Ingrese el JWT generado en /auth/login"**.

![](assets/80-Autoriza.png)


## 6. Generación del Token en endpoint público
**Descripción:** Se ejecuta correctamente el endpoint de inicio de sesión `/auth/login`. Como esta ruta es pública, no necesita un token para ser utilizada.

![](assets/81-Register.png)

Después de ingresar las credenciales correctas, el sistema genera y devuelve un token JWT válido.

![](assets/82-Token.png)


## 7. Restricción de seguridad en endpoints protegidos (Error 401/403)
**Descripción:** Se intenta ejecutar un endpoint protegido desde Swagger UI sin estar autenticado.

El sistema rechaza la petición porque el usuario no tiene una autenticación válida. Esto demuestra que los endpoints protegidos requieren autenticación y que la aplicación utiliza una política de sesión `STATELESS`.

![](assets/83-Listar.png)


## 8. Bloqueo de seguridad
**Descripción:** Demuestra que la autorización basada en roles (RBAC) está funcionando, impidiendo que un `ROLE_USER` acceda a un recurso de `ROLE_ADMIN`.

![](assets/83-Listar.png)



## 9. Acceso exitoso inyectando el Token JWT
**Descripción:** Se ingresa un token JWT válido (`ROLE_ADMIN`) mediante el botón **"Authorize"** y se ejecuta nuevamente un endpoint protegido.

Swagger agrega automáticamente el token en la cabecera `Authorization` utilizando el formato `Bearer <token>`. El servidor valida el token y permite el acceso, devolviendo una respuesta `200 OK`.

![](assets/84-Admin.png)


---

## Explicación del Flujo de Datos Completo (API REST ↔ PostgreSQL)

### 1. Petición (Ida)

El cliente, por ejemplo Postman, envía una solicitud HTTP con los datos en formato JSON. El controlador recibe la petición y la envía al servicio, donde se ejecuta la lógica de negocio correspondiente.

### 2. Transformación y Persistencia

En la capa de servicio, los datos recibidos se transforman de un DTO a un modelo interno y luego a una entidad. Después, el repositorio utiliza Hibernate para guardar la información en la base de datos PostgreSQL mediante una operación de inserción.

### 3. Rol de BaseEntity

La entidad hereda de la clase BaseEntity, que proporciona automáticamente campos comunes como el identificador, las fechas de creación y actualización, y el estado lógico del registro. Además, estos valores se gestionan de forma automática mediante anotaciones de persistencia, evitando código repetitivo.

### 4. Respuesta (Vuelta)

Una vez almacenados los datos, PostgreSQL genera el identificador del registro y devuelve la información guardada. Posteriormente, la entidad se transforma en un DTO de respuesta y se envía al cliente con un estado HTTP exitoso.


### 5. ¿Cuál es la diferencia entre Page y Slice?

`Page` devuelve una respuesta paginada completa, incluyendo los datos, el número total de registros y el total de páginas disponibles. Para obtener esta información, Spring Data JPA realiza una consulta para recuperar los datos y otra para contar el número total de registros.

Por otro lado, `Slice` solo indica si existe una página siguiente o anterior, sin calcular el total de registros. Esto lo hace más eficiente cuando únicamente se necesita navegar entre páginas.

En general, Page es recomendable cuando se requiere mostrar el número total de resultados o páginas, mientras que Slice es una mejor opción para funciones como el desplazamiento infinito (infinite scroll), donde la prioridad es el rendimiento.

### 6. ¿Por qué la paginación debe aplicarse en el repositorio y no después de traer todos los datos en memoria?

La paginación debe realizarse en el repositorio para que la base de datos devuelva únicamente los registros solicitados. Si primero se cargan todos los datos en memoria y luego se paginan, el sistema consume más memoria, utiliza más ancho de banda y aumenta el tiempo de respuesta, especialmente cuando existen miles de registros.

Al utilizar Pageable, Spring Data JPA traduce la solicitud a instrucciones SQL como LIMIT y OFFSET, permitiendo que la base de datos envíe solo los datos necesarios. Esto mejora el rendimiento y hace que la aplicación sea más eficiente y escalable.