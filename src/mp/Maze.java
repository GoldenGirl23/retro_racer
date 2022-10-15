package mp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

/**
 * This class is responsible for creating the players(clients) and will connect them to the server through the TCP ports.
 * 
 * @author ziadelharairi
 *
 */
public class Maze extends JFrame {

	private int width;
	private int height;
	private int playerID;
	private int otherPlayer;
 
	private ClientSideConnection csc;
	
	public Maze() {
		width = 480;
		height = 550;

	}
	/**
	 * This method creates an instance of the game and sets the size, title and visibility.
	 */
	public void initGUI() {
		add(new Board());
		this.setSize(width,height);
		this.setTitle("Player #"+ playerID);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		if (playerID == 1) {
			otherPlayer = 2;
		}else {
			otherPlayer = 1;
		}
	}
	/**
	 * This is connect to server method.
	 * 
	 * This method will run the instance to run the connection to the client.
	 *
	 */
	public void connectToServer() {
		csc = new ClientSideConnection();
	}

	
	/**
	 * Client Side Connection will send and receive the player's data using the the localhost and the port number.
	 * @author ziadelharairi
	 *
	 */
	private class ClientSideConnection{
    	private Socket socket;
    	private ObjectOutputStream out_state;
		private ObjectInputStream in_state;
		
    	public ClientSideConnection() {
    		System.out.println("---Client---");
    		try {
    			socket = new Socket("127.0.0.1",49152);
    			System.out.println(socket.toString());
				out_state = new ObjectOutputStream(socket.getOutputStream());
				in_state = new ObjectInputStream(socket.getInputStream());
    			playerID = ((PlayerState)in_state.readObject()).getId();
    			System.out.println("Connected to server as Player #" + playerID + ".");
    		} catch(IOException ex) {
    			System.out.println("IOException from CSC constructor");
    		} catch (ClassNotFoundException e) {
				System.out.println("ClassNotFoundException from CSC constructor");
			}
		}
    	/**
    	 * This method will send the player state to the server.
    	 * @param playerState
    	 */
    	public void sendPlayerState(PlayerState playerState){
			try {
				out_state.writeObject(playerState);
				out_state.flush();
			} catch (IOException exception) {
				System.out.println("IOException from CSC constructor");
			}
		}

    	/**
    	 * This method will receive the player state from the server.
    	 * @return PlayerState
    	 */
		public PlayerState receivePlayerState() {
    		PlayerState p = null;
			try {
				p = (PlayerState) in_state.readObject();
			} catch (IOException | ClassNotFoundException exception) {
				System.out.println("IOException from CSC constructor");
			}
			return p;
		}
    }
	public static void main(String[] args) {
		Maze m = new Maze();
		m.connectToServer();
		m.initGUI();
	}
	
	/**
	 * The Board class shows is responsible for the game mechanics.
	 * @author ziadelharairi
	 *
	 */
	public class Board extends JPanel implements ActionListener {
		
		private Dimension d;
	    private final Font smallFont = new Font("Arial", Font.BOLD, 14);
	    private boolean inGame = false;
	    private boolean endGame = false;
	    private int winnerID = 0;
	    private final int winningScore = 200;

	    private final int bSize = 24;
	    private final int nBlocks = 20;
	    private final int screenSize = nBlocks * bSize;
	    private int playerSpeed = 6;
	    private int score, scoreTwo; 
	   
	    private Image up, down, left, right;
	    private Image opponent;

	    private int playerPosX, playerPosY, playerD_X, playerD_Y;
	    private int reqdx, reqdy;

