package chess;

import java.util.*;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessPiece.PieceType.*;

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
    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> moves = new HashSet<>();
        //up & right
        for (int i = myPosition.getRow()+1, j = myPosition.getColumn()+1; i <= 8 && j <= 8; i++, j++) {
            ChessPosition pos = new ChessPosition(i,j);
            ChessPiece piece = board.getPiece(pos);
            ChessMove potMov = new ChessMove(myPosition, pos, null);
            //if another piece is not there
            //else if not empty and piece is opposing teams
            //else (all other cases: if empty and my team)
            if (piece == null) {
                moves.add(potMov);
            } else if (piece.getTeamColor() != teamColor) {
                moves.add(potMov);
                break;
            } else if (piece.getTeamColor() == teamColor) {
                break;
            }
        }
        //down & right
        for (int i = myPosition.getRow()-1, j = myPosition.getColumn()+1; i > 0 && j <= 8; i--, j++) {
            ChessPosition pos = new ChessPosition(i,j);
            ChessPiece piece = board.getPiece(pos);
            ChessMove potMov = new ChessMove(myPosition, pos, null);
            //if another piece is not there
            //else if not empty and piece is opposing teams
            //else (all other cases: if empty and my team)
            if (piece == null) {
                moves.add(potMov);
            } else if (piece.getTeamColor() != teamColor) {
                moves.add(potMov);
                break;
            } else if (piece.getTeamColor() == teamColor) {
                break;
            }
        }
        //down & left
        for (int i = myPosition.getRow()-1, j = myPosition.getColumn()-1; i > 0 && j > 0; i--, j--) {
            ChessPosition pos = new ChessPosition(i,j);
            ChessPiece piece = board.getPiece(pos);
            ChessMove potMov = new ChessMove(myPosition, pos, null);
            //if another piece is not there
            //else if not empty and piece is opposing teams
            //else (all other cases: if piece and my team)
            if (piece == null) {
                moves.add(potMov);
            } else if (piece.getTeamColor() != teamColor) {
                moves.add(potMov);
                break;
            } else if (piece.getTeamColor() == teamColor) {
                break;
            }
        }
        //up and left
        for (int i = myPosition.getRow()+ 1, j = myPosition.getColumn()-1; i <= 8 && j > 0; i++, j--) {
            ChessPosition pos = new ChessPosition(i,j);
            ChessPiece piece = board.getPiece(pos);
            ChessMove potMov = new ChessMove(myPosition, pos, null);
            //if another piece is not there
            //else if not empty and piece is opposing teams
            //else (all other cases: if empty and my team)
            if (piece == null) {
                moves.add(potMov);
            } else if (piece.getTeamColor() != teamColor) {
                moves.add(potMov);
                break;
            } else if (piece.getTeamColor() == teamColor) {
                break;
            }
        }
        return moves;
    }
    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> moves = new HashSet<>();
        Set<ChessMove> potMoves = new HashSet<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
//        System.out.println(myPosition);
//        System.out.println(board);
        //3 spots above king
        if (myRow != 8) {
            for (int i = myCol-1; i < myCol+2; i++){
                if (i < 9 && i > 0){ potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow+1, i), null));}
            }
        }
        //3 spots below king
        if (myRow != 0){
            for (int i = myCol-1; i < myCol+2; i++){
                if (i < 9 && i > 0){ potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow-1, i), null));}
            }
        }
        //left & right spots
        if (myCol != 0) { potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow, myCol-1), null)); }
        if (myCol != 8) { potMoves.add(new ChessMove(myPosition, new ChessPosition(myRow, myCol+1), null)); }
//        board.addPiece(new ChessPosition(myRow, myCol+1), new ChessPiece(BLACK, KING));
//        System.out.println(board);
        return verifyMoves(potMoves, board);
    }
    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> moves = new HashSet<>();
        Set<ChessMove> potMoves = new HashSet<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
//        System.out.println(myPosition);
//        System.out.println(board);
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
    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<>();
//        Set<ChessMove> potMoves = new HashSet<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
//        System.out.println(myPosition);
//        System.out.println(board);
        //up
        for (int i = myRow+1; i <= 8; i++) {
            ChessPosition pos = new ChessPosition(i,myCol);
            ChessPiece piece = board.getPiece(pos);
            ChessMove potMov = new ChessMove(myPosition, pos, null);
            if (piece == null) {
                moves.add(potMov);
            } else if (piece.getTeamColor() != teamColor) {
                moves.add(potMov);
                break;
            } else if (piece.getTeamColor() == teamColor) {
                break;
            }
        }
        //down
        for (int i = myRow-1; i >= 1; i--) {
            ChessPosition pos = new ChessPosition(i,myCol);
            ChessPiece piece = board.getPiece(pos);
            ChessMove potMov = new ChessMove(myPosition, pos, null);
            if (piece == null) {
                moves.add(potMov);
            } else if (piece.getTeamColor() != teamColor) {
                moves.add(potMov);
                break;
            } else if (piece.getTeamColor() == teamColor) {
                break;
            }
        }
        //right
        for (int i = myCol+1; i <= 8; i++) {
            ChessPosition pos = new ChessPosition(myRow,i);
            ChessPiece piece = board.getPiece(pos);
            ChessMove potMov = new ChessMove(myPosition, pos, null);
            if (piece == null) {
                moves.add(potMov);
            } else if (piece.getTeamColor() != teamColor) {
                moves.add(potMov);
                break;
            } else if (piece.getTeamColor() == teamColor) {
                break;
            }
        }
        //left
        for (int i = myCol-1; i >= 1; i--) {
            ChessPosition pos = new ChessPosition(myRow,i);
            ChessPiece piece = board.getPiece(pos);
            ChessMove potMov = new ChessMove(myPosition, pos, null);
            if (piece == null) {
                moves.add(potMov);
            } else if (piece.getTeamColor() != teamColor) {
                moves.add(potMov);
                break;
            } else if (piece.getTeamColor() == teamColor) {
                break;
            }
        }
        return moves;
    }
    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<>();
        Set<ChessMove> diagonalMoves = (Set<ChessMove>) bishopMoves(board, myPosition);

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
        if (pieceType == BISHOP) { return bishopMoves(board, myPosition);}
        if (pieceType == KING) { return kingMoves(board, myPosition);}
        if (pieceType == KNIGHT) { return knightMoves(board, myPosition);}
        if (pieceType == ROOK) { return rookMoves(board, myPosition); }
        if (pieceType == QUEEN) { return queenMoves(board, myPosition);}

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
