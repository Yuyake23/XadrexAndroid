package com.example.xadrez.chess.pieces;

import com.example.xadrez.boardgame.Board;
import com.example.xadrez.boardgame.Position;
import com.example.xadrez.chess.ChessMatch;
import com.example.xadrez.chess.ChessPiece;
import com.example.xadrez.chess.Color;

public class Knight extends ChessPiece {
    private static final long serialVersionUID = -8424278947212534686L;

    public Knight(Board board, ChessMatch chessMatch, Color color) {
        super(board, chessMatch, color);
    }

    private boolean canMove(Position position) {
        ChessPiece chessPiece = (ChessPiece) getBoard().getPiece(position);
        return chessPiece == null || !chessPiece.getColor().equals(this.getColor());
    }

    @Override
    public String toString() {
        return "N";
    }

    @Override
    public boolean[][] getAllPossibleMoves() {
        boolean[][] pm = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);
        int v1[] = {+1, -1};
        int v2[] = {+2, -2};

        for (int i : v1) {
            for (int j : v2) {
                p.setValues(position.getRow() + i, position.getColumn() + j);
                if (getBoard().positionExists(p) && canMove(p))
                    pm[p.getRow()][p.getColumn()] = true;

                p.setValues(position.getRow() + j, position.getColumn() + i);
                if (getBoard().positionExists(p) && canMove(p))
                    pm[p.getRow()][p.getColumn()] = true;
            }
        }

        return pm;
    }

}
