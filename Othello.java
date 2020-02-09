import java.awt.Font;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

public class Othello {

	private int X_SIZE = 8;
	private int Y_SIZE = 8;

	private boolean HINT_USE = false;

	private OthelloCell[][] cell;
	private OthelloView gui;

	private boolean canClick=false;
	private int currentColor = OthelloCell.BLACK;

	private boolean COMPlay = false;
	private boolean COMFirst = false;
	private int COMColor = OthelloCell.BLACK;
	private String player1 = "BLACK ";
	private String player2 = "WHITE ";

	public Othello() {
		new Othello(8,8);
	}

	public Othello(int xSize, int ySize) {
		X_SIZE = xSize;
		Y_SIZE = ySize;
		initField();
	}

	private void initField() {
		cell = new OthelloCell[X_SIZE][Y_SIZE];
		for(int x=0; x<cell.length; x++) {
			for(int y=0; y<cell.length; y++) {
				cell[x][y] = new OthelloCell();
				cell[x][y].setPosX(x);
				cell[x][y].setPosY(y);
				cell[x][y].setFont(new Font("Arial", Font.PLAIN, 50));
			}
		}
		for(int x=0; x<cell.length; x++) {
			for(int y=0; y<cell.length; y++) {
				cell[x][y].setNeighbors(neighborCells(x,y));
			}
		}

		gui = new OthelloView(this);
		reset();
		canClick=true;
		gui.setVisible(true);
	}