	    private final short levelData[] = {
	    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				  0,19,22,0,23,0,23,0,19,18,18,22,0,23,0,23,0,19,22,0,
				  0,17,20,0,21,0,21,0,17,16,16,20,0,21,0,21,0,17,20,0,
				  0,17,24,26,24,26,24,26,16,24,24,16,26,24,26,24,26,24,20,0,
				  0,21,0,0,0,0,0,0,21,0,0,21,0,0,0,0,0,0,21,0,
				  0,17,26,22,0,0,19,26,28,0,0,25,26,22,0,0,19,26,20,0,
				  0,21,0,21,0,0,21,0,0,0,0,0,0,21,0,0,21,0,21,0,
				  0,21,0,25,26,18,24,18,18,26,26,18,18,24,18,26,28,0,21,0,
				  0,21,0,0,0,21,0,17,20,0,0,17,20,0,21,0,0,0,21,0,
				  0,17,26,26,26,20,0,17,20,0,0,17,20,0,17,26,26,26,20,0,
				  0,21,0,0,0,21,0,17,20,0,0,17,20,0,21,0,0,0,21,0,
				  0,21,0,19,26,24,18,24,24,26,26,24,24,18,24,26,22,0,21,0,
				  0,21,0,21,0,0,21,0,0,0,0,0,0,21,0,0,21,0,21,0,
				  0,17,26,28,0,0,25,26,22,0,0,19,26,28,0,0,25,26,20,0,
				  0,21,0,0,0,0,0,0,21,0,0,21,0,0,0,0,0,0,21,0,
				  0,17,18,26,26,18,26,26,16,26,26,16,26,26,18,26,26,18,20,0,
				  0,17,20,0,0,21,0,0,21,0,0,21,0,0,21,0,0,17,20,0,
				  0,17,20,0,0,21,0,0,21,0,0,21,0,0,21,0,0,17,20,0,
				  0,25,24,26,26,24,26,26,24,26,26,24,26,26,24,26,26,24,28,0,
				  0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
	    };

	    private short[] screenData;
	    private Timer timer;
	    private Color mazeColor;
		private PlayerState opponentState;
		
	    public Board() {

	        loadImages();
	        initVariables();
	        addKeyListener(new TAdapter());
	        setFocusable(true);
	        setBackground(Color.black);
	        setDoubleBuffered(true);
			initPlayerStateExchange();
	    }
	    /**
	     * This method receives player state, however using a thread to start the multiplayer
	     * game using the game menu.
	     */
	    private void initPlayerStateExchange() {
			Thread t = new Thread(() -> {
				while(true) {
					opponentState = csc.receivePlayerState();
				}
			});
			t.start();
		}
	    
	    /**
	     * This method finds the Images needed for showing the racers on the game screen.
	     */
	    private void loadImages() {
	    	down = new ImageIcon("res/playerDown.png").getImage();
	    	up = new ImageIcon("res/playerUp.png").getImage();
	    	left = new ImageIcon("res/playerLeft.png").getImage();
	    	right = new ImageIcon("res/playerRight.png").getImage();
	    	opponent = new ImageIcon("res/circle.png").getImage();

	    }
	    
	    /**
	     * This method initializes the variables needed to start the game.
	     */
	       private void initVariables() {

	        screenData = new short[nBlocks * nBlocks];
	        mazeColor = new Color(5,100,5);
	        d = new Dimension(400, 400);
	        
	        timer = new Timer(40, this);
	        timer.start();
	    }
	       public void addNotify() {
	           super.addNotify();

	           initGame();
	       }
	       
	       /**
	        * This method will exchange each racer's player state.
	        */
	    private void exchangePlayerState() {
	    	PlayerState playerState = new PlayerState(playerID, score, playerPosX, playerPosY);
			csc.sendPlayerState(playerState);
		}

	    /**
	     * This method checks if either racer collected all 200 coins.
	     * @param g2d
	     */
		private void checkEndGame(Graphics2D g2d) {
			if(opponentState.getScore() >= winningScore || score >= winningScore) {
				endGame = true;
				winnerID = opponentState.getScore() >= winningScore? opponentState.getId():playerID;
				gameOverScreen(g2d);
			}
		}

	    private void playGame(Graphics2D g2d) {

	            moveRacer();
	            drawRacer(g2d);
				exchangePlayerState();
				drawOpponent(g2d);
				checkEndGame(g2d);
	        
	    }

