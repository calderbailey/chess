package chess.PieceMoveCalculators;

import chess .*;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalculator {
    public static Collection<ChessMove> calculateKingMoves(ChessGame.TeamColor pieceColor,
                                                             ChessBoard board,
                                                             ChessPosition myPosition) {
        ArrayList<ChessMove> MoveList = new ArrayList<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        int rowIndex = myRow-1;
        int colIndex = myCol-1;
        int rowMax = myRow+1;
        int colMax = myCol+1;
        //SCREENING SURROUNDING SQUARES
        while (rowIndex <= rowMax && rowIndex >= 1 && rowIndex <= 8) {
            while (colIndex <= colMax && colIndex >= 1 && colIndex <= 8) {
                ChessPosition NewPosition = new ChessPosition(rowIndex, colIndex);
                ChessPiece NewPositionPiece = board.getPiece(NewPosition);
                if (NewPositionPiece == null) {
                    ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                    MoveList.add(NewMove);
                } else if (NewPositionPiece.getTeamColor() != pieceColor){
                    ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                    MoveList.add(NewMove);
                }
                colIndex++;
            }
            colIndex = myCol-1;
            rowIndex++;
        }
        return MoveList;
    }
}
