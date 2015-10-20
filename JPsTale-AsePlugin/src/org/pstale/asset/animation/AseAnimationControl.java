package org.pstale.asset.animation;

import java.util.ArrayList;
import java.util.Collection;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class AseAnimationControl extends AbstractControl {

	private Vector3f initTran;
	private Quaternion initRot;
	
	float[] times;
	Vector3f[] trans;
	Quaternion[] rots;
	
	private Collection<Keyframe> keyframes = null;
	float duration;
	float position = 0;
	
	float speed = 1f;
	
	@Override
    public void setSpatial(Spatial spatial) {
        if (this.spatial != null && spatial != null && spatial != this.spatial) {
            throw new IllegalStateException("This control has already been added to a Spatial");
        }   
        this.spatial = spatial;
        initTran = spatial.getLocalTranslation().clone();
        initRot = spatial.getLocalRotation().clone();
    }
    
	@Override
	protected void controlUpdate(float tpf) {
		position += tpf * speed;
		if (position > duration) {
			position -= duration;
		}
		
		Vector3f outTrans = new Vector3f();
		Quaternion outRots = new Quaternion();
		
		sample(position, outTrans, outRots);

		spatial.setLocalTranslation(outTrans.add(initTran));
		spatial.setLocalRotation(outRots);
	}

	public final void sample(float position, Vector3f outTrans, Quaternion outRots) {
		assert outTrans != null;
		assert outRots != null;
		if (times == null || trans == null || rots == null)
			return;
		int min = 0;
		int max = times.length;
		while (min < max-1) {
			int pos = (min + max) / 2;
			if (times[pos] > position)
				max = pos;
			else
				min = pos;
		}
		float maxValue;
		if (min == times.length-1) {
			max = 0;
			maxValue = duration;
		}
		else
			maxValue = times[max];
		float minValue = times[min];
		float interpolate = (position - minValue) / (maxValue - minValue);
		lerp(trans[min], trans[max], interpolate, outTrans);
		lerp(rots[min], rots[max], interpolate, outRots);
	}

	private final static void lerp(Vector3f a, Vector3f b, float rho, Vector3f out) {
		if (a == null)
			if (b == null)
				return;
			else
				out.set(b);
		else
			if (b == null)
				out.set(a);
			else
				out.set(
					a.x * (1-rho) + b.x * rho,
					a.y * (1-rho) + b.y * rho,
					a.z * (1-rho) + b.z * rho
					);
	}

	private final static void lerp(Quaternion a, Quaternion b, float rho, Quaternion out) {
		if (a == null)
			if (b == null)
				return;
			else
				out.set(b);
		else
			if (b == null)
				out.set(a);
			else {
				if (a.dot(b) > 0)
					out.set(
						a.getX() * (1-rho) + b.getX() * rho,
						a.getY() * (1-rho) + b.getY() * rho,
						a.getZ() * (1-rho) + b.getZ() * rho,
						a.getW() * (1-rho) + b.getW() * rho
						);
				else
					out.set(		//	negate "a" in the multiplication to take the short way round
							a.getX() * (rho-1) + b.getX() * rho,
							a.getY() * (rho-1) + b.getY() * rho,
							a.getZ() * (rho-1) + b.getZ() * rho,
							a.getW() * (rho-1) + b.getW() * rho
							);
				out.normalizeLocal();
			}
	}
	
	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub
	}

	/**
	 * Internal use only.
	 */
	public Control cloneForSpatial(Spatial spatial) {
		AseAnimationControl clone = (AseAnimationControl) clone();
		clone.spatial = spatial;
		return clone;
	}

	@Override
	protected Object clone() {
		AseAnimationControl clone = new AseAnimationControl();
		clone.keyframes = this.keyframes;
		clone.times = times;
		clone.trans = trans;
		clone.rots = rots;
		clone.duration = duration;
		clone.speed = this.speed;
		clone.initTran = this.initTran;
		clone.initRot = this.initRot;
		return clone;
	}

	public void setKeyframes(float[] timeArray, Vector3f[] transArray,
			Quaternion[] rotArray, float animationLength) {
		times = timeArray;
		trans = transArray;
		rots = rotArray;
		duration = animationLength;
		
	}

	public Collection<Keyframe> getKeyframes() {
		if (keyframes == null) {
			keyframes = new ArrayList<Keyframe>();
			for(int i=0; i<times.length; i++) {
				Keyframe keyframe = new Keyframe();
				keyframe.translation = trans[i];
				keyframe.rotation = rots[i];
				keyframes.add(keyframe);
			}
		}
		
		return keyframes;
	}

	public void setKeyframes(float[] timeArray, Collection<Keyframe> values,
			float animationLength) {
		times = timeArray;
		trans = new Vector3f[times.length];
		rots = new Quaternion[times.length];
		keyframes = values;
		
		int i=0;
		for(Keyframe key : values) {
			trans[i] = key.translation;
			rots[i] = key.rotation;
			i++;
		}
		duration = animationLength;
		
	}

	public void updateKeyframes() {
		int i=0;
		for(Keyframe key : keyframes) {
			trans[i] = key.translation;
			rots[i] = key.rotation;
			i++;
		}
	}
}
