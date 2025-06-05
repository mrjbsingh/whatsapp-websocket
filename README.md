# WhatsApp-like WebSocket Chat Application

A real-time chat application built with Spring Boot WebSocket and basic HTML/CSS/JavaScript frontend, similar to WhatsApp functionality.

## Features

- Real-time messaging using WebSocket
- User join/leave notifications
- Multiple users can chat simultaneously
- WhatsApp-like UI design
- Responsive design for mobile and desktop

## Technology Stack

### Backend
- Java 11
- Spring Boot 2.7.0
- Spring WebSocket
- Maven

### Frontend
- HTML5
- CSS3
- JavaScript (ES5)
- SockJS (WebSocket fallback)
- STOMP (messaging protocol)

## Project Structure

```
whatsapp-websocket-chat/
├── src/
│   └── main/
│       ├── java/com/chat/
│       │   ├── ChatApplication.java          # Main Spring Boot application
│       │   ├── config/
│       │   │   └── WebSocketConfig.java      # WebSocket configuration
│       │   ├── controller/
│       │   │   ├── ChatController.java       # Message handling controller
│       │   │   └── WebSocketEventListener.java # WebSocket event listener
│       │   └── model/
│       │       └── ChatMessage.java          # Message model
│       └── resources/
│           ├── static/
│           │   ├── index.html               # Main HTML page
│           │   ├── style.css                # CSS styling
│           │   └── main.js                  # JavaScript WebSocket client
│           └── application.properties        # Spring Boot configuration
├── pom.xml                                  # Maven dependencies
└── README.md                                # This file
```

## How to Run

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Steps

1. **Clone or navigate to the project directory:**
   ```bash
   cd whatsapp-websocket-chat
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application:**
   Open your browser and go to: `http://localhost:8080`

## How to Test

1. **Single Browser Testing:**
   - Open `http://localhost:8080`
   - Enter a username and start chatting
   - Open another tab/window with the same URL
   - Enter a different username
   - Start chatting between the two tabs

2. **Multiple Browser Testing:**
   - Open the application in different browsers (Chrome, Firefox, Safari)
   - Use different usernames
   - Test real-time messaging between browsers

3. **Mobile Testing:**
   - Access `http://localhost:8080` from your mobile device
   - The UI is responsive and should work well on mobile

## WebSocket Implementation Details

### Backend Components

1. **WebSocketConfig.java:**
   - Configures STOMP messaging
   - Sets up message broker with `/topic` prefix
   - Registers WebSocket endpoint at `/ws`

2. **ChatController.java:**
   - Handles incoming messages via `@MessageMapping`
   - Broadcasts messages to all connected clients via `@SendTo`

3. **WebSocketEventListener.java:**
   - Handles user connect/disconnect events
   - Notifies other users when someone leaves

4. **ChatMessage.java:**
   - Message model with sender, content, type, and timestamp
   - Supports CHAT, JOIN, and LEAVE message types

### Frontend Components

1. **main.js:**
   - Establishes WebSocket connection using SockJS
   - Uses STOMP protocol for messaging
   - Handles message sending and receiving
   - Updates UI dynamically

2. **index.html:**
   - Username entry form
   - Chat interface with message area
   - Message input form

