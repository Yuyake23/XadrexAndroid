package com.example.xadrez;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class BoardFragment extends Fragment {

    private final AppCompatImageButton[][] pieces = new AppCompatImageButton[8][8];

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

                gridLayout.addView(cp);
                this.pieces[i][j] = (AppCompatImageButton) cp;
            }
        }

        configureSetup();

        return rootView;
    }

    private void configureSetup() {
        Context cnt = getContext();
        assert cnt != null;
        Drawable blackHook = ContextCompat.getDrawable(cnt, R.drawable.hook);
        Drawable blackKnight = ContextCompat.getDrawable(cnt, R.drawable.knight);
        Drawable blackBishop = ContextCompat.getDrawable(cnt, R.drawable.bishop);
        Drawable blackQueen = ContextCompat.getDrawable(cnt, R.drawable.queen);
        Drawable blackKing = ContextCompat.getDrawable(cnt, R.drawable.king);
        Drawable blackPawn = ContextCompat.getDrawable(cnt, R.drawable.pawn);

        Drawable whiteHook = ContextCompat.getDrawable(cnt, R.drawable.hook1);
        Drawable whiteKnight = ContextCompat.getDrawable(cnt, R.drawable.knight1);
        Drawable whiteBishop = ContextCompat.getDrawable(cnt, R.drawable.bishop1);
        Drawable whiteQueen = ContextCompat.getDrawable(cnt, R.drawable.queen1);
        Drawable whiteKing = ContextCompat.getDrawable(cnt, R.drawable.king1);
        Drawable whitePawn = ContextCompat.getDrawable(cnt, R.drawable.pawn1);

        pieces[0][0].setImageDrawable(blackHook);
        pieces[0][1].setImageDrawable(blackKnight);
        pieces[0][2].setImageDrawable(blackBishop);
        pieces[0][3].setImageDrawable(blackQueen);
        pieces[0][4].setImageDrawable(blackKing);
        pieces[0][5].setImageDrawable(blackBishop);
        pieces[0][6].setImageDrawable(blackKnight);
        pieces[0][7].setImageDrawable(blackHook);
        for (AppCompatImageButton piece : pieces[1]) {
            piece.setImageDrawable(blackPawn);
        }

        pieces[7][0].setImageDrawable(whiteHook);
        pieces[7][1].setImageDrawable(whiteKnight);
        pieces[7][2].setImageDrawable(whiteBishop);
        pieces[7][3].setImageDrawable(whiteQueen);
        pieces[7][4].setImageDrawable(whiteKing);
        pieces[7][5].setImageDrawable(whiteBishop);
        pieces[7][6].setImageDrawable(whiteKnight);
        pieces[7][7].setImageDrawable(whiteHook);
        for (AppCompatImageButton piece : pieces[6]) {
            piece.setImageDrawable(whitePawn);
        }

    }
}