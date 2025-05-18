async function filtrar() {
    const tipo = document.getElementById('tipo').value;
    const score = document.getElementById('score').value;
    await cargarRestaurantesEnMapa(tipo || null, score || null);
}

async function cargarRestaurantesEnMapa(tipo = null, minScore = null) {
    let url = `${API_BASE}/restaurantes/filtrar`;
    const params = new URLSearchParams();
    if (tipo) params.append('tipo', tipo);
    if (minScore) params.append('minScore', minScore);
    if ([...params].length > 0) url += `?${params.toString()}`;

    try {
        const res = await fetch(url);
        const data = await res.json();
        pintarEnMapa(data);
    } catch (err) {
        console.error('Error al cargar restaurantes', err);
    }
}

function pintarEnMapa(restaurantes) {
    const map = L.map('map').setView([40.4168, -3.7038], 12);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '© OpenStreetMap'
    }).addTo(map);

    restaurantes.forEach(r => {
        const marker = L.marker([r.latitude, r.longitude]).addTo(map);
        marker.bindPopup(`<b>${r.name_restaurant}</b><br>⭐ ${r.averageScore}`);
    });
}

// Cargar mapa automáticamente
if (document.getElementById('map')) {
    window.addEventListener('DOMContentLoaded', () => cargarRestaurantesEnMapa());
}
