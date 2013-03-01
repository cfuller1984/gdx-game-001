package com.cfuller.gdxgame001.gameplay.player;

import org.w3c.dom.Element;

import com.cfuller.gdxgame001.gameplay.GameData;
import com.cfuller.gdxgame001.gameplay.ScoreData;
import com.cfuller.gdxgame001.gameplay.StatsManager;
import com.cfuller.gdxgame001.gameplay.level.Level;
import com.invertedlogic.componentsystem.CameraComponent;
import com.invertedlogic.componentsystem.Component;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.physics.PhysicsConstants;
import com.invertedlogic.physics.RigidBody;
import com.invertedlogic.scene.GameScene;
import com.invertedlogic.scene.Scene;
import com.invertedlogic.util.Util;

public class Player extends Component {

	GameScene mScene;
	CameraComponent mCamera;
	Level mLevel;
	ScoreData mScoreData;
	RigidBody mRigidBody;
	boolean mAlive;
	
	public Player(GameObject pGameObject) {
		super(pGameObject, Component.skInstanceType_Single);
	}

	@Override
	protected void parseFromXml(Element pXmlNode) {
		super.parseFromXml(pXmlNode);
	}

	@Override
	public void setup() {
		mRigidBody = (RigidBody)mGameObject.getComponentOfType(RigidBody.class);
		//mScene = (GameScene)mTransform.getScene();
		mCamera = (CameraComponent)Scene.FindGameObject("Main Camera").getComponentOfType(CameraComponent.class);
		//mCamera.setPosition(new Vector3(mTransform.getX(), 0.0f, 0.0f));
		mLevel = (Level)Scene.FindGameObject("level").getComponentOfType(Level.class);
		mScoreData = GameData.Get().getScoreData();
		mAlive = true;
		
		//QuadComponent quad = (QuadComponent)mGameObject.getComponentOfType(QuadComponent.class);
		
		//quad.animate();
	}

	@Override
	public void reset() {
		mAlive = true;
	}

	@Override
	public void update() {
		Util.DebugLog("Game", "Position: " + mTransform.getX());
		mCamera.getTransform().setX(mTransform.getX());
		mCamera.getTransform().setY(360.0f);
		
		int distanceTravelled = Math.round(mTransform.getX());
		mScoreData.setDistanceScore(distanceTravelled * 17);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onEnabled() {
		//QuadComponent quad = (QuadComponent)mGameObject.getComponentOfType(QuadComponent.class);
		//quad.animate();
	}
	
	@Override
	public void onDisabled() {
		//QuadComponent quad = (QuadComponent)mGameObject.getComponentOfType(QuadComponent.class);
		//quad.stopAnimation();
	}

	public ScoreData getScoreData() {
		return mScoreData;
	}
	
	protected void die() {
		if (mAlive) {
			mAlive = false;
			StatsManager.Get().onDistanceRunIncreased(mTransform.getWorldX() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
			mLevel.gameOver();
		}
	}
}
