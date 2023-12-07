document.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem('jwtToken');

    const sendHeaders = {
        Authorization: 'Bearer ' + token
    };

    if (!token) {
        window.location.href = 'login.html';
    }

    const socket = new WebSocket('ws://' + window.location.host + '/webs?jwtToken=' + token);
    const stompClient = Stomp.over(socket);

    // Set the Authorization header with the Bearer token
    const headers = {
        Authorization: 'Bearer ' + token
    };

    stompClient.connect(headers, function (frame) {
        console.log('Connected: ' + frame);

        // Subscribe to the topic to receive chat history
        stompClient.subscribe('/user/queue/history', function (message) {
            const chatHistory = JSON.parse(message.body);
            displayChatHistory(chatHistory);
        });

        stompClient.subscribe('/topic/webs-topic', function (message) {
            const payload = JSON.parse(message.body);

            // Check the type of message received
            if (payload.users) {
                displayActiveUsers(payload.users);
            } else if (payload.sender) {
                displayNewMessage(payload);
            }
        });

        // Ask for chat history
        stompClient.send('/app/chat.subscribe', sendHeaders);

    });

    socket.onerror = function (event) {
        localStorage.removeItem('jwtToken');
        alert('Соединение прервано. Авторизуйтесь заново');
        window.location.href = 'login.html';
    };

    socket.onclose = function (event) {
        alert('Соединение прервано. Обновите страницу.');
    };

    function formatMessageDate(timestamp) {
        const options = {
            day: 'numeric',
            month: 'numeric',
            year: 'numeric',
            hour: 'numeric',
            minute: 'numeric',
            hour12: false, // 24-hour format
        };

        return new Date(timestamp).toLocaleString('en-GB', options);
    }

    function displayChatHistory(chatHistory) {
        const chatBox = document.getElementById('chat-box');
        chatBox.innerHTML = '';

        chatHistory.forEach(message => {
            const messageElement = createMessageElement(message);
            chatBox.appendChild(messageElement);
        });
    }

    function displayNewMessage(message) {
        const chatBox = document.getElementById('chat-box');
        const messageElement = createMessageElement(message);
        chatBox.appendChild(messageElement);
    }

    function displayActiveUsers(activeUsers) {
        const activeUsersTable = document.getElementById('active-users-table');
        activeUsersTable.innerHTML = '';

        activeUsers.forEach(userInfo => {
            const row = document.createElement('tr');
            const usernameCell = document.createElement('td');
            const lastActivityCell = document.createElement('td');

            usernameCell.textContent = userInfo.user.username;
            lastActivityCell.textContent = userInfo.lastActivity ? formatMessageDate(userInfo.lastActivity) : 'Н/А';

            row.appendChild(usernameCell);
            row.appendChild(lastActivityCell);
            activeUsersTable.appendChild(row);
        });
    }

    function createMessageElement(message) {
        const messageElement = document.createElement('div');
        const formattedDate = formatMessageDate(message.timestamp);
        const usernameLink = document.createElement('a');
        usernameLink.textContent = message.sender.username;
        usernameLink.href = `/chat/user.html?id=${message.sender.id}`;
        usernameLink.target = '_blank';
        messageElement.innerHTML = `<strong>${usernameLink.outerHTML} [${formattedDate}]:</strong> ${message.text}`;

        return messageElement;
    }

    document.getElementById('message-form').addEventListener('submit', function (event) {
        event.preventDefault();
        const messageInput = document.getElementById('message-input');
        const messageText = messageInput.value.trim();

        if (messageText !== '') {
            // Set the Authorization header for the STOMP send command
            stompClient.send('/app/chat.sendMessage', sendHeaders, JSON.stringify({ 'text': messageText }));
            messageInput.value = '';
        }
    });
});
