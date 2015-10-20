package org.pstale.app;

import org.pstale.asset.AseKey;
import org.pstale.asset.AseLoader;
import org.pstale.asset.FileLocator;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class TestLoadMob extends SimpleApplication {

	public static void main(String[] args) {
		TestLoadMob app = new TestLoadMob();
		app.setPauseOnLostFocus(false);
		app.start();
	}

	@Override
	public void simpleInitApp() {
		System.out.println("Before");
		System.out.println("Loc:" + cam.getLocation() + " Rotation:" + cam.getRotation() + " Direction:" + cam.getDirection() + " Up:" + cam.getUp());
		System.out.println("ZERO:" + Vector3f.ZERO + " X:" + Vector3f.UNIT_X + " Y:" + Vector3f.UNIT_Y + " Z:" + Vector3f.UNIT_Z + " XYZ:" + Vector3f.UNIT_XYZ);
		
		assetManager.registerLoader(AseLoader.class, "ase");
		assetManager.registerLocator("D:/Priston Tale/0_ËØ²Ä/Client", FileLocator.class);
		
		AmbientLight light = new AmbientLight();
		light.setColor(ColorRGBA.White);
		rootNode.addLight(light);
		
		Spatial mob = assetManager.loadAsset(new AseKey("char/monster/death_knight/death_knight.ASE"));
		mob.scale(0.05f);
		rootNode.attachChild(mob);
		
		System.out.println("After");
		System.out.println("Loc:" + cam.getLocation() + " Rotation:" + cam.getRotation() + " Direction:" + cam.getDirection() + " Up:" + cam.getUp());
		System.out.println("ZERO:" + Vector3f.ZERO + " X:" + Vector3f.UNIT_X + " Y:" + Vector3f.UNIT_Y + " Z:" + Vector3f.UNIT_Z + " XYZ:" + Vector3f.UNIT_XYZ);
		
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
	}

}
