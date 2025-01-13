import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Test {

    public static void main(String[] args) {
        ChessBoard Board = new chess.ChessBoard();
        ChessPosition piecePosition = new ChessPosition(2,3);
        Board.addPiece(piecePosition,
                new ChessPiece(ChessGame.TeamColor.BLACK,
                ChessPiece.PieceType.ROOK));
        Board.addPiece(new ChessPosition(2,1),
                new ChessPiece(ChessGame.TeamColor.BLACK,
                ChessPiece.PieceType.PAWN));
        ChessPiece piece = Board.getPiece(new ChessPosition(2,3));
        ChessPiece.PieceType pieceType = piece.getPieceType();
        System.out.printf("This Piece is a " + pieceType.toString() + "." + "\n");
        Collection<ChessMove> moves = piece.pieceMoves(Board, piecePosition);
        Collection<ChessMove> moves1 = piece.pieceMoves(Board, new ChessPosition(2,1));
        if (moves.equals(moves1) == true) {
            System.out.print(true);
        }
        else {
            System.out.print(false);
        }
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
