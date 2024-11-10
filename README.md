# Java Socket Programming - Messaging Application

This project demonstrates a simple messaging application implemented in Java using socket programming. The application includes a **Client-Server architecture** where a server communicates with one client over a socket connection. It provides a console-based messaging system with options to manage messages, including sending, editing, finding, and sorting.

## Features

- **Bidirectional Messaging:** Allows the client and server to send messages to each other.
- **Message Management:** Options to edit, delete, find, and display messages.
- **Sorting and Filtering:** Sort messages by ID, time, or content.
- **Multi-language Support:** Message formats available in English and Roman.

## Project Structure

- `Client`: The client-side application that establishes a connection to the server and handles user interactions.
- `Server`: The server-side application that listens for incoming client connections and processes requests.
- `SMS`: Represents a message object with attributes like message ID, content, timestamp, and edit status.
- `SMSapp`: Manages a collection of messages and provides functionalities for manipulating messages.
- `MenuOption (Enum)`: Represents the different options in the client and server menus.
- `EnglishSMS` and `RomanSMS`: Subclasses of `SMS` representing messages in English and Roman.
- **Comparators**: Custom comparators for sorting messages by ID, time, or content.

### Running the Application

1. **Start the Server**: Run the server first to listen for incoming connections.

   ```bash
   java SMSSocketApp.Server
   ```

2. **Start the Client**: In a separate terminal window, run the client.

   ```bash
   java SMSSocketApp.Client
   ```

   The client will prompt for the server's IP address. Enter the appropriate IP address or `localhost` if running locally.

## Usage Instructions

After establishing the client-server connection, both the client and server can use the following options from their respective menus.

### Client and Server Menu Options

| Option | Description                           |
|--------|---------------------------------------|
| 1      | **SEND MESSAGE**: Sends a new message to the connected entity (server or client) |
| 2      | **DISPLAY MESSAGES**: Lists all sent and received messages |
| 3      | **FIND BY ID/CONTENT**: Searches for messages by ID or content |
| 4      | **EDIT BY ID**: Edits a message by its ID |
| 5      | **DELETE BY ID/CONTENT**: Deletes messages by ID or content |
| 6      | **SORT-BY-ID**: Sorts messages by their unique ID |
| 7      | **SORT-BY-TIME**: Sorts messages by the timestamp of sending |
| 8      | **SORT-BY-CONTENT**: Sorts messages alphabetically by content |
| 9      | **EXIT**: Closes the client or server application |

### `MenuOption` Enum

The `MenuOption` enum simplifies the menu handling by mapping numeric choices to specific actions:

The `getValueOf(int choice)` method converts the user’s input (an integer) into a `MenuOption` value, allowing for simpler switch-case handling in both the `Client` and `Server` classes.

### Message Format Options

When sending a message, you can choose a language:

- **English**: Standard English message
- **Roman**: Romanized version (or transliteration) of message content

### Example of Message Sending

1. Choose option `1` to send a message.
2. Enter the language choice (e.g., "English" or "Roman").
3. Enter the message content.
4. The message is added to the `SMSapp` message list and sent over the socket connection.

### Message Storage and Sorting

All sent and received messages are stored in a list within the `SMSapp` class. They can be sorted using:

- **ID Comparator**: Sorts messages by their unique ID.
- **Time Comparator**: Sorts messages by the timestamp.
- **Content Comparator**: Sorts messages alphabetically by content.

## Technical Details

- **Socket Communication**: The client and server communicate via sockets on a specified port (default is `1234`). The server listens for incoming connections, and the client initiates a connection to the server.
- **Multithreading**:
  - Each client and server run their messaging in separate threads to handle incoming and outgoing messages concurrently.
- **SMS Management**: `SMSapp` manages operations on the message list, including adding, editing, deleting, and displaying messages.
- **Message Receivers**: Separate `MessageReceiver` threads handle incoming messages for both client and server to ensure the UI is responsive for user input.

## Error Handling

- **Connection Errors**: Errors during connection (e.g., invalid IP or unavailable server) are captured, and a descriptive message is displayed to the user.
- **Invalid Input**: Menu selection and other user inputs are validated, and invalid inputs trigger prompts to retry.

## Sample Output

### Client Initialization

```
****************************
THE CLIENT INITIALIZATION!
****************************
Attempting to connect the server!
****************************
Enter the Server IP to connect:
```

### Server Initialization

```
****************************
THE SERVER INITIALIZATION!
****************************
Server is Waiting for client!
****************************
Connected to the Client with PORT: 1234...
```

## Future Enhancements

- **Support for Multiple Clients**: Expand to handle multiple clients in a single server instance.
- **Persistent Storage**: Add functionality to save messages to a database or file.
- **Encryption**: Implement encryption for secure message transfer.
