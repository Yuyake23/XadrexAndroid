package com.example.xadrez;

import androidx.appcompat.app.AlertDialog;

import com.example.xadrez.BoardFragment;
import com.example.xadrez.R;
import com.example.xadrez.boardgame.Position;
import com.example.xadrez.chess.ChessException;
import com.example.xadrez.chess.ChessPosition;
import com.example.xadrez.chess.NonePieceToPromoteWasGiven;
import com.example.xadrez.chess.pieces.PieceType;

public class LocalBoard extends BoardFragment {

    private ChessPosition sourcePosition;
    private ChessPosition targetPosition;

    public LocalBoard(Do doWhenFinish) {
        super(doWhenFinish);
    }

    @Override
    protected void positionCliked(int i, int j) {
        try {
            if (sourcePosition == null) {
                sourcePosition = ChessPosition.fromPosition(new Position(i, j));
                super.updateView(chessMatch.possibleMovies(sourcePosition));
            } else if (targetPosition == null) {
                targetPosition = ChessPosition.fromPosition(new Position(i, j));
                try {
                    chessMatch.performChessMove(sourcePosition, targetPosition, null);
                } catch (NonePieceToPromoteWasGiven e) {
                    this.onNeedPieceToPromote(sourcePosition, targetPosition);
                }
                sourcePosition = targetPosition = null;
                super.updateView(null);
            }

        } catch (ChessException e) {
            sourcePosition = targetPosition = null;
            super.updateView(null);
        }

        if (chessMatch.matchIsOver()) {
            super.finalizarPartida();
        }
    }

    @Override
    protected void onNeedPieceToPromote(ChessPosition source, ChessPosition target) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Escolha uma peça")
                .setItems(R.array.piece_type_array, (dialog, which) -> {
                    PieceType pieceType = null;
                    switch (which) {
                        case 0:
                            pieceType = PieceType.BISHOP;
                            break;
                        case 1:
                            pieceType = PieceType.KNIGHT;
                            break;
                        case 2:
                            pieceType = PieceType.QUEEN;
                            break;
                        case 3:
                            pieceType = PieceType.ROOK;
                            break;
                    }
                    chessMatch.performChessMove(source, target, pieceType);
                    updateView(null);
                });

        builder.create().show();

    }


}
