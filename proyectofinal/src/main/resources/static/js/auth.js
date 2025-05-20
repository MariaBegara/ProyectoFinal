document.getElementById("form-login").addEventListener("submit", async function (e) {
  e.preventDefault();

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;
  const errorMsg = document.getElementById("login-error");

  try {
    // Envío de login
    const response = await fetch("http://localhost:8080/usuario/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      credentials: "include", // Necesario para aceptar la cookie 'session'
      body: JSON.stringify({
        email: email,
        password: password
      })
    });

    if (!response.ok) {
      errorMsg.textContent = "Credenciales incorrectas";
      return;
    }

    // Hago una petición para saber el rol
    const perfilResp = await fetch("http://localhost:8080/usuario/yo", {
      method: "GET",
      credentials: "include"
    });

    if (!perfilResp.ok) {
      errorMsg.textContent = "No se pudo cargar el perfil";
      return;
    }

    const perfil = await perfilResp.json();
    const rol = perfil.role.toLowerCase();

    if (rol === "owner") {
      window.location.href = "dueno.html";
    } else {
      window.location.href = "cliente.html";
    }

  } catch (err) {
    errorMsg.textContent = "Error al conectar con el servidor";
    console.error(err);
  }
});
