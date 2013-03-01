package com.cfuller.gdxgame001.gamemode;

import java.awt.event.KeyEvent;

import com.invertedlogic.scene.Scene;

public class GameMode implements IGameMode {
	protected static final int CAMERA_WIDTH = 1280;
	protected static final int CAMERA_HEIGHT = 720;
	
	//protected Scene mScene;
	//protected Scene mSceneObject;
	
	public GameMode() {
	}
	
	public void reset() {
	}
	
	public void onCreate() {
	}
	
	public void onDestroy() {
		//mSceneObject.dispose();
		
		//GameObjectFactory.DestroyGameObject(mSceneObject);
		//mSceneObject = null;
	}

	@Override
	public void onLoadResources() {
	}
	
	@Override
	public void onUnloadResources() {
	}
	
	@Override
	public void onPreUpdate() {
		//EngineData.getEngine().getScene().setChildScene(mSceneObject.getScene(), false, false, true);
	}
	
	@Override
	public void onPostUpdate() {
		//EngineData.getEngine().getScene().back();
	}
	
	@Override
	public void update() {
		//mSceneObject.update();
	}
	
	@Override
	public void render() {
		//mSceneObject.render();
	}
	
	@Override
	public void onResume() {
	}

	@Override
	public void onPause() {
	}
	
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		return false;
	}
	/*
	public Scene getScene() {
		return mSceneObject.getScene();
	}
	
	public SceneObject getSceneObject() {
		return mSceneObject;
	}*/
}
