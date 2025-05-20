document.getElementById("form-reseña").addEventListener("submit", async function (e) {
  e.preventDefault();

  const restaurantId = document.getElementById("restaurantId").value;
  const score = document.getElementById("score").value;
  const content = document.getElementById("content").value;
  const mensaje = document.getElementById("mensaje-reseña");

  // Modelo para el backend
  const review = {
    score: parseFloat(score),
    content: content,
    restaurantId: restaurantId
  };

  try {
    const response = await fetch("/review/nuevo", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify(review)
    });

    if (response.ok) {
      mensaje.textContent = "Reseña enviada correctamente.";
      document.getElementById("form-reseña").reset();
    } else {
      const error = await response.text();
      mensaje.textContent = "Error al enviar la reseña: " + error;
    }
  } catch (err) {
    mensaje.textContent = "Error al conectar con el servidor.";
    console.error(err);
  }
});

