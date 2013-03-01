package com.cfuller.gdxgame001.gameplay;

public class ScoreData {
	int mDistanceScore;
	int mCoinsCollected;
	
	public int getTotalScore() {
		return mDistanceScore;
	}
	
	public void setDistanceScore(int pScore) {
		mDistanceScore = pScore;
	}
	
	public void addToCoinsCollected(int pCoinsCollected) {
		mCoinsCollected += pCoinsCollected;
		SaveData.Get().setCoinsCollected(mCoinsCollected);
	}
	
	public void setCoinsCollected(int pCoinsCollected) {
		mCoinsCollected = pCoinsCollected;
	}
	
	public int getCoinsCollected() {
		return mCoinsCollected;
	}
	
	public void reset() {
		mDistanceScore = 0;
		mCoinsCollected = 0;
	}
}
