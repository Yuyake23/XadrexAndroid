package com.example.xadrez.chess.pieces;


import com.example.xadrez.boardgame.Board;
import com.example.xadrez.boardgame.Position;
import com.example.xadrez.chess.ChessMatch;
import com.example.xadrez.chess.ChessPiece;
import com.example.xadrez.chess.Color;

public class Bishop extends ChessPiece {
	private static final long serialVersionUID = 6493847842904924709L;

	public Bishop(Board board, ChessMatch chessMatch, Color color) {
		super(board, chessMatch, color);
	}

	@Override
	public String toString() {
		return "B";
	}

	@Override
	public boolean[][] getAllPossibleMoves() {
		boolean[][] pm = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, 0);
		// nw
		p.setValues(this.position.getRow() - 1, this.position.getColumn() - 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			pm[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() - 1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			pm[p.getRow()][p.getColumn()] = true;

		// ne
		p.setValues(this.position.getRow() - 1, this.position.getColumn() + 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			pm[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() + 1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			pm[p.getRow()][p.getColumn()] = true;

		// se
		p.setValues(this.position.getRow() + 1, this.position.getColumn() + 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			pm[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() + 1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			pm[p.getRow()][p.getColumn()] = true;

		// sw
		p.setValues(this.position.getRow() + 1, this.position.getColumn() - 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			pm[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() - 1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			pm[p.getRow()][p.getColumn()] = true;

		return pm;
	}

}
