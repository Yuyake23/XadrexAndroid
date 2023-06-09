package com.example.xadrez.ui.network;


import com.example.xadrez.chess.ChessMatch;
import com.example.xadrez.chess.ChessPiece;
import com.example.xadrez.chess.Color;
import com.example.xadrez.ui.Terminal;
import com.example.xadrez.ui.network.enums.Description;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class NetworkTerminal extends Terminal {

	private final ServerSocket host;
	private final Socket client;
	private ObjectOutputStream saida;
	private ObjectInputStream entrada;

	public NetworkTerminal(Color playerColor, ServerSocket host, Socket client) {
		super(playerColor);
		this.host = host;
		this.client = client;
		try {
			this.saida = new ObjectOutputStream(client.getOutputStream());
			this.entrada = new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(3);
		}
	}

	@Override
	public String readSourcePosition() {
		String chessPosition = null;
		try {
			saida.reset();
			saida.writeUnshared(new Object[] { Description.SOURCE_POSITION });

			Object[] obj = (Object[]) entrada.readObject();
			if (obj[0] == Description.SOURCE_POSITION) {
				chessPosition = (String) obj[1];
			} else if (obj[0] == Description.EXCEPTION) {
				saida.reset();
				saida.writeUnshared(new Object[] { Description.MESSAGE, "Valid values are from a1 to h8." });
				return this.readSourcePosition();
			}
		} catch (IOException | ClassNotFoundException e) {
			releaseResources();
			e.printStackTrace();
			System.exit(3);
		}
		return chessPosition;
	}

	@Override
	public String readTargetPosition() {
		String chessPosition = null;
		try {
			saida.reset();
			saida.writeUnshared(
					new Object[] { Description.TARGET_POSITION});

			Object[] obj = (Object[]) entrada.readObject();
			if (obj[0] == Description.TARGET_POSITION) {
				chessPosition = (String) obj[1];
			} else if (obj[0] == Description.EXCEPTION) {
				saida.reset();
				saida.writeUnshared(new Object[] { Description.MESSAGE, "Valid values are from a1 to h8." });
				return this.readSourcePosition();
			}
		} catch (IOException | ClassNotFoundException e) {
			releaseResources();
			e.printStackTrace();
			System.exit(3);
		}
		return chessPosition;
	}

	@Override
	public String chosePieceTypeToPromotion() {
		String pieceType = null;
		try {
			saida.reset();
			saida.writeUnshared(new Object[] { Description.PIECE_TYPE_TO_PROMOTION });

			Object[] obj = (Object[]) entrada.readObject();
			if (obj[0] == Description.PIECE_TYPE_TO_PROMOTION) {
				pieceType = (String) obj[1];
			} else {
				System.exit(1);
			}
		} catch (IOException | ClassNotFoundException | NullPointerException e) {
			releaseResources();
			e.printStackTrace();
			System.exit(3);
		}
		return pieceType;
	}

	@Override
	public void message(String s) {
		try {
			saida.reset();
			saida.writeUnshared(new Object[] { Description.MESSAGE, s });
		} catch (IOException e) {
			releaseResources();
			e.printStackTrace();
			System.exit(3);
		}
	}

	@Override
	public void finish(ChessMatch chessMatch, List<ChessPiece> capturedPieces, Color winner) {
		try {
			saida.reset();
			saida.writeUnshared(new Object[] { Description.FINISH, chessMatch, capturedPieces, winner });

			releaseResources();
		} catch (IOException e) {
			releaseResources();
			e.printStackTrace();
			System.exit(3);
		}
	}

	@Override
	public void exceptionMessage(Exception e) {
		try {
			saida.reset();
			saida.writeUnshared(new Object[] { Description.EXCEPTION, e });
		} catch (IOException e1) {
			releaseResources();
			e.printStackTrace();
			System.exit(3);
		}
	}

	@Override
	public void update(ChessMatch chessMatch, List<ChessPiece> capturedPieces, boolean[][] possibleMoves) {
		try {
			saida.reset();
			saida.writeUnshared(new Object[] { Description.UPDATE, chessMatch, capturedPieces, possibleMoves });
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(3);
		}
	}

	private void releaseResources() {
		try {
			entrada.close();
			saida.close();
			client.close();
			host.close();
		} catch (IOException e) {
			System.out.println("Falha ao liberar recursos: " + e.getMessage());
		}
	}

}
