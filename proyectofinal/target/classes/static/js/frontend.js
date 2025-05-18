const API_BASE = 'http://localhost:8080';

// Login
const loginForm = document.getElementById('login-form');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const email = e.target.email.value;
        const password = e.target.password.value;

        const params = new URLSearchParams();
        params.append('email', email);
        params.append('password', password);

        const res = await fetch(`${API_BASE}/usuario/login`, {
            method: 'POST',
            body: params,
            credentials: 'include'
        });

        if (res.ok) {
            alert('Login exitoso');
            window.location.href = '/mapa.html';
        } else {
            alert('Credenciales incorrectas');
        }
    });
}

// Registro
const registerForm = document.getElementById('register-form');
if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(registerForm);
        const data = Object.fromEntries(formData);

        const res = await fetch(`${API_BASE}/usuario/nuevo`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            alert('Usuario creado');
        } else {
            alert('Error al registrar');
        }
    });
}

// Perfil
async function cargarPerfilUsuario() {
    const res = await fetch(`${API_BASE}/usuario/yo`, {
        credentials: 'include'
    });
    if (res.ok) {
        const perfil = await res.json();
        document.getElementById('perfil-info').innerText = JSON.stringify(perfil, null, 2);
    }
}

async function logout() {
    await fetch(`${API_BASE}/usuario/logout`, {
        method: 'POST',
        credentials: 'include'
    });
    window.location.href = '/index.html';
}

async function eliminarCuenta() {
    const res = await fetch(`${API_BASE}/usuario/yo`, {
        method: 'DELETE',
        credentials: 'include'
    });
    if (res.ok) window.location.href = '/index.html';
}

// Ejecutar perfil si existe el div
if (document.getElementById('perfil-info')) {
    cargarPerfilUsuario();
}
