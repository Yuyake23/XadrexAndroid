package com.example.xadrez;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.xadrez.boardgame.Position;
import com.example.xadrez.chess.ChessException;
import com.example.xadrez.chess.ChessMatch;
import com.example.xadrez.chess.ChessPiece;
import com.example.xadrez.chess.ChessPosition;
import com.example.xadrez.chess.Color;
import com.example.xadrez.chess.pieces.PieceType;

import java.util.Objects;


public class BoardFragment extends Fragment {

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
    private ChessMatch chessMatch = new ChessMatch(this::chosePieceTypeToPromotion);

    private ChessPosition sourcePosition;
    private ChessPosition targetPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                this.chessMatch.performChessMove(sourcePosition, targetPosition, null);
                sourcePosition = targetPosition = null;
                this.updateView(null);
            }

        } catch (ChessException e) {
            sourcePosition = targetPosition = null;
            updateView(null);
        }

        if (chessMatch.matchIsOver()) {
//         TODO: . . .
        }

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
                    this.pieces[i][j].setBackgroundDrawable(
                            ContextCompat.getDrawable(requireContext(), R.color.purple_700));
                } else if (sourcePosition != null && sourcePosition.toPosition().isIn(i, j)) {
//                    TODO: Colocar efeito de fundo
//                    Aqui é a peça selecionada para mover
                    this.pieces[i][j].setBackgroundDrawable(
                            ContextCompat.getDrawable(requireContext(), R.color.purple_200));
                } else {
                    this.pieces[i][j].setBackgroundDrawable((i % 2 == j % 2) ? whiteBg : blackBg);
                }

            }
        }
        if(chessMatch.getCheck()){
            Position position = chessMatch.king(chessMatch.getCurrentPlayer()).getPosition();
            // TODO: Colocar efeito do rei em check
            this.pieces[position.getRow()][position.getColumn()].setBackgroundDrawable(
                    ContextCompat.getDrawable(requireContext(), R.color.teal_200)
            );
        }
    }

    private void finalizarPartida() {
        salvarLog();
        // TODO: . . .
    }

    private void salvarLog() {
        // TODO: . . .
    }

    private Drawable selectPieceDrawable(ChessPiece piece) {
        if (piece == null)
            return null;
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

    private String chosePieceTypeToPromotion() {
        // TODO: Abrir janela para o usuário escolher a peça a ser promovida
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
}