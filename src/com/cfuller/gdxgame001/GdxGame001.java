package com.cfuller.gdxgame001;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.cfuller.gdxgame001.gamemode.GameModeManager;
import com.cfuller.gdxgame001.gameplay.GamePlay;
import com.cfuller.gdxgame001.gameplay.SaveData;
import com.cfuller.gdxgame001.gameplay.StatsManager;
import com.cfuller.gdxgame001.ui.FrontendUI;
import com.invertedlogic.core.InvertedLogic;
import com.invertedlogic.gameobject.GameObjectFactory;
import com.invertedlogic.physics.PhysicsManager;
import com.invertedlogic.scene.Scene;

public class GdxGame001 extends Game {
	InputMultiplexer mInputMultiplexer;
	
	FrontendUI mFrontendUI;
	GamePlay mGamePlay;
	
	@Override
	public void create() {
		// Create the Inverted Logic engine
		InvertedLogic.Create();
		InvertedLogic.Get().init();
		
		// Stats
		StatsManager.Create();
		
		// Save Data
		SaveData.Create();
		
		// Physics
		PhysicsManager.Create(30, true);
		
		// Game Mode Manager
		GameModeManager.Create();
		
		// Create game modes
		mFrontendUI = new FrontendUI();
		mGamePlay = new GamePlay();
		
		GameModeManager.Get().registerGameMode("Frontend", mFrontendUI);
		GameModeManager.Get().registerGameMode("GamePlay", mGamePlay);
		
		GameObjectFactory.LoadGameObjectLibraryFromXml("xml/Library/GameObjectLibrary.xml");
		//mScene = SceneFactory.LoadSceneFromXml("xml/FrontendUI/FrontendUI.xml");
		//mScene = SceneFactory.LoadSceneFromXml("xml/test.xml");
		//mScene = SceneFactory.LoadSceneFromXml("xml/Game/Scenes/GameScene.xml");
		//Scene.SetCurrentScene(mScene);
		
		GameModeManager.Get().requestGameMode("GamePlay");
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(0.1f, 0.2f, 0.6f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//Util.GetFPSLogger().log();
		
		GameModeManager.Get().update();
		GameModeManager.Get().render();
		
		if (Scene.GetCurrentScene() != null) {
			Scene.GetCurrentScene().update();
			Scene.GetCurrentScene().render();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
