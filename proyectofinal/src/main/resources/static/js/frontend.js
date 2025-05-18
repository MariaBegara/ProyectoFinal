// frontend.js

const API_BASE = 'http://localhost:8080'; // ajusta esto si cambias el puerto

// Mostrar todos los restaurantes en el mapa
async function cargarRestaurantesEnMapa(tipo = null, minScore = null) {
    let url = `${API_BASE}/restaurantes/filtrar`;
    const params = new URLSearchParams();
    if (tipo) params.append('tipo', tipo);
    if (minScore) params.append('minScore', minScore);
    if ([...params].length > 0) url += `?${params.toString()}`;

    try {
        const res = await fetch(url);
        if (!res.ok) throw new Error('Error al cargar restaurantes');
        const data = await res.json();

        console.log('Restaurantes:', data);
        pintarEnMapa(data);
    } catch (err) {
        console.error(err);
    }
}

// a modificar !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// Lógica para pintar marcadores en un mapa (ej. Leaflet o similar)
function pintarEnMapa(restaurantes) {
    restaurantes.forEach(r => {
        // Esto es pseudocódigo, sustituye por tu librería real (ej. Leaflet, Google Maps...)
        console.log(`${r.name_restaurant} [${r.latitude}, ${r.longitude}] ${r.averageScore}`);
        // L.map.addMarker({ lat: r.latitude, lng: r.longitude, popup: r.name_restaurant })
    });
}

// Iniciar sesión y guardar cookie
async function login(email, password) {
    const params = new URLSearchParams();
    params.append('email', email);
    params.append('password', password);

    const res = await fetch(`${API_BASE}/usuario/login`, {
        method: 'POST',
        body: params,
        credentials: 'include' // importante para guardar cookie "session"
    });

    if (!res.ok) throw new Error('Login incorrecto');
    const token = await res.json();
    console.log('Token recibido:', token);
}

// Obtener perfil del usuario actual
async function cargarPerfilUsuario() {
    const res = await fetch(`${API_BASE}/usuario/yo`, {
        method: 'GET',
        credentials: 'include'
    });
    if (!res.ok) throw new Error('No autenticado');
    const perfil = await res.json();
    console.log('Perfil:', perfil);
}

// Ejemplo: al cargar página, mostrar restaurantes filtrados
window.addEventListener('DOMContentLoaded', () => {
    cargarRestaurantesEnMapa('FUSION', 4.0);
});
