package com.cfuller.gdxgame001.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.invertedlogic.scene.Scene;

public class SaveData {
	int mHighscore;
	int mCoinsCollected;
	
	Preferences mPreferences;
	
	private static SaveData smThis;
	
	public static void Create() {
		smThis = new SaveData();
		smThis.load();
	}
	
	public static SaveData Get() {
		return smThis;
	}
	
	public SaveData() {
		mPreferences = Gdx.app.getPreferences("SaveData");
	}
	
	public boolean setHighscore(int pHighscore) {
		mHighscore = pHighscore;
		return save();
	}
	
	public int getHighscore() {
		return mHighscore;
	}
	
	public boolean setCoinsCollected(int pCoinsCollected) {
		mCoinsCollected = pCoinsCollected;
		return save();
	}
	
	public int getCoinsCollected() {
		return mCoinsCollected;
	}
	
	public void load() {
		mHighscore = mPreferences.getInteger("Highscore");
		mCoinsCollected = mPreferences.getInteger("Coins Collected");
		
		StatsManager.Get().load();
	}
	
	public boolean save() {
		mPreferences.putInteger("Highscore", mHighscore);
		mPreferences.putInteger("Coins Collected", mCoinsCollected);
		
		ObjectivesManager objectivesManager = (ObjectivesManager)Scene.FindGameObject("Objectives Manager").getComponentOfType(ObjectivesManager.class);
		objectivesManager.save();
		
		StatsManager.Get().save();
		
		mPreferences.flush();
		return true;
	}
	
	public Preferences beginSave(String pName) {
		return Gdx.app.getPreferences(pName);
	}
	
	public boolean finaliseSave(Preferences pPreferences) {
		pPreferences.flush();
		return true;
	}
	
	public Preferences beginLoad(String pName) {
		return Gdx.app.getPreferences(pName);
	}
}
