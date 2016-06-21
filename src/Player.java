import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class Player {
    private static final double MOVE_SPEED = 7;
    private double x;
    private double y;
    protected double health;
    public int MAX_HEALTH = 100;
    public int shotWaitTime = 8;
    public int coins = 0;

    public int kills = 0;

    private final ArrayList<Bullet> removals = new ArrayList<Bullet>();
    private final ArrayList<Zombie> killed = new ArrayList<Zombie>();

    public double xpos;
    public double ypos;

    public boolean DEAD = false;

    private final Main main;

    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private boolean shooting = false;
    private int shootCount = 0;

    private String DIR = "forward";
    private int walkCount = 0;
    private int walkCountInterval = 0;

    public static final double SCALE = 1;

    int width = 150;
    int height = 117;
    int imagey = 117;

    public ArrayList<Bullet> attacks = new ArrayList<Bullet>();

    final int[][] shootPos = { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } };

    public Player(int x, int y, Main main) {
	this.health = MAX_HEALTH;
	this.x = x;
	this.y = y;
	this.main = main;
	xpos = (main.getWidth() / 2) - (width / 2);
	ypos = (main.getHeight() / 2) - (height / 2);
    }

    public double getX() {
	return x;
    }

    public double getY() {
	return y;
    }

    public String getDir() {
	return DIR;
    }

    public void damage(int healthdamage) {
	health -= healthdamage;
    }

    public void trapKill(Zombie zombie) {
	killed.add(zombie);
	coins -= 3; // to counteract the 5 gained, to make it 1 gold
    }

    public void kill(Zombie zombie) {
	coins += 2;
	kills++;
	killed.add(zombie);
    }

    public static void play(String filename) {
	try {

	    Clip clip = AudioSystem.getClip();
	    clip.open(AudioSystem.getAudioInputStream(new File(filename)));
	    clip.start();
	} catch (Exception exc) {
	    // exc.printStackTrace(System.out);
	}
    }

    public boolean collisionDetect(double playerx, double playery) {
	playerx += (main.getWidth() / 2) - (width / 2);
	playery += (main.getHeight() / 2) - (height / 2);
	playerx -= 35;
	playery += 40;
	ArrayList<ArrayList<Tile>> tiles = main.gamepanel.tiles;
	if (tiles != null) {
	    for (ArrayList<Tile> tileset : tiles) {
		for (Tile tile : tileset) {
		    if (tile.isSolid()) {
			if (!((playery + height - 40) < tile.getY()
				|| playery > (tile.getY() + 64)
				|| (playerx + width - 10) < tile.getX() || playerx + 70 > (tile
					.getX() + 64))) {
			    // System.out.println(playerx + ", " + playery);
			    // System.out.println(tile.getX() + ", " +
			    // tile.getY());
			    return true;
			}
		    }
		}
	    }
	}
	return false;
    }

    public void start(char dir) {
	if (dir == 'a') {
	    left = true;
	    right = false;
	    up = false;
	    down = false;
	    DIR = "left";
	} else if (dir == 'd') {
	    left = false;
	    right = true;
	    up = false;
	    down = false;
	    DIR = "right";
	} else if (dir == 'w') {
	    up = true;
	    down = false;
	    left = false;
	    right = false;
	    DIR = "forward";
	} else if (dir == 's') {
	    down = true;
	    up = false;
	    left = false;
	    right = false;
	    DIR = "backward";
	} else if (dir == ' ') {
	    shooting = true;
	}
    }

    public void disposeBullet(Bullet bullet) {
	removals.add(bullet);
    }

    public void end(char dir) {
	if (dir == 'a') {
	    left = false;
	} else if (dir == 'd') {
	    right = false;
	} else if (dir == 'w') {
	    up = false;
	} else if (dir == 's') {
	    down = false;
	} else if (dir == ' ') {
	    shooting = false;
	}
    }

    public void shoot() {
	int modifierx = 0;
	int modifiery = 0;
	if (DIR == "left") {
	    modifierx = 30;
	    modifiery = 87;
	} else if (DIR == "right") {
	    modifierx = 100;
	    modifiery = 87;
	} else if (DIR == "forward") {
	    modifierx = 65;
	    modifiery = 40;
	} else if (DIR == "backward") {
	    modifierx = 53;
	    modifiery = 100;
	}
	main.player.attacks
	.add(new Bullet(modifierx + x + xpos, modifiery + y + ypos,
		DIR, main));
	play(main.BP + "gunshot.wav");
    }

    public void move() {
	if (shootCount >= shotWaitTime) {
	    if (shooting) {
		shoot();
		shootCount = 0;
	    }
	} else {
	    shootCount++;
	}
	if (right) {
	    if (!collisionDetect(x + MOVE_SPEED, y)) {
		x += MOVE_SPEED;
	    }
	    imagey = 351;
	}
	if (left) {
	    if (!collisionDetect(x - MOVE_SPEED, y)) {
		x -= MOVE_SPEED;
	    }
	    imagey = 234;
	}
	if (up) {
	    if (!collisionDetect(x, y - MOVE_SPEED)) {
		y -= MOVE_SPEED;
	    }
	    imagey = 117;
	}
	if (down) {
	    if (!collisionDetect(x, y + MOVE_SPEED)) {
		y += MOVE_SPEED;
	    }
	    imagey = 0;
	}

    }

    public void draw(Graphics g) {
	if (main.waveWaitTime != 0 && (int) health != MAX_HEALTH) {
	    health += 0.008333333; // so that in 30 secs you'll gain 25 life
	}
	xpos = (main.getWidth() / 2) - (width / 2);
	ypos = (main.getHeight() / 2) - (height / 2);
	if (!DEAD) {
	    move();
	    if (left || right || up || down) {
		if (walkCountInterval == 3) {
		    if (walkCount < 6) {
			walkCount++;
		    } else {
			walkCount = 0;
		    }
		    walkCountInterval = 0;
		} else {
		    walkCountInterval++;
		}
	    } else {
		walkCount = 0;
	    }
	}
	for (Bullet bullet : removals) {
	    attacks.remove(bullet);
	}
	removals.clear();
	for (Zombie zombie : killed) {
	    main.zombies.remove(zombie);
	}
	killed.clear();
	g.drawImage(main.kit.getImage(main.BP + "/player.png"), (int) xpos,
		(int) ypos,
		(int) (width / SCALE) + (int) xpos, (int) (height / SCALE)
		+ (int) ypos,
		(walkCount * width), imagey, width
		+ (walkCount * width), height + imagey,
		null);
    }
}
