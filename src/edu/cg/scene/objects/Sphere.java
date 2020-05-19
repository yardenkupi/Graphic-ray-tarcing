package edu.cg.scene.objects;

import edu.cg.UnimplementedMethodException;
import edu.cg.algebra.Hit;
import edu.cg.algebra.Ops;
import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;

public class Sphere extends Shape {
	private Point center;
	private double radius;
	
	public Sphere(Point center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	
	public Sphere() {
		this(new Point(0, -0.5, -6), 0.5);
	}
	
	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return "Sphere:" + endl + 
				"Center: " + center + endl +
				"Radius: " + radius + endl;
	}
	
	public Sphere initCenter(Point center) {
		this.center = center;
		return this;
	}
	
	public Sphere initRadius(double radius) {
		this.radius = radius;
		return this;
	}

	@Override
	public boolean equals(Shape shape){
		Point center = ((Sphere)shape).center;
		return(shape instanceof Sphere && ((Sphere)shape).radius == this.radius && center.equals(this.center));
	}

	@Override
	public Hit intersect(Ray ray) {
		Hit hit = null;
		double t1, t2;
		double t = 0;
		double a = Math.pow(ray.direction().x,2) + Math.pow(ray.direction().y,2) + Math.pow(ray.direction().z,2);
		double b = (2 * ray.direction().x * ray.source().x) - (2.0 * this.center.x * ray.direction.x) +
				(2 * ray.direction().y * ray.source().y )- (2.0 * this.center.y * ray.direction.y) +
				(2 * ray.direction().z * ray.source().z) - (2.0 * this.center.z * ray.direction.z);
		double c = Math.pow(ray.source().x,2) - (2.0 * this.center.x * ray.source().x) + Math.pow(this.center.x,2) +
				Math.pow(ray.source().y,2) - (2.0 * this.center.y * ray.source().y) + Math.pow(this.center.y,2) +
				Math.pow(ray.source().z,2) - (2.0 * this.center.z * ray.source().z) + Math.pow(this.center.z,2)
				- Math.pow(this.radius,2);

		double determinant = b * b - 4 * a * c;

		if(determinant > 0) {
            t1 = (-b + Math.sqrt(determinant)) / (2.0 * a);
            t2 = (-b - Math.sqrt(determinant)) / (2.0 * a);

            if(t1 < t2 && t1 > Ops.epsilon)
            {
            	t = t1;
            }
            if(t2 < t1 && t2 > Ops.epsilon)
            {
            	t = t2;
            }
        }
        // Condition for real and equal roots
        else if(determinant == 0) {
            t = -b / (2.0 * a);
        }

		if(t != 0)
		{
			Point hitPoint = ray.add(t);
			hit = new Hit(t, hitPoint.sub(this.center).normalize());
			hit.setHitPoint(hitPoint); 
		}
		
    return hit;
	}
}