	    /**
	     * This method will show a screen before the racers start the game.
	     * @param g2d
	     */
	    private void introScreen(Graphics2D g2d) {
	    	g2d.setColor(new Color(0, 32, 48));
	        g2d.fillRect(50, screenSize / 2 - 30, screenSize - 100, 50);
	        g2d.setColor(Color.white);
	        g2d.drawRect(50, screenSize / 2 - 30, screenSize - 100, 50);
	        
	        String s = "Press space to start.";
	        Font small = new Font("Helvetica", Font.BOLD, 14);
	        FontMetrics metr = this.getFontMetrics(small);

	        g2d.setColor(Color.white);
	        g2d.setFont(small);
	        g2d.drawString(s, (screenSize - metr.stringWidth(s)) / 2, screenSize / 2);
	    }
	    
	    /**
	     * This method prints game over screen indicating that the game is over and that one the players has won the game.
	     * @param g2d
	     */
	    private void gameOverScreen(Graphics2D g2d) {
	    	g2d.setColor(new Color(0, 32, 48));
	        g2d.fillRect(50, screenSize / 2 - 30, screenSize - 100, 50);
	        g2d.setColor(Color.white);
	        g2d.drawRect(50, screenSize / 2 - 30, screenSize - 100, 50);

	        String s = "Player "+winnerID+" wins with total " + winningScore + " points";

			Font small = new Font("Helvetica", Font.BOLD, 14);
	        FontMetrics metr = this.getFontMetrics(small);

	        g2d.setColor(Color.white);
	        g2d.setFont(small);
	        g2d.drawString(s, (screenSize - metr.stringWidth(s)) / 2, screenSize / 2);
	    }
	    
	    /**
	     * This method will show the players score on the bottom right of the screen.
	     * @param g
	     */
	    private void drawScore(Graphics2D g) {
	    	String s;

	        g.setFont(smallFont);
	        g.setColor(new Color(96, 128, 255));
	        s = "My Score: " + score;
	        g.drawString(s, screenSize / 2, screenSize + 16);

	    }
	    
	    /**
	     * This method will show the opponent's score on the bottom right of the screen.
	     * @param g
	     */
	    private void drawScoreTwo(Graphics2D g) {
	    	
			String s;

            g.setFont(smallFont);
            g.setColor(new Color(255,218,68));
            s = "Opponent: " + (opponentState != null? opponentState.getScore()+"":0+"");
            g.drawString(s, screenSize / 2 + 96, screenSize + 16);
        }
	    

	    /**
	     * This method is responsible for the movement of the racer.
	     */
	    private void moveRacer() {

	        int pos;
	        short ch;

	        if (playerPosX % bSize == 0 && playerPosY % bSize == 0) {
	            pos = playerPosX / bSize + nBlocks * (int) (playerPosY / bSize);
	            ch = screenData[pos];

	            if ((ch & 16) != 0) {
	                screenData[pos] = (short) (ch & 15);
	                score++;
	            }

	            if (reqdx != 0 || reqdy != 0) {
	                if (!((reqdx == -1 && reqdy == 0 && (ch & 1) != 0)
	                        || (reqdx == 1 && reqdy == 0 && (ch & 4) != 0)
	                        || (reqdx == 0 && reqdy == -1 && (ch & 2) != 0)
	                        || (reqdx == 0 && reqdy == 1 && (ch & 8) != 0))) {
	                    playerD_X = reqdx;
	                    playerD_Y = reqdy;
	                }
	            }

	            // Check for standstill
	            if ((playerD_X == -1 && playerD_Y == 0 && (ch & 1) != 0)
	                    || (playerD_X == 1 && playerD_Y == 0 && (ch & 4) != 0)
	                    || (playerD_X == 0 && playerD_Y == -1 && (ch & 2) != 0)
	                    || (playerD_X == 0 && playerD_Y == 1 && (ch & 8) != 0)) {
	                playerD_X = 0;
	                playerD_Y = 0;
	            }
	            
	        } 
	        playerPosX = playerPosX + playerSpeed * playerD_X;
	        playerPosY = playerPosY + playerSpeed * playerD_Y;
	    }
	    
	    /**
	     * This method is responsible for changing the racer's image when the direction changes.
	     * @param g2d
	     */
	    private void drawRacer(Graphics2D g2d) {

	        if (reqdx == -1) {
	        	g2d.drawImage(left, playerPosX + 1, playerPosY + 1, this);
	        } else if (reqdx == 1) {
	        	g2d.drawImage(right, playerPosX + 1, playerPosY + 1, this);
	        } else if (reqdy == -1) {
	        	g2d.drawImage(up, playerPosX + 1, playerPosY + 1, this);
	        } else {
	        	g2d.drawImage(down, playerPosX + 1, playerPosY + 1, this);
	        }
	    }

