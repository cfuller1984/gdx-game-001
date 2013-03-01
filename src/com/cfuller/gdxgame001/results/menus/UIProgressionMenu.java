package com.cfuller.gdxgame001.results.menus;

import com.cfuller.gdxgame001.gamemode.GameModeManager;
import com.cfuller.gdxgame001.gameplay.GamePlay;
import com.cfuller.gdxgame001.ui.UIMenu;
import com.invertedlogic.gameobject.GameObject;

public class UIProgressionMenu extends UIMenu {

	public UIProgressionMenu(GameObject pGameObject) {
		super(pGameObject);
	}

	@Override
	protected void onEnterMenu() {
		// Clear the back stack?
	}
	
	@Override
	public void update() {
		super.update();
		
		if (mDelayedAction != null) {
			if (mDelayedAction.equalsIgnoreCase("retry")) {
				GamePlay gamePlay = (GamePlay)GameModeManager.Get().getCurrentGameMode();
				gamePlay.getResultsFlow().endResultsFlow();
				
				GameModeManager.Get().requestGameMode("GamePlay");
			} else if (mDelayedAction.equalsIgnoreCase("quit")) {
				GamePlay gamePlay = (GamePlay)GameModeManager.Get().getCurrentGameMode();
				gamePlay.getResultsFlow().endResultsFlow();
				
				GameModeManager.Get().requestGameMode("Frontend");
			}
			
			mDelayedAction = null;
		}
	}
	
	@Override
	protected String onOptionClicked(UIMenuOption pOption) {
		String destinationMenuId = super.onOptionClicked(pOption);
		
		mDelayedAction = pOption.getId();
		
		return destinationMenuId;
	}
}
