

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

        // Subscribe to the topic to receive new messages
        stompClient.subscribe('/topic/webs-topic', function (message) {
            const newMessage = JSON.parse(message.body);
            displayNewMessage(newMessage);
        });

        // Ask for chat history
        stompClient.send('/app/chat.subscribe', sendHeaders);
    });

    socket.onerror = function (event) {
        localStorage.removeItem('jwtToken');
        alert('Authentication error. Please login again.');
        window.location.href = 'login.html';
    };

    socket.onclose = function (event) {
        localStorage.removeItem('jwtToken');
        alert('Authentication error. Please login again.');
        window.location.href = 'login.html';
    };

    function displayChatHistory(chatHistory) {
        console.log("got history:" + chatHistory);
        const chatBox = document.getElementById('chat-box');
        chatBox.innerHTML = '';

        chatHistory.forEach(message => {
            const messageElement = document.createElement('div');
            messageElement.textContent = `${message.sender.username}: ${message.text}`;
            chatBox.appendChild(messageElement);
        });
    }

    function displayNewMessage(message) {
        const chatBox = document.getElementById('chat-box');
        const messageElement = document.createElement('div');
        messageElement.textContent = `${message.sender.username}: ${message.text}`;
        chatBox.appendChild(messageElement);
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
