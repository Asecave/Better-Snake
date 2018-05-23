package game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class BetterSnake extends JPanel implements ActionListener, KeyListener, MouseListener{
	
	int startSize = 3;						//default: 3
	int appleSpawnRate = 50;				//default: 50
	int gameSpeed = 100;					//default: 100
	int dropChance = 5;						//default: 5
	boolean despawn = true;					//default: true
	int despawnChance = 200;				//default: 200
	int despawnStart = 10;					//default: 10
	int grow = 3;							//default: 3
	Color backgroundColor = Color.GREEN;	//default: green
	Color textColor = Color.BLACK;			//default: black
	Color player1Color = Color.BLACK;		//default: black
	Color player2Color = Color.BLACK;		//default: black
	Color appleColor = Color.RED;			//default: red
	
	Timer tm = new Timer(gameSpeed, this);
	
	final static int WIDTH = 808;
	final static int HEIGHT = 634;
	
	boolean reset = true;
	
	String surface = "main menu";
	
	short mapSizeX = 80, mapSizeY = 60;
	
	int[][] points = new int[mapSizeX][mapSizeY];
	
	int menuChoice = 0;
	
	int player1Kills = 0;
	int player2Kills = 0;
	int player1Kill_streak = 0;
	int player2Kill_streak = 0;
	
	int appleTimer = appleSpawnRate;
	int appleCount;
	int respawnTimer1 = 20;
	int respawnTimer2 = 20;
	
	boolean w_pressed = false, a_pressed = false, s_pressed = false, d_pressed = false;
	boolean up_pressed = false, left_pressed = false, down_pressed = false, right_pressed = false;
	boolean enter_pressed = false;
	boolean escape_pressed = false;
	
	boolean gamePaused = false;
	
	int optionsCursor_x = 5, optionsCursor_y = 90;
	
	int player1_x = 1, player1_y = 30;
	int player2_x = 78, player2_y = 30;
	boolean up1 = false, left1 = false, down1 = false, right1 = true;
	boolean up2 = false, left2 = true, down2 = false, right2 = false;
	int size1 = startSize;
	int size2 = startSize+10000;
	
	public BetterSnake() {
		tm.start();
		addKeyListener(this);
		addMouseListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		if (!gamePaused) {
			//reset
			if (reset) {
				for (int reset_x = 0; reset_x < mapSizeX; reset_x++) {
					for (int reset_y = 0; reset_y < mapSizeY; reset_y++) {
						points[reset_x][reset_y] = 0;
					}
				}
				menuChoice = 0;              
				player1Kills = 0;            
				player2Kills = 0;                                 
				appleTimer = appleSpawnRate;                   
				respawnTimer1 = 20;          
				respawnTimer2 = 20;
				player1_x = 1;
				player1_y = 30; 
				player2_x = 78;
				player2_y = 30;
				up1 = false; left1 = false; down1 = false; right1 = true;
				up2 = false; left2 = true; down2 = false; right2 = false;
				size1 = startSize;
				size2 = startSize;
				player1Kill_streak = 0;
				player2Kill_streak = 0;
				reset = false;
			}
			//Loop
			if (surface == "running") {
				if (w_pressed && !down1) {
					up1 = true;
					left1 = false;
					down1 = false;
					right1 = false;
				}
				if (a_pressed && !right1) {
					up1 = false;
					left1 = true;
					down1 = false;
					right1 = false;
				}
				if (s_pressed && !up1) {
					up1 = false;
					left1 = false;
					down1 = true;
					right1 = false;
				}
				if (d_pressed && !left1) {
					up1 = false;
					left1 = false;
					down1 = false;
					right1 = true;
				}
				if (up_pressed && !down2) {
					up2 = true;
					left2 = false;
					down2 = false;
					right2 = false;
				}
				if (left_pressed && !right2) {
					up2 = false;
					left2 = true;
					down2 = false;
					right2 = false;
				}
				if (down_pressed && !up2) {
					up2 = false;
					left2 = false;
					down2 = true;
					right2 = false;
				}
				if (right_pressed && !left2) {
					up2 = false;
					left2 = false;
					down2 = false;
					right2 = true;
				}

				if (up1) {
					player1_y--;
				} else if (left1) {
					player1_x--;
				} else if (down1) {
					player1_y++;
				} else if (right1) {
					player1_x++;
				}
				if (up2) {
					player2_y--;
				} else if (left2) {
					player2_x--;
				} else if (down2) {
					player2_y++;
				} else if (right2) {
					player2_x++;
				}

				if (player1_x < 0 || player1_x > mapSizeX - 1 || player1_y < 0 || player1_y > mapSizeY - 1) {
					player1dead();
					player1Kill_streak = 0;
				}
				if (player2_x < 0 || player2_x > mapSizeX - 1 || player2_y < 0 || player2_y > mapSizeY - 1) {
					player2dead();
					player2Kill_streak = 0;
				}
				if (respawnTimer1 != 20) {
					player1_x = 1;
					player1_y = 30;
					up1 = false;
					left1 = false;
					down1 = false;
					right1 = false;
					if (respawnTimer1 == 19) {
						right1 = true;
						player1_x++;
					}
					respawnTimer1++;
				}
				if (respawnTimer2 != 20) {
					player2_x = 78;
					player2_y = 30;
					up2 = false;
					left2 = false;
					down2 = false;
					right2 = false;
					if (respawnTimer2 == 19) {
						left2 = true;
						player2_x--;
					}
					respawnTimer2++;
				}

				if ((points[player1_x][player1_y] > 10000 && respawnTimer1 == 20)
						&& ((points[player2_x][player2_y] < 10000 && points[player2_x][player2_y] > 0)
								&& respawnTimer2 == 20)) {
					player1Kills++;
					player2Kills++;
					player1dead();
					player2dead();
					player1Kill_streak = 0;
					player2Kill_streak = 0;
				}
				if (points[player1_x][player1_y] == -5) {
					p1Bigger(grow);
				} else if ((points[player1_x][player1_y] > 0 && points[player1_x][player1_y] < 10000)
						&& respawnTimer1 == 20) {
					player1dead();
					player1Kill_streak = 0;
				} else if ((points[player1_x][player1_y] > 10000) && respawnTimer1 == 20) {
					player2Kills++;
					player1dead();
					player1Kill_streak = 0;
					player2Kill_streak++;
				}
				if (points[player2_x][player2_y] == -5) {
					p2Bigger(grow);
				} else if ((points[player2_x][player2_y] > 10000) && respawnTimer2 == 20) {
					player2dead();
					player2Kill_streak = 0;
				} else if ((points[player2_x][player2_y] < 10000 && points[player2_x][player2_y] > 0)
						&& respawnTimer2 == 20) {
					player1Kills++;
					player2dead();
					player2Kill_streak = 0;
					player1Kill_streak++;
				}

				try {
					points[player1_x][player1_y] = size1;
				} catch (Exception e) {
					error(e);
				}
				try {
					points[player2_x][player2_y] = size2;
				} catch (Exception e) {
					error(e);
				}

				if (appleTimer >= appleSpawnRate) {
					spawnApple();
					appleTimer = 0;
				}
				appleTimer++;

				if (despawn && (appleCount > despawnStart)) {
					for (int despawn_x = 0; despawn_x < mapSizeX; despawn_x++) {
						for (int despawn_y = 0; despawn_y < mapSizeY; despawn_y++) {
							if (points[despawn_x][despawn_y] == -5 && ((int) (Math.random() * despawnChance)) == 0) {
								points[despawn_x][despawn_y] = 0;
							}
						}
					}
				}

				appleCount = 0;

				for (int draw_x = 0; draw_x < mapSizeX; draw_x++) {
					for (int draw_y = 0; draw_y < mapSizeY; draw_y++) {
						if (points[draw_x][draw_y] == 0 || points[draw_x][draw_y] == 10000) {
							g2d.setColor(backgroundColor);
							g2d.fillRect(draw_x * 10, draw_y * 10, 10, 10);
						} else if ((points[draw_x][draw_y] > 0) && (points[draw_x][draw_y] < 10000)) {
							g2d.setColor(player1Color);
							g2d.fillRect(draw_x * 10, draw_y * 10, 10, 10);
						} else if (points[draw_x][draw_y] > 10000) {
							g2d.setColor(player2Color);
							g2d.fillRect(draw_x * 10, draw_y * 10, 10, 10);
						} else if (points[draw_x][draw_y] == -5) {
							g2d.setColor(appleColor);
							g2d.fillRect(draw_x * 10, draw_y * 10, 10, 10);
							appleCount++;
						}

						if (points[draw_x][draw_y] > 0 && points[draw_x][draw_y] != 10000) {
							points[draw_x][draw_y]--;
						}
					}
				}
				//stats
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				g2d.setColor(textColor);
				g2d.setFont(new Font("Arial", Font.BOLD, 20));
				//kills of player 1
				g2d.drawString("Kills: "+player1Kills, 5, 50);
				//kills of player 2
				g2d.drawString("Kills: "+player2Kills, 712, 50);
				//kill streak of player 1
				g2d.drawString("Kill streak: "+player1Kill_streak, 5, 25);
				//kill streak of player 2
				g2d.drawString("Kill streak: "+player2Kill_streak, 658, 25);
			}
		}
		if (gamePaused) {
			if (enter_pressed) {
				gamePaused = false;
				surface = "main menu";
				return;
			}
			g2d.setFont(new Font("Arial", Font.BOLD, 30));
			g2d.setColor(textColor);
			g2d.drawString("Game is paused", 280, 40);
			g2d.setFont(new Font("Arial", Font.BOLD, 20));
			g2d.drawString("Press 'escape' to continue", 275, 70);
			g2d.drawString("Press 'enter' to return to menu", 250, 100);
			g2d.setFont(new Font("Arial", 0, 18));
			g2d.drawString("All your stats will be gone!", 295, 125);
		}
		//main menu====================================================
			
		if (surface == "main menu") {
			if ((w_pressed || up_pressed) && menuChoice > 0) {
				menuChoice--;
			}
			if ((s_pressed || down_pressed) && menuChoice < 1) {
				menuChoice++;
			}
			if (enter_pressed) {
				switch (menuChoice) {
				case 0:
					reset = true;
					surface = "running";
					return;
				case 1:
					surface = "options";
					return;
				}
			}
			
			g2d.setColor(backgroundColor);
			g2d.fillRect(0, 0, WIDTH, HEIGHT);
			
			g2d.setColor(textColor);
			g2d.setFont(new Font("Arial", Font.BOLD, 50));
			g2d.drawString("BETTER SNAKE", 195, 100);
			g2d.fillRect(170, 110, 450, 10);
			
			if (menuChoice == 0) {
				g2d.fillRect(100, 245, 600, 75);
				g2d.setColor(backgroundColor);
				g2d.fillRect(105, 250, 590, 65);
			}
			g2d.setFont(new Font("Arial", Font.BOLD, 40));
			g2d.setColor(textColor);
			g2d.drawString("Start", 350, 300);
			
			if (menuChoice == 1) {
				g2d.fillRect(100, 345, 600, 75);
				g2d.setColor(backgroundColor);
				g2d.fillRect(105, 350, 590, 65);
			}
			g2d.setColor(textColor);
			g2d.drawString("Options", 330, 400);
		}
		//options=====================================================
		
		if (surface == "options") {
			if ((w_pressed || up_pressed) && (optionsCursor_y != 90 || optionsCursor_x == 380)) {
				optionsCursor_y -= 40;
			}
			if ((s_pressed || down_pressed) && (optionsCursor_y != 250 || optionsCursor_x != 380)) {
				optionsCursor_y += 40;
			}
			if (optionsCursor_y == 410 && (s_pressed || down_pressed) && optionsCursor_x == 5) {
				optionsCursor_x = 380;
				optionsCursor_y = 90;
			}
			if (optionsCursor_y == 50 && (w_pressed || up_pressed) && optionsCursor_x == 380) {
				optionsCursor_x = 5;
				optionsCursor_y = 370;
			}
			if (a_pressed || left_pressed) {
				optionsPrevious();
			}
			if (d_pressed || right_pressed) {
				optionsNext();
			}
			if (escape_pressed) {
				tm.setDelay(gameSpeed);
				surface = "main menu";
				return;
			}
			g2d.setColor(backgroundColor);
			g2d.fillRect(0, 0, WIDTH, HEIGHT);
			
			g2d.setColor(textColor);
			g2d.setFont(new Font("Arial", Font.BOLD, 40));
			g2d.drawString("OPTIONS", 20, 60);
			g2d.setFont(new Font("Arial", Font.BOLD, 20));
			
			g2d.drawString("Start size: ", 20, 120);
			g2d.drawString(Integer.toString(startSize), 200, 120);
			g2d.drawString("Apple spawnrate: ", 20, 160);
			g2d.drawString(Integer.toString(appleSpawnRate), 200, 160);
			g2d.drawString("Game speed: ", 20, 200);
			g2d.drawString(Integer.toString(gameSpeed), 200, 200);
			g2d.drawString("Drop chance: ", 20, 240);
			g2d.drawString(Integer.toString(dropChance), 200, 240);
			g2d.drawString("Apple-despawn: ", 20, 280);
			g2d.drawString(Boolean.toString(despawn), 200, 280);
			g2d.drawString("Despawn-speed: ", 20, 320);
			g2d.drawString(Integer.toString(despawnChance), 200, 320);
			g2d.drawString("Despawn-Start: ", 20, 360);
			g2d.drawString(Integer.toString(despawnStart), 200, 360);
			g2d.drawString("Grow: ", 20, 400);
			g2d.drawString(Integer.toString(grow), 200, 400);
			g2d.drawString("Background color: ", 400, 120);
			g2d.drawString(getColorName(backgroundColor), 600, 120);
			g2d.drawString("Text color: ", 400, 160);
			g2d.drawString(getColorName(textColor), 600, 160);
			g2d.drawString("Player 1 color: ", 400, 200);
			g2d.drawString(getColorName(player1Color), 600, 200);
			g2d.drawString("Player 2 color: ", 400, 240);
			g2d.drawString(getColorName(player2Color), 600, 240);
			g2d.drawString("Apple color: ", 400, 280);
			g2d.drawString(getColorName(appleColor), 600, 280);
			
			g2d.fillRect(optionsCursor_x, optionsCursor_y, 10, 40);
			
			g2d.drawString("Press 'escape' to return to the menu", 5, 590);
		}
	}
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			w_pressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			a_pressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			s_pressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			d_pressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			up_pressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left_pressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			down_pressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right_pressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			enter_pressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			escape_pressed = true;
			if (surface == "running") {
				gamePaused = !gamePaused;
			}
		}
	}
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			w_pressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			a_pressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			s_pressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			d_pressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			up_pressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left_pressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			down_pressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right_pressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			enter_pressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			escape_pressed = false;
		}
	}
	
	public void keyTyped(KeyEvent e) {}
	
	public static void main(String[] args) {
		BetterSnake r = new BetterSnake();
		JFrame jf = new JFrame();
		jf.setTitle("Better Snake");
		jf.setSize(WIDTH, HEIGHT);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);
		jf.setResizable(false);
		jf.add(r);
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			
		}
	}
	public void mouseReleased(MouseEvent e) {
		
	}
	
	public void error(Exception e) {
		System.err.println("An error has occurred. Plaese contact the Developer.");
		System.err.println("Error: "+e);
		System.exit(0);
	}
	public void p1Bigger(int elargement) {
		for (int x = 0; x < mapSizeX; x++) {
			for (int y = 0; y < mapSizeY; y++) {
				if ((points[x][y] < 10000) && (points[x][y] > 0)) {
					points[x][y] += elargement;
				}
			}
		}
		size1 += elargement;
	}
	public void p2Bigger(int elargement) {
		for (int x = 0; x < mapSizeX; x++) {
			for (int y = 0; y < mapSizeY; y++) {
				if (points[x][y] > 10000) {
					points[x][y] += elargement;
				}
			}
		}
		size2 += elargement;
	}
	public void spawnApple() {
		int random_num1 = (int)(Math.random()*mapSizeX);
		int random_num2 = (int)(Math.random()*mapSizeY);
		if (points[random_num1][random_num2] == 0) {
			points[random_num1][random_num2] = -5;
		}else {
			spawnApple();
		}
	}
	public void player1dead() {
		player1_x = 1;
		player1_y = 30;
		size1 = startSize;
		respawnTimer1 = 0;
		for (int x = 0; x < mapSizeX; x++) {
			for (int y = 0; y < mapSizeY; y++) {
				if (points[x][y] > 0 && points[x][y] < 10000) {
					points[x][y] = 0;
					if (((int)(Math.random()*dropChance)) == 0) {
						points[x][y] = -5;
					}
				}
			}
		}
	}
	public void player2dead() {
		player2_x = 78;
		player2_y = 30;
		size2 = startSize+10000;
		respawnTimer2 = 0;
		for (int x = 0; x < mapSizeX; x++) {
			for (int y = 0; y < mapSizeY; y++) {
				if (points[x][y] > 10000) {
					points[x][y] = 0;
					if (((int)(Math.random()*dropChance)) == 0) {
						points[x][y] = -5;
					}
				}
			}
		}
	}
	public String getColorName(Color c) {
		String color = "Black";
		if (c == Color.BLACK) {
			color = "Black";
		}else if (c == Color.BLUE) {
			color = "Blue";
		}else if (c == Color.CYAN) {
			color = "Cyan";
		}else if (c == Color.DARK_GRAY) {
			color = "Dark gray";
		}else if (c == Color.GRAY) {
			color = "Gray";
		}else if (c == Color.GREEN) {
			color = "Green";
		}else if (c == Color.LIGHT_GRAY) {
			color = "Light gray";
		}else if (c == Color.MAGENTA) {
			color = "Magenta";
		}else if (c == Color.ORANGE) {
			color = "Orange";
		}else if (c == Color.PINK) {
			color = "Pink";
		}else if (c == Color.RED) {
			color = "Red";
		}else if (c == Color.WHITE) {
			color = "White";
		}else if (c == Color.YELLOW) {
			color = "Yellow";
		}
		return color;
	}
	public void optionsNext() {
		if (optionsCursor_y == 90 && optionsCursor_x == 5) {
			startSize++;
		}else if (optionsCursor_y == 130 && optionsCursor_x == 5) {
			appleSpawnRate++;
		}else if (optionsCursor_y == 170 && optionsCursor_x == 5 && gameSpeed < 500) {
			gameSpeed++;
		}else if (optionsCursor_y == 210 && optionsCursor_x == 5) {
			dropChance++;
		}else if (optionsCursor_y == 250 && optionsCursor_x == 5) {
			despawn = true;
		}else if (optionsCursor_y == 290 && optionsCursor_x == 5) {
			despawnChance++;
		}else if (optionsCursor_y == 330 && optionsCursor_x == 5) {
			despawnStart++;
		}else if (optionsCursor_y == 370 && optionsCursor_x == 5) {
			grow++;
		}else if (optionsCursor_y == 90 && optionsCursor_x == 380) {
			backgroundColor = nextColor(backgroundColor);
		}else if (optionsCursor_y == 130 && optionsCursor_x == 380) {
			textColor = nextColor(textColor);
		}else if (optionsCursor_y == 170 && optionsCursor_x == 380) {
			player1Color = nextColor(player1Color);
		}else if (optionsCursor_y == 210 && optionsCursor_x == 380) {
			player2Color = nextColor(player2Color);
		}else if (optionsCursor_y == 250 && optionsCursor_x == 380) {
			appleColor = nextColor(appleColor);
		}
	}
	public void optionsPrevious() {
		if (optionsCursor_y == 90 && optionsCursor_x == 5 && startSize > 1) {
			startSize--;
		}else if (optionsCursor_y == 130 && optionsCursor_x == 5 && appleSpawnRate > 1) {
			appleSpawnRate--;
		}else if (optionsCursor_y == 170 && optionsCursor_x == 5 && gameSpeed > 20) {
			gameSpeed--;
		}else if (optionsCursor_y == 210 && optionsCursor_x == 5 && dropChance > 1) {
			dropChance--;
		}else if (optionsCursor_y == 250 && optionsCursor_x == 5) {
			despawn = false;
		}else if (optionsCursor_y == 290 && optionsCursor_x == 5 && despawnChance > 1) {
			despawnChance--;
		}else if (optionsCursor_y == 330 && optionsCursor_x == 5 && despawnStart > 1) {
			despawnStart--;
		}else if (optionsCursor_y == 370 && optionsCursor_x == 5 && grow > 1) {
			grow--;
		}else if (optionsCursor_y == 90 && optionsCursor_x == 380) {
			backgroundColor = previousColor(backgroundColor);
		}else if (optionsCursor_y == 130 && optionsCursor_x == 380) {
			textColor = previousColor(textColor);
		}else if (optionsCursor_y == 170 && optionsCursor_x == 380) {
			player1Color = previousColor(player1Color);
		}else if (optionsCursor_y == 210 && optionsCursor_x == 380) {
			player2Color = previousColor(player2Color);
		}else if (optionsCursor_y == 250 && optionsCursor_x == 380) {
			appleColor = previousColor(appleColor);
		}
	}
	public Color nextColor(Color c){
		if (c == Color.BLACK) {
			c = Color.BLUE;
		}else if (c == Color.BLUE) {
			c = Color.CYAN;
		}else if (c == Color.CYAN) {
			c = Color.DARK_GRAY;
		}else if (c == Color.DARK_GRAY) {
			c = Color.GRAY;
		}else if (c == Color.GRAY) {
			c = Color.GREEN;
		}else if (c == Color.GREEN) {
			c = Color.LIGHT_GRAY;
		}else if (c == Color.LIGHT_GRAY) {
			c = Color.MAGENTA;
		}else if (c == Color.MAGENTA) {
			c = Color.ORANGE;
		}else if (c == Color.ORANGE) {
			c = Color.PINK;
		}else if (c == Color.PINK) {
			c = Color.RED;
		}else if (c == Color.RED) {
			c = Color.WHITE;
		}else if (c == Color.WHITE) {
			c = Color.YELLOW;
		}else if (c == Color.YELLOW) {
			c = Color.BLACK;
		}
		return c;
	}
	public Color previousColor(Color c) {
		if (c == Color.BLACK) {
			c = Color.YELLOW;
		}else if (c == Color.BLUE) {
			c = Color.BLACK;
		}else if (c == Color.CYAN) {
			c = Color.BLUE;
		}else if (c == Color.DARK_GRAY) {
			c = Color.CYAN;
		}else if (c == Color.GRAY) {
			c = Color.DARK_GRAY;
		}else if (c == Color.GREEN) {
			c = Color.GRAY;
		}else if (c == Color.LIGHT_GRAY) {
			c = Color.GREEN;
		}else if (c == Color.MAGENTA) {
			c = Color.LIGHT_GRAY;
		}else if (c == Color.ORANGE) {
			c = Color.MAGENTA;
		}else if (c == Color.PINK) {
			c = Color.ORANGE;
		}else if (c == Color.RED) {
			c = Color.PINK;
		}else if (c == Color.WHITE) {
			c = Color.RED;
		}else if (c == Color.YELLOW) {
			c = Color.WHITE;
		}
		return c;
	}
}