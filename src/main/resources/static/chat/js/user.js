document.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem('jwtToken');

    if (!token) {
        window.location.href = 'login.html';
    }

    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('id');

    if (!userId) {
        alert("Неверный параметр пользователя")
        return;
    }

    fetch(`/api/v1/user?id=${userId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({ userId: parseInt(userId) }),
    })
        .then(response => response.json())
        .then(data => {
            displayUserInfo(data);
        })
        .catch(error => {
            console.error('Error:', error);
            alert("Пользователь не найден")
        });

    function displayUserInfo(user) {
        document.getElementById('username').textContent = user.user.username;
        document.getElementById('firstName').textContent = user.user.firstName;
        document.getElementById('lastName').textContent = user.user.lastName;

        const lastActivityElement = document.getElementById('lastActivity');
        if (user.lastActivity) {
            lastActivityElement.textContent = new Date(user.lastActivity).toLocaleString();
        } else {
            lastActivityElement.textContent = 'нет';
        }

        const profilePictureElement = document.getElementById('profile-picture');
        const firstLetter = user.user.firstName.charAt(0).toUpperCase();
        profilePictureElement.src = `https://placehold.it/100x100&text=${firstLetter}`;
    }
});