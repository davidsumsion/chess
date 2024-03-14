Phase 5
- Cosnole UI
  - formatting and text based graphics
  - Control codes
    - clear the terminal
    - set the curson locatio
    - set background oclor, text color for each character printed to the terminal
    - bold, faint, italic, blinking, underland
      - EscapeSequences (you can add whatever you want)
    - set a color then new line and then type the color you want
    - unicode characters slides -- chess peices
      - use capital letters, add more in if you want the unicode characters
        - /u2654 - white king
          - slides give it to you
          - spacing issue with these, start with capital letters
            - terminal font to monospace (console font)
            - /u2003 or /u2001 for when u use space (SEE SLIDES)
      - important to break your code down, draw square, draw horizontal bar
        - repl - read evaluate process loop
  - login menu and logged in menu
    - var is AuthToken, if null or not

ServerFascade -- put it somewhere other than the client
- LoginResponse login(LoginRequest)


phase 5
- draw board
- create menus
- access server
- write tests for server fascade
  - test that data is being sent back and forth correctly


Access Server
- 
HTTP Code examples, cut and paste directly into project


Client
- main
- manages methods
- just provides a menu
UIChessBoard client references (depends on UI)
- join game or observe game form client
- just draws a chess board
Server client references (depends on server)
Server Facade Class
- - WHAT YOU UNIT TEST
- login method
- logout method
- register method
- ... one for each endpoint
Client Comunicator (Server Facade depends on  Client Communicator depends on )
- Method for GET and POST
- inheritence only one function for GET/POST/DELETE




Logging
- important for
  - software devs
  - system admins
  - customer support agent
    - user gets an error, stack tracers don't help users, something went wrong contact customer support at this number
    - log the error, file in this place, customer support can fix or send it to Developers
  - Programs send log messages to "loggers"
  - Each message has a level
    - SEVER, WARNING, INFO, CONFIG, FINE, FINER, FINEST
      - shows everything at that level and everything below
      - never need to remove them
    - Logger.setLevel(level) method
    - types
      - ConsoleHandler -- sends messages to the console
      - FileHandler -- sends messages to a file
      - SocketHandler -- sends messages to a network socket
    - format
      - each handler has a formatter
        - default SimpleFormatter
        - XMLFormatter -- industry is leaving XML
        - Could write more
- Log4J is used more than the standard build in Logger
  -very similar to java.util.logging
- SFL4J sends your logs to Log4J or java.util.logging depending on configs
- 









