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
