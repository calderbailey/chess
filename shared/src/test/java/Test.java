import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Test {

    public static void main(String[] args) {
        ChessBoard Board = new chess.ChessBoard();
        ChessPosition piecePosition = new ChessPosition(2,2);
        Board.addPiece(piecePosition,
                new ChessPiece(ChessGame.TeamColor.BLACK,
                ChessPiece.PieceType.ROOK));
        ChessPiece piece = Board.getPiece(new ChessPosition(2,2));
        ChessPiece.PieceType pieceType = piece.getPieceType();
        System.out.printf("This Piece is a " + pieceType.toString() + "." + "\n");
        Collection<ChessMove> moves = piece.pieceMoves(Board, piecePosition);
        for (ChessMove move : moves) {
            System.out.printf("[%d, %d] to [%d, %d]%s",
                    move.getStartPosition().getRow(),
                    move.getStartPosition().getColumn(),
                    move.getEndPosition().getRow(),
                    move.getEndPosition().getColumn(),
                    "\n");  // Print the move followed by a newline
        }
    }
}
