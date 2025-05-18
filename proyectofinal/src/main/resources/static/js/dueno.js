document.addEventListener("DOMContentLoaded", () => {
  cargarUsuarioYRestaurantes();

  document.getElementById("form-restaurante").addEventListener("submit", async function (e) {
    e.preventDefault();
    await subirRestaurante();
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
    const restaurantesResp = await fetch("http://localhost:8080/restaurantes/mis-restaurantes", {
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
  const nombre_res = document.getElementById("nombre_restaurante").value;
  const dir = document.getElementById("direccion").value;
  const movil = document.getElementById("movil").value;
  const tipo = document.getElementById("type").value;
  const lat = document.getElementById("latitud").value;
  const long = document.getElementById("longitud").value;
  const mensaje = document.getElementById("mensaje-envio");

  const modelo = {
    nombre_res,
    dir,
    movil,
    tipo,
    lat,
    long
  };

  try {
    const response = await fetch("/restaurantes/nuevo", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify(modelo)
    });

    if (response.ok) {
      mensaje.textContent = "Restaurante subido correctamente.";
      document.getElementById("form-restaurante").reset();
      cargarUsuarioYRestaurantes();
    } else {
      const error = await response.text();
      mensaje.textContent = "Error al subir el restaurante: " + error;
    }
  } catch (err) {
    console.error("Error enviando el  restaurante:", err);
    mensaje.textContent = "Error al conectar con el servidor.";
  }
}
