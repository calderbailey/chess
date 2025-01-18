package chess.PieceMoveCalculators;

import chess .*;

import java.util.ArrayList;
import java.util.Collection;

public class validMoves {
    public static Collection<ChessMove> calculateValidMoves(ChessGame.TeamColor pieceColor,
                                                            ChessBoard board,
                                                            Collection<ChessMove> MoveList) {
        Collection<ChessMove> modifiedMoveList= new ArrayList<>();
        for (ChessMove move : MoveList) {
            if (inBounds(move) && spaceAvailable(move, board, pieceColor)) {
                modifiedMoveList.add(move);
            }
        }
        return modifiedMoveList;
    }

    private static boolean inBounds(ChessMove move) {
        int row = move.getEndPosition().getRow();
        int col = move.getEndPosition().getColumn();
        if (row >= 1 && row <= 8) {
            if (col >= 1 && col <= 8) {
                return true;
            }
        }
        return false;
    }

    private static boolean spaceAvailable(ChessMove move,
                                          ChessBoard board,
                                          ChessGame.TeamColor pieceColor) {
        ChessPiece spaceStatus = board.getPiece(move.getEndPosition());
        if (spaceStatus != null && spaceStatus.getTeamColor() == pieceColor) {
            return false;
        }
        return true;
    }
}


