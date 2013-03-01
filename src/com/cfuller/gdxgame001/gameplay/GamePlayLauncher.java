package com.cfuller.gdxgame001.gameplay;

import com.cfuller.gdxgame001.gamemode.GameModeDescriptor;
import com.cfuller.gdxgame001.gamemode.GameModeManager;

public class GamePlayLauncher {
	private static GamePlayLauncher smThis;
	
	public static void Create() {
		smThis = new GamePlayLauncher();
	}
	
	public static GamePlayLauncher Get() {
		return smThis;
	}
	
	GameModeDescriptor mGameDescriptor = new GameModeDescriptor();
	
	public GameModeDescriptor getGameDescriptor() {
		return mGameDescriptor;
	}
	
	// Launches into gameplay using the game descriptor
	public void launchGame() {
		GameModeManager.Get().requestGameMode("GamePlay");
	}
}
