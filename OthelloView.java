import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.swing.*;

public class OthelloView extends JFrame {

	private JTextField txtMessage;
	private JTextField blackResult;
	private JTextField whiteResult;

	private Othello game;

	private JPanel gameField;
	private JPanel messageField;
	private JPanel buttonField;

	OthelloView(Othello parent){
		
		 try {
			    UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
			 } catch (Exception e) {
			            e.printStackTrace();
			 }

		game = parent;
		JButton[][] cells = game.getCells();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Othello");
		setBounds(100,80,850,650);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		contentPane.setLayout(new BorderLayout(0,0));
		setContentPane(contentPane);

		gameField = new JPanel();
		contentPane.add(gameField, BorderLayout.CENTER);
		gameField.setLayout(new GridLayout(8,8,0,0));

		messageField = new JPanel();
		messageField.setLayout(new GridLayout(1,3,0,0));


		blackResult = new JTextField();
		blackResult.setBackground(Color.BLACK);
		blackResult.setForeground(Color.WHITE);
		blackResult.setFont(new Font("Arial", Font.PLAIN, 30));
		blackResult.setText("BLACK: 2");
		messageField.add(blackResult);
		
		txtMessage = new JTextField();
		txtMessage.setBackground(Color.RED);
		txtMessage.setForeground(Color.YELLOW);
		txtMessage.setHorizontalAlignment(JTextField.CENTER);
		txtMessage.setFont(new Font("Arial", Font.PLAIN, 20));
		txtMessage.setText("Othello");
		messageField.add(txtMessage);

		whiteResult = new JTextField();
		whiteResult.setBackground(Color.WHITE);
		whiteResult.setForeground(Color.BLACK);
		whiteResult.setHorizontalAlignment(JTextField.RIGHT);
		whiteResult.setFont(new Font("Arial", Font.PLAIN, 30));
		whiteResult.setText("WHITE: 2");
		messageField.add(whiteResult);

		contentPane.add(messageField, BorderLayout.NORTH);


		for(int x=0; x<8 ;x++) {
			for(int y=0; y<8 ;y++) {
				gameField.add(cells[x][y]);
				cells[x][y].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						game.action(((OthelloCell)e.getSource()).getPosX(),
								((OthelloCell)e.getSource()).getPosY());
					}
				});
			}
		}

		JButton btnNewButton = new JButton("Reset");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.reset();
			}
		});
		JButton hintButton = new JButton("TURN ON HINT");
		hintButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.getHint();
			}
		});
		JButton nohintButton = new JButton("TURN OFF HINT");
		nohintButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.stopHint();
			}
		});
		buttonField = new JPanel();
		buttonField.setLayout(new GridLayout(1,3,0,0));
		buttonField.add(btnNewButton);
		buttonField.add(hintButton);
		buttonField.add(nohintButton);
		contentPane.add(buttonField, BorderLayout.SOUTH);
	}

	public void setText(String message) {
		txtMessage.setText(message);
	}

	public void setResult(int x, int y) {
		blackResult.setText("BLACK: "+x);
		whiteResult.setText("WHITE: "+y);
	}
}
