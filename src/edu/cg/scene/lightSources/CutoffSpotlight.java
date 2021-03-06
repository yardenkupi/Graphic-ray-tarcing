package edu.cg.scene.lightSources;

import edu.cg.UnimplementedMethodException;
import edu.cg.algebra.Hit;
import edu.cg.algebra.Ops;
import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;
import edu.cg.scene.objects.Surface;

public class CutoffSpotlight extends PointLight {
	private Vec direction;
	private double cutoffAngle;

	public CutoffSpotlight(Vec dirVec, double cutoffAngle) {
		this.direction = dirVec;
		this.cutoffAngle = Math.toRadians(cutoffAngle);
	}

	public CutoffSpotlight initDirection(Vec direction) {
		this.direction = direction;
		return this;
	}

	public CutoffSpotlight initCutoffAngle(double cutoffAngle) {
		this.cutoffAngle = cutoffAngle;
		return this;
	}

	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return "Spotlight: " + endl + description() + "Direction: " + direction + endl;
	}

	@Override
	public CutoffSpotlight initPosition(Point position) {
		return (CutoffSpotlight) super.initPosition(position);
	}

	@Override
	public CutoffSpotlight initIntensity(Vec intensity) {
		return (CutoffSpotlight) super.initIntensity(intensity);
	}

	@Override
	public CutoffSpotlight initDecayFactors(double q, double l, double c) {
		return (CutoffSpotlight) super.initDecayFactors(q, l, c);
	}

	@Override
	public boolean isOccludedBy(Surface surface, Ray rayToLight) {
		return super.isOccludedBy(surface,rayToLight);
	}


	/**
	 * Create the angle between the vectors by the dot product of the negation of the rays direction and
	 * @return color vector of the currents intensity
	 */
	@Override
	public Vec intensity(Point hittingPoint, Ray rayToLight) {


		if(Math.acos(rayToLight.direction.dot(this.direction.mult(-1))) > this.cutoffAngle)
		{
			return new Vec(0,0,0);
		}
		return super.intensity(hittingPoint, rayToLight).mult(this.direction.dot(Ops.neg(rayToLight.direction)));
	}
}
