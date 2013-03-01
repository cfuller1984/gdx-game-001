package com.cfuller.gdxgame001.gameplay;

import com.invertedlogic.componentsystem.Component;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.gameobject.Transform;
import com.invertedlogic.scene.Scene;

public class Powerup extends Component {

	protected Transform mPlayer;
	
	public Powerup(GameObject pGameObject) {
		super(pGameObject, Component.skInstanceType_Single);
	}

	@Override
	public void setup() {
		mPlayer = Scene.FindGameObject("player").getTransform();
	}

	@Override
	public void reset() {
	}

	@Override
	public void update() {
	}

	@Override
	public void onDestroy() {
	}
	
	public void onActivate() {}
	public void onDeactivate() {}
}
