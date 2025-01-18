package chess.PieceMoveCalculators;

import chess .*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator {

    public static Collection<ChessMove> calculatePawnMoves(ChessGame.TeamColor pieceColor,
                                                           ChessBoard board,
                                                           ChessPosition myPosition) {
        ArrayList<ChessMove> MoveList = new ArrayList<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        //White
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            ChessPosition NewPosition = new ChessPosition(myRow + 1, myCol);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                if (myRow == 2) {
                    NewPosition = new ChessPosition(myRow + 2, myCol);
                    NewPositionPiece = board.getPiece(NewPosition);
                    if (NewPositionPiece == null) {
                        NewMove = new ChessMove(myPosition, NewPosition, null);
                        MoveList.add(NewMove);
                    }
                }
            }
            if (myCol + 1 <= 8) {
                NewPosition = new ChessPosition(myRow + 1, myCol + 1);
                NewPositionPiece = board.getPiece(NewPosition);
                if (NewPositionPiece != null && NewPositionPiece.getTeamColor() != pieceColor) {
                    ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                    MoveList.add(NewMove);
                }
            }
            if (myCol - 1 >= 1) {
                NewPosition = new ChessPosition(myRow + 1, myCol - 1);
                NewPositionPiece = board.getPiece(NewPosition);
                if (NewPositionPiece != null && NewPositionPiece.getTeamColor() != pieceColor) {
                    ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                    MoveList.add(NewMove);
                }
            }
        }
        //Black
        if (pieceColor == ChessGame.TeamColor.BLACK) {
            ChessPosition NewPosition = new ChessPosition(myRow - 1, myCol);
            ChessPiece NewPositionPiece = board.getPiece(NewPosition);
            if (NewPositionPiece == null) {
                ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                MoveList.add(NewMove);
                if (myRow == 7) {
                    NewPosition = new ChessPosition(myRow - 2, myCol);
                    NewPositionPiece = board.getPiece(NewPosition);
                    if (NewPositionPiece == null) {
                        NewMove = new ChessMove(myPosition, NewPosition, null);
                        MoveList.add(NewMove);
                    }
                }
            }
            if (myCol + 1 <= 8) {
                NewPosition = new ChessPosition(myRow - 1, myCol + 1);
                NewPositionPiece = board.getPiece(NewPosition);
                if (NewPositionPiece != null && NewPositionPiece.getTeamColor() != pieceColor) {
                    ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                    MoveList.add(NewMove);
                }
            }
            if (myCol - 1 >= 1) {
                NewPosition = new ChessPosition(myRow - 1, myCol - 1);
                NewPositionPiece = board.getPiece(NewPosition);
                if (NewPositionPiece != null && NewPositionPiece.getTeamColor() != pieceColor) {
                    ChessMove NewMove = new ChessMove(myPosition, NewPosition, null);
                    MoveList.add(NewMove);
                }
            }
        }
        return checkForPromotion(MoveList);
    }

    private static Collection<ChessMove> checkForPromotion(Collection<ChessMove> input) {
        Collection<ChessMove> output = new ArrayList<>();
        output.addAll(input);
        for (ChessMove move : input) {
            if (move.getEndPosition().getRow() == 8 || move.getEndPosition().getRow() == 1) {
                output.remove(move);
                for (ChessPiece.PieceType pt : ChessPiece.PieceType.values()) {
                    if (!pt.equals(ChessPiece.PieceType.PAWN) && !pt.equals(ChessPiece.PieceType.KING)) {
                        ChessMove NewMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), pt);
                        output.add(NewMove);
                    }
                }
            }
        }
        return output;
    }
}