package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import static chess.ChessBoard.*;
import static chess.ChessMove.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor teamTurn = TeamColor.BLACK;
    ChessBoard board = new ChessBoard();

    public ChessGame() {
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if (team == TeamColor.WHITE){
            teamTurn = TeamColor.BLACK;
        } else { teamTurn = TeamColor.WHITE; }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> pieceMoves = piece.pieceMoves(getBoard(), startPosition);
        // for move in pieceMoves, does it put my king in Check?, need to implement check first
        return pieceMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        if (!validMoves(start).contains(move)) { throw new InvalidMoveException("Move is not Valid"); }
        ChessBoard board = getBoard();
        ChessPiece piece = board.getPiece(start);
        board.addPiece(end, piece);
        board.removePiece(start);
    }


    public Collection<ChessMove> getOtherTeamPotMoves(TeamColor teamColor) {
        HashSet<ChessMove> potMoves = new HashSet<>();
        //rows
        for (int i = 1; i <= 8; i++){
            //columns
            for (int j = 1; j <=8; j++){
                ChessPosition piecePos = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(piecePos);
                if (piece != null && piece.getTeamColor() != teamColor){
                    Collection<ChessMove> piecePotMoves = piece.pieceMoves(board, piecePos);
                    potMoves.addAll(piecePotMoves);
                }
            }
        }
        // loop through each row
            //loop through each column
                //if piece isn't null and piece color != teamColor
                    //add PotMoves to set
        //return that set
        return potMoves;
    }

    public ChessPosition findKing(TeamColor teamColor) {
        ChessPosition noKing = null;
        for (int i = 1; i <= 8; i++){
            //columns
            for (int j = 1; j <=8; j++){
                ChessPosition piecePos = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(piecePos);
                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING){

                    return piecePos;
                }
            }
        }
        return noKing;
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // run through every move for every piece on opposite team, add to set (no duplicates), then check if King is contained in that set
        Collection<ChessMove> otherTeamPotMoves = getOtherTeamPotMoves(teamColor);
        ArrayList<ChessMove> otherTeamMovesArray = new ArrayList<>(otherTeamPotMoves);
        ChessPosition kingPos = findKing(teamColor);
        for (ChessMove potMov : otherTeamMovesArray){
            if (potMov.getEndPosition().equals(kingPos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //is in check and can't get out of check if any piece on that team is moved
        //clone board
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //returns true if there are no valid(legal) moves a team can make
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
