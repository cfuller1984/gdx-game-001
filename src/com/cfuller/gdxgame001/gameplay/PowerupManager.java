package com.cfuller.gdxgame001.gameplay;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.invertedlogic.componentsystem.CameraComponent;
import com.invertedlogic.componentsystem.Component;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.gameobject.GameObjectFactory;
import com.invertedlogic.gameobject.Transform;
import com.invertedlogic.scene.Scene;
import com.invertedlogic.util.Assert;
import com.invertedlogic.util.Util;

public class PowerupManager extends Component {

	ArrayList<Class<? extends Powerup>> mPowerUps;
	GameObject mCollectablePowerup;
	Powerup mActivePowerup;
	Transform mPlayer;
	CameraComponent mCamera;
	
	float mPowerupTimer;
	float mSpawnTimer;
	
	float minSpawnTime;
	float maxSpawnTime;
	
	static String[] smPowerupTypes = new String[] {
		"magnetpowerup",
		"boostpowerup",
	};
	
	public PowerupManager(GameObject pGameObject) {
		super(pGameObject, Component.skInstanceType_Single);
		
		mPowerUps = new ArrayList<Class<? extends Powerup>>();
		mPowerUps.add(MagnetPowerup.class);
	}

	@Override
	public void setup() {
		//mCollectablePowerup = GameObjectFactory.InheritGameObject("magnet", mGameObject);
		//mCollectablePowerup.getTransform().setX(400.0f);
		//mCollectablePowerup.getTransform().setY(250.0f);
		
		mPlayer = Scene.FindGameObject("player").getTransform();
		mCamera = (CameraComponent)Scene.FindGameObject("Main Camera").getComponentOfType(CameraComponent.class);
		
		mSpawnTimer = maxSpawnTime;
	}

	@Override
	public void inheritFrom(Component pComponent) {
		PowerupManager powerupManager = (PowerupManager)pComponent;
		minSpawnTime = powerupManager.minSpawnTime;
		maxSpawnTime = powerupManager.maxSpawnTime;
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void update() {
		if (mCollectablePowerup != null) {
			if (mPlayer.collidesWithTransform(mCollectablePowerup.getTransform().findChildTransform("collectable"))) {
				collectPowerup();
			} else if (mCollectablePowerup.getTransform().getWorldX() < mCamera.getPosition().x - mCamera.getViewportWidth()) {
				GameObjectFactory.DestroyGameObject(mCollectablePowerup);
				mCollectablePowerup = null;
			}
		} else {
			mSpawnTimer -= Gdx.graphics.getDeltaTime();
			if (mSpawnTimer < 0.0f) {
				int powerupType = MathUtils.random(0, smPowerupTypes.length - 1);
				Util.DebugLog("gameplay", "Spawned power up type " + powerupType);
				
				mCollectablePowerup = GameObjectFactory.InheritGameObject(smPowerupTypes[powerupType], mGameObject);
				mCollectablePowerup.getTransform().setX(mCamera.getPosition().x + mCamera.getViewportWidth());
				mCollectablePowerup.getTransform().setY(500.0f);
				
				mSpawnTimer = MathUtils.random(minSpawnTime, maxSpawnTime);
			}
		}
		
		if (mActivePowerup != null) {
			mPowerupTimer -= Gdx.graphics.getDeltaTime();
			
			if (mPowerupTimer <= 0.0f) {
				mActivePowerup.onDeactivate();
				
				GameObject go = mActivePowerup.getGameObject();
				GameObjectFactory.DestroyGameObject(go);
				
				mActivePowerup = null;
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	public void collectPowerup() {
		Assert.assertNotNull(mCollectablePowerup);
		
		mActivePowerup = (Powerup)mCollectablePowerup.getComponentOfType(Powerup.class);
		mCollectablePowerup = null;
		
		mActivePowerup.getGameObject().findChild("collectable").setVisible(false);
		mActivePowerup.setEnabled(true);
		
		mActivePowerup.onActivate();
		
		mPowerupTimer = 5.0f;
	}
}
