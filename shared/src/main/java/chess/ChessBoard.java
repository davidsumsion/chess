package chess;

import java.util.Arrays;
import static chess.ChessPiece.PieceType.*;
import static chess.ChessGame.TeamColor.*;



/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {
    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {

    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ChessBoard clonedBoard = (ChessBoard) super.clone();
        clonedBoard.squares = new ChessPiece[8][8];
        for (int i =0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (this.squares[i][j] == null){
                    clonedBoard.squares[i][j] = null;
                }
                else{
                    clonedBoard.squares[i][j] = new ChessPiece(this.squares[i][j].getTeamColor(), this.squares[i][j].getPieceType());
                }
            }
        }
        return clonedBoard;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    public void removePiece(ChessPosition position) {
        squares[position.getRow()-1][position.getColumn()-1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];
        //black pawns
        for (int i = 1; i < 9; i++){ addPiece(new ChessPosition(7,i), new ChessPiece(BLACK, PAWN)); }
        //black rooks
        addPiece(new ChessPosition(8,1), new ChessPiece(BLACK, ROOK));
        addPiece(new ChessPosition(8,8), new ChessPiece(BLACK, ROOK));
        //black knights
        addPiece(new ChessPosition(8,2), new ChessPiece(BLACK, KNIGHT));
        addPiece(new ChessPosition(8,7), new ChessPiece(BLACK, KNIGHT));
        //black bishops
        addPiece(new ChessPosition(8,3), new ChessPiece(BLACK, BISHOP));
        addPiece(new ChessPosition(8,6), new ChessPiece(BLACK, BISHOP));
        //black king/queen
        addPiece(new ChessPosition(8,4), new ChessPiece(BLACK, QUEEN));
        addPiece(new ChessPosition(8,5), new ChessPiece(BLACK, KING));

        //white pawns
        for (int i = 1; i < 9; i++){ addPiece(new ChessPosition(2,i), new ChessPiece(WHITE, PAWN)); }
        //white rooks
        addPiece(new ChessPosition(1,1), new ChessPiece(WHITE, ROOK));
        addPiece(new ChessPosition(1,8), new ChessPiece(WHITE, ROOK));
        //white knights
        addPiece(new ChessPosition(1,2), new ChessPiece(WHITE, KNIGHT));
        addPiece(new ChessPosition(1,7), new ChessPiece(WHITE, KNIGHT));
        //white bishops
        addPiece(new ChessPosition(1,3), new ChessPiece(WHITE, BISHOP));
        addPiece(new ChessPosition(1,6), new ChessPiece(WHITE, BISHOP));
        //white king/queen
        addPiece(new ChessPosition(1,4), new ChessPiece(WHITE, QUEEN));
        addPiece(new ChessPosition(1,5), new ChessPiece(WHITE, KING));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        String end = "";
        for (int i = 7; i >= 0; i--){
            end += i + 1;
            end += "|";
            for (int j = 0; j < 8; j++){
                if (squares[i][j] == null) {
                    end += "           ";
                }
                else {
                    end += squares[i][j].toString();
                }
                end += "|";
            }
            end += "\n";
        }
        end += "        1           2           3           4           5           6           7           8\n";
        return end;
    }
}
