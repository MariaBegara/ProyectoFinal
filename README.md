# Proyecto Final PAT - Aplicación de Restaurantes

Este proyecto es una plataforma web donde los usuarios pueden explorar, registrar y valorar restaurantes. Está desarrollado como parte del Proyecto Final de la asignatura PAT, combinando backend en **Spring Boot** con un frontend en **HTML, CSS y JavaScript** puro.

---

## Tecnologías utilizadas

- **Backend:** Java 21, Spring Boot 3, Spring Data JPA
- **Base de datos:** H2 en memoria
- **Frontend:** HTML5, CSS3, JavaScript 
- **Seguridad:** Cookies de sesión (`session`) para autenticación
- **Librerías externas:** OpenCSV, Leaflet.js (mapas interactivos)

---

##  Roles de usuario

###  Cliente (`USER`)
- Visualiza todos los restaurantes sobre un mapa.
- Filtra por tipo de comida y puntuación mínima.
- Envía reseñas a los restaurantes (de 1 a 5 puntos).

###  Dueño de restaurante (`OWNER`)
- Registra nuevos restaurantes desde un formulario.
- Visualiza la lista de sus propios restaurantes.
- Cada restaurante incluye nombre, dirección, tipo, teléfono y coordenadas.

---

##  Autenticación y gestión de usuarios

- Los usuarios pueden **registrarse** indicando su rol (cliente o dueño).
- Login mediante `fetch` con almacenamiento de sesión en cookie.
- El backend identifica al usuario en cada endpoint protegido gracias a `@CookieValue("session")`.

---

## Funcionalidades del cliente

- **Mapa interactivo** con Leaflet.js mostrando restaurantes.
- **Filtros** por tipo de comida y puntuación mínima.
- **Formulario de reseñas** con validación y envío de comentarios al backend.

---

## Funcionalidades del dueño

- Formulario para **crear un restaurante** nuevo, seleccionando tipo, dirección, y ubicación.
- Visualización de sus propios restaurantes.
- Validación de datos tanto en frontend como en backend.

---
