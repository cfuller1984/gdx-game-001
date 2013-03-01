package com.cfuller.gdxgame001.ui;

import java.awt.event.KeyEvent;

import com.cfuller.gdxgame001.gamemode.GameMode;
import com.cfuller.gdxgame001.gameplay.pause.menus.UIObjectivesMenu;
import com.cfuller.gdxgame001.ui.menus.UIMainMenu;
import com.invertedlogic.componentsystem.ComponentFactory;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.scene.Scene;
import com.invertedlogic.scene.SceneFactory;

public class FrontendUI extends GameMode {
	GameObject mRoot;
	UIMenuSystem mMenuSystem;
	
	public FrontendUI() {
		ComponentFactory.RegisterComponentType(UIMenuSystem.class);
		ComponentFactory.RegisterComponentType(UIMenu.class);
		ComponentFactory.RegisterComponentType(UIMenuOptionObject.class);
		
		// Frontend menus
		ComponentFactory.RegisterComponentType(UIMainMenu.class);
		ComponentFactory.RegisterComponentType(UIObjectivesMenu.class);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();

		// Create the main scene
		//mSceneObject = new SceneObject(new Scene());
		Scene scene = SceneFactory.LoadSceneFromXml("xml/FrontendUI/FrontendUI.xml");
		Scene.SetCurrentScene(scene);
		
		// Setup the menu system
		mMenuSystem = (UIMenuSystem)Scene.FindGameObject("Menu System").getComponentOfType(UIMenuSystem.class);
		mMenuSystem.gainFocus();
	}
	
	@Override
	public void onDestroy() {
		mMenuSystem.loseFocus();
		mMenuSystem = null;
		
		super.onDestroy();
	}
	
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		/*if (pKeyCode == KeyEvent.KEYCODE_BACK
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if (mMenuSystem.getBackStackSize() > 0) {
				mMenuSystem.onBack();
				return true;
			} else {
				// quit
			}
		}*/
		
		return false;
	}
}
