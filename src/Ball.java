import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Ball {

	double mass;
	double xVelocity, yVelocity;
	double radius;
	double x, oldX, oldY, y;// these are the centres of the circles
	Color col;

	/**
	 * Constructs a ball with the specified properties
	 */
	public Ball(double mass, double radius, double x, double y, double xSpeed, double ySpeed, Color colour) {
		this.mass = mass;
		this.radius = radius;
		this.x = x;
		this.y = y;
		this.xVelocity = xSpeed;
		this.yVelocity = ySpeed;
		this.col = colour;
	}

	/**
	 * Constructs a Ball which has random properties within a certain range. The ball constructed is guaranteed to not
	 * overlap with any of the balls in the balls List.
	 * 
	 * @param balls
	 *            the list of balls which have already been added
	 */
	public Ball(List<Ball> balls) {
		this.mass = Math.random() * 50;
		this.radius = Math.random() * 20 + 10;
		this.xVelocity = Math.random() * 400 + 50;
		this.yVelocity = Math.random() * 400 + 50;
		this.col = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
		boolean algood = false;
		while (!algood) {// ensures that a ball isn't overlapping with another ball
			this.x = Math.random() * 200 + 70 + radius / 2.2;
			this.y = Math.random() * 200 + 70 + radius / 2.2;
			int i = 0;
			for (; i < balls.size(); i++) {
				Ball b = balls.get(i);
				if (touchingCircles(x, y, radius, b.x, b.y, b.radius)) {
					break;
				}
			}
			if (i == balls.size())
				algood = true;
		}
	}

	/**
	 * Called every 0.003 seconds, moves the position of the balls 0.001 of their speed in the correct direction
	 */
	public void move() {
		double yMovement = yVelocity * 0.001;
		double xMovement = xVelocity * 0.001;
		oldX = x;
		oldY = y;
		x += xMovement;
		y += yMovement;
	}

	/**
	 * Checks whether this ball is touching the other ball, if it is then it changes the celocities of both balls to
	 * simulate the collision.
	 * 
	 * @param b
	 *            the other ball
	 */
	public void compareBalls(Ball b) {
		// if the balls touch, we have a collision
		if (this.touchingCircles(this.x, this.y, this.radius, b.x, b.y, b.radius)) {
			double ballxVel1 = (this.xVelocity * (this.mass - b.mass) + 2 * b.mass * b.xVelocity)
					/ (this.mass + b.mass);
			double ballxVel2 = (b.xVelocity * (b.mass - this.mass) + 2 * this.mass * this.xVelocity)
					/ (this.mass + b.mass);

			double ballyVel1 = (this.yVelocity * (this.mass - b.mass) + 2 * b.mass * b.yVelocity)
					/ (this.mass + b.mass);
			double ballyVel2 = (b.yVelocity * (b.mass - this.mass) + 2 * this.mass * this.yVelocity)
					/ (this.mass + b.mass);

			this.xVelocity = ballxVel1;
			this.yVelocity = ballyVel1;
			b.xVelocity = ballxVel2;
			b.yVelocity = ballyVel2;

		}
	}

	/**
	 * Draws the ball the graphics pane
	 * 
	 * @param g
	 */
	public void drawNew(Graphics g) {
		g.setColor(col);
		g.fillOval((int) (x - radius), (int) (y - radius), (int) (radius * 2), (int) (radius * 2));
	}

	private boolean touchingCircles(double x1, double y1, double rad1, double x2, double y2, double rad2) {
		double xDistance = Math.abs(x1 - x2);// x distance between centers
		double yDistance = Math.abs(y1 - y2);// y distance between centers
		return (Math.hypot(xDistance, yDistance) <= rad1 + rad2);
	}

}
