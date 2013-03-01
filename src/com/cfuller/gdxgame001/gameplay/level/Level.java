package com.cfuller.gdxgame001.gameplay.level;

import org.w3c.dom.Element;

import com.badlogic.gdx.math.MathUtils;
import com.cfuller.gdxgame001.gamemode.GameModeManager;
import com.cfuller.gdxgame001.gameplay.GamePlay;
import com.cfuller.gdxgame001.gameplay.player.PlayerController;
import com.cfuller.gdxgame001.results.ResultsFlow;
import com.invertedlogic.componentsystem.Component;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.gameobject.GameObjectFactory;
import com.invertedlogic.scene.Scene;

public class Level extends Component {

	GamePlay mGamePlay;
	ResultsFlow mResultsFlow;
	
	GameObject mPlayerObject;
	PlayerController mPlayerController;
	
	GameObject mLevelSectionsRoot;
	GameObject mCurrentSectionObject;
	GameObject mNextSectionObject;
	GameObject mPreviousSectionObject;
	
	public Level(GameObject pGameObject) {
		super(pGameObject, Component.skInstanceType_Single);
	}

	@Override
	protected void parseFromXml(Element pXmlNode) {
		super.parseFromXml(pXmlNode);
	}

	@Override
	public void setup() {
		mGamePlay = (GamePlay)GameModeManager.Get().getCurrentGameMode();
		mResultsFlow = mGamePlay.getResultsFlow();
		
		mPlayerObject = Scene.FindGameObject("player");
		mPlayerController = (PlayerController)mPlayerObject.getComponentOfType(PlayerController.class);
		
		mLevelSectionsRoot = mGameObject.findChild("sections");
		mCurrentSectionObject = GameObjectFactory.InheritGameObject("testsection001", mLevelSectionsRoot);
		
		mGameObject.calculateBoundingBox();
	}

	@Override
	public void inheritFrom(Component pComponent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		if (mNextSectionObject == null
				&& mPlayerObject.getTransform().getX() > mCurrentSectionObject.getBoundingBox().getMaxX() - 640.0f) {
			int section = MathUtils.random(0, 100) < 50 ? 1 : 2;
			mNextSectionObject = GameObjectFactory.InheritGameObject("testsection00" + section, mLevelSectionsRoot);
			mNextSectionObject.getTransform().setX(mCurrentSectionObject.getBoundingBox().getMaxX());
			mNextSectionObject.calculateBoundingBox();
			
			mPreviousSectionObject = mCurrentSectionObject;
			mCurrentSectionObject = mNextSectionObject;
			mNextSectionObject = null;
			
			mGameObject.calculateBoundingBox();
		}
		else if (mPreviousSectionObject != null
				&& mCurrentSectionObject != null
				&& mPlayerObject.getTransform().getX() > mCurrentSectionObject.getBoundingBox().getMinX() + 640.0f)
		{
			// Detach the previous segment
			mLevelSectionsRoot.getTransform().detachGameObject(mPreviousSectionObject);
			
			// Destroy it
			GameObjectFactory.DestroyGameObject(mPreviousSectionObject);
			
			// Update the reference
			mPreviousSectionObject = null;
			
			// Next section is now the current section
			mNextSectionObject = null;
			
			// Update the bounding box
			mGameObject.calculateBoundingBox();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
/*
	@Override
	public boolean onTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		return mPlayerController.onTouchEvent(pScene, pSceneTouchEvent);
	}
	*/
	public void gameOver() {
		mGamePlay.gameOver();
	}
}
