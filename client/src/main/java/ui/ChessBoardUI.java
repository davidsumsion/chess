package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import static ui.EscapeSequences.*;

import chess.*;

public class ChessBoardUI {
    static Boolean reverse = true;

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(EscapeSequences.ERASE_SCREEN);

        drawChessBoard(out);
    }

    private static void drawChessBoard(PrintStream out) {
        ChessBoard chessBoard = getDummyData();

        setBackGroundBlack(out);
        drawHeaders(out);
        drawRows(out, chessBoard);
        drawHeaders(out);
        setBackGroundBlack(out);
    }

    private static void printIndex(PrintStream out, int i){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        if (reverse == false) {
            out.print(i + 1);
        } else {
            out.print(reverseMap.get(i) + 1);
        }

        out.print(" ");
        setBackGroundBlack(out);
    }

    private static void drawRows(PrintStream out, ChessBoard chessBoard) {
        //this is where you choose if you are printing which board

        String color = SET_BG_COLOR_BLACK;
        for (int boardRow = 0; boardRow < 8; ++boardRow) {
            out.print(EMPTY);
            printIndex(out, boardRow);
            color = setTileColor(color, out);
            for (int boardColumn = 7; boardColumn >= 0; --boardColumn){
                color = setTileColor(color, out);
                out.print(getPieceName(chessBoard, boardRow, boardColumn, reverse));
            }
            setBackGroundBlack(out);
            out.print(" ");
            printIndex(out, boardRow);
            out.println();
        }
    }

    final static Map<Integer, Integer> reverseMap = Map.of(
                0,7,
                1,6,
                2,5,
                3,4,
                4,3,
                5,2,
                6,1,
                7,0);

    private static String getPieceName(ChessBoard chessBoard, int boardRow, int boardColumn, Boolean reverse){
        if (reverse){
            boardRow = reverseMap.get(boardRow);
            boardColumn = reverseMap.get(boardColumn);
        }
        ChessPiece chessPiece = chessBoard.getPiece(new ChessPosition(boardRow + 1, boardColumn + 1));
        if (chessPiece == null) return EMPTY;
        ChessGame.TeamColor teamColor = chessPiece.getTeamColor();
        ChessPiece.PieceType pieceType = chessPiece.getPieceType();
        if (pieceType == ChessPiece.PieceType.KING && teamColor == ChessGame.TeamColor.WHITE) return WHITE_KING;
        if (pieceType == ChessPiece.PieceType.KING && teamColor == ChessGame.TeamColor.BLACK) return BLACK_KING;
        if (pieceType == ChessPiece.PieceType.QUEEN && teamColor == ChessGame.TeamColor.WHITE) return WHITE_QUEEN;
        if (pieceType == ChessPiece.PieceType.QUEEN && teamColor == ChessGame.TeamColor.BLACK) return BLACK_QUEEN;
        if (pieceType == ChessPiece.PieceType.BISHOP && teamColor == ChessGame.TeamColor.WHITE) return WHITE_BISHOP;
        if (pieceType == ChessPiece.PieceType.BISHOP && teamColor == ChessGame.TeamColor.BLACK) return BLACK_BISHOP;
        if (pieceType == ChessPiece.PieceType.KNIGHT && teamColor == ChessGame.TeamColor.WHITE) return WHITE_KNIGHT;
        if (pieceType == ChessPiece.PieceType.KNIGHT && teamColor == ChessGame.TeamColor.BLACK) return DARK_KNIGHT;
        if (pieceType == ChessPiece.PieceType.ROOK && teamColor == ChessGame.TeamColor.WHITE) return WHITE_ROOK;
        if (pieceType == ChessPiece.PieceType.ROOK && teamColor == ChessGame.TeamColor.BLACK) return BLACK_ROOK;
        if (pieceType == ChessPiece.PieceType.PAWN && teamColor == ChessGame.TeamColor.WHITE) return WHITE_PAWN;
        if (pieceType == ChessPiece.PieceType.PAWN && teamColor == ChessGame.TeamColor.BLACK) return BLACK_PAWN;
        return EMPTY;
    }

    private static String setTileColor(String color, PrintStream out){
        if (Objects.equals(color, SET_BG_COLOR_BLUE)) {
            setBackGroundLightGrey(out);
            return SET_BG_COLOR_LIGHT_GREY;
        } else {
            setBackGroundBlue(out);
            return SET_BG_COLOR_BLUE;
        }

    }

    private  static void drawHeaders(PrintStream out) {
        String[] headers = { EMPTY, "A", "B", "C", "D", "E", "F", "G", "H", EMPTY};
        if (reverse == false) {
            headers = new String[]{EMPTY, "H", "G", "F", "E", "D", "C", "B", "A", EMPTY};
        }

        out.print(" ");
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

    private static void setBackGroundLightGrey(PrintStream out){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }

    private static void setBackGroundBlue(PrintStream out){
        out.print(SET_BG_COLOR_BLUE);
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }


    private static ChessBoard getDummyData(){
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.resetBoard();
        return chessBoard;
    }


}
