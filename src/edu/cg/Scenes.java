package edu.cg;

import edu.cg.algebra.Point;
import edu.cg.algebra.Vec;
import edu.cg.scene.Scene;
import edu.cg.scene.lightSources.CutoffSpotlight;
import edu.cg.scene.lightSources.DirectionalLight;
import edu.cg.scene.lightSources.Light;
import edu.cg.scene.objects.*;

public class Scenes {
	public static Scene scene1() {
		Shape sphereShape1 = new Sphere(new Point(0.0), 1.0);
		Material sphereMat1 = new Material().initKa(new Vec(0.8, 0.05, 0.05)).initKd(new Vec(0.0)).initKs(new Vec(0.9))
				.initShininess(10).initIsTransparent(false).initRefractionIntensity(0.0);
		Surface boxSurface1 = new Surface(sphereShape1, sphereMat1);

		Light dirLight = new DirectionalLight(new Vec(-1.0, -1.0, -1.0), new Vec(0.9));

		return new Scene().initAmbient(new Vec(1.0))
				.initCamera(new Point(4, 4, 1.5), new Vec(-1.0, -1.0, -0.3), new Vec(0, 0, 1), 3)
				.addLightSource(dirLight).addSurface(boxSurface1).initName("scene1").initAntiAliasingFactor(1)
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(3);
	}

	public static Scene scene2() {
		// Define basic properties of the scene
		Scene finalScene = new Scene().initAmbient(new Vec(1.0))
				.initCamera(/* Camera Position = */new Point(0.0, 2.0, 6.0), 
						/* Towards Vector = */ new Vec(0.0, -0.1 ,-1.0),
						/* Up vector = */new Vec(0.0, 1.0, 0.0), 
						/*Distance to plain =*/ 2.0)
				.initName("scene2").initAntiAliasingFactor(1)
				.initAmbient(new Vec(0.4))
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(6);
        // Add Surfaces to the scene.
		// (1) A plain that represents the ground floor.
		Shape plainShape = new Plain(new Vec(0.0,1.0,0.0), new Point(0.0, -1.0, 0.0));
		Material plainMat = Material.getMetalMaterial();
		Surface plainSurface = new Surface(plainShape, plainMat);
		finalScene.addSurface(plainSurface);
		
		// (2) We will also add spheres to form a triangle shape (similar to a pool game). 
		for (int depth = 0; depth < 4; depth++) {
			for(int width=-1*depth; width<=depth; width++) {
				Shape sphereShape = new Sphere(new Point((double)width, 0.0, -1.0*(double)depth), 0.5);
				Material sphereMat = Material.getRandomMaterial();
				Surface sphereSurface = new Surface(sphereShape, sphereMat);
				finalScene.addSurface(sphereSurface);
			}
			
		}
		// Add lighting condition:
		DirectionalLight directionalLight=new DirectionalLight(new Vec(0.5,-0.5,0.0),new Vec(0.7));
		finalScene.addLightSource(directionalLight);

		
		return finalScene;
	}
	
