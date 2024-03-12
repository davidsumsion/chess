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
- 