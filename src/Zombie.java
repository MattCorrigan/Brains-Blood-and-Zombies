import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class Zombie {
    private double x;
    private double y;
    private final int type;
    private final Main main;
    private final double origx;
    private final double origy;
    private double hp;
    public int MAX_HP = 25;
    private char dirFix = 'x';

    private int moveCount = 0;
    private int moveCountInterval = 0;
    private int dir = 2;
    private int damageInterval = 100;

    public static double MOVEMENT = 0.5;

    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;

    private final int[][] types = { { 0, 0 }, { 96, 0 }, { 192, 0 },
	    { 288, 0 }, { 0, 128 }, { 96, 128 }, { 192, 128 }, { 288, 128 } };

    public Zombie(int x, int y, int type, Main main) {
	hp = MAX_HP;
	this.x = x;
	this.y = y;
	this.type = type;
	this.main = main;
	origx = x;
	origy = y;
	this.x += 100;
	this.y += 100;
    }

    public void FixX() {
	int drawx = (int) (x - (main.player.getX() - origx));
	double playerx = ((main.getWidth() / 2) - (150 / 2));
	dirFix = 'x';
	if (drawx > playerx) {
	    x -= MOVEMENT;
	    left = true;
	    up = false;
	    down = false;
	    right = false;
	    dir = 1;
	} else {
	    x += MOVEMENT;
	    right = true;
	    up = false;
	    down = false;
	    left = false;
	    dir = 2;
	}
    }

    public void FixY() {
	int drawy = (int) (y - (main.player.getY() - origy));
	double playery = ((main.getHeight() / 2) - (117 / 2));
	dirFix = 'y';
	if (drawy > playery) {
	    y -= MOVEMENT;
	    up = true;
	    down = false;
	    left = false;
	    right = false;
	    dir = 3;
	} else if (drawy < playery) {
	    y += MOVEMENT;
	    down = true;
	    up = false;
	    right = false;
	    left = false;
	    dir = 0;
	}
    }

    public void move() {
	int drawx = (int) (x - (main.player.getX() - origx));
	int drawy = (int) (y - (main.player.getY() - origy));
	double playerx = ((main.getWidth() / 2) - (150 / 2));
	double playery = ((main.getHeight() / 2) - (117 / 2));
	boolean ydone = false;
	boolean xdone = false;
	if (drawy >= playery - 10 && drawy <= playery + 80) {
	    ydone = true;
	}
	if (drawx >= playerx && drawx <= playerx + 80) {
	    xdone = true;
	}

	if (!xdone && !ydone) {
	    if (dirFix == 'x') {
		if ((int) (Math.random() * 300) == 0) {
		    FixY();
		} else {
		    FixX();
		}
	    } else {
		if ((int) (Math.random() * 300) == 0) {
		    FixX();
		} else {
		    FixY();
		}
	    }
	} else {
	    if (xdone && !ydone) {
		FixY();
	    } else if (ydone && !xdone) {
		FixX();
	    }
	}
	if (xdone && ydone) {
	    left = false;
	    right = false;
	    up = false;
	    down = false;
	    if (damageInterval == 100) {
		damageInterval = 0;
		int damage = (int) ((Math.random() * 5) + 5); // random integer between 5 and 10
		main.player.damage(damage);
	    } else {
		damageInterval++;
	    }
	}
	if (left || right || up || down) {
	    if (moveCountInterval == 14) {
		moveCount++;
		if (moveCount == 3) {
		    moveCount = 0;
		}
		moveCountInterval = 0;
	    } else {
		moveCountInterval++;
	    }
	} else {
	    moveCount = 0;
	}
    }

    public void damage(int damage) {
	hp -= damage;
    }

    public double getX() {
	double drawx = x - (main.player.getX() - origx);
	return drawx;
    }

    public double getY() {
	double drawy = y - (main.player.getY() - origy);
	return drawy;
    }

    public void draw(Graphics g) {
	collisionDetect();
	checkTraps();
	if (!main.player.DEAD) {
	    move();
	}
	int drawx = (int) (x - (main.player.getX() - origx));
	int drawy = (int) (y - (main.player.getY() - origy));
	int modx = 0, mody = 0;
	modx = types[type - 1][0];
	mody = types[type - 1][1];
	mody += dir * 32;
	modx += moveCount * 32;
	if (hp <= 0) {
	    main.player.kill(this);
	} else {
	    if (hp < MAX_HP) {
		g.setColor(Color.red);
		g.fillRect(drawx + 10, drawy - 3, (int) (hp / (MAX_HP / 50.0)),
			2);
	    }
	}
	g.drawImage(main.kit.getImage("zombies.png"), drawx, drawy, drawx + 64,
		drawy + 64,
		modx, mody, modx + 32, mody + 32,
		null);
    }

    public boolean collisionDetect() {
	int drawx = (int) (x - (main.player.getX() - origx));
	int drawy = (int) (y - (main.player.getY() - origy));
	ArrayList<ArrayList<Tile>> tiles = main.gamepanel.tiles;
	if (tiles != null) {
	    for (ArrayList<Tile> tileset : tiles) {
		for (Tile tile : tileset) {
		    if (tile.isSolid()) {
			int tilex = tile.getX() - (int) (main.player.getX());
			int tiley = tile.getY() - (int) (main.player.getY());
			if (!((drawy + 64) < tiley || drawy > (tiley + 64)
				|| (drawx + 64) < tilex || drawx > (tilex + 64))) {
			    MOVEMENT = 0.2;
			    return true;
			}
		    }
		}
	    }
	}
	MOVEMENT = 0.5;
	return false;
    }

    public boolean checkTraps() {
	int drawx = (int) (x - (main.player.getX() - origx));
	int drawy = (int) (y - (main.player.getY() - origy));
	ArrayList<Trap> traps = main.traps;
	if (traps != null) {
	    for (Trap trap : traps) {
		int trapx = trap.getX() - (int) (main.player.getX());
		int trapy = trap.getY() - (int) (main.player.getY());
		if (!((drawy + 64) < trapy || drawy + 55 > (trapy + 64) // 55 is to make it the feet
			|| (drawx + 64) < trapx || drawx > (trapx + 64))) {
		    hp -= 0.03;
		    if (hp <= 0) {
			main.player.trapKill(this);
		    }
		    return true;
		}
	    }
	}
	return false;
    }
}