3. **style.css:**
   - WhatsApp-inspired design
   - Responsive layout
   - Green color scheme (#128C7E, #25D366)

## Key Learning Points

### WebSocket Concepts
- **Real-time Communication:** Bidirectional communication between client and server
- **STOMP Protocol:** Simple Text Oriented Messaging Protocol over WebSocket
- **SockJS:** WebSocket fallback for browsers that don't support WebSocket
- **Message Broker:** Routes messages between clients

### Spring Boot WebSocket
- **@EnableWebSocketMessageBroker:** Enables WebSocket message handling
- **@MessageMapping:** Maps incoming messages to handler methods
- **@SendTo:** Broadcasts messages to specific destinations
- **SimpMessageSendingOperations:** Programmatically send messages

## Possible Enhancements

1. **Private Messaging:** Add one-to-one chat functionality
2. **Message Persistence:** Store messages in database
3. **User Authentication:** Add login/registration
4. **File Sharing:** Support image and file uploads
5. **Message Status:** Read receipts and delivery status
6. **Emoji Support:** Add emoji picker
7. **Chat Rooms:** Multiple chat rooms/groups
8. **Message History:** Load previous messages on join

## Troubleshooting

1. **Port 8080 already in use:**
   - Change port in `application.properties`: `server.port=8081`

2. **WebSocket connection fails:**
   - Check if firewall is blocking the connection
   - Ensure browser supports WebSocket

3. **Messages not appearing:**
   - Check browser console for JavaScript errors
   - Verify WebSocket connection status

## API Endpoints

- **WebSocket Endpoint:** `/ws`
- **Message Destinations:**
  - Send: `/app/chat.sendMessage`
  - Subscribe: `/topic/public`
  - Add User: `/app/chat.addUser`

This project provides a solid foundation for understanding WebSocket communication and can be extended with additional features as needed.

# 🔍 **Complete Code Flow Analysis - WhatsApp WebSocket Chat**

Let me walk you through the entire code flow from when a user opens the browser to when messages are exchanged between multiple users.

## **📋 Table of Contents**
1. [Application Startup Flow](#startup)
2. [User Connection Flow](#connection)
3. [Message Sending Flow](#sending)
4. [Message Receiving Flow](#receiving)
5. [User Disconnection Flow](#disconnection)
6. [Technical Architecture](#architecture)

---

## **🚀 1. Application Startup Flow** {#startup}

### **Step 1: Spring Boot Application Starts**
```java
// ChatApplication.java
@SpringBootApplication
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
}
```

**What happens:**
1. Spring Boot scans for `@Configuration` classes
2. Finds `WebSocketConfig.java` with `@EnableWebSocketMessageBroker`
3. Sets up WebSocket infrastructure

### **Step 2: WebSocket Configuration Initialization**
```java
// WebSocketConfig.java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");           // Creates message broker
        config.setApplicationDestinationPrefixes("/app"); // Sets message routing prefix
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();      // Creates WebSocket endpoint
    }
}
```

**What happens:**
1. **Message Broker Setup**: Creates in-memory broker for `/topic/*` destinations
2. **Endpoint Registration**: Registers `/ws` as WebSocket endpoint with SockJS fallback
3. **STOMP Configuration**: Sets up STOMP protocol over WebSocket

### **Step 3: Controllers Registration**
```java
// ChatController.java - Spring automatically registers these mappings
@MessageMapping("/chat.sendMessage")  // Maps to /app/chat.sendMessage
@MessageMapping("/chat.addUser")      // Maps to /app/chat.addUser
```

**Server is now ready to accept WebSocket connections on `ws://localhost:8080/ws`**

---

## **🔌 2. User Connection Flow** {#connection}

### **Step 1: User Opens Browser**
```html
<!-- index.html loads -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script src="main.js"></script>
```

### **Step 2: User Enters Username and Clicks "Start Chatting"**
```javascript
// main.js - connect() function triggered
function connect(event) {
    username = document.querySelector('#name').value.trim();
    
    if(username) {
        // Hide username page, show chat page
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        // Create WebSocket connection
        var socket = new SockJS('/ws');           // 1. SockJS connection
        stompClient = Stomp.over(socket);         // 2. STOMP over SockJS

        // Connect with callbacks
        stompClient.connect({}, onConnected, onError);
    }
}
```

### **Step 3: WebSocket Handshake (Network Level)**
```
Browser → Server: HTTP Upgrade Request
GET /ws/683/abc123/websocket HTTP/1.1
Upgrade: websocket
Connection: Upgrade

Server → Browser: HTTP 101 Switching Protocols
HTTP/1.1 101 Switching Protocols
Upgrade: websocket
Connection: Upgrade
```

### **Step 4: STOMP Connection Established**
```javascript
// main.js - onConnected() callback
function onConnected() {
    // Subscribe to public topic for receiving messages
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Send JOIN message to server
    stompClient.send("/app/chat.addUser", {}, 
        JSON.stringify({sender: username, type: 'JOIN'})
    );

    connectingElement.classList.add('hidden');
}
```

### **Step 5: Server Processes JOIN Message**
```java
// ChatController.java
@MessageMapping("/chat.addUser")
@SendTo("/topic/public")
public ChatMessage addUser(@Payload ChatMessage chatMessage,
                           SimpMessageHeaderAccessor headerAccessor) {
    // Store username in WebSocket session
    headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
    return chatMessage;  // This gets broadcast to /topic/public
}
```

### **Step 6: JOIN Message Broadcast**
```javascript
// All connected clients receive this message
function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    
    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
        // Display "User1 joined!" in chat
    }
}
```

**🔄 Connection Complete: User is now connected and can send/receive messages**

---

## **📤 3. Message Sending Flow** {#sending}

### **Step 1: User Types Message and Clicks Send**
```javascript
// main.js - sendMessage() function triggered
function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };
        
        // Send message via WebSocket (NOT HTTP!)
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';  // Clear input field
    }
    event.preventDefault();
}
```

### **Step 2: STOMP Frame Transmission**
```
Browser → Server (WebSocket Frame):
SEND
destination:/app/chat.sendMessage
content-length:93

{"sender":"User1","content":"Hello World!","type":"CHAT"}
```

### **Step 3: Server Receives and Processes Message**
```java
// ChatController.java
@MessageMapping("/chat.sendMessage")
@SendTo("/topic/public")
public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
    // Spring automatically:
    // 1. Deserializes JSON to ChatMessage object
    // 2. Calls this method
    // 3. Serializes return value to JSON
    // 4. Sends to all subscribers of /topic/public
    
    return chatMessage;  // Broadcast to ALL connected clients
}
```

### **Step 4: Message Broadcast to All Clients**
```
Server → All Browsers (WebSocket Frames):
MESSAGE
destination:/topic/public
content-type:application/json
subscription:sub-0
message-id:abc-123

{"content":"Hello World!","sender":"User1","receiver":null,"type":"CHAT","timestamp":null}
```

---

## **📥 4. Message Receiving Flow** {#receiving}

### **Step 1: All Connected Clients Receive Message**
```javascript
// main.js - onMessageReceived() triggered for ALL connected clients
function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    var messageElement = document.createElement('li');

    if(message.type === 'CHAT') {
        messageElement.classList.add('chat-message');

        // Create avatar with first letter of sender name
        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        // Create username element
        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);

        messageElement.appendChild(avatarElement);
        messageElement.appendChild(usernameElement);
    }

    // Create message content
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);
    messageElement.appendChild(textElement);

    // Add to chat area and scroll to bottom
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}
```

### **Step 2: UI Updates in Real-time**
- Message appears in chat area with avatar and username
- Chat scrolls to show latest message
- Input field remains ready for next message

---

## **🔌 5. User Disconnection Flow** {#disconnection}

### **Step 1: User Closes Browser/Tab**
```javascript
// Browser automatically closes WebSocket connection
// No explicit JavaScript needed
```

### **Step 2: Server Detects Disconnection**
```java
// WebSocketEventListener.java
@EventListener
public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    
    String username = (String) headerAccessor.getSessionAttributes().get("username");
    if(username != null) {
        logger.info("User Disconnected : " + username);

        // Create LEAVE message
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.LEAVE);
        chatMessage.setSender(username);

        // Broadcast to all remaining connected clients
        messagingTemplate.convertAndSend("/topic/public", chatMessage);
    }
}
```

### **Step 3: LEAVE Message Broadcast**
```javascript
// All remaining clients receive LEAVE message
function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    
    if(message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
        // Display "User1 left!" in chat
    }
}
```

---

## **🏗️ 6. Technical Architecture** {#architecture}

### **Component Interaction Diagram**
```
┌─────────────────┐    WebSocket     ┌─────────────────┐
│   Browser 1     │◄────────────────►│                 │
│   (User1)       │     /ws          │                 │
└─────────────────┘                  │                 │
                                     │   Spring Boot   │
┌─────────────────┐    WebSocket     │     Server      │
│   Browser 2     │◄────────────────►│                 │
│   (User2)       │     /ws          │                 │
└─────────────────┘                  └─────────────────┘
```

### **Message Flow Architecture**
```
Frontend (main.js)
    ↓ stompClient.send()
SockJS/STOMP Layer
    ↓ WebSocket Frame
Spring WebSocket Handler
    ↓ @MessageMapping
ChatController.sendMessage()
    ↓ @SendTo("/topic/public")
Message Broker
    ↓ Broadcast
All Subscribed Clients
    ↓ onMessageReceived()
UI Update
```

### **Key Design Patterns Used**

1. **Observer Pattern**: Clients subscribe to `/topic/public`
2. **Publisher-Subscriber**: Server publishes to topic, clients subscribe
3. **Event-Driven**: WebSocket events trigger handlers
4. **MVC Pattern**: Controller handles message routing
5. **Dependency Injection**: Spring manages WebSocket components

### **Threading Model**
```
Main Thread: Spring Boot Application
    ├── WebSocket Thread Pool: Handles connections
    ├── Message Broker Thread: Processes STOMP messages  
    ├── Inbound Channel: Receives messages from clients
    └── Outbound Channel: Sends messages to clients
```

### **Memory Management**
- **Session Storage**: Username stored in WebSocket session attributes
- **Message Broker**: In-memory broker (messages not persisted)
- **Connection Pool**: Spring manages WebSocket connection lifecycle

This architecture enables real-time, bidirectional communication between multiple clients through a single persistent connection per client, making it highly efficient for chat applications!