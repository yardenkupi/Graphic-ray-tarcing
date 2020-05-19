package edu.cg.scene.objects;

import edu.cg.UnimplementedMethodException;
import edu.cg.algebra.Hit;
import edu.cg.algebra.Ops;
import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;

public class Dome extends Shape {
	private Sphere sphere;
	private Plain plain;
	private Point center;

	public Dome() {
		sphere = new Sphere().initCenter(new Point(0, -0.5, -6));
		plain = new Plain(new Vec(-1, 0, -1), new Point(0, -0.5, -6));
	}

	public Dome(Point center, double radious, Vec plainDirection) {
		sphere = new Sphere(center, radious);
		plain = new Plain(plainDirection, center);
		this.center = center;
	}

	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return "Dome:" + endl + sphere + plain + endl;
	}

	@Override
	public Hit intersect(Ray ray) {
		Hit sphareHit = this.sphere.intersect(ray);
		Hit planeHit = this.plain.intersect(ray);
		
		if(sphareHit == null)
		{
			return null;
		}
		//check if hit the surface from the dome side, if so return the sphare hit, else the spare.
		return planeHit.getNormalToSurface().dot(plain.normal()) >= 0 ? sphareHit : planeHit;
	}
	
	@Override
	public boolean equals(Shape shape){
		return (shape instanceof Dome && (((Dome)shape).sphere).equals(this.sphere) && (((Dome)shape).plain).equals(this.plain));
	}
}
