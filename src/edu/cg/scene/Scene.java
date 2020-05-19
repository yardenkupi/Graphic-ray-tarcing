package edu.cg.scene;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.cg.Logger;
import edu.cg.UnimplementedMethodException;
import edu.cg.algebra.Hit;
import edu.cg.algebra.Ops;
import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;
import edu.cg.scene.camera.PinholeCamera;
import edu.cg.scene.lightSources.Light;
import edu.cg.scene.objects.Surface;

public class Scene {
	private String name = "scene";
	private int maxRecursionLevel = 1;
	private int antiAliasingFactor = 1; // gets the values of 1, 2 and 3
	private boolean renderRefarctions = false;
	private boolean renderReflections = false;

	private PinholeCamera camera;
	private Vec ambient = new Vec(1, 1, 1); // white
	private Vec backgroundColor = new Vec(0, 0.5, 1); // blue sky
	private List<Light> lightSources = new LinkedList<>();
	private List<Surface> surfaces = new LinkedList<>();

	private transient static int count = 0;
	// MARK: initializers
	public Scene initCamera(Point eyePoistion, Vec towardsVec, Vec upVec, double distanceToPlain) {
		this.camera = new PinholeCamera(eyePoistion, towardsVec, upVec, distanceToPlain);
		return this;
	}

	public Scene initAmbient(Vec ambient) {
		this.ambient = ambient;
		return this;
	}

	public Scene initBackgroundColor(Vec backgroundColor) {
		this.backgroundColor = backgroundColor;
		this.backgroundColor = backgroundColor;
		return this;
	}

	public Scene addLightSource(Light lightSource) {
		lightSources.add(lightSource);
		return this;
	}

	public Scene addSurface(Surface surface) {
		surfaces.add(surface);
		return this;
	}

	public Scene initMaxRecursionLevel(int maxRecursionLevel) {
		this.maxRecursionLevel = maxRecursionLevel;
		return this;
	}

	public Scene initAntiAliasingFactor(int antiAliasingFactor) {
		this.antiAliasingFactor = antiAliasingFactor;
		return this;
	}

	public Scene initName(String name) {
		this.name = name;
		return this;
	}

	public Scene initRenderRefarctions(boolean renderRefarctions) {
		this.renderRefarctions = renderRefarctions;
		return this;
	}

	public Scene initRenderReflections(boolean renderReflections) {
		this.renderReflections = renderReflections;
		return this;
	}

	// MARK: getters
	public String getName() {
		return name;
	}

	public int getFactor() {
		return antiAliasingFactor;
	}

	public int getMaxRecursionLevel() {
		return maxRecursionLevel;
	}

	public boolean getRenderRefarctions() {
		return renderRefarctions;
	}

