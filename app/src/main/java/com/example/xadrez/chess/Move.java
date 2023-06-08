package com.example.xadrez.chess;

import java.io.Serializable;

public class Move implements Serializable {

    private ChessPosition sourcePosition;
    private ChessPosition targetPosition;
    private String promotedPiece;

    public Move(ChessPosition sourcePosition, ChessPosition targetPosition, String promotedPiece) {
        this.sourcePosition = sourcePosition;
        this.targetPosition = targetPosition;
        this.promotedPiece = promotedPiece;
    }

    public ChessPosition sourcePosition() {
        return sourcePosition;
    }

    public ChessPosition targetPosition() {
        return targetPosition;
    }

    public String promotedPiece() {
        return promotedPiece;
    }
}
