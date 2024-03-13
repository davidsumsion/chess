package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoard {

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(EscapeSequences.ERASE_SCREEN);

        drawChessBoard(out);

//        out.print(SET_BG_COLOR_BLACK);
//        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawChessBoard(PrintStream out) {
        setBackGroundBlack(out);
        drawHeaders(out);


//        drawFooter(out);
        setBackGroundBlack(out);
    }


    private  static void drawHeaders(PrintStream out) {
        String[] headers = { "A", "B", "C", "D", "E", "F", "G", "H"};
        for (int boardCol = 0; boardCol < 8; ++boardCol) {
            drawHeader(out, headers[boardCol]);
            if (boardCol < 8 - 1) {
                out.print(EMPTY.repeat(1));
            }
        }
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = 3 / 2;
        int suffixLength = 3 - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBackGroundBlack(out);
    }

    private static void setBackGroundBlack(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void setBackGroundWhite(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_WHITE);
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }




}
