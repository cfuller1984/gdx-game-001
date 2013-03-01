package com.cfuller.gdxgame001.gamemode;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map.Entry;

import com.invertedlogic.util.Assert;

public class GameModeManager {
	private static GameModeManager smThis;
	
	public static void Create() {
		smThis = new GameModeManager();
	}
	
	public static GameModeManager Get() {
		return smThis;
	}
	
	HashMap<String, GameMode> mGameModes = new HashMap<String, GameMode>();
	
	GameMode mCurrentGameMode;
	GameMode mRequestedGameMode;
	
	public GameModeManager() {
	}
	
	public void registerGameMode(String pGameModeId, GameMode pGameMode) {
		mGameModes.put(pGameModeId, pGameMode);
	}
	
	public void requestGameMode(String pGameModeId) {
		mRequestedGameMode = mGameModes.get(pGameModeId);
		Assert.assertNotNull(mRequestedGameMode);
	}
	
	public GameMode getCurrentGameMode() {
		return mCurrentGameMode;
	}
	
	public void onLoadResources() {
		for (Entry<String, GameMode> entry : mGameModes.entrySet()) {
			GameMode gameMode = entry.getValue();
			gameMode.onLoadResources();
		}
	}
	
	public void onUnloadResources() {
		for (Entry<String, GameMode> entry : mGameModes.entrySet()) {
			GameMode gameMode = entry.getValue();
			gameMode.onUnloadResources();
		}
	}
	
	public void update() {
		if (mRequestedGameMode != null) {
			
			if (mCurrentGameMode != null) {
				// post update
				mCurrentGameMode.onPostUpdate();
				mCurrentGameMode.onDestroy();
				//mCurrentGameMode.onUnloadResources();
			}
			
			// set as the current game mode
			mCurrentGameMode = mRequestedGameMode;
			mRequestedGameMode = null;
			
			// pre update
			//mCurrentGameMode.onLoadResources();
			mCurrentGameMode.onCreate();
			mCurrentGameMode.onPreUpdate();

			// reset the game mode
			mCurrentGameMode.reset();
			//mCurrentGameMode.update();
		}
		
		if (mCurrentGameMode != null) {
			mCurrentGameMode.update();
		}
	}
	public void render() {
		if (mCurrentGameMode != null) {
			mCurrentGameMode.render();
		}
	}

	public void reset() {
		if (mCurrentGameMode != null) {
			mCurrentGameMode.reset();
		}
	}
	
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if (mCurrentGameMode != null) {
			return mCurrentGameMode.onKeyDown(pKeyCode, pEvent);
		}
		
		return false;
	}
}
