package com.cfuller.gdxgame001.gameplay;

import java.util.Map;

import com.cfuller.gdxgame001.gamemode.GameMode;
import com.cfuller.gdxgame001.gamemode.IGameType;

public class GameTypeManager {
	private static GameTypeManager smThis;
	
	public static void Create() {
		smThis = new GameTypeManager();
	}
	
	public static GameTypeManager Get() {
		return smThis;
	}
	
	Map<String, IGameType> mGameTypes;
	
	GameMode mCurrentGameType;
	GameMode mRequestedGameType;
}
