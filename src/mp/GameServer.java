package mp;

import java.io.*;

import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * This class is responsible for connecting the clients and sending and receiving data from the Retro Racer Players.
 * @author ziadelharairi
 *
 */
public class GameServer {
	private ServerSocket listener;
	private int numPlayers;
	
	private ServerSideConnection player1;
	private ServerSideConnection player2;
	
//	private enum STATE{
//		MENU,
//		SINGLEPLAYER,
//		MULTIPLAYER
//	};
//	private STATE state = STATE.MENU;
	/**
	 * 
	 */
	public GameServer() {
		System.out.println("----Game Server----");
		numPlayers = 0;
		try {
			listener = new ServerSocket(49152);
			listener.setReuseAddress(true);
		}catch(IOException ex) {
			System.out.println("IOException from GameServer Constructor");
		}
	}
	
	/**
	 * This method creates a ServerSideConnection for each player. The game will only accept two players to play .
	 */
	public void acceptConnections() {
		initPlayerStateExchange();
	}


	private void initPlayerStateExchange() {
		Thread t = new Thread(() -> {
			numPlayers = 0;
			try {
				System.out.println("Waiting for connections...");
				ExecutorService pool = Executors.newFixedThreadPool(2);
				while (numPlayers < 2) {
					Socket s = listener.accept();
					numPlayers++;
					if(numPlayers == 1) {
						player1 = new ServerSideConnection(s, numPlayers);
						pool.execute(player1);
					} else if(numPlayers == 2) {
						player2 = new ServerSideConnection(s, numPlayers);
						pool.execute(player2);
					}
					if(numPlayers == 2)
						System.out.println("We now have 2 players connected. No longer accepting connnections.");
				}

			} catch(IOException ex) {
				System.out.println("IOException from acceptConnections");
			}
		});
		t.start();
	}

	/**
	 * This is an inner class inside the GameServer class. It will send and receive data using Input and Output stream.
	 * This class also implements Runnable which means that it has a run() method.
	 * 
	 * @author ziadelharairi
	 *
	 */
	private class ServerSideConnection implements Runnable{
		
		private Socket socket;
    	private int playerID;
    	private ObjectInputStream in_state;
		private ObjectOutputStream out_state;
		
		/**
		 * Constructs and initializes a ServerSideConnection using socket and player id.
		 * @param s socket number
		 * @param id player id
		 */
		public ServerSideConnection(Socket s, int id) {
    		socket = s;
    		playerID = id;
    		try {
				out_state = new ObjectOutputStream(socket.getOutputStream());
				in_state = new ObjectInputStream(socket.getInputStream());
    			
    		} catch (IOException ex) {
    			System.out.println("IOException from run() SSC");
    		}
    	}
		@Override
		public void run() {
    		System.out.println(playerID+" Running");
    		PlayerState p = null;
			// TODO Auto-generated method stub
			try {
				out_state.writeObject(new PlayerState(playerID, 0, 0, 0));
				out_state.flush();

				while(true) {
					if (playerID == 1) {
						p = null;
						try {
							p = (PlayerState) in_state.readObject();
						} catch (Exception e) {
							System.out.println("Error while sending player state");
						}
						if(p != null) {
							System.out.println("Player 1 x-position is " + p.getPosX() +" and the y-position is " + p.getPoxY() +" scoreIn: "+ p.getScore());
						}
						if(player2 != null)
							player2.sendPlayerStatus(p);

					}else if(playerID == 2) {
						p = null;
						try {
							p = (PlayerState) in_state.readObject();
						} catch (Exception e) {
							System.out.println("Error while sending player state");
						}
						if(p != null)
							System.out.println("Player 2 x-position is " + p.getPosX() +" and the y-position is " + p.getPoxY() +" scoreIn: "+ p.getScore());

						if(player1 != null)
							player1.sendPlayerStatus(p);
					}
				}
				
			
			} catch (Exception ex) {
				System.out.println("IOException from run() SSc");
			}
			
		}
		
		/**
		 * Method for sending the the player state to one of the clients.
		 * @param playerState 
		 */
		public void sendPlayerStatus(PlayerState playerState) {
			try {
				out_state.writeObject(playerState);
				out_state.flush();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GameServer gs = new GameServer();
		gs.acceptConnections();
	}

}
