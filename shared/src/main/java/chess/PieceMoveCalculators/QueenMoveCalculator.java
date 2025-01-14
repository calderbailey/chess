package chess.PieceMoveCalculators;

import chess .*;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoveCalculator {
    public static Collection<ChessMove> calculateQueenMoves(ChessGame.TeamColor pieceColor,
                                                             ChessBoard board,
                                                             ChessPosition myPosition) {
        Collection<ChessMove> BishopMoves = BishopMoveCalculator.calculateBishopMoves(pieceColor, board, myPosition);
        Collection<ChessMove> RookMoves = RookMoveCalculator.calculateRookMoves(pieceColor, board, myPosition);
        Collection<ChessMove> QueenMoves = new ArrayList<>();
        QueenMoves.addAll(BishopMoves);
        QueenMoves.addAll(RookMoves);
        return QueenMoves;
    }
}
