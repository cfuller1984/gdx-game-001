package com.cfuller.gdxgame001.gamemode;

public interface IGameType {
	public void registerComponentTypes();
	public void onLoad();
	public void onDestroy();
}
