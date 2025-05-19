let map;
let markers = [];

document.addEventListener("DOMContentLoaded", () => {
  initMap();
  cargarRestaurantes();

  document.getElementById("filtro-form").addEventListener("submit", function (e) {
    e.preventDefault();
    cargarRestaurantes();
  });
});

function initMap() {
  map = L.map('map').setView([40.4168, -3.7038], 12); // Centro: Madrid

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: 'OpenStreetMap contributors'
  }).addTo(map);
}

async function cargarRestaurantes() {
  const tipo = document.getElementById("tipo").value;
  const minScore = document.getElementById("minScore").value;
  const lista = document.getElementById("lista-restaurantes");
  lista.innerHTML = "";

  let url = "http://localhost:8080/restaurantes/mostrarlista";
  if (tipo || minScore) {
    const params = new URLSearchParams();
    if (tipo) params.append("tipo", tipo);
    if (minScore) params.append("minScore", minScore);
    url = "http://localhost:8080/restaurantes/filtrar?" + params.toString();
  }

  try {
    const response = await fetch(url);
    const restaurantes = await response.json();

    limpiarMarcadores();

    restaurantes.forEach(r => {
      // Añadir a la lista
      const li = document.createElement("li");
      li.innerHTML = `<strong>${r.name_restaurant}</strong> (${r.type})<br>
                      Dirección: ${r.direction}<br>
                      Nota media: ${r.averageScore.toFixed(1)}`;
      lista.appendChild(li);

      // Añadir marcador al mapa
      const marker = L.marker([parseFloat(r.latitude), parseFloat(r.longitude)])
        .addTo(map)
        .bindPopup(`<strong>${r.name_restaurant}</strong><br>${r.direction}<br> ${r.averageScore.toFixed(1)}`);
      markers.push(marker);
    });

    if (restaurantes.length > 0) {
      const first = restaurantes[0];
      map.setView([parseFloat(first.latitude), parseFloat(first.longitude)], 13);
    }

  } catch (err) {
    console.error("Error cargando restaurantes:", err);
  }
}

function limpiarMarcadores() {
  markers.forEach(m => map.removeLayer(m));
  markers = [];
}
