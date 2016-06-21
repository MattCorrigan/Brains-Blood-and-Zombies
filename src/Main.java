
import java.awt.Container;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Main extends JFrame {
    private static final long serialVersionUID = 1L;
    Timer timer;
    Toolkit kit;
    Player player;
    GamePanel gamepanel;
    public String BP = (new File(".")).getAbsolutePath();
    ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    ArrayList<Explosion> explosions = new ArrayList<Explosion>();
    int spawnCount = 0;
    int spawnInterval = 40;
    int spawnCountInterval = 0;
    public static int MAX_SPAWNS = 100;
    Main main;
    public int wave = 1;
    int waveWaitTime = 0;
    ArrayList<Trap> traps = new ArrayList<Trap>();
    public boolean start = false;
    int timeSinceMoan = 0;

    // ArrayList<Weapon> toolbar = new ArrayList<Weapon>();

    public Main() {
	BP = BP.substring(0, BP.length() - 1);
	getContentPane().requestFocus();
	showScreen();
	main = this;
	timer = new javax.swing.Timer(20, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		// game loop
		repaintWindow();
		if (!player.DEAD) {
		    spawn();
		}
		timeSinceMoan++;
		if (timeSinceMoan > 250) {// 5 seconds
		    int chance = (int) (Math.random() * 250); // 250 * 20 == 5 seconds
		    if (chance == 1) {
			String filename = "moan.wav";
			if ((int) (Math.random() * 3) == 1) {
			    filename = "brainzzz.wav";
			}
			Player.play(filename);
			timeSinceMoan = 0;
		    }
		}
	    }
	});
	timer.start();
	player = new Player(0, 0, this);
	explosions.add(new Explosion(this, 100, 100, 20));
	setVisible(true);
    }

    public void spawn() {
	if (spawnCount != MAX_SPAWNS) {
	    if (spawnCountInterval == spawnInterval) {
		spawnCountInterval = 0;
		spawnCount++;
		zombies.add(new Zombie((int) (Math.random() * 2496) - (main.getWidth() / 2),
			(int) (Math.random() * 1600) - (main.getWidth() / 2),
			(int) (Math.random() * 7) + 1, main));
	    } else {
		spawnCountInterval++;
	    }
	}
	if (player.kills == (wave * MAX_SPAWNS) + ((wave - 1) * 20)) {
	    waveWaitTime++;
	    if (waveWaitTime == 100 * 15) { // wait 30 seconds
		wave++;
		spawnCount = 0;
		waveWaitTime = 0;
		MAX_SPAWNS += 20;
	    }
	}
    }

    public void repaintWindow() {
	getContentPane().repaint();
    }

    public void showScreen() {
	kit = Toolkit.getDefaultToolkit();
	Image img = kit.getImage(BP + "/zombieicon.png");
	setIconImage(img);
	setSize(750, 393);
	setUndecorated(true);
	setLocation(250, 300);
	setTitle("Brains, Blood, and Zombies");
	Container contentPane = getContentPane();
	gamepanel = new GamePanel(this);
	contentPane.add(gamepanel);
    }

    public void stop() {
	timer.stop();
	this.dispose();
    }

    public static void main(String[] args) {
	Main main = new Main();
	main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	main.setResizable(false);
	main.addWindowListener(new java.awt.event.WindowAdapter() {
	    @Override
	    public void windowClosing(java.awt.event.WindowEvent evt) {

	    }
	});
    }
}
