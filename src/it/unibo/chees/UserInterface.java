package it.unibo.chees;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class UserInterface extends JPanel implements MouseListener,
		MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//dimensione dei quadrati
	static int squareSize = 90;
	//posizionamento del mouse
	static int mouseX, mouseY, newMouseX, newMouseY;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.blue);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		// disegna la scachiera
		for (int i = 0; i < 64; i += 2) {
			g.setColor(new Color(255, 200, 100));
			g.fillRect((i % 8 + (i / 8) % 2) * squareSize,
					(i / 8) * squareSize, squareSize, squareSize);
			g.setColor(new Color(150, 50, 30));
			g.fillRect(((i + 1) % 8 - ((i + 1) / 8) % 2) * squareSize,
					((i + 1) / 8) * squareSize, squareSize, squareSize);
		}

		Image chessPiecesImage;
		chessPiecesImage = new ImageIcon("/home/asd/EclipseWorkspace/Chess4AI/IMG/ChessPieces.png").getImage();
		//Image whitePawn =  new ImageIcon("/home/asd/EclipseWorkspace/Chess4AI/IMG/whitePawn.png").getImage();
				


		for (int i=0;i<64;i++) {
            int j=-1,k=-1;
            switch (AlphaBetaChess.chessBoard[i/8][i%8]) {
                case "P": j=5; k=0;
                    break;
                case "p": j=5; k=1;
                    break;
                case "A": j=0; k=0;
                    break;
                case "a": j=0; k=1;
                    break;
            }
            if (j!=-1 && k!=-1) {
                g.drawImage(chessPiecesImage, (i%8)*squareSize, (i/8)*squareSize, (i%8+1)*squareSize, (i/8+1)*squareSize, j*64, k*64, (j+1)*64, (k+1)*64, this);
            }
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {}
    
    @Override
    //prendo la posizione del clic se sono dentro i bordi
    public void mousePressed(MouseEvent e) {
        if ( e.getX() < (8*squareSize) && e.getY() < (8*squareSize)) {
            mouseX=e.getX();
            mouseY=e.getY();
            repaint();
        }
    }
    
    //rilascio il pezzo
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getX()<8*squareSize &&e.getY()<8*squareSize) {
            //se sono dentro la sacchiera
            newMouseX=e.getX();
            newMouseY=e.getY();
            if (e.getButton() == MouseEvent.BUTTON1) {
                String dragMove="";
                if (newMouseY/squareSize==0 && mouseY/squareSize==1 && "P".equals(AlphaBetaChess.chessBoard[mouseY/squareSize][mouseX/squareSize])) {
                    //pawn promotion
                	dragMove=""+mouseY/squareSize+mouseX/squareSize+newMouseY/squareSize+newMouseX/squareSize+AlphaBetaChess.chessBoard[newMouseY/squareSize][newMouseX/squareSize];
                    AlphaBetaChess.makeMove(dragMove);
                    repaint();
                	JOptionPane.showMessageDialog(null, "Hai Vinto!");
                	System.out.println("Hai vinto!");
                	System.exit(0);
                } else {
                    //regular move
                    dragMove=""+mouseY/squareSize+mouseX/squareSize+newMouseY/squareSize+newMouseX/squareSize+AlphaBetaChess.chessBoard[newMouseY/squareSize][newMouseX/squareSize];
                }
                String userPosibilities=AlphaBetaChess.posibleMoves();
                if (userPosibilities.replaceAll(dragMove, "").length()<userPosibilities.length()) {
                    //if valid move
                    AlphaBetaChess.makeMove(dragMove);
                    AlphaBetaChess.flipBoard();
                    AlphaBetaChess.makeMove(AlphaBetaChess.alphaBeta(AlphaBetaChess.globalDepth, 1000000, -1000000, "", 0));
                    AlphaBetaChess.flipBoard();
                    repaint();
                    AlphaBetaChess.blackWins();
                    AlphaBetaChess.isPatta();
                    //AlphaBetaChess.printChessBoard();
                }
            }
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}