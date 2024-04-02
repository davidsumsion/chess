package ui;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ui.EscapeSequences.*;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ChessBoardUI {

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        Collection<ChessMove> arg3 = null;
        Gson gson = new Gson();
        if (args.length > 2) {
            Type collectionType = new TypeToken<Collection<ChessMove>>(){}.getType();
            arg3 = gson.fromJson(args[2], collectionType);
        }
        drawChessBoard(out, args[0], gson.fromJson(args[1], ChessBoard.class), arg3);
    }

    private static void drawChessBoard(PrintStream out, String color, ChessBoard chessBoard, Collection<ChessMove> highlightedMoves) {
        Boolean reverse = true;
        if (color.equals("BLACK")) { reverse = false; }
        setBackGroundBlack(out);
        drawHeaders(out, reverse);
        drawRows(out, chessBoard, reverse, highlightedMoves);
        drawHeaders(out, reverse);
//        setBackGroundBlack(out);
        resetBackGround(out);

    }

    private static void printIndex(PrintStream out, int i, Boolean reverse){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        if (!reverse) {
            out.print(i + 1);
        } else {
            out.print(reverseMap.get(i) + 1);
        }

        out.print(" ");
        setBackGroundBlack(out);
    }

    private static Collection<ChessMove> reverseMoves(Collection<ChessMove> highlightMoves){    //, Boolean reverseRows
        Collection<ChessMove> reverseMoves = new HashSet<>();
        for (ChessMove chessMove : highlightMoves) {
            ChessPosition startPosition = chessMove.getStartPosition();
            ChessPosition endPosition = chessMove.getEndPosition();
            Integer startRow = startPosition.getRow();
            Integer startCol = startPosition.getColumn();
            Integer endRow = endPosition.getRow();
            Integer endCol = endPosition.getColumn();
            ChessPosition reversedStartPos = new ChessPosition(reverseMap.get(startRow), reverseMap.get(startCol));
            ChessPosition reversedEndPos = new ChessPosition(reverseMap.get(endRow), reverseMap.get(endCol));
            reverseMoves.add(new ChessMove(reversedStartPos, reversedEndPos, chessMove.getPromotionPiece()));
        }
        return reverseMoves;
    }

    private static Boolean highlightBool(Integer row, Integer col, Collection<ChessMove> moves){
        for (ChessMove chessMove: moves) {
            ChessPosition endPosition = chessMove.getEndPosition();
            if (endPosition.getRow() == row && endPosition.getColumn() == col){
                return true;
            }
        }
        return false;
    }
    private static void drawRows(PrintStream out, ChessBoard chessBoard, Boolean reverse, Collection<ChessMove> moves) {
        Collection<ChessMove> highlightedMoves = (reverse && moves != null) ? reverseMoves(moves) : moves;
        String color = SET_BG_COLOR_BLACK;
        for (int boardRow = 0; boardRow < 8; ++boardRow) {
            out.print(EMPTY);
            printIndex(out, boardRow, reverse);
            color = setTileColor(color, out, false);
            for (int boardColumn = 7; boardColumn >= 0; --boardColumn){
                Boolean highlightIndicator = false;
                if (moves != null) {
                    highlightIndicator = highlightBool(boardRow, boardColumn, highlightedMoves);
                }
                color = setTileColor(color, out, highlightIndicator);
                out.print(getPieceName(chessBoard, boardRow, boardColumn, reverse));
            }
            setBackGroundBlack(out);
            out.print(" ");
            printIndex(out, boardRow, reverse);
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

    private static String setTileColor(String color, PrintStream out, Boolean highlight){
        if (Objects.equals(color, SET_BG_COLOR_BLUE) || Objects.equals(color, SET_BG_COLOR_WHITE)) {
            if (highlight) setBackGroundGreen(out);
            else setBackGroundLightGrey(out);
            return SET_BG_COLOR_LIGHT_GREY;
        } else {
            if (highlight) setBackGroundWhite(out);
            setBackGroundBlue(out);
            return SET_BG_COLOR_BLUE;
        }
    }

    private  static void drawHeaders(PrintStream out, Boolean reverse) {
        String[] headers = { EMPTY, "A", "B", "C", "D", "E", "F", "G", "H", EMPTY};
        if (!reverse) {
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

    private static void setBackGroundGreen(PrintStream out){
        out.print(SET_BG_COLOR_GREEN);
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }

    private static void setBackGroundBlue(PrintStream out){
        out.print(SET_BG_COLOR_BLUE);
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }

    private static void resetBackGround(PrintStream out){
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.print(SET_TEXT_COLOR_WHITE);
    }


    private static ChessBoard getDummyData(){
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.resetBoard();
        return chessBoard;
    }


}
