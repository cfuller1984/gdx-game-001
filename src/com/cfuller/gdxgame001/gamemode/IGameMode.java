package com.cfuller.gdxgame001.gamemode;

public interface IGameMode {
	
	public void reset();
	public void onLoadResources();
	public void onUnloadResources();
	public void onDestroy();
	
	public void onPreUpdate();
	public void onPostUpdate();
	public void update();
	public void render();
	
	public void onResume();
	public void onPause();
}
