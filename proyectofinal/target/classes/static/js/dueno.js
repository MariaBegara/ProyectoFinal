document.addEventListener("DOMContentLoaded", () => {
  cargarUsuarioYRestaurantes();

  document.getElementById("form-restaurante").addEventListener("submit", async function (e) {
    await subirRestaurante();
    e.preventDefault(); //HE CAMBIADO EL ORDEN
  });
});

async function cargarUsuarioYRestaurantes() {
  try {
    const userResp = await fetch("http://localhost:8080/usuario/yo", {
      method: "GET",
      credentials: "include"
    });

    if (!userResp.ok) {
      window.location.href = "login.html";
      return;
    }

    const usuario = await userResp.json();
    console.log("Usuario autenticado:", usuario);

    // Cargar los restaurantes del dueño
    const restaurantesResp = await fetch("http://localhost:8080/restaurantes/yo", {
      method: "GET",
      credentials: "include"
    });

    if (!restaurantesResp.ok) {
      console.error("No se pudieron obtener los restaurantes del dueño.");
      return;
    }

    const lista = await restaurantesResp.json();
    const ul = document.getElementById("lista-propios");
    ul.innerHTML = "";

    lista.forEach(r => {
      const li = document.createElement("li");
      li.innerHTML = `<strong>${r.name_restaurant}</strong> (${r.type})<br>${r.direction} ${r.phone}`;
      ul.appendChild(li);
    });

  } catch (err) {
    console.error("Error cargando perfil o restaurantes del dueño:", err);
  }
}
async function subirRestaurante() {
  const name_restaurant = document.getElementById("nombre_restaurante").value;
  const direction = document.getElementById("direccion").value;
  const phone = document.getElementById("movil").value;
  const tipo = document.getElementById("type").value;
  const latitude = parseFloat(document.getElementById("latitud").value);
  const longitude = parseFloat(document.getElementById("longitud").value);
  const mensaje = document.getElementById("mensaje-envio");

  // Validación básica antes de enviar
  if (!name_restaurant || !direction || !phone || !tipo || isNaN(latitude) || isNaN(longitude)) {
    mensaje.textContent = "Por favor, rellena todos los campos correctamente.";
    return;
  }
  if (!Object.values(TypeEnum).includes(tipo)) {
    window.location.href = "cliente.html";
    return;
  }

  let email = "ejemplo@ejemplo.com";
  let password = "1234";

  const modelo = {
    name_restaurant,
    direction,
    phone,
    type:tipo,
    latitude,
    longitude,
    email,
    password
  };


  try {
    const response = await fetch("http://localhost:8080/restaurantes/nuevo", {

      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify(modelo)
    });

    if (response.status === 201  || response.ok) {
      mensaje.textContent = "Restaurante subido correctamente.";
      document.getElementById("form-restaurante").reset();
      cargarUsuarioYRestaurantes();
    } else {
      window.location.href = "cliente.html";
      const error = await response.text();
      mensaje.textContent = "Error al subir el restaurante: " + error;
    }
  } catch (err) {
    console.error("Error enviando el restaurante:", err);
    mensaje.textContent = "Error al conectar con el servidor.";
  }
}