package com.example.xadrez.chess;


import com.example.xadrez.boardgame.BoardException;

public class ChessException extends BoardException {

	private static final long serialVersionUID = 1L;

	public ChessException(String msg) {
		super(msg);
	}
}