	private void setGame(){
		this.COMFirst = false;
		this.COMPlay = false;
		
		String[] options = {"Two Players", "One Player"};
        int x = JOptionPane.showOptionDialog(null, "Please set number of Human Player！",
                "Player",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		
		if(x==0){
			System.out.println("Two Human Players Game");
			this.COMPlay = false;
			this.player1 = "BLACK ";
			this.player2 = "WHITE ";
		}else{
			System.out.println("One Human Player Game");
			this.COMPlay = true;
			String[] options2 = {"Me", "COMPUTER"};
			int y = JOptionPane.showOptionDialog(null, "Please Choose Player1(who will go first)！",
				"Player1",JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options2, options2[0]);
				System.out.println(y);
			if(y==0){
				System.out.println("COMPUTER：WHITE");
				this.COMColor = OthelloCell.WHITE;
				this.player1 = "BLACK-YOU";
				this.player2 = "WHITE-COMPUTER";
			}else{
				System.out.println("COMPUTER: BLACK!");
				this.COMColor = OthelloCell.BLACK;
				this.player2 = "WHITE-YOU";
				this.player1 = "BLACK-COMPUTER";
				this.COMFirst = true;
			}
		}
	}

	public void reset() {
		gui.setText("Othello");
		for(int x=0; x<cell.length; x++) {
			for(int y=0; y<cell.length; y++) {
				cell[x][y].setColorState(OthelloCell.EMPTY);
			}
		}
		cell[3][3].setColorState(OthelloCell.WHITE);
		cell[4][4].setColorState(OthelloCell.WHITE);
		cell[3][4].setColorState(OthelloCell.BLACK);
		cell[4][3].setColorState(OthelloCell.BLACK);

		repaintCells();
		gui.setResult(2, 2);
		setGame();
		currentColor = OthelloCell.WHITE;
		changeTurnColor();
		canClick=true;
		if(COMFirst) COM();
	}

	public void getHint() {
		HINT_USE = true;
		canPut(currentColor,HINT_USE);
		//HINT_USE = false;
	}

	public void stopHint() {
		HINT_USE = false;
		canPut(currentColor,HINT_USE);
	}

	private int[][] getCellColors(){
		int[][] cellColors = null;
		for(int x=0; x<cell.length; x++) {
			for(int y=0; y<cell.length; y++) {
				cellColors[x][y]=cell[x][y].getColorState();
			}
		}
		return cellColors;
	}

	private void setCellColors(int[][] colorState) {
		for(int x=0; x<cell.length; x++) {
			for(int y=0; y<cell[0].length; y++) {
				cell[x][y].setColorState(colorState[x][y]);
			}
		}
	}

	public OthelloCell[][] getCells(){
		return cell;
	}

	public OthelloCell getCellAt(int x,int y) {
		if(x>=0 && x<X_SIZE && y>=0 && y<Y_SIZE) {
			return cell[x][y];
		}
		return null;
	}

	private boolean canPut(int puttingColor,boolean hint) {
		int count=0;
		boolean result;

		for(int x=0; x<8; x++) {
			for(int y=0; y<8; y++) {
				int k = cell[x][y].countReversible(puttingColor);
				if(k>0){
					count+=1;
					if(hint) {
						cell[x][y].hintCell(puttingColor,k);
						
					}
					else cell[x][y].redrawCell();
				}else {
					cell[x][y].redrawCell();
				}
			}
		}
		//stopHint();
		if(count>0) {
			result = true;
			//System.out.println("canPut: "+count);
		}
		else {
			result = false;
			//System.out.println("canPut: FALSE");
		}
		
		return result;
	}

	boolean changeTurnColor() {
		currentColor *= -1;
		if(canPut(currentColor,HINT_USE)) {
			if(gui!=null) {
				gui.setText((currentColor==OthelloCell.BLACK?player1:player2));
			}
			return true;
		}

		if(canPut(currentColor*-1,HINT_USE)) {
			if(gui!=null) {
				gui.setText((currentColor==OthelloCell.BLACK?player1:player2)+" cannot put, so SKIP!");
				JOptionPane.showMessageDialog(gui, (currentColor==OthelloCell.BLACK?player1:player2)+" cannot put, so SKIP!");
			}
			currentColor*=-1;
			if(gui!=null) {
				gui.setText((currentColor==OthelloCell.BLACK?player1:player2));
			}
		}else {
			if(gui!=null) {
				JOptionPane.showMessageDialog(gui, "Finish!");
			}
			return false;
		}
		return true;
	}

	int[] countStone() {
		int[] result = new int[2];
		for(int x=0; x<cell.length; x++) {
			for(int y=0; y<cell[0].length; y++) {
				if(cell[x][y].getColorState()==OthelloCell.EMPTY) continue;
				if(cell[x][y].getColorState()==OthelloCell.BLACK)
					result[0]+=1;
				else
					result[1]+=1;
			}
		}
		return result;
	}

	public void COM(){
		int max=0;
		int xmax=0,ymax=0;
		for(int x=0; x<8; x++) {
			for(int y=0; y<8; y++) {
				int k = cell[x][y].countReversible(currentColor);
				if(k>0){
					if(max<k) {
						max = k;
						xmax = x; ymax = y;
					}
				}else {
					;
				}
			}
		}
		System.out.println("COM: (x="+xmax+" ; y="+ymax+") GOTTEN MASK: "+max);
		OthelloCell clickedCell = getCellAt(xmax,ymax);

		if(clickedCell.reverse(currentColor,true)) {
			int[] BW = countStone();
			gui.setResult(BW[0], BW[1]);
			if(!changeTurnColor()) {
				canClick = false;

				gui.setText(player1+BW[0]+", "+player2+BW[1]);
				String s = player1+" - WIN！";
				int d=BW[0]-BW[1];
				if(d<0) s = player2+"-  WIN!";
				if(d==0) s = "Draw!";
				gui.setText(s);
				JOptionPane.showMessageDialog(gui, player1+BW[0]+", "+player2+BW[1]);
				System.out.println(player1+BW[0]+", "+player2+BW[1]+ ":"+s);
			}
		}

	}

	public void action(int x,int y) {
		if(!canClick) { return;}
		OthelloCell clickedCell = getCellAt(x,y);
		//System.out.println("currentColorYou:"+currentColor+"(x,y):"+x+","+y);
		if(clickedCell.reverse(currentColor,true)) {
			int[] BW = countStone();
			gui.setResult(BW[0], BW[1]);
			if(!changeTurnColor()) {
				canClick = false;

				gui.setText(player1+BW[0]+", "+player2+BW[1]);
				String s = player1+" - WIN！";
				int d=BW[0]-BW[1];
				if(d<0) s = player2+"-  WIN!";
				if(d==0) s = "Draw!";
				gui.setText(s);
				JOptionPane.showMessageDialog(gui, player1+BW[0]+", "+player2+BW[1]);
			}
		}
		if(COMPlay && currentColor==COMColor) {
			COM();
		}
	}

	public OthelloCell[] neighborCells(int x, int y) {
		OthelloCell[] neighbor = new OthelloCell[8];
		neighbor[0] = getCellAt(x-1,y-1);
		neighbor[1] = getCellAt(x  ,y-1);
		neighbor[2] = getCellAt(x+1,y-1);
		neighbor[3] = getCellAt(x-1,y  );
		neighbor[4] = getCellAt(x+1,y  );
		neighbor[5] = getCellAt(x-1,y+1);
		neighbor[6] = getCellAt(x  ,y+1);
		neighbor[7] = getCellAt(x+1,y+1);

		return neighbor;
	}

	public void repaintCells() {
		for(int x=0; x<cell.length; x++) {
			for(int y=0; y<cell[0].length; y++) {
				cell[x][y].drawCell();
			}
		}
	}

	public static void main(String[] args) {
		new Othello();
	}
}
