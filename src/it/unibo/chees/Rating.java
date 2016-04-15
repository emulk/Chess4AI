package it.unibo.chees;

//presi da
// http://chessprogramming.wikispaces.com/Simplified+evaluation+function
public class Rating {
	//i pesi per ogni pezzo
	static int pawnBoard[][] = { 
			{ 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000 },
			{ 50, 50, 50, 50, 50, 50, 50, 50 },
			{ 10, 10, 20, 30, 30, 20, 10, 10 }, 
			{ 5, 5, 10, 25, 25, 10, 5, 5 },
			{ 0, 0, 0, 20, 20, 0, 0, 0 }, 
			{ 5, -5, -10, 0, 0, -10, -5, 5 },
			{ 5, 10, 10, -20, -20, 10, 10, 5 },
			{ 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000 } };

	static int kingMidBoard[][] = { 
			{ -10, -20, -30, -30, -30, -30, -20, -10 },
			{ -20, -20, -10, -10, -10, -10, -20, -20 },
			{ -20, -20, -20, -20, -20, -20, -20, -20 },
			{ -10, -20, -20, -30, -30, -20, -20, -10 },
			{ -20, -30, -30, -40, -40, -30, -30, -20 },
			{ -10, -20, -20, -20, -20, -20, -20, -10 },
			{ 20, 20, 0, 0, 0, 0, 20, 20 }, 
			{ 20, 30, 10, 0, 0, 10, 30, 20 } };

	static int kingEndBoard[][] = { 
			{ 10, 10, 10, 0, 0, 10, 10, 10 },
			{ 30, 20, 10, 0, 0, 10, 20, 30 },
			{ -30, -10, 20, 30, 30, 20, -10, -30 },
			{ -30, -10, 20, 10, 10, 20, -10, -30 },
			{ -30, -10, 20, 10, 10, 20, -10, -30 },
			{ -30, -10, 20, 30, 30, 20, -10, -30 },
			{ -30, -30, 0, 0, 0, 0, -30, -30 },
			{ -50, -30, -30, -30, -30, -30, -30, -50 } };

	public static int rating(int list, int depth) {
		int counter = 0, material = rateMaterial();
		//controlla se il re puo essere attaccato
		counter += rateAttack();
		counter += material;
		//counter += rateMoveablitly(list, depth, material);
		counter += ratePositional(material);
		AlphaBetaChess.flipBoard();
		material = rateMaterial();
		counter -= rateAttack();
		counter -= material;
		//counter -= rateMoveablitly(list, depth, material);
		counter -= ratePositional(material);
		AlphaBetaChess.flipBoard();
		return -(counter + depth * 50);
	}
	
	//Controllo se il re puo essere attaccato da qualche posizione
	public static int rateAttack() {
		int counter = 0;
		int tempPositionC = AlphaBetaChess.kingPositionC;
		for (int i = 0; i < 64; i++) {
			switch (AlphaBetaChess.chessBoard[i / 8][i % 8]) {
			case "P": {
				AlphaBetaChess.kingPositionC = i;
				if (!AlphaBetaChess.kingSafe()) {
					counter -= 64;
				}
			}
				break;
			}
		}
		AlphaBetaChess.kingPositionC = tempPositionC;
		if (!AlphaBetaChess.kingSafe()) {
			counter -= 200;
		}
		return counter / 2;
	}

	/**assegna 100 punti ad ogni pedone **/
	public static int rateMaterial(){
		int counter = 0;
		for(int row=0; row<8; row++){
			for(int col=0; col < 8; col++){
				if("P".equals(AlphaBetaChess.chessBoard[row][col])){
					counter+=100;
				}
			}
		}
		return counter;
	}


	public static int rateMoveablitly(int listLength, int depth, int material) {
		int counter = 0;
		counter += listLength;// 5 per ogni mossa valida
		if (listLength == 0) {// checkmate or stalemate
			if (!AlphaBetaChess.kingSafe()) {// scacco matto
				counter += -200000 * depth;
			} else {// stalemate: finisce pari
				counter += -150000 * depth;
			}
		}
		return counter;
	}

	//valuta la posizione di ogni pezzo
	public static int ratePositional(int material) {
		int counter = 0;
		for (int i = 0; i < 64; i++) {
			switch (AlphaBetaChess.chessBoard[i / 8][i % 8]) {
			case "P":
				counter += pawnBoard[i / 8][i % 8];
				break;
			case "A":
				if (material >= 1750) {
					counter += kingMidBoard[i / 8][i % 8];
					counter += AlphaBetaChess.posibleA(
							AlphaBetaChess.kingPositionC).length() * 10;
				} else {
					counter += kingEndBoard[i / 8][i % 8];
					counter += AlphaBetaChess.posibleA(
							AlphaBetaChess.kingPositionC).length() * 30;
				}
				break;
			}
		}
		return counter;
	}
}