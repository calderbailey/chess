package chess.PieceMoveCalculators;

import chess .*;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalculator {
    public static Collection<ChessMove> calculateKnightMoves(ChessGame.TeamColor pieceColor,
                                                           ChessBoard board,
                                                           ChessPosition myPosition) {
        ArrayList<ChessMove> MoveList = new ArrayList<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        //UP and DOWN Heavy Moves
        int colIndex = myCol +1;
        int rowIndex = myRow +2;
        while (rowIndex >= myRow -2) {
            while (colIndex >= myCol -1) {
                MoveList.add(new ChessMove(myPosition,
                             new ChessPosition(rowIndex, colIndex),
                             ChessPiece.PieceType.KNIGHT));
                colIndex = colIndex -2;
                System.out.printf("colIndex: " + colIndex + "\n");
            }
            colIndex = myCol +1;
            rowIndex = rowIndex -4;
            System.out.printf("rowIndex: " + rowIndex + "\n");

        }

        //LEFT and RIGHT Heavy Moves
        colIndex = myCol +2;
        rowIndex = myRow +1;
        while (colIndex >= myCol -2) {
            while (rowIndex >= myRow -1) {
                MoveList.add(new ChessMove(myPosition,
                        new ChessPosition(rowIndex, colIndex),
                        ChessPiece.PieceType.KNIGHT));
                rowIndex = rowIndex -2;
                System.out.printf("rowIndex: " + rowIndex + "\n");
            }
            rowIndex = myRow +1;
            colIndex = colIndex -4;
            System.out.printf("colIndex: " + colIndex + "\n");
        }

        //Final Return
        return validMoves.calculateValidMoves(pieceColor, board, MoveList);
    }

    private static class validMoves {
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
}
