package com.cfuller.gdxgame001.ui.menus;

import com.cfuller.gdxgame001.gamemode.GameModeManager;
import com.cfuller.gdxgame001.ui.UIMenu;
import com.invertedlogic.gameobject.GameObject;

public class UIMainMenu extends UIMenu {

	public UIMainMenu(GameObject pGameObject) {
		super(pGameObject);
	}

	@Override
	protected void onEnterMenu() {
		// Clear the back stack?
	}
	
	@Override
	protected String onOptionClicked(UIMenuOption pOption) {
		String destinationMenuId = super.onOptionClicked(pOption);
		
		if (pOption.getId().equalsIgnoreCase("play")) {
			GameModeManager.Get().requestGameMode("GamePlay");
		}
		
		return destinationMenuId;
	}
}
