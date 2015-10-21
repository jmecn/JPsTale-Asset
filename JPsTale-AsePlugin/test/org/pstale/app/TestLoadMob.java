package org.pstale.app;

import org.pstale.asset.AseKey;
import org.pstale.asset.AseLoader;
import org.pstale.asset.FileLocator;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.StatsView;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class TestLoadMob extends SimpleApplication {

	public static void main(String[] args) {
		TestLoadMob app = new TestLoadMob();
		app.setPauseOnLostFocus(false);
		app.start();
	}
	
	Spatial mob = null;
	
	@Override
	public void simpleInitApp() {
		assetManager.registerLoader(AseLoader.class, "ase");
		assetManager.registerLocator("models", FileLocator.class);
		
		AmbientLight light = new AmbientLight();
		light.setColor(ColorRGBA.White);
		rootNode.addLight(light);
		
		mob = assetManager.loadAsset(new AseKey("char/monster/death_knight/death_knight.ASE"));
		mob.scale(0.05f);
		rootNode.attachChild(mob);
		
		initKeys();
	}
	
	void initKeys() {
		inputManager.addMapping("Test", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(new ActionListener() {
			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				if (isPressed && name.equals("Test")) {
					System.out.println("=== Camera settings ===");
					System.out.println("Location:" + cam.getLocation() + " Rotation:" + cam.getRotation() + " Direction:" + cam.getDirection() + " Up:" + cam.getUp());
					
					System.out.println("=== Statics ===");
					System.out.println("Vector3f ZERO:" + Vector3f.ZERO + " X:" + Vector3f.UNIT_X + " Y:" + Vector3f.UNIT_Y + " Z:" + Vector3f.UNIT_Z + " XYZ:" + Vector3f.UNIT_XYZ);
					System.out.println("Matrix4f ZERO:" + Matrix4f.ZERO + " IDENTITY:" + Matrix4f.IDENTITY);
					System.out.println("Quaternion IDENTITY:" + Quaternion.IDENTITY + " ZERO:" + Quaternion.ZERO + " Z:" + Quaternion.DIRECTION_Z);
					
					System.out.println("=== Model ===");
					System.out.println("Location:" + mob.getLocalTranslation() + " Rotation:" + mob.getLocalRotation() + " Scale:" + mob.getLocalScale());

					System.out.println("=== StatsView ===");
					StatsAppState statsAppState = stateManager.getState(StatsAppState.class);
					if (statsAppState != null) {
						StatsView view = statsAppState.getStatsView();
						if (view != null) {
							Quaternion rot = view.getLocalRotation();
							System.out.println("Rotation:" + rot);
						}
					}
				}
			}}, "Test");
	}

}
