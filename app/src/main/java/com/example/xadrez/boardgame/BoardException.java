package com.example.xadrez.boardgame;

public class BoardException extends RuntimeException {
    private static final long serialVersionUID = 5387442010037533494L;

    public BoardException(String msg) {
        super(msg);
    }
}
