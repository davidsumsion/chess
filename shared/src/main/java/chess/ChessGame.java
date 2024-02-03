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
public class ChessGame implements Cloneable {
    TeamColor teamTurn = TeamColor.WHITE;
    ChessBoard board = new ChessBoard();

    public ChessGame() {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ChessGame clonedGame = (ChessGame) super.clone();
        clonedGame.board = (ChessBoard) this.board.clone();
        return clonedGame;
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
        teamTurn = team;
//        if (team == TeamColor.WHITE){
//            teamTurn = TeamColor.WHITE;
//        } else { teamTurn = TeamColor.BLACK; }
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
        if (piece == null) { return null;}
        //error
//        setTeamTurn(piece.getTeamColor());
        Collection<ChessMove> pieceMoves = piece.pieceMoves(getBoard(), startPosition);
        ArrayList<ChessMove> potMovesArray = new ArrayList<>(pieceMoves);
        Collection<ChessMove> outOfCheckMoves = new HashSet<>();
        for (ChessMove potMove : potMovesArray){
            //if move moves team out of check then add to outOfCheckMoves
            try {
                ChessGame clonedGame = (ChessGame) this.clone();
                clonedGame.board.addPiece(potMove.getEndPosition(), clonedGame.board.getPiece(potMove.getStartPosition()));
                clonedGame.board.removePiece(potMove.getStartPosition());
                System.out.println(clonedGame.board);
//                if (!clonedGame.isInCheck(clonedGame.teamTurn)){
//                    outOfCheckMoves.add(potMove);
//                }
                if (!clonedGame.isInCheck(piece.getTeamColor())){
                    outOfCheckMoves.add(potMove);
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return outOfCheckMoves;
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
        if (!validMoves(start).contains(move)) {
            throw new InvalidMoveException("Move is not Valid: Move is not in move list");
        }
        ChessPiece piece = board.getPiece(start);
        if (piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Move is not Valid: not that piece's turn");
        }
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && start.getRow() == 7 && end.getRow() == 8
                || piece.getPieceType() == ChessPiece.PieceType.PAWN && start.getRow() == 2 && end.getRow() == 1){
            board.addPiece(end, new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        }
        else{
            board.addPiece(end, piece);
        }
        board.removePiece(start);
        if (teamTurn == TeamColor.BLACK){
            setTeamTurn(TeamColor.WHITE);
        } else {
            setTeamTurn(TeamColor.BLACK);
        }
    }

    public Collection<ChessMove> getTeamPotMoves(TeamColor teamColor) {
        HashSet<ChessMove> potMoves = new HashSet<>();
        //rows
        for (int i = 1; i <= 8; i++){
            //columns
            for (int j = 1; j <=8; j++){
                ChessPosition piecePos = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(piecePos);
                if (piece != null && piece.getTeamColor() == teamColor){
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
        TeamColor otherTeamColor = TeamColor.WHITE;
        if (teamColor == TeamColor.WHITE) { otherTeamColor = TeamColor.BLACK; }
        Collection<ChessMove> otherTeamPotMoves = getTeamPotMoves(otherTeamColor);
        ArrayList<ChessMove> otherTeamMovesArray = new ArrayList<>(otherTeamPotMoves);
        ChessPosition kingPos = findKing(teamColor);
        for (ChessMove potMov : otherTeamMovesArray){
            if (potMov.getEndPosition().equals(kingPos)) {
                return true;
            }
        }
        return false;
    }

    public Collection<ChessMove> checkStaleMateHelper(TeamColor teamColor){
        Collection<ChessMove> potMoves = new HashSet<>();
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8 ; j++){
                ChessPosition potPos = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(potPos);
                if (piece != null && piece.getTeamColor() == teamColor){
                    potMoves.addAll(validMoves(potPos));
                }
            }
        }
        return potMoves;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            return false;
        }
        Collection<ChessMove> potMoves = checkStaleMateHelper(teamColor);
        return potMoves.isEmpty();
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
        if (isInCheck(teamColor)){
            return false;
        }
        Collection<ChessMove> potMoves = checkStaleMateHelper(teamColor);
        return potMoves.isEmpty();
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
