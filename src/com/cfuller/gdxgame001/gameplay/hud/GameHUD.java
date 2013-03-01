package com.cfuller.gdxgame001.gameplay.hud;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.cfuller.gdxgame001.gamemode.GameModeManager;
import com.cfuller.gdxgame001.gameplay.GameData;
import com.cfuller.gdxgame001.gameplay.GamePlay;
import com.cfuller.gdxgame001.gameplay.ScoreData;
import com.invertedlogic.componentsystem.Component;
import com.invertedlogic.componentsystem.TextComponent;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.gameobject.TransformGroup;
import com.invertedlogic.physics.RigidBody;
import com.invertedlogic.scene.Scene;

public class GameHUD extends Component {
	GamePlay mGamePlay;
	//GameObject mHudObject;
	
	ScoreData mScoreData;
	TextComponent mScoreComponent;
	TextComponent mCoinsComponent;
	TextComponent mFPSComponent;
	
	//SceneTouchHandler mTouchHandler;

	public GameHUD(GameObject pGameObject) {
		super(pGameObject, Component.skInstanceType_Single);
	}
	
	public void setup() {
		mGamePlay = (GamePlay)GameModeManager.Get().getCurrentGameMode();
		
		// Create the touch handler
		//mTouchHandler = new SceneTouchHandler(EngineData.getEngine().getCamera().getHUD());
		
		//mHudObject = GameObjectFactory.LoadGameObjectFromXml("xml/Game/HUD/Hud.xml", null);
		//mHudObject.attachToScene(EngineData.getEngine().getCamera().getHUD());
		//mHudObject.setVisible(true);
		
		mScoreData = GameData.Get().getScoreData();
		mScoreComponent = (TextComponent)mGameObject.findChild("score").getComponentOfType(TextComponent.class);
		mCoinsComponent = (TextComponent)mGameObject.findChild("coins").getComponentOfType(TextComponent.class);
		mFPSComponent = (TextComponent)mGameObject.findChild("fps").getComponentOfType(TextComponent.class);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
	
	public void update()
	{
		mScoreComponent.setText("Score: " + String.format("%8s", mScoreData.getTotalScore()).replace(' ', '0'));
		mCoinsComponent.setText("Coins: " + String.format("%6s", mScoreData.getCoinsCollected()).replace(' ', '0'));
		
		//float fps = Util.GetFPSCounter().getFPS();
		//mFPSComponent.setText("FPS: " + String.format("%s", Float.toString(fps)));
		
		RigidBody rb = (RigidBody)Scene.FindGameObject("Player").getComponentOfType(RigidBody.class);
		mFPSComponent.setText("Player velocity: " + rb.getLinearVelocity().x);
	}
	
	public void setText(String pId, String pText)
	{
		TextComponent textComponent = (TextComponent)mGameObject.findChild(pId).getComponentOfType(TextComponent.class);
		textComponent.setText(pText);
	}
	
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int position, int button) {
		// Get the menu option that was touched
		TransformGroup group = (TransformGroup)event.getTarget();
		GameObject go = group.getGameObject();
		if (go.getId().equals("pause")) {
			mGamePlay.getPause().beginPause();
			return true;
		}
		
		return false;
	}
	/*
	@Override
	public boolean onTouchEvent(TouchEvent pSceneTouchEvent, ITouchArea pTouchArea, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if (pSceneTouchEvent.isActionDown()) {
			// Get the touch area that was touched
			TouchArea touchArea = (TouchArea)pTouchArea;
			TouchCollider touchCollider = (TouchCollider)touchArea.getUserData();
			
			// Get the menu option that was touched
			GameObject go = touchCollider.getGameObject();
			if (go.getId().equals("pause")) {
				mGamePlay.getPause().beginPause();
				return true;
			}
		}
		
		return false;
	}*/
}
