document.getElementById("form-registro").addEventListener("submit", async function (e) {
  e.preventDefault();

  const name_user = document.getElementById("name_user").value;
  const name = document.getElementById("name").value;
  const email = document.getElementById("email").value;
  const password1 = document.getElementById("password1").value;
  const password2 = document.getElementById("password2").value;
  const role = document.getElementById("role").value;
  const mensajeError = document.getElementById("mensaje-error");

  if (password1 !== password2) {
    mensajeError.textContent = "Las contraseñas no coinciden.";
    return;
  }

  const model = {
    name_user,
    email,
    password1,
    password2,
    name,
    role
  };

  try {
    const response = await fetch("http://localhost:8080/usuario/nuevo", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(model),
    });

    if (response.status === 201) {
      window.location.href = "login.html";
    } else if (response.status === 409) {
      const res = await response.text();
      mensajeError.textContent = "Ya existe un usuario con ese correo.";
    } else {
      const res = await response.text();
      mensajeError.textContent = "Error: " + res;
    }
  } catch (err) {
    mensajeError.textContent = "Error de conexión con el servidor.";
    console.error(err);
  }
});
