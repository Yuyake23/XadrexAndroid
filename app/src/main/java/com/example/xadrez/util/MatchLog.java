package com.example.xadrez.util;

import com.example.xadrez.chess.Color;
import com.example.xadrez.chess.Move;

import java.sql.Timestamp;
import java.util.List;

public class MatchLog {
    private String username;
    private Timestamp timestamp;
    private Color winner;
    private List<Move> moves;

    public MatchLog() {
    }

    public MatchLog(String username, Timestamp timestamp, Color winner, List<Move> moves) {
        this.username = username;
        this.timestamp = timestamp;
        this.winner = winner;
        this.moves = moves;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Color getWinner() {
        return winner;
    }

    public void setWinner(Color winner) {
        this.winner = winner;
    }

    public List<Move> getMoves() {
        return moves;
    }

}
