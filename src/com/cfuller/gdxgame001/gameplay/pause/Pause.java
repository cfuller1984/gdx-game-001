package com.cfuller.gdxgame001.gameplay.pause;

import com.cfuller.gdxgame001.gameplay.GamePlay;
import com.cfuller.gdxgame001.gameplay.pause.menus.UIObjectivesMenu;
import com.cfuller.gdxgame001.gameplay.pause.menus.UIPauseMenu;
import com.cfuller.gdxgame001.ui.UIMenuSystem;
import com.invertedlogic.componentsystem.ComponentFactory;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.gameobject.GameObjectFactory;
import com.invertedlogic.graphics.LayerManager;
import com.invertedlogic.scene.Scene;

public class Pause {
	GamePlay mGamePlay;
	
	GameObject mRoot;
	UIMenuSystem mMenuSystem;
	
	public Pause(GamePlay pGamePlay) {
		mGamePlay = pGamePlay;
		
		// Pause menu components
		ComponentFactory.RegisterComponentType(UIPauseMenu.class);
		ComponentFactory.RegisterComponentType(UIObjectivesMenu.class);
		
		// Load the Results UI from Xml
		mRoot = GameObjectFactory.LoadGameObjectFromXml("xml/Game/Pause/UIPause.xml", null);
		mRoot.setLayerMask(LayerManager.Get().getLayerMask("UI"));
		Scene.GetCurrentScene().addActor(mRoot.getTransform().getActor());
		
		// Setup the menu system
		mMenuSystem = (UIMenuSystem)mRoot.findChild("Pause Menu System").getComponentOfType(UIMenuSystem.class);
	}
	
	public void beginPause() {
		mMenuSystem.gainFocus();
		mMenuSystem.navigateToMenu("Pause Menu");
		
		mGamePlay.onPause();
	}
	
	public void endPause() {
		mMenuSystem.navigateToMenu(UIMenuSystem.skInvalidMenu);
		mMenuSystem.loseFocus();
		
		mGamePlay.onResume();
	}
	
	public boolean isPaused() {
		return mMenuSystem.hasFocus();
	}
	
	public void update() {
		mRoot.update();
	}
}
