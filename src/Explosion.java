import java.awt.Graphics;

public class Explosion {

    int x;
    int y;
    int radius;
    Main main;

    public Explosion(Main main, int x, int y, int radius) {
	this.x = x;
	this.y = y;
	this.radius = radius;
	this.main = main;
    }

    public void update() {

    }

    public void draw(Graphics g) {
	update();
	int drawx = (int) (2 * x - main.player.getX());
	int drawy = (int) (2 * y - main.player.getY());
	g.fillOval(drawx, drawy, radius * 2, radius * 2);
    }
}
