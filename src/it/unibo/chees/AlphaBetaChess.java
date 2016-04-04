package it.unibo.chees;

import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * peci=BIANCHI/neri
 * pedoni=P/p
 * re=A/a

 * 
 * (1234p rappresenta riga1,colonna2 muove in riga3, colonna4 e mangia p (uno spazio sinifica che non mangio nulla))
 */
public class AlphaBetaChess {
	//schacchiera iniziale, matrice contenente tutti i pezzi
	static String chessBoard[][]={
        {" "," "," ","a"," "," "," "," "},
        {"p","p","p","p","p","p","p","p"},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {"P","P","P","P","P","P","P","P"},
        {" "," "," "," ","A"," "," "," "}};
	//posizione del re bianco
    static int kingPositionC=0;
    //posizione del re nero
    static int kingPositionL=0;
    //1 gioca prima il bianco, 0 gioca prima il nero
    static int humanAsWhite =0;
    static int globalDepth=4;
    public static void main(String[] args) {
    	//Ciclo la matrice per trovare la posizione dei re
    	for(int i =0; i<8; i++){
    		for(int j=0; j<8; j++){
    			if("A".equals(chessBoard[i][j])){
    				kingPositionC=(i*8)+j;
    			}
    			if("a".equals(chessBoard[i][j])){
    				kingPositionL=(i*8)+j;
    			}
    		}
    	}

        //titolo window
        JFrame f=new JFrame("Chess4AI");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UserInterface ui=new UserInterface();
        f.add(ui);
        //dimensione della schachiera
        f.setSize(720, 750);
        f.setVisible(true);
        
        //System.out.println(sortMoves(posibleMoves()));
        Object[] option={"Computer","Umano"};
        humanAsWhite=JOptionPane.showOptionDialog(null, "Chi muove prima?", "Opzioni", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
        if (humanAsWhite==0) {
        	//muovono prima i neri
            //long startTime=System.currentTimeMillis();
        	// beta + infinito, alpha= -infinito
            makeMove(alphaBeta(globalDepth, 1000000, -1000000, "", 0));
            //long endTime=System.currentTimeMillis();
            //System.out.println("That took "+(endTime-startTime)+" milliseconds");
            flipBoard();
            f.repaint();
        }

        /*
        for (int i=0;i<8;i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
        */
    }
    //4 beta alpha vuoto 0
    public static String alphaBeta(int depth, int beta, int alpha, String move, int player) {
        //ritorna cle mosse possibili ome 1234p##########
        String list=posibleMoves();
        if (depth==0 || list.length()==0) {
        	//se gioca l'IA ritorna un valore negativo altrimenti positivo
        	return move+(Rating.rating(list.length(), depth)*(player*2-1));
        }
        
        list=sortMoves(list);
        player=1-player;//1 umano 0 computer
        for (int i=0;i<list.length();i+=5) {
            makeMove(list.substring(i,i+5));
            flipBoard();
            String returnString=alphaBeta(depth-1, beta, alpha, list.substring(i,i+5), player);
            //prende il rating della mosssa
            int value=Integer.valueOf(returnString.substring(5));
            flipBoard();
            undoMove(list.substring(i,i+5));
            if (player==0) {
                if (value <= beta) {
                	beta=value; 
                	if (depth == globalDepth) {
                		move=returnString.substring(0,5);
                	}
                }
            } else {
                if (value > alpha) {
                	alpha=value; 
                	if (depth==globalDepth) {
                		move=returnString.substring(0,5);
                	}
                }
            }
            if (alpha >= beta) {
                if (player==0) {
                	return move+beta;
                } else {
                	return move+alpha;
                }
            }
        }
        
        if (player==0) {
        	return move+beta;
        } else {
        	return move+alpha;
        }

    }
    
    //indicano quando i neri vincono
    public static void blackWins(){
    	for(int j=0; j< 8; j++){
        	if("p".equals(chessBoard[7][j])){
        		JOptionPane.showMessageDialog(null, "Hai Perso!");
        		System.out.println("I Neri hanno vinto");
        		System.exit(0);
        	}else if("p".equals(chessBoard[7][j]) && " ".equals(chessBoard[7][j])){
        		//se sono nella riga 6 ma non ho nessuno davanti ho vinto lo stesso
        		JOptionPane.showMessageDialog(null, "Hai Perso!");
        		System.out.println("I Neri hanno vinto");
        		System.exit(0);
        	}
        	
        }
    }
    
    //avvisa quando la partita è in patta
    public static void isPatta(){
    	int pedoni =0;
    	for(int i =0; i<8; i++){
    		for(int j=0; j<8; j++){
    			if("P".equals(chessBoard[i][j])){
    				pedoni++;
    			}
    			if("p".equals(chessBoard[i][j])){
    				pedoni++;
    			}
    		}
    	}
    	if(pedoni == 0){
    		JOptionPane.showMessageDialog(null, "Patta!");
    		System.out.println("Finisce in parita");
    		System.exit(0);
    	}
    	
    }
    //funzione che stampa la scacchiera
    public static void printChessBoard(){
        for (int i=0;i<8;i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }
    //gira la schacchiera ogni volta che viene chiamata
    public static void flipBoard() {
        String temp;
        for (int i=0;i<32;i++) {
            int r=i/8, c=i%8;
            if (Character.isUpperCase(chessBoard[r][c].charAt(0))) {
                temp=chessBoard[r][c].toLowerCase();
            } else {
                temp=chessBoard[r][c].toUpperCase();
            }
            if (Character.isUpperCase(chessBoard[7-r][7-c].charAt(0))) {
                chessBoard[r][c]=chessBoard[7-r][7-c].toLowerCase();
            } else {
                chessBoard[r][c]=chessBoard[7-r][7-c].toUpperCase();
            }
            chessBoard[7-r][7-c]=temp;
        }
        int kingTemp=kingPositionC;
        kingPositionC=63-kingPositionL;
        kingPositionL=63-kingTemp;
    }
    
    //fa una mossa
    public static void makeMove(String move) {
        if (move.charAt(4)!='P') {
            chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
            chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=" ";
            if ("A".equals(chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])) {
                kingPositionC=8*Character.getNumericValue(move.charAt(2))+Character.getNumericValue(move.charAt(3));
            }
        } else {
            //promozione del pedone
            chessBoard[1][Character.getNumericValue(move.charAt(0))]=" ";
            chessBoard[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(3));
        }
    }
    //torna indietro con la mossa
    public static void undoMove(String move) {
        if (move.charAt(4)!='P') {
            chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
            chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=String.valueOf(move.charAt(4));
            if ("A".equals(chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))])) {
                kingPositionC=8*Character.getNumericValue(move.charAt(0))+Character.getNumericValue(move.charAt(1));
            }
        } else {
            //promozione del pedone
            chessBoard[1][Character.getNumericValue(move.charAt(0))]="P";
            chessBoard[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(2));
        }
    }
    //indica le possibili mosse dei pedoni e del re
    public static String posibleMoves() {
        String list="";
        for (int i=0; i<64; i++) {
            switch (chessBoard[i/8][i%8]) {
                case "P": list+=posibleP(i);
                    break;
                case "A": list+=posibleA(i);
                    break;
            }
        }
        return list;//x1,y1,x2,y2,pezzo
    }
    public static String posibleP(int i) {
        String list="", oldPiece;
        int r=i/8, c=i%8;
        for (int j=-1; j<=1; j+=2) {
            try {//capture
                if (Character.isLowerCase(chessBoard[r-1][c+j].charAt(0)) && i>=16) {
                    oldPiece=chessBoard[r-1][c+j];
                    chessBoard[r][c]=" ";
                    chessBoard[r-1][c+j]="P";
                    if (kingSafe()) {
                        list=list+r+c+(r-1)+(c+j)+oldPiece;
                    }
                    chessBoard[r][c]="P";
                    chessBoard[r-1][c+j]=oldPiece;
                }
            } catch (Exception e) {}
            try {//promotion && capture
                if (Character.isLowerCase(chessBoard[r-1][c+j].charAt(0)) && i<16) {
                	oldPiece=chessBoard[r-1][c];
                    chessBoard[r][c]=" ";
                    chessBoard[r-1][c]="P";
                    if (kingSafe()) {
                        list=list+r+c+(r-1)+c+oldPiece;
                    }
                    chessBoard[r][c]="P";
                    chessBoard[r-1][c]=oldPiece;
                }
            } catch (Exception e) {}
        }
        try {//muovo il pedone di uno
            if (" ".equals(chessBoard[r-1][c]) && i>=16) {
                oldPiece=chessBoard[r-1][c];
                chessBoard[r][c]=" ";
                chessBoard[r-1][c]="P";
                if (kingSafe()) {
                    list=list+r+c+(r-1)+c+oldPiece;
                }
                chessBoard[r][c]="P";
                chessBoard[r-1][c]=oldPiece;
            }
        } catch (Exception e) {}
        try {//promotion && no capture

            if (" ".equals(chessBoard[r-1][c]) && i<16) {
                String[] temp={"Q","R","B","K"};
                for (int k=0; k<4; k++) {
                	oldPiece=chessBoard[r-1][c];
                    chessBoard[r][c]=" ";
                    chessBoard[r-1][c]="P";
                    if (kingSafe()) {
                        list=list+r+c+(r-1)+c+oldPiece;
                    }
                    chessBoard[r][c]="P";
                    chessBoard[r-1][c]=oldPiece;
                }
            }
        } catch (Exception e) {}
        try {//move two up
            if (" ".equals(chessBoard[r-1][c]) && " ".equals(chessBoard[r-2][c]) && i>=48) {
                oldPiece=chessBoard[r-2][c];
                chessBoard[r][c]=" ";
                chessBoard[r-2][c]="P";
                if (kingSafe()) {
                    list=list+r+c+(r-2)+c+oldPiece;
                }
                chessBoard[r][c]="P";
                chessBoard[r-2][c]=oldPiece;
            }
        } catch (Exception e) {}
        return list;
    }
    
    //indica le possibili mosse del re
    public static String posibleA(int i) {
        String list="", oldPiece;
        int r=i/8, c=i%8;
        for (int j=0;j<9;j++) {
            if (j!=4) {
                try {
                    if (Character.isLowerCase(chessBoard[r-1+j/3][c-1+j%3].charAt(0)) || " ".equals(chessBoard[r-1+j/3][c-1+j%3])) {
                        oldPiece=chessBoard[r-1+j/3][c-1+j%3];
                        chessBoard[r][c]=" ";
                        chessBoard[r-1+j/3][c-1+j%3]="A";
                        int kingTemp=kingPositionC;
                        kingPositionC=i+(j/3)*8+j%3-9;
                        if (kingSafe()) {
                            list=list+r+c+(r-1+j/3)+(c-1+j%3)+oldPiece;
                        }
                        chessBoard[r][c]="A";
                        chessBoard[r-1+j/3][c-1+j%3]=oldPiece;
                        kingPositionC=kingTemp;
                    }
                } catch (Exception e) {}
            }
        }
        return list;
    }
    
    //ordina le mosse in base alla bontà
    public static String sortMoves(String list) {
    	int lenLista = list.length();
        int[] score=new int [list.length()/5];
        //valuta ogni mossa possibile
        for (int i=0; i<lenLista; i+=5) {
            makeMove(list.substring(i, i+5));
            score[i/5]=-Rating.rating(-1, 0);
            undoMove(list.substring(i, i+5));
        }
        
        String newListA="", newListB=list;
        for (int i=0;i<Math.min(6, list.length()/5);i++) {//solo le prime mose
            int max=-1000000, maxLocation=0;
            for (int j=0;j<list.length()/5;j++) {
                if (score[j]>max) {
                	max=score[j]; 
                	maxLocation=j;
                }
            }
            score[maxLocation]=-1000000;
            //la mossa migliore
            newListA+=list.substring(maxLocation*5,maxLocation*5+5);
            // rimpiazza la mossa migliore con vuoto
            newListB=newListB.replace(list.substring(maxLocation*5,maxLocation*5+5), "");
        }
        return newListA+newListB;
    }
    
    //controllo se il re è a riscchio oppure no
    public static boolean kingSafe() {
       
        //se ho un pedone nero davanti sulla sinistra non sono al sicuro
        if (kingPositionC>=16) {
            try {
                if ("p".equals(chessBoard[kingPositionC/8-1][kingPositionC%8-1])) {
                    return false;
                }
            } catch (Exception e) {}
            //se ho un pedone nero davati sulla destra non sono al sicuro
            try {
                if ("p".equals(chessBoard[kingPositionC/8-1][kingPositionC%8+1])) {
                    return false;
                }
            } catch (Exception e) {}
            //se ho il re nero intorno al mio re non sono al sicuro
            for (int i=-1; i<=1; i++) {
                for (int j=-1; j<=1; j++) {
                    if (i!=0 || j!=0) {
                        try {
                            if ("a".equals(chessBoard[kingPositionC/8+i][kingPositionC%8+j])) {
                                return false;
                            }
                        } catch (Exception e) {}
                    }
                }
            }
        }
        return true;
    }
}