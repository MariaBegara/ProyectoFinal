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
  const name_restaurant = document.getElementById("name_restaurant").value;
  const direction = document.getElementById("direction").value;
  const phone = document.getElementById("phone").value;
  const type = document.getElementById("type").value;
  const latitude = document.getElementById("latitude").value;
  const longitude = document.getElementById("longitude").value;
  const mensaje = document.getElementById("mensaje-envio");

  const modelo = {
    name_restaurant,
    direction,
    phone,
    type,
    latitude,
    longitude
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
      mensaje.textContent = "Error al subir restaurante: " + error;
    }
  } catch (err) {
    console.error("Error enviando restaurante:", err);
    mensaje.textContent = "Error al conectar con el servidor.";
  }
}
