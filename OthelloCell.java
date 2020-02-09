import java.awt.Color;

import javax.swing.JButton;
import javax.swing.UIManager;

public class OthelloCell extends JButton{

	public static final int BLACK = 1;
	public static final int WHITE = -1;
	public static final int EMPTY = 0;

	private OthelloCell[] neighbor = new OthelloCell[8];

	private int myColor;
	private int posX=-1;
	private int posY=-1;
	
	

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosX(int x) {
		posX=x;
	}

	public void setPosY(int y) {
		posY=y;
	}

	public OthelloCell() {
		try {
			UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		} catch (Exception e) {
			e.printStackTrace();
		}
		setBackground(Color.GREEN);

	}

	private boolean isSameColor(int color) {
		return (myColor!=EMPTY && color==myColor);
	}

	private boolean IamEmpty() {
		return (myColor==EMPTY);
	}

	public int getColorState() {
		return myColor;
	}

	public void setColorState(int state) {
		myColor = state;
	}

	public void drawCell() {
		try {
			UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(myColor==BLACK) {setBackground(Color.GREEN);setForeground(Color.BLACK);setText("●");return;}
		if(myColor==WHITE) {setBackground(Color.GREEN);setForeground(Color.WHITE);setText("●");return;}
		setText("");
	}

	public void hintCell(int hintColor,int k) {
		try {
			UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(hintColor==BLACK) {setBackground(Color.BLACK);setForeground(Color.WHITE);setText(Integer.toString(k));return;}
		if(hintColor==WHITE) {setBackground(Color.WHITE);setForeground(Color.BLACK);setText(Integer.toString(k));return;}
		setText("");
	}

	public void redrawCell() {
		try {
			UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		} catch (Exception e) {
			e.printStackTrace();
		}
		setBackground(Color.GREEN);
		drawCell();
	}

	public void setNeighbors(OthelloCell[] cells) {
		for(int i=0; i<cells.length; i++) {
			neighbor[i] = cells[i];
		}
	}

	public int countReversible(int puttingColor) {
		int count=0;
		if(!IamEmpty()) return 0;
		if(puttingColor!=BLACK && puttingColor!=WHITE) return 0;
		for(int dir=0; dir<8; dir++) {
			if(neighbor[dir]!=null) {
				count += neighbor[dir].countReversible_sub(puttingColor,dir,0);
			}
		}
		return count;
	}

	public int countReversible_sub(int puttingColor, int direction, int count) {
		if(IamEmpty()) return 0;
		if(isSameColor(puttingColor)) return count;
		if(neighbor[direction]==null) return 0;
		return neighbor[direction].countReversible_sub(puttingColor, direction, count+1);
	}

	public boolean reverse(int puttingColor, boolean doPaint) {
		boolean done=false;
		if(!IamEmpty()) return done;
		if(puttingColor!=BLACK && puttingColor!=WHITE) return done;

		for(int dir=0; dir<8; dir++) {
			if(neighbor[dir]!=null) {
				int k=neighbor[dir].countReversible_sub(puttingColor,dir,0);
				//System.out.println(k);
				if(k>0) {
					neighbor[dir].reverse_sub(puttingColor, dir,doPaint);
					done=true;
				}
			}
		}

		if(done) {myColor=puttingColor;}
		if(doPaint)drawCell();
		
		return done;
	}

	private void reverse_sub(int puttingColor, int direction, boolean doPaint) {
		if(IamEmpty()) return;
		if(isSameColor(puttingColor)) return;
		if(neighbor[direction]==null) return;
		myColor *= -1;
		if(doPaint) drawCell();
		neighbor[direction].reverse_sub(puttingColor, direction, doPaint);
	}
}