	public boolean getRenderReflections() {
		return renderReflections;
	}

	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return "Camera: " + camera + endl + "Ambient: " + ambient + endl + "Background Color: " + backgroundColor + endl
				+ "Max recursion level: " + maxRecursionLevel + endl + "Anti aliasing factor: " + antiAliasingFactor
				+ endl + "Light sources:" + endl + lightSources + endl + "Surfaces:" + endl + surfaces;
	}

	private transient ExecutorService executor = null;
	private transient Logger logger = null;

	private void initSomeFields(int imgWidth, int imgHeight, Logger logger) {
		this.logger = logger;
		// TODO: initialize your additional field here.
		//DONT FORGET TO ADD THEM AS: private transient Object aField
		
		
	}

	public BufferedImage render(int imgWidth, int imgHeight, double viewAngle, Logger logger)
			throws InterruptedException, ExecutionException, IllegalArgumentException {

		initSomeFields(imgWidth, imgHeight, logger);

		BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		camera.initResolution(imgHeight, imgWidth, viewAngle);
		int nThreads = Runtime.getRuntime().availableProcessors();
		nThreads = nThreads < 2 ? 2 : nThreads;
		this.logger.log("Intitialize executor. Using " + nThreads + " threads to render " + name);
		executor = Executors.newFixedThreadPool(nThreads);

		@SuppressWarnings("unchecked")
		Future<Color>[][] futures = (Future<Color>[][]) (new Future[imgHeight][imgWidth]);

		this.logger.log("Starting to shoot " + (imgHeight * imgWidth * antiAliasingFactor * antiAliasingFactor)
				+ " rays over " + name);

		for (int y = 0; y < imgHeight; ++y)
			for (int x = 0; x < imgWidth; ++x)
				futures[y][x] = calcColor(x, y);
		System.out.println(count);

		this.logger.log("Done shooting rays.");
		this.logger.log("Wating for results...");

		for (int y = 0; y < imgHeight; ++y)
			for (int x = 0; x < imgWidth; ++x) {
				Color color = futures[y][x].get();
				img.setRGB(x, y, color.getRGB());
			}

		executor.shutdown();

		this.logger.log("Ray tracing of " + name + " has been completed.");

		executor = null;
		this.logger = null;

		return img;
	}

	private Future<Color> calcColor(int x, int y) {
		return executor.submit(() -> {
			// TODO: You need to re-implement this method if you want to handle
			// super-sampling. You're also free to change the given implementation if you
			// want.
			Point centerPoint = camera.transform(x, y);
			Ray ray = new Ray(camera.getCameraPosition(), centerPoint);
			Vec color = calcColor(ray, 0);
			return color.toColor();
		});
	}

	private Vec calcColor(Ray ray, int recusionLevel) {
		// This is the recursive method in RayTracing.
		Vec color = new Vec(0,0,0);
		Surface closestSurface = null;
		Hit minHit = new Hit(Integer.MAX_VALUE,new Vec(0,0,0));

		//check which is the closest surface to be hit
		for	(Surface surface : surfaces) {

				Hit hit = surface.intersect(ray);
				if (hit != null) {
					if (hit.compareTo(minHit)< 0 && hit.t > 0) {
						closestSurface = surface;
						minHit = hit;
					}
				}
		}

		if(closestSurface == null){
			return backgroundColor;
		}
		color = closestSurface.Ka().mult(ambient);
		
		//for loop over light sources to check if they hit chosen surface and calc ray tracing
		for (Light light : lightSources) {
			Ray raytoLight = light.rayToLight(minHit.hitPoint);
			
			if(minHit.getNormalToSurface().dot(raytoLight.direction()) > 0 && isExposed(light,closestSurface,raytoLight)){
				Vec Intensity = light.intensity(minHit.hitPoint, raytoLight);
				Vec diffuse = closestSurface.Kd().mult(minHit.getNormalToSurface().dot(raytoLight.direction)).mult(Intensity);
				color = color.add(diffuse);
				Vec V = minHit.hitPoint.sub(ray.source()).normalize();
				Vec R = Ops.reflect(raytoLight.direction().mult(-1),minHit.getNormalToSurface()).normalize();
				Vec specular = closestSurface.Ks().mult(Math.pow(V.dot(R),closestSurface.shininess())).mult(Intensity);
				color = color.add(specular);
			}
		}

		if(color.x > 1){
			color.x = 1;
		}else if(color.y > 1){
			color.y = 1;
		}else if(color.z > 1){
			color.z = 1;
		}

		//base case
		if(recusionLevel ==0){
			count++;
			return color;
		}

		//recursive call
		if(closestSurface.reflectionIntensity() != 0){
			Ray reflectanceRay = new Ray(minHit.hitPoint,Ops.reflect(ray.direction(),minHit.getNormalToSurface()).normalize());
			return calcColor(reflectanceRay,recusionLevel-1);
		}else{
			//TODO (refractions)
		}
		return null;
		
	}
	public boolean isExposed(Light light,Surface closestSurface,Ray rayToLight){
		for (Surface surface : surfaces) {
			if(light.isOccludedBy(surface, rayToLight)){
				return false;
			}
		}
		return true;
	}

	public Hit closestSurface(Ray ray){
		Hit minHit = null; //TODO should be initiarte
		Surface closestSurface = null; //TODO should be initiarte
		for	(Surface surface : surfaces) {
			Hit hit = surface.intersect(ray);
			if(hit != null){
				if(hit.t < minHit.t && hit.t > 0){
					closestSurface = surface;
					minHit = hit;
				}
			}
		}
		return minHit; //TODO 
	}
}
