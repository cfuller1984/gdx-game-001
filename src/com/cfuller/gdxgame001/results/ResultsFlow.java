package com.cfuller.gdxgame001.results;

import com.cfuller.gdxgame001.gameplay.GamePlay;
import com.cfuller.gdxgame001.results.menus.UIProgressionMenu;
import com.cfuller.gdxgame001.results.menus.UIStatsMenu;
import com.cfuller.gdxgame001.ui.UIMenuSystem;
import com.invertedlogic.componentsystem.ComponentFactory;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.gameobject.GameObjectFactory;
import com.invertedlogic.graphics.LayerManager;
import com.invertedlogic.scene.Scene;
import com.invertedlogic.util.Util;

public class ResultsFlow {
	GamePlay mGamePlay;
	
	GameObject mGameObject;
	UIMenuSystem mMenuSystem;
	
	//SceneTouchHandler mTouchHandler;
	
	public ResultsFlow(GamePlay pGamePlay) {
		mGamePlay = pGamePlay;
		
		//ComponentFactory.RegisterComponentType(UIMenuSystem.class);
		//ComponentFactory.RegisterComponentType(UIMenu.class);
		//ComponentFactory.RegisterComponentType(UIMenuOptionObject.class);
		
		// Results menus
		ComponentFactory.RegisterComponentType(UIStatsMenu.class);
		ComponentFactory.RegisterComponentType(UIProgressionMenu.class);
		
		// Load the Results UI from Xml
		mGameObject = GameObjectFactory.LoadGameObjectFromXml("xml/Game/Results/UIResultsFlow.xml", null);
		mGameObject.setLayerMask(LayerManager.Get().getLayerMask("UI"));
		Scene.GetCurrentScene().addActor(mGameObject.getTransform().getActor());
		//mRoot.attachToScene(EngineData.getEngine().getCamera().getHUD());
		
		// Touch handler
		//mTouchHandler = new SceneTouchHandler(EngineData.getEngine().getCamera().getHUD());
		
		// Setup the menu system
		mMenuSystem = (UIMenuSystem)mGameObject.getComponentOfType(UIMenuSystem.class);
	}
	
	public void beginResultsFlow(String pMenuId) {
		Util.DebugLog("ui", "Beginning results flow.");
		mMenuSystem.gainFocus();
		mMenuSystem.navigateToMenu(pMenuId);
	}
	
	public void endResultsFlow() {
		Util.DebugLog("ui", "Ending results flow.");
		mMenuSystem.navigateToMenu(UIMenuSystem.skInvalidMenu);
		mMenuSystem.loseFocus();
	}
	
	public boolean isResultsFlowActive() {
		return mMenuSystem.hasFocus();
	}
	
	public void update() {
		mGameObject.update();
	}
	
	public void render() {
		//mGameObject.render();
	}
}
