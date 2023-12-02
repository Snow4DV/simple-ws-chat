document.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem('jwtToken');

    if (!token) {
        window.location.href = 'login.html';
    } else {
        document.body.innerHTML = `<div class="container mt-5"><h2>Authorized</h2></div>`;
    }
});

