package com.example.xadrez;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.xadrez.boardgame.Position;
import com.example.xadrez.chess.ChessMatch;
import com.example.xadrez.chess.ChessPiece;
import com.example.xadrez.chess.ChessPosition;
import com.example.xadrez.chess.Color;
import com.example.xadrez.chess.Move;
import com.example.xadrez.chess.pieces.PieceType;

import java.util.List;
import java.util.Objects;


public abstract class BoardFragment extends Fragment {
    @FunctionalInterface
    public interface Do {
        void run();
    }

    private final Do doWhenFinish;

    private Drawable blackBg;
    private Drawable whiteBg;
    private Drawable possibleBg;
    private Drawable chequeBg;

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

    protected final AppCompatImageButton[][] pieces = new AppCompatImageButton[8][8];
    protected ChessMatch chessMatch = new ChessMatch();

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

        this.configureSetup();

        return rootView;
    }

    protected void updateView(boolean[][] possibleMoves) {
        ChessPiece[][] pieces = chessMatch.getPieces();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.pieces[i][j].setImageDrawable(selectPieceDrawable(pieces[i][j]));
                if (possibleMoves != null && possibleMoves[i][j]) {
//                    TODO: Colocar efeito de fundo
//                    Aqui são os lugares no qual uma peça pode se mover
//                    Não aloque os recurso da forma abaixo, assim vai precisar carregar toda hora
//                    Olha como ta sendo feito em configureSetup()
                    this.pieces[i][j].setBackgroundDrawable(possibleBg);
                } else {
                    this.pieces[i][j].setBackgroundDrawable((i % 2 == j % 2) ? whiteBg : blackBg);
                }

            }
        }

        if (chessMatch.getCheck()) {
            Position position = chessMatch.king(chessMatch.getCurrentPlayer()).getPosition();
            // TODO: Colocar efeito do rei em check
            this.pieces[position.getRow()][position.getColumn()].setBackgroundDrawable(chequeBg);
        }

        // TODO: Caso queira fazer algo com as peças capturadas:
//        this.chessMatch.getCapturedPieces(). . .
    }

    protected void finalizarPartida() {
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

        this.doWhenFinish.run();
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
        this.blackBg = ContextCompat.getDrawable(requireContext(), R.color.madeira_fundo);
        this.whiteBg = ContextCompat.getDrawable(requireContext(), R.color.madeira_bege);
        this.possibleBg = ContextCompat.getDrawable(requireContext(), R.drawable.circle);
        this.chequeBg = ContextCompat.getDrawable(requireContext(), R.color.teal_200);

        this.blackRook = ContextCompat.getDrawable(requireContext(), R.drawable.rook);
        this.blackKnight = ContextCompat.getDrawable(requireContext(), R.drawable.knight);
        this.blackBishop = ContextCompat.getDrawable(requireContext(), R.drawable.bishop);
        this.blackQueen = ContextCompat.getDrawable(requireContext(), R.drawable.queen);
        this.blackKing = ContextCompat.getDrawable(requireContext(), R.drawable.king);
        this.blackPawn = ContextCompat.getDrawable(requireContext(), R.drawable.pawn);

        this.whiteRook = ContextCompat.getDrawable(requireContext(), R.drawable.rook1);
        this.whiteKnight = ContextCompat.getDrawable(requireContext(), R.drawable.knight1);
        this.whiteBishop = ContextCompat.getDrawable(requireContext(), R.drawable.bishop1);
        this.whiteQueen = ContextCompat.getDrawable(requireContext(), R.drawable.queen1);
        this.whiteKing = ContextCompat.getDrawable(requireContext(), R.drawable.king1);
        this.whitePawn = ContextCompat.getDrawable(requireContext(), R.drawable.pawn1);

        this.updateView(null);
    }

    public List<Move> getMoves() {
        return chessMatch.getMoveDeque();
    }

    public ChessMatch getChessMatch() {
        return chessMatch;
    }

    protected abstract void positionCliked(int i, int j);

    protected abstract void onNeedPieceToPromote(ChessPosition source, ChessPosition target);
}