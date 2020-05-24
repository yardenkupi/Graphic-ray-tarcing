package edu.cg.scene.camera;

import edu.cg.UnimplementedMethodException;
import edu.cg.algebra.Point;
import edu.cg.algebra.Vec;

public class PinholeCamera {
	
	private Vec towardsVec;
	private Vec upVec;
	private Vec rightVec;
	private Point cameraPosition;
	private double distanceToPlain;

	private Point Center;
	private double height;
	private double width;
	private double viewAngle;
	private double pixelWidth;
	

	/**
	 * Initializes a pinhole camera model with default resolution 200X200 (RxXRy)
	 * and View Angle 90.
	 * 
	 * @param cameraPosition  - The position of the camera.
	 * @param towardsVec      - The towards vector of the camera (not necessarily
	 *                        normalized).
	 * @param upVec           - The up vector of the camera.
	 * @param distanceToPlain - The distance of the camera (position) to the center
	 *                        point of the image-plain.
	 * 
	 */
	public PinholeCamera(Point cameraPosition, Vec towardsVec, Vec upVec, double distanceToPlain) {
		this.towardsVec = towardsVec.normalize();
		this.cameraPosition = cameraPosition;

		this.setRightVec((towardsVec.cross(upVec)).normalize());
		this.setUpVec((this.getRightVec().cross(towardsVec)).normalize());
		this.distanceToPlain = distanceToPlain;
		this.Center = cameraPosition.add(this.towardsVec.mult(distanceToPlain));
	}

	/**
	 * Initializes the resolution and width of the image.
	 * 
	 * @param height    - the number of pixels in the y direction.
	 * @param width     - the number of pixels in the x direction.
	 * @param viewAngle - the view Angle.
	 */
	public void initResolution(int height, int width, double viewAngle) {
		this.height = (double)height;
		this.width = (double)width;
		this.viewAngle = viewAngle;
		double screenWidth = Math.tan(Math.toRadians(viewAngle/2.0)) * this.distanceToPlain * 2.0;
		double Width = (double)width;
		double Screenwidth = screenWidth;
        this.setPixelWidth(Screenwidth / Width);
	}

	/**
	 * Transforms from pixel coordinates to the center point of the corresponding
	 * pixel in model coordinates.
	 * 
	 * @param x - the pixel index in the x direction.
	 * @param y - the pixel index in the y direction.
	 * @return the middle point of the pixel (x,y) in the model coordinates.
	 */
	public Point transform(int x, int y) {
		Point P = this.Center.add(getRightVec().mult((x - Math.floor(width/2.0))*getPixelWidth())).add(getUpVec().mult(((0.0 - y) + Math.floor(height/2.0))*getPixelWidth()));
		return P;
	}

	/**
	 * Returns the camera position
	 * 
	 * @return a new point representing the camera position.
	 */
	public Point getCameraPosition() {
		return cameraPosition;
	}

	public double getPixelWidth() {
		return pixelWidth;
	}

	public void setPixelWidth(double pixelWidth) {
		this.pixelWidth = pixelWidth;
	}

	public Vec getRightVec() {
		return rightVec;
	}

	public void setRightVec(Vec rightVec) {
		this.rightVec = rightVec;
	}

	public Vec getUpVec() {
		return upVec;
	}

	public void setUpVec(Vec upVec) {
		this.upVec = upVec;
	}
}
