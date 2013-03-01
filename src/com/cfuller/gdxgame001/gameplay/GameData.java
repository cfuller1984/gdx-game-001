package com.cfuller.gdxgame001.gameplay;

public class GameData {
	static GameData smThis = null;
	
	static public void Create() { smThis = new GameData(); }
	static public GameData Get() { return smThis; }
	
	ScoreData mScoreData;
	
	public GameData() {
		mScoreData = new ScoreData();
	}
	
	public ScoreData getScoreData() {
		return mScoreData;
	}
}