	public static Scene scene3() {
		// Define basic properties of the scene
		Scene finalScene = new Scene().initAmbient(new Vec(1.0))
				.initCamera(/* Camera Position = */new Point(0.0, 2.0, 6.0), 
						/* Towards Vector = */ new Vec(0.0, -0.1 ,-1.0),
						/* Up vector = */new Vec(0.0, 1.0, 0.0), 
						/*Distance to plain =*/ 2.0)
				.initName("scene3").initAntiAliasingFactor(1)
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(6);
        // Add Surfaces to the scene.
		// (1) A plain that represents the ground floor.
		Shape plainShape = new Plain(new Vec(0.0,1.0,0.0), new Point(0.0, -1.0, 0.0));
		Material plainMat = Material.getMetalMaterial();
		Surface plainSurface = new Surface(plainShape, plainMat);
		finalScene.addSurface(plainSurface);
		
		// (2) We will also add spheres to form a triangle shape (similar to a pool game). 
		for (int depth = 0; depth < 4; depth++) {
			for(int width=-1*depth; width<=depth; width++) {
				Shape sphereShape = new Sphere(new Point((double)width, 0.0, -1.0*(double)depth), 0.5);
				Material sphereMat = Material.getRandomMaterial();
				Surface sphereSurface = new Surface(sphereShape, sphereMat);
				finalScene.addSurface(sphereSurface);
			}
			
		}
		
		// Add light sources:
		CutoffSpotlight cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 45.0);
		cutoffSpotlight.initPosition(new Point(4.0, 4.0, -3.0));
		cutoffSpotlight.initIntensity(new Vec(1.0,0.6,0.6));
		finalScene.addLightSource(cutoffSpotlight);
		cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 30.0);
		cutoffSpotlight.initPosition(new Point(-4.0, 4.0, -3.0));
		cutoffSpotlight.initIntensity(new Vec(0.6,1.0,0.6));
		finalScene.addLightSource(cutoffSpotlight);
		cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 30.0);
		cutoffSpotlight.initPosition(new Point(0.0, 4.0, 0.0));
		cutoffSpotlight.initIntensity(new Vec(0.6,0.6,1.0));
		finalScene.addLightSource(cutoffSpotlight);
		DirectionalLight directionalLight=new DirectionalLight(new Vec(0.5,-0.5,0.0),new Vec(0.2));
		finalScene.addLightSource(directionalLight);
		
		return finalScene;
	}

	public static Scene scene4() {

		// Define basic properties of the scene
		Scene finalScene = new Scene().initAmbient(new Vec(1.0))
				.initCamera(/* Camera Position = */new Point(0.0, 2.0, 6.0), 
						/* Towards Vector = */ new Vec(0.0, -0.1 ,-1.0),
						/* Up vector = */new Vec(0.0, 1.0, 0.0), 
						/*Distance to plain =*/ 2.0)
				.initName("scene4").initAntiAliasingFactor(1)
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(6);
        // Add Surfaces to the scene.
		
		// (2) Add two domes to make it look like we split a sphere in half. 
		Shape domeShape = new Dome(new Point(2.0, 0.0, -10.0), 5.0, new Vec(1.0, 0.0, 0.0));
		Material domeMat = Material.getRandomMaterial();
		Surface domeSurface = new Surface(domeShape, domeMat);
		finalScene.addSurface(domeSurface);
		
		domeShape = new Dome(new Point(-2.0, 0.0, -10.0), 5.0, new Vec(-1.0, 0.0, 0.0));
		domeSurface = new Surface(domeShape, domeMat);
		finalScene.addSurface(domeSurface);
		
		// Add light sources:
		CutoffSpotlight cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 75.0);
		cutoffSpotlight.initPosition(new Point(0.0, 6.0, -10.0));
		cutoffSpotlight.initIntensity(new Vec(.5,0.5,0.5));
		finalScene.addLightSource(cutoffSpotlight);
		
		return finalScene;
	}


	/**
	 * our own scene
	 * @return scene with new triangle shape we added functionality for
	 */
	public static Scene scene5() {
		// Define basic properties of the scene
		Scene finalScene = new Scene().initAmbient(new Vec(1.0))
				.initCamera(/* Camera Position = */new Point(0.0, 2.0, 6.0),
						/* Towards Vector = */ new Vec(0.0, -0.1 ,-1.0),
						/* Up vector = */new Vec(0.0, 1.0, 0.0),
						/*Distance to plain =*/ 2.0)
				.initName("scene5").initAntiAliasingFactor(1)
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(6);
		// Add Surfaces to the scene.

		Shape triangleShape = new Triangle(new Point(1.0, 0.0, 0.0), new Point(0.0, 6.0, 0.0),new Point(0.0, 0.0, 1.0));
		Material triangleMat = Material.getRandomMaterial();
		Surface triangleSurface = new Surface(triangleShape, triangleMat);
		finalScene.addSurface(triangleSurface);


		// Add light sources:
		CutoffSpotlight cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 75.0);
		cutoffSpotlight.initPosition(new Point(0.0, 6.0, -10.0));
		cutoffSpotlight.initIntensity(new Vec(.5,0.5,0.5));
		finalScene.addLightSource(cutoffSpotlight);

		return finalScene;
	}


	/**
	 * our own scene
	 * @return scene with 3 spheres and one metal dome
	 */
	public static Scene scene6() {

		Scene finalScene = new Scene().initAmbient(new Vec(1.0))
				.initCamera(/* Camera Position = */new Point(0.0, 2.0, 6.0),
						/* Towards Vector = */ new Vec(0.0, -0.1 ,-1.0),
						/* Up vector = */new Vec(0.0, 1.0, 0.0),
						/*Distance to plain =*/ 2.0)
				.initName("scene6").initAntiAliasingFactor(1)
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(6);

		Shape sphereShape1 = new Sphere(new Point(0.0), 1.0);
		Material sphereMat1 = Material.getRandomMaterial();
		Surface boxSurface1 = new Surface(sphereShape1, sphereMat1);
		finalScene.addSurface(boxSurface1);

		Shape sphereShape2 = new Sphere(new Point(-3.0), 1.0);
		Material sphereMat2 = Material.getRandomMaterial();
		Surface boxSurface2 = new Surface(sphereShape2, sphereMat2);
		finalScene.addSurface(boxSurface2);

		Shape sphereShape3 = new Sphere(new Point(2.0), 1.0);
		Material sphereMat3 = Material.getRandomMaterial();
		Surface boxSurface3 = new Surface(sphereShape3, sphereMat3);
		finalScene.addSurface(boxSurface3);

		Shape domeShape = new Dome(new Point(-1.0, 9.0, -10.0), 5.0, new Vec(1.0, 0.0, 0.0));
		Material domeMat = Material.getMetalMaterial();
		Surface domeSurface = new Surface(domeShape, domeMat);
		finalScene.addSurface(domeSurface);

		Light dirLight = new DirectionalLight(new Vec(-1.0, -1.0, -1.0), new Vec(0.9));

		CutoffSpotlight cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 75.0);
		cutoffSpotlight.initPosition(new Point(0.0, 6.0, -10.0));
		cutoffSpotlight.initIntensity(new Vec(.5,0.5,0.5));
		finalScene.addLightSource(cutoffSpotlight);

		return finalScene.addLightSource(dirLight);
	}

	/**
	 * our own scene
	 * @return scene with 3 spheres and one metal dome, different lighting and angle of dome
	 */
	public static Scene scene7() {

		Scene finalScene = new Scene().initAmbient(new Vec(1.0))
				.initCamera(/* Camera Position = */new Point(0.0, 2.0, 6.0),
						/* Towards Vector = */ new Vec(0.0, -0.1 ,-1.0),
						/* Up vector = */new Vec(0.0, 1.0, 0.0),
						/*Distance to plain =*/ 2.0)
				.initName("scene7").initAntiAliasingFactor(1)
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(6);

		Shape sphereShape1 = new Sphere(new Point(0.0), 1.0);
		Material sphereMat1 = Material.getRandomMaterial();
		Surface boxSurface1 = new Surface(sphereShape1, sphereMat1);
		finalScene.addSurface(boxSurface1);

		Shape sphereShape2 = new Sphere(new Point(-3.0), 1.0);
		Material sphereMat2 = Material.getRandomMaterial();
		Surface boxSurface2 = new Surface(sphereShape2, sphereMat2);
		finalScene.addSurface(boxSurface2);

		Shape sphereShape3 = new Sphere(new Point(2.0), 1.0);
		Material sphereMat3 = Material.getRandomMaterial();
		Surface boxSurface3 = new Surface(sphereShape3, sphereMat3);
		finalScene.addSurface(boxSurface3);

		Shape domeShape = new Dome(new Point(-1.0, 9.0, -10.0), 5.0, new Vec(0.0, 1.0, 0.0));
		Material domeMat = Material.getMetalMaterial();
		Surface domeSurface = new Surface(domeShape, domeMat);
		finalScene.addSurface(domeSurface);

		Light dirLight = new DirectionalLight(new Vec(1.0, 1.0, 1.0), new Vec(0.7));

		CutoffSpotlight cutoffSpotlight = new CutoffSpotlight(new Vec(1.0, 0.0, 0.0), 90.0);
		cutoffSpotlight.initPosition(new Point(0.0, 6.0, -10.0));
		cutoffSpotlight.initIntensity(new Vec(.5,0.5,0.5));
		finalScene.addLightSource(cutoffSpotlight);

		return finalScene.addLightSource(dirLight);
	}


	/**
	 * our own scene - going all out
	 * @return scene with 16 spheres and a dome, spheres are a series
	 */
	public static Scene scene8() {

		Scene finalScene = new Scene().initAmbient(new Vec(1.0))
				.initCamera(/* Camera Position = */new Point(0.0, 2.0, 6.0),
						/* Towards Vector = */ new Vec(0.0, -0.1 ,-1.0),
						/* Up vector = */new Vec(0.0, 1.0, 0.0),
						/*Distance to plain =*/ 2.0)
				.initName("scene8").initAntiAliasingFactor(1)
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(6);

		Shape sphereShape1 = new Sphere(new Point(0.0), 1.0);
		Material sphereMat1 = Material.getRandomMaterial();
		Surface boxSurface1 = new Surface(sphereShape1, sphereMat1);
		finalScene.addSurface(boxSurface1);

		for (double i = 0; i < 15; i++) {

			Shape sphereShape = new Sphere(new Point((-i/4)), i/4);
			Material sphereMat = Material.getRandomMaterial();
			Surface boxSurface = new Surface(sphereShape, sphereMat);
			finalScene.addSurface(boxSurface);
		}

		Shape domeShape = new Dome(new Point(-1.0, 9.0, -10.0), 5.0, new Vec(1.0, 0.0, 0.0));
		Material domeMat = Material.getMetalMaterial();
		Surface domeSurface = new Surface(domeShape, domeMat);
		finalScene.addSurface(domeSurface);

		Light dirLight = new DirectionalLight(new Vec(-1.0, -1.0, -1.0), new Vec(0.9));

		CutoffSpotlight cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 75.0);
		cutoffSpotlight.initPosition(new Point(0.0, 6.0, -10.0));
		cutoffSpotlight.initIntensity(new Vec(.5,0.5,0.5));
		finalScene.addLightSource(cutoffSpotlight);

		return finalScene.addLightSource(dirLight);
	}
}
