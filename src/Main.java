import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Bouncing balls :)
 * 
 * @author Thomas Edwards
 *
 */
public class Main {

	private final int boundaryX = 50;
	private final int boundaryY = 50;
	private final int boundaryWidth = 400;
	private final int boundaryHeight = 400;

	private List<Ball> balls = new ArrayList<Ball>();

	public Main() {
		addBalls();
		JComponent panel = setupGUI();// this panel is what contains the paint component
		while (true) {
			panel.repaint();
			this.update();
		}
	}

	/**
	 * setups the gui.
	 * 
	 * @return the JComponent which is responsible for drawing the balls.
	 */
	public JComponent setupGUI() {
		JFrame frame = new JFrame();
		JComponent panel = new JComponent() {
			protected void paintComponent(Graphics g) {
				g.drawRect(boundaryX - 2, boundaryY - 2, boundaryWidth + 3, boundaryHeight + 3);
				g.clearRect(boundaryX - 1, boundaryY - 1, boundaryWidth + 2, boundaryHeight + 2);
				for (Ball b : balls) {
					b.drawNew(g);
				}
			}
		};
		frame.setSize(boundaryX * 3 + boundaryWidth, boundaryY * 3 + boundaryHeight);
		frame.setTitle("Bouncing Balls");
		frame.add(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		return panel;
	}

	/**
	 * Adds balls, 33% to add totally random balls. 33% chance to add varied balls, 33% chance to add black and red
	 * balls.
	 */
	public void addBalls() {
		// double rand = Math.random();
		// if (rand < 0.33)
		// addRandomBalls((int) (Math.random() * 15));
		// else if (rand < 0.66)
		// addVariedBalls();
		// else
		addEvenBalls();
	}

	public void addRandomBalls(int amount) {
		for (int i = 0; i < amount; i++) {
			Ball b1 = new Ball(balls);
			balls.add(b1);
		}
	}

	public void addVariedBalls() {
		Ball b1 = new Ball(15, 10, 295, 230, 50, 300, Color.BLUE);
		balls.add(b1);
		Ball b2 = new Ball(20, 15, 250, 250, 300, 40, Color.RED);
		balls.add(b2);
		Ball b3 = new Ball(30, 8, 100, 150, 200, 300, Color.GREEN);
		balls.add(b3);
		Ball b4 = new Ball(40, 15, 100, 250, 400, 600, Color.pink);
		balls.add(b4);
		Ball b5 = new Ball(50, 20, 80, 90, -300, 0, Color.YELLOW);
		balls.add(b5);
		Ball b6 = new Ball(200, 20, 200, 90, 300, 0, Color.black);
		balls.add(b6);
		Ball b7 = new Ball(150, 10, 195, 230, -400, -300, Color.orange);
		balls.add(b7);
	}

	public void addEvenBalls() {
		Ball b1 = new Ball(10, 15, 295, 330, 50, 300, Color.BLACK);
		balls.add(b1);
		Ball b2 = new Ball(10, 15, 250, 250, 300, 540, Color.RED);
		balls.add(b2);
		Ball b3 = new Ball(10, 15, 100, 170, 200, 300, Color.BLACK);
		balls.add(b3);
		Ball b4 = new Ball(10, 15, 100, 250, 400, 600, Color.RED);
		balls.add(b4);
		Ball b5 = new Ball(10, 15, 360, 90, -300, 650, Color.BLACK);
		balls.add(b5);
		Ball b6 = new Ball(10, 15, 200, 90, 300, -450, Color.RED);
		balls.add(b6);
		Ball b7 = new Ball(10, 15, 195, 230, -600, -300, Color.BLACK);
		balls.add(b7);
		Ball b8 = new Ball(10, 15, 300, 190, -300, -450, Color.RED);
		balls.add(b8);
		Ball b9 = new Ball(10, 15, 300, 150, -300, 50, Color.BLACK);
		balls.add(b9);
		Ball b10 = new Ball(10, 15, 200, 400, 300, -550, Color.RED);
		balls.add(b10);
		Ball b11 = new Ball(10, 15, 95, 330, -400, -300, Color.BLACK);
		balls.add(b11);
		Ball b12 = new Ball(10, 15, 100, 400, 700, -450, Color.RED);
		balls.add(b12);
	}

	/**
	 * Sleeps for 0.003 seconds after updating
	 */
	public void update() {
		for (Ball b : balls) {
			b.move();
			checkCollision(b);
		}
		try {
			Thread.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void checkCollision(Ball b1) {
		this.touchingTopBotWalls(b1);
		this.touchingLeftRightWalls(b1);
		for (int j = 0; j < balls.size(); j++) {
			Ball b2 = balls.get(j);
			if (b1 == b2)
				continue;
			b1.compareBalls(b2);
		}
	}

	public void touchingTopBotWalls(Ball b) {
		if (b.y + b.radius >= boundaryHeight + boundaryY - 1 || b.y - b.radius <= boundaryY + 1) {
			b.yVelocity = -b.yVelocity;
		}
	}

	public void touchingLeftRightWalls(Ball b) {
		if (b.x - b.radius <= boundaryX + 1 || b.x + b.radius >= boundaryX + boundaryWidth - 1) {
			b.xVelocity = -b.xVelocity;
		}
	}

	public static void main(String[] args) {
		new Main();
	}

}
