package com.example.xadrez;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.xadrez.boardgame.Position;
import com.example.xadrez.chess.ChessException;
import com.example.xadrez.chess.ChessMatch;
import com.example.xadrez.chess.ChessPiece;
import com.example.xadrez.chess.ChessPosition;
import com.example.xadrez.chess.Color;
import com.example.xadrez.chess.Move;
import com.example.xadrez.chess.NonePieceToPromoteWasGiven;
import com.example.xadrez.chess.pieces.PieceType;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;


public class BoardFragment extends Fragment {
    @FunctionalInterface
    public interface Do {
        void run();
    }

    private Do doWhenFinish;

    private Drawable blackBg;
    private Drawable whiteBg;

    private Drawable blackRook;
    private Drawable blackKnight;
    private Drawable blackBishop;
    private Drawable blackQueen;
    private Drawable blackKing;
    private Drawable blackPawn;

    private Drawable whiteRook;
    private Drawable whiteKnight;
    private Drawable whiteBishop;
    private Drawable whiteQueen;
    private Drawable whiteKing;
    private Drawable whitePawn;

    private final AppCompatImageButton[][] pieces = new AppCompatImageButton[8][8];
    private ChessMatch chessMatch = new ChessMatch();

    private ChessPosition sourcePosition;
    private ChessPosition targetPosition;

    public BoardFragment(Do doWhenFinish) {
        this.doWhenFinish = doWhenFinish;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board, container, false);
        GridLayout gridLayout = rootView.findViewById(R.id.boardGrid);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int fundo = (i % 2 == j % 2) ? R.layout.chess_white_position : R.layout.chess_black_position;
                View cp = inflater.inflate(fundo, gridLayout, false);
                int finalI = i;
                int finalJ = j;
                cp.setOnClickListener(v -> positionCliked(finalI, finalJ));
                gridLayout.addView(cp);
                this.pieces[i][j] = (AppCompatImageButton) cp;
            }
        }

        configureSetup();

        return rootView;
    }

    private void positionCliked(int i, int j) {
        try {
            if (sourcePosition == null) {
                sourcePosition = ChessPosition.fromPosition(new Position(i, j));
                this.updateView(chessMatch.possibleMovies(sourcePosition));
            } else if (targetPosition == null) {
                targetPosition = ChessPosition.fromPosition(new Position(i, j));
                try {
                    this.chessMatch.performChessMove(sourcePosition, targetPosition, null);
                } catch (NonePieceToPromoteWasGiven e) {
                    onNeedPieceToPromote(sourcePosition, targetPosition);
                }
                sourcePosition = targetPosition = null;
                this.updateView(null);
            }

        } catch (ChessException e) {
            sourcePosition = targetPosition = null;
            updateView(null);
        }

        if (chessMatch.matchIsOver()) {
            finalizarPartida();
        }

    }

    private void onNeedPieceToPromote(ChessPosition source, ChessPosition target) {
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

    private void updateView(boolean[][] possibleMoves) {
        ChessPiece[][] pieces = chessMatch.getPieces();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.pieces[i][j].setImageDrawable(selectPieceDrawable(pieces[i][j]));
                if (possibleMoves != null && possibleMoves[i][j]) {
//                    TODO: Colocar efeito de fundo
//                    Aqui são os lugares no qual uma peça pode se mover
//                    Não aloque os recurso da forma abaixo, assim vai precisar carregar toda hora
//                    Olha como ta sendo feito em configureSetup()
                    this.pieces[i][j].setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.circle));
                } else if (sourcePosition != null && sourcePosition.toPosition().isIn(i, j)) {
//                    TODO: Colocar efeito de fundo
//                    Aqui é a peça selecionada para mover
                    this.pieces[i][j].setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.color.purple_200));
                } else {
                    this.pieces[i][j].setBackgroundDrawable((i % 2 == j % 2) ? whiteBg : blackBg);
                }

            }
        }

        if (chessMatch.getCheck()) {
            Position position = chessMatch.king(chessMatch.getCurrentPlayer()).getPosition();
            // TODO: Colocar efeito do rei em check
            this.pieces[position.getRow()][position.getColumn()].setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.color.teal_200));
        }

        // TODO: Caso queira fazer algo com as peças capturadas:
//        this.chessMatch.getCapturedPieces(). . .
    }

    private void finalizarPartida() {
        // Mostrar Dialog na tela
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.end_mach);
        TextView textMensagem = dialog.findViewById(R.id.mensagem_fim_partida);
        String mensagem = chessMatch.getWinner() == Color.WHITE ? "O branco ganhou!!!" : "O preto ganhou!!!";
        textMensagem.setText(mensagem);
        dialog.show();

        // Bloquear movimentação
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.pieces[i][j].setClickable(false);
            }
        }

        doWhenFinish.run();
    }

    private Drawable selectPieceDrawable(ChessPiece piece) {
        if (piece == null) return null;
        PieceType pieceType = PieceType.pieceTypeByChar(piece.toString());

        if (piece.getColor() == Color.WHITE) {
            switch (Objects.requireNonNull(pieceType)) {
                case ROOK:
                    return whiteRook;
                case KNIGHT:
                    return whiteKnight;
                case BISHOP:
                    return whiteBishop;
                case QUEEN:
                    return whiteQueen;
                case KING:
                    return whiteKing;
                case PAWN:
                    return whitePawn;
            }
        } else {
            switch (Objects.requireNonNull(pieceType)) {
                case ROOK:
                    return blackRook;
                case KNIGHT:
                    return blackKnight;
                case BISHOP:
                    return blackBishop;
                case QUEEN:
                    return blackQueen;
                case KING:
                    return blackKing;
                case PAWN:
                    return blackPawn;
            }
        }

        return null;
    }

    private void configureSetup() {
        blackBg = ContextCompat.getDrawable(requireContext(), R.color.madeira_fundo);
        whiteBg = ContextCompat.getDrawable(requireContext(), R.color.madeira_bege);

        blackRook = ContextCompat.getDrawable(requireContext(), R.drawable.rook);
        blackKnight = ContextCompat.getDrawable(requireContext(), R.drawable.knight);
        blackBishop = ContextCompat.getDrawable(requireContext(), R.drawable.bishop);
        blackQueen = ContextCompat.getDrawable(requireContext(), R.drawable.queen);
        blackKing = ContextCompat.getDrawable(requireContext(), R.drawable.king);
        blackPawn = ContextCompat.getDrawable(requireContext(), R.drawable.pawn);

        whiteRook = ContextCompat.getDrawable(requireContext(), R.drawable.rook1);
        whiteKnight = ContextCompat.getDrawable(requireContext(), R.drawable.knight1);
        whiteBishop = ContextCompat.getDrawable(requireContext(), R.drawable.bishop1);
        whiteQueen = ContextCompat.getDrawable(requireContext(), R.drawable.queen1);
        whiteKing = ContextCompat.getDrawable(requireContext(), R.drawable.king1);
        whitePawn = ContextCompat.getDrawable(requireContext(), R.drawable.pawn1);


        updateView(null);
    }

    public List<Move> getMoves() {
        return chessMatch.getMoveDeque();
    }

    public ChessMatch getChessMatch() {
        return chessMatch;
    }
}