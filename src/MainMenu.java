

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import mp.GameServer;
import mp.Maze;
import sp.Game;


public class MainMenu extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static JFrame frame = new JFrame();
	static JPanel panel = new JPanel();
	private JButton sp, mp, ctrl, exit;
	static JFrame controlFrame = new JFrame();

	
	GameServer gs = new GameServer();
	
	//Menu constructor
	public MainMenu() {
		
		//Retro Racer heading
		String headingpath = "res\\rrgif.gif";
		Icon headingicon = new ImageIcon(headingpath);
		JLabel headinglabel = new JLabel(headingicon);
		headinglabel.setBounds(150, 20, 500, 200);
		panel.add(headinglabel);
		
		//Car gif
		String carpath = "res\\cargif3.gif";
		Icon caricon = new ImageIcon(carpath);
		JLabel carlabel = new JLabel(caricon);
		carlabel.setBounds(0, 300, 829, 324);
		panel.add(carlabel);
		
		frame.setTitle("Retro Racer");
		frame.setSize(800, 600);
		
		panel.setLayout(null);
		panel.setBackground(Color.BLACK);
		//panel.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLUE));
		panel.setBorder(BorderFactory.createDashedBorder(Color.YELLOW, 6, 1, 2, true));
		
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		
		
		//control frame
		controlFrame.setTitle("Controls");
		controlFrame.setSize(400, 300);
		//controlFrame.setUndecorated(true);
		controlFrame.setLocation(200, 170);
		
		ImageIcon controls = new ImageIcon("res//controls.png"); 
		JLabel controlslabel = new JLabel(controls); 
        controlFrame.getContentPane().add(controlslabel);
       
		
		
		//Single player button
		sp = new JButton("Single Player");
		panel.add(sp);
		sp.setBounds(330, 180, 150, 50);
		sp.setFont(new Font("Verdana", Font.BOLD, 18));
		//sp.setFont(Font.getFont(FONT_PATH));
		sp.setBackground(Color.BLUE);
		sp.setForeground(Color.YELLOW);
		sp.setBorder(BorderFactory.createBevelBorder(0, Color.yellow, Color.yellow, Color.red, Color.red));
		sp.setActionCommand("single");
		sp.addActionListener(this);
		
		sp.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        sp.setBackground(Color.RED);
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        sp.setBackground(Color.BLUE);
		    }
		});
		
	
		//Multiplayer 1 button
		mp = new JButton("Multiplayer");
		panel.add(mp);
		mp.setBounds(330, 240, 150, 50);
		mp.setFont(new Font("Verdana", Font.BOLD, 18));
		mp.setBackground(Color.BLUE);
		mp.setForeground(Color.YELLOW);
		mp.setBorder(BorderFactory.createBevelBorder(0, Color.yellow, Color.yellow, Color.red, Color.red));
		mp.setActionCommand("multi");
		mp.addActionListener(this);
		
		mp.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        mp.setBackground(Color.RED);
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        mp.setBackground(Color.BLUE);
		    }
		});
		
	
		//Multiplayer 2 button
		ctrl = new JButton("Controls");
		panel.add(ctrl);
		ctrl.setBounds(330, 300, 150, 50);
		ctrl.setFont(new Font("Verdana", Font.BOLD, 18));
		ctrl.setBackground(Color.BLUE);
		ctrl.setForeground(Color.YELLOW);
		ctrl.setBorder(BorderFactory.createBevelBorder(0, Color.yellow, Color.yellow, Color.red, Color.red));
		ctrl.setActionCommand("control");
		ctrl.addActionListener(this);
		
		ctrl.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	ctrl.setBackground(Color.RED);
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	ctrl.setBackground(Color.BLUE);
		    }
		});

		
		//Exit button
		exit = new JButton("Exit");
		panel.add(exit);
		exit.setBounds(330, 360, 150, 50);
		exit.setFont(new Font("Verdana", Font.BOLD, 18));
		exit.setBackground(Color.BLUE);
		exit.setForeground(Color.YELLOW);
		exit.setBorder(BorderFactory.createBevelBorder(0, Color.yellow, Color.yellow, Color.red, Color.red));
		exit.setActionCommand("exit");
		exit.addActionListener(this);
		
		exit.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        exit.setBackground(Color.RED);
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        exit.setBackground(Color.BLUE);
		    }
		});
		
	
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		
		//Single player button action
		if (action.equals("single")) {
            //System.out.println("Button1 pressed!");
            Game game = new Game();
            JFrame frame = new JFrame();
    		
    		frame.setTitle("Retro Racer");
    		frame.setSize(800, 600);
    		frame.add(game);
    		frame.setResizable(false);
    		frame.pack();
    		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		frame.setLocationRelativeTo(null);
    		
    		frame.setVisible(true);
    		
    		game.start();
        }
		
		//Multiplayer button action
		else if (action.equals("multi")) {
            System.out.println("Button2 pressed!");
            
    	    gs.acceptConnections();
    		
    		Maze player1 = new Maze();
			Maze player2 = new Maze();
			player1.connectToServer();
    		player1.initGUI();
			player2.connectToServer();
			player2.initGUI();
        }
		
		//Control button action
		else if (action.equals("control")) {
            System.out.println("Button3 pressed!");
           
            controlFrame.setVisible(true);
           
 
        }
		
		//Exit button action
		else if (action.equals("exit")) {
            System.exit(0);
		
        }
		
	}
	
	//Main
	public static void main (String[] args) {
		new MainMenu();
	}
	

}
