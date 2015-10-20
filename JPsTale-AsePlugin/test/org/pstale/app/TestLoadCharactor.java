package org.pstale.app;

import java.io.File;

import org.pstale.asset.AseKey;
import org.pstale.asset.AseLoader;
import org.pstale.asset.FileLocator;

import com.jme3.animation.AnimControl;
import com.jme3.animation.Skeleton;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.debug.SkeletonDebugger;
import com.jme3.shadow.DirectionalLightShadowRenderer;

public class TestLoadCharactor extends SimpleApplication {

	public static void main(String[] args) {
		TestLoadCharactor a = new TestLoadCharactor();
		a.start();
	}

	Node ata;
	@Override
	public void simpleInitApp() {
		this.setPauseOnLostFocus(false);
		
		cam.setLocation(new Vector3f(100, 30, 30));
		cam.lookAt(new Vector3f(0, 30, 30), cam.getUp());
		this.flyCam.setMoveSpeed(100f);
		
		assetManager.registerLoader(AseLoader.class, "ase");
		
		// I work both at home and at company. for each computer i have a working folder
		if (new File("D:/Priston Tale/0_匼第/Client").isDirectory()) {
			assetManager.registerLocator("D:/Priston Tale/0_匼第/Client", FileLocator.class);
		}
		if (new File("F:/1_DEVELOP/3_匼第").isDirectory()) {
			assetManager.registerLocator("F:/1_DEVELOP/3_匼第", FileLocator.class);
		}
		
		// Y = 46 Z = 10 X = 10;
		Node ata = getAta();
		ata.move(0, -23f, 0);
		rootNode.attachChild(ata);

		Node priest = getPriest();
		priest.move(0, -23f, 20);
		rootNode.attachChild(priest);

		Node ft = getFt();
		ft.move(0, -23f, 40);
		rootNode.attachChild(ft);
		
		Node d = getManD();
		d.move(0, -23f, 60);
		rootNode.attachChild(d);

		
		rootNode.setShadowMode(ShadowMode.CastAndReceive);

		initAmbient();
		
//		initSun();
		
		viewPort.setBackgroundColor(ColorRGBA.LightGray);
		
//		debugSke(ata);
//		debugSke(priest);
//		debugSke(ft);
//		debugSke(d);
		showNodeAxes(100);

	}
	private Node getManD() {
		Node node = new Node("ManD");
		Spatial body = assetManager.loadAsset(new AseKey("char/tmABCD/MmbD01.ASE"));
		node.attachChild(body);
		
		Spatial head = assetManager.loadAsset(new AseKey("char/tmABCD/MmhD01a.ASE"));
		node.attachChild(head);
		
		return node;
	}
	private Node getFt() {
		Node node = new Node("Ft");
		Spatial body = assetManager.loadAsset(new AseKey("char/tmABCD/MmbA01.ASE"));
		node.attachChild(body);
		
		Spatial head = assetManager.loadAsset(new AseKey("char/tmABCD/MmhA01a.ASE"));
		node.attachChild(head);
		
		return node;
	}
	private Node getAta() {
		Node node = new Node("Ata");
		Spatial body = assetManager.loadAsset(new AseKey("char/tmABCD/MfbB01.ASE"));
		node.attachChild(body);
		
		Spatial head = assetManager.loadAsset(new AseKey("char/tmABCD/MfhB01a.ASE"));
		node.attachChild(head);
		
		return node;
	}
	private Node getPriest() {
		Node node = new Node("Priest");
		Spatial body = assetManager.loadAsset(new AseKey("char/tmABCD/MfbC01.ASE"));
		node.attachChild(body);
		
		Spatial head = assetManager.loadAsset(new AseKey("char/tmABCD/MfhC01a.ASE"));
		node.attachChild(head);
		
		return node;
	}
	
	private void initAmbient() {
		AmbientLight light = new AmbientLight();
		light.setColor(ColorRGBA.White);
		rootNode.addLight(light);
	}
	
