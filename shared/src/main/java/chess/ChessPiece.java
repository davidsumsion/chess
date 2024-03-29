package chess;

import java.util.*;

//import static chess.ChessGame.TeamColor.BLACK;
//import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable {
    private PieceType pieceType;
    private ChessGame.TeamColor teamColor;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.teamColor = pieceColor;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }


    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
    return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }


    /* Notes
    Implement .equals
     */
    public  Collection<ChessMove> verifyMoves(Collection<ChessMove> potMoves, ChessBoard board){
        Set<ChessMove> moves = new HashSet<>();
        for (ChessMove potPieceMove : potMoves){
            ChessPosition pos = potPieceMove.getEndPosition();
            ChessPiece piece = board.getPiece(pos);
            if (piece == null) {
                moves.add(potPieceMove);
            } else if (piece.getTeamColor() != teamColor) {
                moves.add(potPieceMove);
            }
        }
        return moves;
    }

    public Boolean breaker(Integer i, Integer j, ChessBoard board){
        ChessPosition pos = new ChessPosition(i,j);
        ChessPiece piece = board.getPiece(pos);
        if (piece == null){
            return false;
        } else if (piece.getTeamColor() != teamColor) {
            return true;
        } else if (piece.getTeamColor() == teamColor) {
            return true;
        }
        return false;
    }
    public ChessMove bishopHelper(Integer i, Integer j, ChessBoard board, ChessPosition myPosition) {
        ChessPosition pos = new ChessPosition(i,j);
        ChessPiece piece = board.getPiece(pos);
        ChessMove potMov = new ChessMove(myPosition, pos, null);
        if (piece == null) {
            return potMov;
        } else if (piece.getTeamColor() != teamColor) {
            return potMov;
        } else if (piece.getTeamColor() == teamColor) {
            return null;
        }
        return null;
    }
    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> moves = new HashSet<>();

        //up & right
        for (int i = myPosition.getRow()+1, j = myPosition.getColumn()+1; i <= 8 && j <= 8; i++, j++) {
            ChessMove chessMove = bishopHelper(i, j, board, myPosition);
            if (chessMove != null) { moves.add(chessMove); }
            if (breaker(i, j, board)) { break; }
        }
        //down & right
        for (int i = myPosition.getRow()-1, j = myPosition.getColumn()+1; i > 0 && j <= 8; i--, j++) {
            ChessMove chessMove = bishopHelper(i, j, board, myPosition);
            if (chessMove != null) { moves.add(chessMove); }
            if (breaker(i, j, board)) { break; }
        }
        //down & left
        for (int i = myPosition.getRow()-1, j = myPosition.getColumn()-1; i > 0 && j > 0; i--, j--) {
            ChessMove chessMove = bishopHelper(i, j, board, myPosition);
            if (chessMove != null) { moves.add(chessMove); }
            if (breaker(i, j, board)) { break; }
        }
        //up and left
        for (int i = myPosition.getRow()+ 1, j = myPosition.getColumn()-1; i <= 8 && j > 0; i++, j--) {
            ChessMove chessMove = bishopHelper(i, j, board, myPosition);
            if (chessMove != null) { moves.add(chessMove); }
            if (breaker(i, j, board)) { break; }
        }
        return moves;
    }
    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> potMoves = new HashSet<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        //3 spots above king
        if (myRow != 8) {
            for (int i = myCol-1; i < myCol+2; i++){
                if (i < 9 && i > 0){ potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow+1, i), null));}
            }
        }
        //3 spots below king
        if (myRow != 1){
            for (int i = myCol-1; i < myCol+2; i++){
                if (i < 9 && i > 0){ potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow-1, i), null));}
            }
        }
        //left & right spots
        if (myCol != 1) { potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow, myCol-1), null)); }
        if (myCol != 8) { potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow, myCol+1), null)); }
        return verifyMoves(potMoves, board);
    }
    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> potMoves = new HashSet<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        if (myRow<=6 && myCol<=7) { potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow+2, myCol+1), null));}
        if (myRow<=6 && myCol>=2) { potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow+2, myCol-1), null));}
        if (myRow>=3 && myCol<=7) { potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow-2, myCol+1), null));}
        if (myRow>=3 && myCol>=2) { potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow-2, myCol-1), null));}
        if (myRow<=7 && myCol<=6) { potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow+1, myCol+2), null));}
        if (myRow>=2 && myCol<=6) { potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow-1, myCol+2), null));}
        if (myRow<=7 && myCol>=3) { potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow+1, myCol-2), null));}
        if (myRow>=2 && myCol>=3) { potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow-1, myCol-2), null));}

        return verifyMoves(potMoves, board);
    }

    public  ChessMove rookMovesHelper(Integer i, Integer j, ChessBoard board, ChessPosition myPosition) {
        ChessPosition pos = new ChessPosition(i, j);
        ChessPiece piece = board.getPiece(pos);
        ChessMove potMov = new ChessMove(myPosition, pos, null);
        if (piece == null) {
            return  potMov;
        } else if (piece.getTeamColor() != teamColor) {
            return  potMov;
        } else if (piece.getTeamColor() == teamColor) {
            return null;
        }
        return null;
    }


    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        //up
        for (int i = myRow+1; i <= 8; i++) {
            ChessMove chessMove = rookMovesHelper(i, myCol, board, myPosition);
            if (chessMove != null) { moves.add(chessMove); }
            if (breaker(i, myCol, board)) { break; }
        }
        //down
        for (int i = myRow-1; i >= 1; i--) {
            ChessMove chessMove = rookMovesHelper(i, myCol, board, myPosition);
            if (chessMove != null) { moves.add(chessMove); }
            if (breaker(i, myCol, board)) { break; }
        }
        //right
        for (int i = myCol+1; i <= 8; i++) {
            ChessMove chessMove = rookMovesHelper(myRow, i, board, myPosition);
            if (chessMove != null) { moves.add(chessMove); }
            if (breaker(myRow, i, board)) { break; }
        }
        //left
        for (int i = myCol-1; i >= 1; i--) {
            ChessMove chessMove = rookMovesHelper(myRow, i, board, myPosition);
            if (chessMove != null) { moves.add(chessMove); }
            if (breaker(myRow, i, board)) { break; }
        }
        return moves;
    }
    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<>(bishopMoves(board, myPosition));
        moves.addAll(rookMoves(board, myPosition));
        return moves;
    }
    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> potMoves = new HashSet<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        ChessPiece.PieceType[] types = {QUEEN, ROOK, KNIGHT, BISHOP};
        if (teamColor.equals(WHITE)) {
            if (myRow < 7) { // one forward
                if (board.getPiece(new ChessPosition(myRow+1, myCol)) == null){
                    potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol), null)); // two forward (if in start position)
                    if (myRow == 2) {
                        if (board.getPiece(new ChessPosition(myRow+2, myCol)) == null){
                            potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow + 2, myCol), null));
                        }
                    }
                } // forward/left
                ChessPiece leftPiece = null;
                if (myCol > 1) {leftPiece = board.getPiece(new ChessPosition(myRow + 1, myCol - 1));}
                if (leftPiece != null && leftPiece.getTeamColor() != teamColor) {
                    potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol - 1), null));
                } // forward/right
                ChessPiece rightPiece = null;
                if (myCol < 8) { rightPiece = board.getPiece(new ChessPosition(myRow + 1, myCol + 1)); }
                if (rightPiece != null && rightPiece.getTeamColor() != teamColor) {
                    potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol + 1), null));
                }
            } // 7th row note 8 not included -- PROMOTION -- return 4 of the same coordinate
            if (myRow == 7) { //forward
                if (board.getPiece(new ChessPosition(myRow+1, myCol)) == null) {
                    for (int i = 0; i < 4; i++){
                        potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow+1, myCol),types[i]));
                    }
                } //forward/right
                if (board.getPiece(new ChessPosition(myRow+1, myCol+1)) != null) {
                    if (board.getPiece(new ChessPosition(myRow + 1, myCol + 1)).getTeamColor() != teamColor) {
                        for (int i = 0; i < 4; i++) {
                            potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol + 1), types[i]));
                        }
                    }
                } //forward/left
                if (board.getPiece(new ChessPosition(myRow+1, myCol-1)) != null) {
                    if (board.getPiece(new ChessPosition(myRow + 1, myCol - 1)).getTeamColor() != teamColor) {
                        for (int i = 0; i < 4; i++){
                            potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol - 1), types[i]));
                        }
                    }
                }
            }
        }
        if (teamColor.equals(BLACK)) {
            if (myRow > 2) { // one down
                if (board.getPiece(new ChessPosition(myRow-1, myCol)) == null){
                    potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow-1, myCol), null)); // two forward (if in start position)
                    if (myRow == 7) {
                        if (board.getPiece(new ChessPosition(myRow-2, myCol)) == null){
                            potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow-2, myCol), null));
                        }
                    }
                } // down/left
                ChessPiece leftPiece = null;
                if (myCol > 1) {leftPiece = board.getPiece(new ChessPosition(myRow - 1, myCol - 1));}
                if (leftPiece != null && leftPiece.getTeamColor() != teamColor) {
                    potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol - 1), null));
                } // down/right
                ChessPiece rightPiece = null;
                if (myCol < 8) { rightPiece = board.getPiece(new ChessPosition(myRow - 1, myCol + 1)); }
                if (rightPiece != null && rightPiece.getTeamColor() != teamColor) {
                    potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol + 1), null));
                }
            }
            if (myRow == 2) { //down
                if (board.getPiece(new ChessPosition(myRow-1, myCol)) == null) {
                    for (int i = 0; i < 4; i++){
                        potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow-1, myCol),types[i]));
                    }
                } //down/right
                if (board.getPiece(new ChessPosition(myRow-1, myCol+1)) != null) {
                    if (board.getPiece(new ChessPosition(myRow - 1, myCol + 1)).getTeamColor() != teamColor) {
                        for (int i = 0; i < 4; i++) {
                            potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol + 1), types[i]));
                        }
                    }
                } //down/left
                if (board.getPiece(new ChessPosition(myRow-1, myCol-1)) != null) {
                    if (board.getPiece(new ChessPosition(myRow - 1, myCol - 1)).getTeamColor() != teamColor) {
                        for (int i = 0; i < 4; i++){
                            potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol - 1), types[i]));
                        }
                    }
                }
            }
        }
        return potMoves;
    }
        /**
         * Calculates all the positions a chess piece can move to
         * Does not take into account moves that are illegal due to leaving the king in
         * danger
         *
         * @return Collection of valid moves
         */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (pieceType == BISHOP) { return bishopMoves(board, myPosition);}
        else if (pieceType == KING) { return kingMoves(board, myPosition);}
        else if (pieceType == KNIGHT) { return knightMoves(board, myPosition);}
        else if (pieceType == ROOK) { return rookMoves(board, myPosition); }
        else if (pieceType == QUEEN) { return queenMoves(board, myPosition);}
        else if (pieceType == PAWN) { return pawnMoves(board, myPosition); }

        return new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return Objects.equals(pieceType, that.pieceType) && Objects.equals(teamColor, that.teamColor);
//        return pieceType == that.pieceType && teamColor == that.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, teamColor);
    }

    @Override
    public String toString() {
        if (teamColor == null){
            return " " + "N" + "," + pieceType + ' ';
        } else if (teamColor == BLACK) {
            return "  B" + "," + pieceType + "  ";
        } else {
            return "  W" + "," + pieceType + "  ";
        }
    }
}
