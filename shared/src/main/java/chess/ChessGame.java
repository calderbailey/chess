package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements Cloneable{
    private ChessBoard gameBoard = new ChessBoard();
    private TeamColor teamTurn = TeamColor.WHITE;
    private final Collection<ChessPosition> allPositions = new ArrayList<>();
    public ChessGame() {
        gameBoard.resetBoard();
        for (int rowIndex = 1; rowIndex <= 8; rowIndex ++) {
            for (int colIndex = 1; colIndex <= 8; colIndex ++) {
                allPositions.add(new ChessPosition(rowIndex, colIndex));
            }
        }
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    public void updateTeamTurn() {
        if (getTeamTurn() == TeamColor.BLACK) {setTeamTurn(TeamColor.WHITE);}
        else {setTeamTurn(TeamColor.BLACK);}
    }

    private TeamColor invertTeamColor(TeamColor teamColor) {
        if (teamColor == TeamColor.BLACK) {
            return TeamColor.WHITE;
        } else {
            return TeamColor.BLACK;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = getBoard().getPiece(startPosition);
        TeamColor pieceColor = piece.getTeamColor();
        Collection<ChessMove> pieceMoves = piece.pieceMoves(getBoard(), startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove move : pieceMoves) {
            ChessGame gameClone = clone();
            if (move.getPromotionPiece() != null) {
                piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            }
            gameClone.getBoard().addPiece(move.getEndPosition(), piece);
            gameClone.getBoard().addPiece(move.getStartPosition(), null);
            if (!gameClone.isInCheck(pieceColor)) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = getBoard().getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("No piece at that location");
        } else if (piece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Wrong team");
        } else if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("Move is not valid");
        } else {
            if (move.getPromotionPiece() != null) {
                piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            }
            getBoard().addPiece(move.getEndPosition(), piece);
            getBoard().addPiece(move.getStartPosition(), null);
        }
        updateTeamTurn();
    }

    private Collection<ChessMove> getTeamMoves(TeamColor teamColor) {
        Collection<ChessMove> teamMoves = new ArrayList<>();
        for (ChessPosition position : allPositions) {
            ChessPiece piece =  gameBoard.getPiece(position);
            if (piece != null) {
                if (piece.getTeamColor() == teamColor) {
                    teamMoves.addAll(piece.pieceMoves(gameBoard, position));
                }
            }
        }
        return teamMoves;
    }

    private ChessPosition getKingPosition(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        for (ChessPosition position : allPositions) {
            ChessPiece piece = gameBoard.getPiece(position);
            if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                kingPosition = position;
            }
        }
        return kingPosition;
    }
        /**
         * Determines if the given team is in check
         *
         * @param teamColor which team to check for check
         * @return True if the specified team is in check
         */
    public boolean isInCheck(TeamColor teamColor) {
    ChessPosition kingPosition = getKingPosition(teamColor);
    Collection<ChessMove> enemyMoves = getTeamMoves(invertTeamColor(teamColor));
    for (ChessMove move : enemyMoves) {
        if (move.getEndPosition().equals(kingPosition)) {
            return true;
        }
    }
    return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && !isEscapePossible(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && !isEscapePossible(teamColor);
    }

    public boolean isEscapePossible(TeamColor teamColor) {
        Collection<ChessMove> possibleMoves = getTeamMoves(teamColor);
        for (ChessMove move : possibleMoves) {
            ChessGame gameClone = clone();
            gameClone.setTeamTurn(teamColor);
            ChessPiece piece = gameClone.getBoard().getPiece(move.getStartPosition());
            gameClone.getBoard().addPiece(move.getEndPosition(), piece);
            gameClone.getBoard().addPiece(move.getStartPosition(), null);
            if (!gameClone.isInCheck(teamColor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
    gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
    return gameBoard;
    }

    @Override
    public ChessGame clone() {
        try {
            ChessGame clone = (ChessGame) super.clone();
            clone.setBoard(clone.getBoard().clone());
            TeamColor cloneTurn = clone.getTeamTurn();
            clone.setTeamTurn(cloneTurn);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard) && getTeamTurn() == chessGame.getTeamTurn() && Objects.equals(allPositions, chessGame.allPositions);
    }
}

