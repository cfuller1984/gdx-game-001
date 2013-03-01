package com.cfuller.gdxgame001.gameplay.ai;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.invertedlogic.componentsystem.CameraComponent;
import com.invertedlogic.componentsystem.Component;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.gameobject.GameObjectFactory;
import com.invertedlogic.scene.GameScene;
import com.invertedlogic.scene.Scene;

public class GuardManager extends Component {

	CameraComponent mCamera;
	
	LinkedList<GameObject> mGuards;
	LinkedList<GameObject> mKillList;
	
	float mTimer;
	
	public GuardManager(GameObject pGameObject) {
		super(pGameObject, Component.skInstanceType_Single);
	}

	@Override
	public void setup() {
		mCamera = (CameraComponent)Scene.FindGameObject("Main Camera").getComponentOfType(CameraComponent.class);
		
		mGuards = new LinkedList<GameObject>();
		mKillList = new LinkedList<GameObject>();
	}

	@Override
	public void inheritFrom(Component pComponent) {
		
	}

	@Override
	public void reset() {
		onDestroy();
	}

	@Override
	public void update() {
		// Kill all guards on the kill list
		for (GameObject go : mKillList) {
			mTransform.detachGameObject(go);
			mGuards.remove(go);
			
			GameObjectFactory.DestroyGameObject(go);
		}
		
		// Clear the kill list
		mKillList.clear();
		
		mTimer -= Gdx.graphics.getDeltaTime();
		if (mTimer < 0.0f)
		{
			//Util.DebugLog("npc", "Spawned Guard.");
			spawnGuard();
		}
	}

	@Override
	public void onDestroy() {
		for (GameObject go : mGuards) {
			mTransform.detachGameObject(go);
			GameObjectFactory.DestroyGameObject(go);
		}
		
		mGuards.clear();
	}
	
	void spawnGuard() {
		GameObject go = GameObjectFactory.InheritGameObject("guard001", mGameObject);
		go.getTransform().setX(mCamera.getPosition().x + mCamera.getViewportWidth());
		go.getTransform().setY(700.0f);
		mGuards.push(go);
		
		mTimer = MathUtils.random(1.0f, 3.0f);
	}
	
	public void killGuard(Guard pGuard) {
		mKillList.push(pGuard.getGameObject());
	}
}
