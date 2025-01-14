package chess.PieceMoveCalculators;

import chess .*;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalculator {
    public static Collection<ChessMove> calculateRookMoves(ChessGame.TeamColor pieceColor,
                                                           ChessBoard board,
                                                           ChessPosition myPosition) {
        ArrayList<ChessMove> MoveList = new ArrayList<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        int Index;
        //INCREASING ROW SCREEN
        Index = myRow+1;
        while (Index <= 8) {
            ChessPosition NewPosition = new ChessPosition(Index, myCol);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                Index++;
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
        Index = myRow-1;
        while (Index >= 1) {
            ChessPosition NewPosition = new ChessPosition(Index, myCol);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                Index--;
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
        Index = myCol+1;
        while (Index <= 8) {
            ChessPosition NewPosition = new ChessPosition(myRow, Index);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                Index++;
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
        Index = myCol-1;
        while (Index >= 1) {
            ChessPosition NewPosition = new ChessPosition(myRow, Index);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                Index--;
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