	    /**
	     * This method is responsible for drawing the opponents image.
	     * @param g2d
	     */
		private void drawOpponent(Graphics2D g2d) {
	    	g2d.drawImage(opponent, opponentState.getPosX() + 1, opponentState.getPoxY() + 1, this);
		}

		/**
		 * This method is responsible for drawing the maze using the screenData.
		 * @param g2d
		 */
	    private void drawMaze(Graphics2D g2d) {

	        short i = 0;
	        int x, y;

	        for (y = 0; y < screenSize; y += bSize) {
	            for (x = 0; x < screenSize; x += bSize) {

	                g2d.setColor(new Color(0,72,251));
	                g2d.setStroke(new BasicStroke(2));
	                

	                if ((screenData[i] & 1) != 0) { 
	                    g2d.drawLine(x, y, x, y + bSize - 1);
	                }

	                if ((screenData[i] & 2) != 0) { 
	                    g2d.drawLine(x, y, x + bSize - 1, y);
	                }

	                if ((screenData[i] & 4) != 0) { 
	                    g2d.drawLine(x + bSize - 1, y, x + bSize - 1,
	                            y + bSize - 1);
	                }

	                if ((screenData[i] & 8) != 0) { 
	                    g2d.drawLine(x, y + bSize - 1, x + bSize - 1,
	                            y + bSize - 1);
	                }

	                if ((screenData[i] & 16) != 0) { 
	                    g2d.setColor(new Color(255,255,255));
	                    g2d.fillOval(x + 10, y + 10, 6, 6); // white dot
	               }

	                i++;
	            }
	        }
	    }

	    private void initGame() {

	        score = 0;
	        scoreTwo = 0;
	        initLevel();
	    }

	    private void initLevel() {

	        int i;
	        for (i = 0; i < nBlocks * nBlocks; i++) {
	            screenData[i] = levelData[i];
	        }

	        continueLevel();
	    }

	    private void continueLevel() {

	        playerPosX = 7 * bSize;  //start position
	        playerPosY = 11 * bSize;
	        playerD_X = 0;	//reset direction move
	        playerD_Y = 0;
	        reqdx = 0;		// reset direction controls
	        reqdy = 0;
	    }

	 
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);

	        Graphics2D g2d = (Graphics2D) g;

	        g2d.setColor(Color.black);
	        g2d.fillRect(0, 0, d.width, d.height);

	        drawMaze(g2d);
	        drawScore(g2d);
	        drawScoreTwo(g2d);

	        if (inGame) {
	            playGame(g2d);
	        } else{
	            introScreen(g2d);
	        }

	        Toolkit.getDefaultToolkit().sync();
	        g2d.dispose();
	    }


	    /**
	     * This class is responsible for the key movement.
	     * @author ziadelharairi
	     *
	     */
	    class TAdapter extends KeyAdapter {

	        @Override
	        public void keyPressed(KeyEvent e) {

	            int key = e.getKeyCode();
				System.out.println("Pressed");
	            if (inGame && !endGame) {
	                if (key == KeyEvent.VK_LEFT) {
	                    reqdx = -1;
	                    reqdy = 0;
	                } else if (key == KeyEvent.VK_RIGHT) {
	                    reqdx = 1;
	                    reqdy = 0;
	                } else if (key == KeyEvent.VK_UP) {
	                    reqdx = 0;
	                    reqdy = -1;
	                } else if (key == KeyEvent.VK_DOWN) {
	                    reqdx = 0;
	                    reqdy = 1;
	                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
	                    inGame = false;
	                } 
	            } else if (endGame) {
					if (key == KeyEvent.VK_ESCAPE) {
						inGame = true;
						endGame = false;
					}
				} else {
					if (key == KeyEvent.VK_SPACE) {
						inGame = true;
						initGame();
					}
				}
	        }
	}

		
	    @Override
	    public void actionPerformed(ActionEvent e) {
	        repaint();
	    }
}}
