document.getElementById('registerForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const formData = new FormData(this);

    fetch('127.0.0.1:8080/api/v1/auth/signUp', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(Object.fromEntries(formData)),
    })
    .then(response => response.json())
    .then(data => {
        localStorage.setItem('jwtToken', data.token);
        window.location.href = 'index.html';
    })
    .catch(error => console.error('Error:', error));
});

