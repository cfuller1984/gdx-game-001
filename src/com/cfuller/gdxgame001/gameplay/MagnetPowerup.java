package com.cfuller.gdxgame001.gameplay;

import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.gameobject.Transform;
import com.invertedlogic.scene.Scene;

public class MagnetPowerup extends Powerup {

	CoinManager mCoinManager;
	LinkedList<Transform> mAffectedCoins = new LinkedList<Transform>();
	
	public MagnetPowerup(GameObject pGameObject) {
		super(pGameObject);
	}

	@Override
	public void setup() {
		super.setup();
		
		mCoinManager = (CoinManager)Scene.FindGameObject("Coin Manager").getComponentOfType(CoinManager.class);
	}

	@Override
	public void update() {
		if (mCoinManager.getTransform().hasChildTransforms()) {
			for (Transform child : mCoinManager.getTransform().getChildTransforms()) {
				float distance = new Vector2(mPlayer.getCenterX(), mPlayer.getCenterY()).dst(child.getCenterX(), child.getCenterY());
				if (distance < 200.0f) {
					mCoinManager.attractCoin((Coin)child.getGameObject().getComponentOfType(Coin.class));
				}
			}
		}
	}
	
	@Override
	public void onActivate() {
		
	}
	
	@Override
	public void onDeactivate() {
		
	}
}
