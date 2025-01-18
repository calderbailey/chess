package chess.PieceMoveCalculators;

import chess .*;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalculator {
    public static Collection<ChessMove> calculateBishopMoves(ChessGame.TeamColor pieceColor,
                                                           ChessBoard board,
                                                           ChessPosition myPosition) {
        ArrayList<ChessMove> MoveList = new ArrayList<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        int rowIndex = myRow+1;
        int colIndex = myCol-1;
        //UPPER LEFT SCREEN
        while (rowIndex <= 8 && colIndex >= 1) {
            ChessPosition NewPosition = new ChessPosition(rowIndex, colIndex);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                rowIndex++;
                colIndex--;
            } else if (NewPositionPiece.getTeamColor() != pieceColor){
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                break;
            }
            else {
                break;
            }
        }
        //UPPER RIGHT SCREEN
        rowIndex = myRow+1;
        colIndex = myCol+1;
        while (rowIndex <= 8 && colIndex <= 8) {
            ChessPosition NewPosition = new ChessPosition(rowIndex, colIndex);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                rowIndex++;
                colIndex++;
            } else if (NewPositionPiece.getTeamColor() != pieceColor){
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                break;
            }
            else {
                break;
            }
        }
        //LOWER LEFT SCREEN
        rowIndex = myRow-1;
        colIndex = myCol-1;
        while (rowIndex >= 1 && colIndex >= 1) {
            ChessPosition NewPosition = new ChessPosition(rowIndex, colIndex);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                rowIndex--;
                colIndex--;
            } else if (NewPositionPiece.getTeamColor() != pieceColor){
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                break;
            }
            else {
                break;
            }
        }
        //LOWER RIGHT SCREEN
        rowIndex = myRow-1;
        colIndex = myCol+1;
        while (rowIndex >= 1 && colIndex <= 8) {
            ChessPosition NewPosition = new ChessPosition(rowIndex, colIndex);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                rowIndex--;
                colIndex++;
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
