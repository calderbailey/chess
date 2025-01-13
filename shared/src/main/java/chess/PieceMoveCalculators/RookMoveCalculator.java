package chess.PieceMoveCalculators;

import chess .*;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalculator {
    public static Collection<ChessMove> calculateRookMoves(ChessGame.TeamColor pieceColor,
                                                          ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> MoveList = new ArrayList<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        //INCREASING ROW SCREEN
        int RowIndexInc = myRow+1;
        while (RowIndexInc <= 8) {
            ChessPosition NewPosition = new ChessPosition(RowIndexInc, myCol);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                RowIndexInc++;
            } else if (NewPositionPiece.getTeamColor() != pieceColor){
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                break;
            }
            else {
                break;
            }
        }
        //DECREASING ROW SCREEN
        int RowIndexDec = myRow-1;
        while (RowIndexDec >= 1) {
            ChessPosition NewPosition = new ChessPosition(RowIndexDec, myCol);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                RowIndexDec--;
            } else if (NewPositionPiece.getTeamColor() != pieceColor){
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                break;
            }
            else {
                break;
            }
        }
        //INCREASING COL SCREEN
        int ColIndexInc = myCol+1;
        while (ColIndexInc <= 8) {
            ChessPosition NewPosition = new ChessPosition(myRow, ColIndexInc);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                ColIndexInc++;
            } else if (NewPositionPiece.getTeamColor() != pieceColor){
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                break;
            }
            else {
                break;
            }
        }
        //DECREASING COL SCREEN
        int ColIndexDec = myCol-1;
        while (ColIndexDec >= 1) {
            ChessPosition NewPosition = new ChessPosition(myRow, ColIndexDec);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                ColIndexDec--;
            } else if (NewPositionPiece.getTeamColor() != pieceColor){
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                break;
            }
            else {
                break;
            }
        }
        return MoveList;
    }
}
