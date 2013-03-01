package com.cfuller.gdxgame001.gameplay.level;

import java.util.LinkedList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.invertedlogic.componentsystem.CameraComponent;
import com.invertedlogic.componentsystem.Component;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.gameobject.GameObjectFactory;
import com.invertedlogic.scene.Scene;

public class LevelBackground extends Component {

	CameraComponent mCamera;
	LinkedList<GameObject> mSections;
	
	public LevelBackground(GameObject pGameObject) {
		super(pGameObject, Component.skInstanceType_Single);
	}

	@Override
	public void setup() {
		mCamera = (CameraComponent)Scene.FindGameObject("Main Camera").getComponentOfType(CameraComponent.class);
		mSections = new LinkedList<GameObject>();
		
		addSection();
	}

	@Override
	public void inheritFrom(Component pComponent) {
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		updateBackground();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
	
	void updateBackground() {
		GameObject go = null;
		
		if (mSections.size() > 0) {
			Vector3 cameraPosition = mCamera.getPosition();
			
			// Remove all sections that the camera has already gone passed
			do {
				go = mSections.getFirst();
				if (go.getBoundingBox().getMaxX() < cameraPosition.x) {
					mTransform.detachGameObject(go);
					GameObjectFactory.DestroyGameObject(go);
					
					mSections.removeFirst();
				} else {
					break;
				}
			} while (mSections.size() > 0);
			
			float currentX = 0.0f;
			
			do {
				if (mSections.size() > 0) {
					go = mSections.getLast();
					currentX = go.getBoundingBox().getMaxX();
				}
				
				if (currentX < cameraPosition.x + mCamera.getViewportWidth()) {
					addSection();
				} else {
					break;
				}
			} while (go != null);
		}
	}
	
	void addSection() {
		int section = 1;
		if (MathUtils.random(0, 100) < 50) {
			section = 2;
		}
		GameObject go = GameObjectFactory.InheritGameObject("bg-section00" + section, mGameObject);
		
		if (mSections.size() > 0)
		{
			GameObject last = mSections.getLast();
			go.getTransform().setX(last.getBoundingBox().getMaxX());
		}

		go.calculateBoundingBox();
		mSections.add(go);
	}
}
