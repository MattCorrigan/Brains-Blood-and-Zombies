
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class CreatePanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;
    Creator Creator;

    public CreatePanel(Creator creator) {
	Creator = creator;
	addKeyListener(this);
	addMouseListener(this);
	addMouseMotionListener(this);
	setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	// g.drawString("hi", 100, 100);
	// checking for exit (escape key)
	inputInit();
    }

    public static BufferedImage toBufferedImage(Image img) {
	if (img instanceof BufferedImage) {
	    return (BufferedImage) img;
	}
	BufferedImage bimage;
	try {
	    bimage = new BufferedImage(img.getWidth(null), img.getHeight(null),
		    BufferedImage.TRANSLUCENT);
	} catch (Exception e) {
	    bimage = new BufferedImage(61, 61, BufferedImage.TRANSLUCENT);
	}
	Graphics2D bGr = bimage.createGraphics();
	bGr.drawImage(img, 0, 0, null);
	bGr.dispose();
	return bimage;
    }

    @Override
    public void keyPressed(KeyEvent e) {
	char key = e.getKeyChar();
    }

    @Override
    public void keyReleased(KeyEvent e) {
	char key = e.getKeyChar();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public void inputInit() {
	InputMap im = getInputMap(WHEN_FOCUSED);
	ActionMap am = getActionMap();
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "onExit");

	am.put("onExit", new AbstractAction() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		Creator.stop();
	    }
	});
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}