# Netty-socketio Demo

Demo for [Netty-socketio](https://github.com/mrniko/netty-socketio) project.

# Usage example

1. Build or install Netty-socketio lib to your maven repository.
   `mvn clean install`

2. Switch to /server folder and build server by maven.

3. Run server by command
   `mvn exec:java`

4. Run client in browser, by opening* file /client/index.html

# Note about Chrome and IE browsers
 If you want to open index.html in Chrome or IE browser you need to host it somewhere (nginx or apache, for example),
 or page will not work due to absence of correct "origin" http header.

# Demo scenarios

By default you will run a chat which communcate with server via json objects.
