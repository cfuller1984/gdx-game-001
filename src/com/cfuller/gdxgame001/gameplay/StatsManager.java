package com.cfuller.gdxgame001.gameplay;


public class StatsManager {
	static StatsManager smThis = null;
	public static void Create() { smThis = new StatsManager(); }
	public static StatsManager Get() { return smThis; }
	
	float mTotalDistanceRun;
	int mTotalCoinsCollected;
	float mTotalTimePlayed;
	
	float mDistanceRun;
	int mCoinsCollected;
	float mTimePlayed;
	
	public void clearSaveData() {
		//SharedPreferences.Editor editor = SaveData.Get().beginSave("Stats");
		//editor.clear();
		//editor.commit();
	}

	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
	
	public void save() {
		//SharedPreferences.Editor editor = SaveData.Get().beginSave("Stats");
		
		//editor.putFloat("totaldistancerun", mTotalDistanceRun);
		//editor.putInt("totalcoinscollected", mTotalCoinsCollected);
		//editor.putFloat("totaltimeplayed", mTotalTimePlayed);
		
		//SaveData.Get().finaliseSave(editor);
	}
	
	public void load() {
		//SharedPreferences prefs = SaveData.Get().beginLoad("Objectives");
		//mTotalDistanceRun = prefs.getFloat("totaldistancerun", 0.0f);
		//mTotalCoinsCollected = prefs.getInt("totalcoinscollected", 0);
		//mTotalTimePlayed = prefs.getFloat("totaltimeplayed", 0.0f);
	}
	
	public void onDistanceRunIncreased(float pDistanceRun) {
		mDistanceRun += pDistanceRun;
		mTotalDistanceRun += pDistanceRun;
	}
	
	public void onCoinsCollectedIncreased(int pCoinsCollected) {
		mCoinsCollected += pCoinsCollected;
		mTotalCoinsCollected += pCoinsCollected;
	}
	
	public void onTimePlayedIncreased(float pTimePlayed) {
		mTotalTimePlayed += pTimePlayed;
		mTimePlayed += pTimePlayed;
	}
	
	public float getDistanceRun() { return mDistanceRun; }
	public int getCoinsCollected() { return mCoinsCollected; }
	public float getTimePlayed() { return mTimePlayed; }
}
