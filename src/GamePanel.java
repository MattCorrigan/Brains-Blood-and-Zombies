
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class GamePanel extends JPanel implements KeyListener, MouseListener,
MouseMotionListener {
    private static final long serialVersionUID = 1L;
    Main main;
    ArrayList<ArrayList<Tile>> tiles = new ArrayList<ArrayList<Tile>>();
    Rectangle highlight = new Rectangle(0, 0, 0, 0);

    public static final int SPIKE_TRAP_COST = 10;
    public String error = "";
    private int errorCount = 0;

    public GamePanel(Main main2) {
	main = main2;
	this.setBackground(new Color(0, 0, 0));
	addKeyListener(this);
	addMouseListener(this);
	addMouseMotionListener(this);
	setFocusable(true);
	try {
	    loadTiles();
	} catch (Exception e) {
	    System.out.println("tiles not loading");
	}
    }

    public void loadTiles() throws IOException {
	BufferedReader reader = new BufferedReader(new FileReader(new File(
		main.BP + "/map1.txt")));
	String line = "";
	int xcount = -8;
	int ycount = -8;
	while ((line = reader.readLine()) != null) {
	    String[] letters = line.split(" ");
	    ArrayList<Tile> tileset = new ArrayList<Tile>();
	    for (String alphanumerical : letters) {
		tileset.add(new Tile(xcount, ycount, alphanumerical, main));
		xcount++;
	    }
	    tiles.add(tileset);
	    xcount = -8;
	    ycount++;
	}
	reader.close();
    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	if (!main.start) {
	    g.drawImage(main.kit.getImage(main.BP + "/background.png"), 0, 0, main.getWidth(),
		    main.getHeight(), null); // 444, 94 : 804, 217
	    int xpos = 315;
	    int ypos = 120;
	    g.drawImage(main.kit.getImage(main.BP + "/buttons.png"), xpos,
		    ypos, xpos + 90, ypos + 30,
		    444, 94, 804, 217, null);
	    // g.drawImage(main.kit.getImage(main.BP + "/button2.png"), xpos,
	    // ypos + 40, 90, 30, null);
	    g.drawString("Press esc to exit", xpos, ypos + 50);
	} else {
	    for (ArrayList<Tile> tileset : tiles) {
		for (Tile tile : tileset) {
		    tile.draw(g);
		}
	    }
	    for (Trap trap : main.traps) {
		trap.draw(g);
	    }
	    if (main.waveWaitTime > 0) {
		g.setColor(Color.blue);
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setComposite(AlphaComposite.getInstance(
			AlphaComposite.SRC_OVER,
			0.5f));
		g2d.drawImage(main.kit.getImage(main.BP + "/spikes.png"),
			highlight.x, highlight.y, null);
		g2d.dispose();
	    }
	    for (Zombie zombie : main.zombies) {
		zombie.draw(g);
	    }
	    for (Bullet bullet : main.player.attacks) {
		bullet.draw(g);
	    }

	    for (Explosion e : main.explosions) {
		e.draw(g);
	    }

	    // ui
	    g.drawImage(main.kit.getImage(main.BP + "/grayback.png"), 0, 0,350,100, null);
	    if (main.player.health >= 1) {
		g.setColor(Color.white);
		g.fillRect(19, 9, 221, 14);
		g.setColor(Color.red);
		g.fillRect(20, 10,
			20 + (int) (main.player.health / (main.player.MAX_HEALTH / 200.0)),
			13);
		g.setColor(Color.white);
		g.drawString(Integer.toString((int) main.player.health) + "/"
			+ Integer.toString(main.player.MAX_HEALTH), 115, 21);
	    } else {
		main.player.DEAD = true;
	    }
	    g.drawImage(main.kit.getImage(main.BP + "/killicon.png"), 250, 3, null);
	    g.setColor(Color.white);
	    g.setFont(g.getFont().deriveFont(25f));
	    g.drawString(Integer.toString(main.player.kills), 290, 27);
	    g.drawString("WAVE " + Integer.toString(main.wave), 15, 50);
	    g.drawImage(main.kit.getImage(main.BP + "/coin.png"), 255, 37, 20, 20,
		    null);
	    g.drawString(Integer.toString(main.player.coins), 289, 56);
	    if (main.waveWaitTime > 0) {
		g.drawString(Integer.toString(30 - (main.waveWaitTime / 50))
			+ " secs", 15, 75);
	    }
	    main.player.draw(g);
	    if (main.player.DEAD) {
		// Game Over
		g.drawImage(main.kit.getImage(main.BP + "/gameover.png"),
			(main.getWidth() / 2)
			- (main.kit.getImage(main.BP + "/gameover.png")
				.getWidth(null) / 2),
			(main.getHeight() / 2)
			- (main.kit.getImage(main.BP + "/gameover.png")
				.getHeight(null) / 2),
			null);
	    }

	    if (error.length() > 0) {
		g.setColor(Color.red);
		g.drawString(error, (main.getWidth() / 2) - 50, 30);
		errorCount++;
		if (errorCount == 200) {
		    error = "";
		    errorCount = 0;
		}
	    }

	}
	// checking for exit (escape key)
	inputInit();
    }

    public static BufferedImage toBufferedImage(Image img) {
	if (img instanceof BufferedImage) {
	    return (BufferedImage) img;
	}
	BufferedImage bimage;
	try {
	    // Create a buffered image with transparency
	    bimage = new BufferedImage(img.getWidth(null), img.getHeight(null),
		    BufferedImage.TRANSLUCENT);

	} catch (Exception e) {
	    bimage = new BufferedImage(61, 61, BufferedImage.TRANSLUCENT);
	}

	// Draw the image on to the buffered image
	Graphics2D bGr = bimage.createGraphics();
	bGr.drawImage(img, 0, 0, null);
	bGr.dispose();

	// Return the buffered image
	return bimage;
    }

    @Override
    public void keyPressed(KeyEvent e) {
	char key = e.getKeyChar();
	main.player.start(key);
    }

    @Override
    public void keyReleased(KeyEvent e) {
	char key = e.getKeyChar();
	main.player.end(key);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
	int x = e.getX();
	int y = e.getY();
	if (!main.start) {
	    if (x >= 315 && x <= 405) {
		if (y >= 120 && y <= 150) {
		    main.start = true;
		    main.setExtendedState(main.getExtendedState()
			    | JFrame.MAXIMIZED_BOTH);
		}
	    }
	}
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
	int x = e.getX();
	int y = e.getY();
	if (main.waveWaitTime > 0) {
	    if (main.player.coins >= SPIKE_TRAP_COST) {
		main.player.coins -= SPIKE_TRAP_COST;
		for (ArrayList<Tile> tileset : tiles) {
		    for (Tile tile : tileset) {
			if (!tile.isSolid() && !tile.containsTrap) {
			    int tilex = tile.getX() - (int) main.player.getX();
			    int tiley = tile.getY() - (int) main.player.getY();
			    if (x >= tilex && x <= tilex + 64 && y >= tiley
				    && y <= tiley + 64) {
				tile.containsTrap = true;
				main.traps.add(new Trap(tile.getX() / 64,
					tile.getY() / 64, 's', main));
			    }
			}
		    }
		}
	    } else {
		error = "Not enough gold!";
		errorCount = 0;
	    }
	} else {
	    main.player.start(' ');
	}
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	main.player.end(' ');
    }

    public void inputInit() {
	InputMap im = getInputMap(WHEN_FOCUSED);
	ActionMap am = getActionMap();
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "onExit");

	am.put("onExit", new AbstractAction() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		main.stop();
	    }
	});
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
	int x = e.getX();
	int y = e.getY();
	if (main.waveWaitTime > 0) {
	    for (ArrayList<Tile> tileset : tiles) {
		for (Tile tile : tileset) {
		    if (!tile.isSolid() && !tile.containsTrap) {
			int tilex = tile.getX() - (int) main.player.getX();
			int tiley = tile.getY() - (int) main.player.getY();
			if (x >= tilex && x <= tilex + 64 && y >= tiley && y <= tiley + 64) {
			    highlight = new Rectangle(tilex, tiley, 64, 64);
			}
		    }
		}
	    }
	}
    }
}