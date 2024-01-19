package chess;

import java.util.*;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessPiece.PieceType.BISHOP;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private PieceType pieceType;
    private ChessGame.TeamColor teamColor;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.teamColor = pieceColor;
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
    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> moves = new HashSet<>();
        System.out.println(myPosition);
        System.out.println(board);
        //up & right
        for (int i = myPosition.getRow()+1, j = myPosition.getColumn()+1; i <= 8 && j <= 8; i++, j++) {
            ChessPosition pos = new ChessPosition(i,j);
            ChessPiece piece = board.getPiece(pos);
            ChessMove potMov = new ChessMove(myPosition, pos, null);
            board.addPiece(new ChessPosition(i,j), new ChessPiece(BLACK, BISHOP));
            //if another piece is not there
            //else if not empty and piece is opposing teams
            //else (all other cases: if empty and my team)
            if (piece == null) {
                moves.add(potMov);
            } else if (piece.getTeamColor() != teamColor) {
                moves.add(potMov);
            }
        }
        //down & right
        for (int i = myPosition.getRow()-1, j = myPosition.getColumn()+1; i > 0 && j <= 8; i--, j++) {
            ChessPosition pos = new ChessPosition(i,j);
            ChessPiece piece = board.getPiece(pos);
            ChessMove potMov = new ChessMove(myPosition, pos, null);
            board.addPiece(new ChessPosition(i,j), new ChessPiece(BLACK, BISHOP));

            //if another piece is not there
            //else if not empty and piece is opposing teams
            //else (all other cases: if empty and my team)
            if (piece == null) {
                moves.add(potMov);
            } else if (piece.getTeamColor() != teamColor) {
                moves.add(potMov);
            }
        }
        //down & left
        for (int i = myPosition.getRow()-1, j = myPosition.getColumn()-1; i > 0 && j > 0; i--, j--) {
            ChessPosition pos = new ChessPosition(i,j);
            ChessPiece piece = board.getPiece(pos);
            ChessMove potMov = new ChessMove(myPosition, pos, null);
            board.addPiece(new ChessPosition(i,j), new ChessPiece(BLACK, BISHOP));

            //if another piece is not there
            //else if not empty and piece is opposing teams
            //else (all other cases: if empty and my team)
            if (piece == null) {
                moves.add(potMov);
            } else if (piece.getTeamColor() != teamColor) {
                moves.add(potMov);
            }
        }
        //up and left
        for (int i = myPosition.getRow()+ 1, j = myPosition.getColumn()-1; i <= 8 && j > 0; i++, j--) {
            ChessPosition pos = new ChessPosition(i,j);
            ChessPiece piece = board.getPiece(pos);
            ChessMove potMov = new ChessMove(myPosition, pos, null);
            board.addPiece(new ChessPosition(i,j), new ChessPiece(BLACK, BISHOP));

            //if another piece is not there
            //else if not empty and piece is opposing teams
            //else (all other cases: if empty and my team)
            if (piece == null) {
                moves.add(potMov);
            } else if (piece.getTeamColor() != teamColor) {
                moves.add(potMov);
            }
        }
        System.out.println(board);
        return moves;
    }
    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (pieceType == BISHOP) {
            return bishopMoves(board, myPosition);
        }
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
            return " " + "B" + "," + pieceType + ' ';
        } else {
            return " " + "W" + "," + pieceType + ' ';
        }
    }
}
