package com.cfuller.gdxgame001.gameplay;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.invertedlogic.componentsystem.CameraComponent;
import com.invertedlogic.componentsystem.Component;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.gameobject.GameObjectFactory;
import com.invertedlogic.gameobject.Transform;
import com.invertedlogic.scene.Scene;

public class CoinManager extends Component {

	CameraComponent mCamera;
	ScoreData mScoreData;
	Transform mPlayer;
	ObjectivesManager mObjectivesManager;
	
	LinkedList<Coin> mCoins;
	LinkedList<Coin> mAttractList;
	LinkedList<Coin> mCollectList;
	
	float mSpawnTimer;
	
	public float minSpawnTime;
	public float maxSpawnTime;

	public CoinManager(GameObject pGameObject) {
		super(pGameObject, Component.skInstanceType_Single);
	}

	@Override
	public void setup() {
		mCamera = (CameraComponent)Scene.FindGameObject("Main Camera").getComponentOfType(CameraComponent.class);
		mPlayer = Scene.FindGameObject("player").getTransform();
		mObjectivesManager = (ObjectivesManager)Scene.FindGameObject("Objectives Manager").getComponentOfType(ObjectivesManager.class);
		
		mScoreData = GameData.Get().getScoreData();
		mScoreData.setCoinsCollected(SaveData.Get().getCoinsCollected());
		
		mCoins = new LinkedList<Coin>();
		mCollectList = new LinkedList<Coin>();
		mAttractList = new LinkedList<Coin>();
		
		mSpawnTimer = minSpawnTime;
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
		// Attract coins towards the player
		for (Iterator<Coin> it = mAttractList.iterator(); it.hasNext();) {
			Coin coin = it.next();
			
			Transform t = coin.getTransform();
			float distanceX = t.getWorldAlignedX() - mPlayer.getWorldAlignedX();
			float distanceY = t.getWorldAlignedY() - mPlayer.getWorldAlignedY();
			
			float lerpX = (distanceX);
			float lerpY = (distanceY);
			
			t.setX(t.getX() + (lerpX *-0.05f));
			t.setY(t.getY() + (lerpY *-0.05f));
		}
		
		// Collect coins when the player is close enough
		for (Coin coin : mCoins) {
			Transform t = coin.getTransform();
			
			float distance = new Vector2(mPlayer.getWorldX(), mPlayer.getWorldY()).dst(t.getWorldX(), t.getWorldY());
			if (distance < 57.0f) {
				collectCoin(coin);
			}
		}
		
		// Collect all coins on the collect list
		for (Coin coin : mCollectList) {
			// Remove the coin from the list
			mCoins.remove(coin);
			
			// Remove the coin from the attract list
			if (mAttractList.contains(coin)) {
				mAttractList.remove(coin);
			}
			
			// Register the coin with the objectives manager
			mObjectivesManager.onCoinCollected(coin);
			
			// Register the coin with the stats manager
			StatsManager.Get().onCoinsCollectedIncreased(coin.getCoinValue());
			
			// Register the coin with the score data
			mScoreData.addToCoinsCollected(coin.getCoinValue());
			
			// Destroy the coin object
			GameObject go = coin.getGameObject();
			GameObjectFactory.DestroyGameObject(go);
		}
		
		// Clear the list of collected coins
		mCollectList.clear();
		
		// Spawn new coin groups
		mSpawnTimer -= Gdx.graphics.getDeltaTime();
		if (mSpawnTimer <= 0.0f) {
			spawnCoinGroup();
			mSpawnTimer = MathUtils.random(minSpawnTime, maxSpawnTime);
		}
	}

	@Override
	public void onDestroy() {
		mCoins.clear();
	}
	
	void spawnCoinGroup() {
		for (int i = 0; i < 10; ++i)
		{
			GameObject go = GameObjectFactory.InheritGameObject("coin", mGameObject);
			go.getTransform().setX(mCamera.getPosition().x + mCamera.getViewportWidth() + (i * 32.0f));
			go.getTransform().setY(660.0f);
			
			mCoins.add((Coin)go.getComponentOfType(Coin.class));
		}
	}
	
	public void collectCoin(Coin pCoin) {
		float distance = new Vector2(mPlayer.getWorldX(), mPlayer.getWorldY()).dst(pCoin.getTransform().getWorldX(), pCoin.getTransform().getWorldY());
		if (distance < 50.0f) {
			mCollectList.add(pCoin);
		} else {
			mAttractList.add(pCoin);
		}
	}
}
