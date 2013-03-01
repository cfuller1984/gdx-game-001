package com.cfuller.gdxgame001.gameplay;

import com.cfuller.gdxgame001.gameplay.player.PlayerController;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.scene.Scene;

public class BoostPowerup extends Powerup {

	CoinManager mCoinManager;
	PlayerController mPlayerController;
	
	public BoostPowerup(GameObject pGameObject) {
		super(pGameObject);
	}

	@Override
	public void setup() {
		super.setup();
		
		mPlayerController = (PlayerController)Scene.FindGameObject("Player").getComponentOfType(PlayerController.class);
	}
	
	@Override
	public void onActivate() {
		mPlayerController.beginBoost();
	}
	
	@Override
	public void onDeactivate() {
		mPlayerController.endBoost();
	}
}
