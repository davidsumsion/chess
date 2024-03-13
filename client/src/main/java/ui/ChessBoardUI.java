package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.EscapeSequences.*;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;

public class ChessBoardUI {

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
        ChessBoard chessBoard = getDummyData();
        drawRows(out, chessBoard);

        drawHeaders(out);
        setBackGroundBlack(out);
    }

    private static void printIndex(PrintStream out, int i){
        out.print(EMPTY.repeat(1));
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(i + 1);
        out.print(EMPTY.repeat(1));
        setBackGroundBlack(out);

    }

    private static void drawRows(PrintStream out, ChessBoard chessBoard) {
        String color = SET_BG_COLOR_BLACK;
        for (int boardRow = 0; boardRow < 8; ++boardRow) {
            printIndex(out, boardRow);
            color = setTileColor(color, out);
            for (int boardColumn = 0; boardColumn < 8; ++boardColumn){
                color = setTileColor(color, out);
                out.print(WHITE_KING);
//                out.print(EMPTY.repeat(1));

            }
//            out.print(" " + boardRow + " ");
            out.println();
//            drawRowOfSquares(out, boardRow, chessBoard);

//            if (boardRow < 8 - 1) {
////                drawVerticalLine(out);
////                setBlack(out);
//            }
        }
    }

    private static String setTileColor(String color, PrintStream out){
        if (Objects.equals(color, SET_BG_COLOR_BLACK)) {
            setBackGroundWhite(out);
            return SET_BG_COLOR_WHITE;
        } else {
            setBackGroundBlack(out);
            return SET_BG_COLOR_BLACK;
        }

    }
//    private static void drawRowOfSquares(PrintStream out, int boardRow, ChessBoard chessBoard) {
//
//    }

    private  static void drawHeaders(PrintStream out) {
        String[] headers = { EMPTY, "A", "B", "C", "D", "E", "F", "G", "H", EMPTY};
        out.print(EMPTY.repeat(1));
        for (int boardCol = 0; boardCol < 10; ++boardCol) {
            drawHeader(out, headers[boardCol]);
            if (boardCol < 10 - 1) {
                out.print("  ");
            }
        }
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = 1 / 2;
        int suffixLength = 1 - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);

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


    private static ChessBoard getDummyData(){
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.resetBoard();
        return chessBoard;
    }


}