	private DirectionalLight sun = null;
	protected void initSun() {
		sun = new DirectionalLight();
		sun.setColor(ColorRGBA.White);
		rootNode.addLight(sun);

		DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 1024, 4);
		dlsr.setLight(sun);
		viewPort.addProcessor(dlsr);
	}
	
	float per = FastMath.PI/6;
	float alpha = 0;
	public void simpleUpdate(float tpf) {
		if (sun != null) {
			alpha += tpf*per;
			if (alpha > FastMath.TWO_PI) {
				alpha = 0;
			}
			
			float x = FastMath.sin(alpha);
			float y = FastMath.cos(alpha);
			sun.getDirection().setX(x);
			sun.getDirection().setY(y);
		}
	}
	
	public void showNodeAxes(float axisLen) {
		Geometry gridXZ = new Geometry("AxisXZ", new Grid(31, 31, 5f));
		Material gmXZ = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		gmXZ.setColor("Color", new ColorRGBA(1, 0, 1, 1));
		gmXZ.getAdditionalRenderState().setWireframe(true);
		gridXZ.setMaterial(gmXZ);
		gridXZ.center().move(0, 0, 0);
		rootNode.attachChild(gridXZ);
		
		Geometry gridYZ = new Geometry("AxisYZ",  new Grid(31, 31, 5f));
		Material gmYZ = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		gmYZ.setColor("Color", new ColorRGBA(0, 1, 1, 1));
		gmYZ.getAdditionalRenderState().setWireframe(true);
		gridYZ.setMaterial(gmYZ);
		gridYZ.rotate(0, 0, FastMath.HALF_PI);
		gridYZ.center().move(0, 0, 0);
		rootNode.attachChild(gridYZ);
		
		// 
		Vector3f v = new Vector3f(axisLen, 0, 0);
		Arrow a = new Arrow(v);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Red);
		Geometry geom = new Geometry(rootNode.getName() + "XAxis", a);
		geom.setMaterial(mat);
		rootNode.attachChild(geom);

		//
		v = new Vector3f(0, axisLen, 0);
		a = new Arrow(v);
		mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Green);
		geom = new Geometry(rootNode.getName() + "YAxis", a);
		geom.setMaterial(mat);
		rootNode.attachChild(geom);

		//
		v = new Vector3f(0, 0, axisLen);
		a = new Arrow(v);
		mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Blue);
		geom = new Geometry(rootNode.getName() + "ZAxis", a);
		geom.setMaterial(mat);
		rootNode.attachChild(geom);
	}

	public void debugSke(Spatial model) {
		final AnimControl ac = findAnimControl(model);

		try {
			// add a skeleton debugger to make bones visible
			final Skeleton skel = ac.getSkeleton();
			final SkeletonDebugger skeletonDebug = new SkeletonDebugger(
					"skeleton", skel);
			final Material mat = new Material(assetManager,
					"Common/MatDefs/Misc/Unshaded.j3md");
			mat.setColor("Color", ColorRGBA.Green);
			mat.getAdditionalRenderState().setDepthTest(false);
			skeletonDebug.setMaterial(mat);
			((Node) ac.getSpatial()).attachChild(skeletonDebug);

			// create a channel and start the walk animation
//			final AnimChannel channel = ac.createChannel();
//			channel.setAnim("walk");
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to find the animation control, because it is not on the models
	 * root node.
	 * 
	 * @param parent
	 *            The spatial to search.
	 * @return The {@link AnimControl} or null if it does not exist.
	 */
	public AnimControl findAnimControl(final Spatial parent) {
		final AnimControl animControl = parent.getControl(AnimControl.class);
		if (animControl != null) {
			return animControl;
		}

		if (parent instanceof Node) {
			for (final Spatial s : ((Node) parent).getChildren()) {
				final AnimControl animControl2 = findAnimControl(s);
				if (animControl2 != null) {
					return animControl2;
				}
			}
		}

		return null;
	}
}
