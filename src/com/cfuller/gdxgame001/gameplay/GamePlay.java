package com.cfuller.gdxgame001.gameplay;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.cfuller.gdxgame001.gamemode.GameMode;
import com.cfuller.gdxgame001.gameplay.ai.Guard;
import com.cfuller.gdxgame001.gameplay.ai.GuardManager;
import com.cfuller.gdxgame001.gameplay.hud.GameHUD;
import com.cfuller.gdxgame001.gameplay.level.BackgroundSection;
import com.cfuller.gdxgame001.gameplay.level.Level;
import com.cfuller.gdxgame001.gameplay.level.LevelBackground;
import com.cfuller.gdxgame001.gameplay.level.LevelSection;
import com.cfuller.gdxgame001.gameplay.pause.Pause;
import com.cfuller.gdxgame001.gameplay.player.Player;
import com.cfuller.gdxgame001.gameplay.player.PlayerController;
import com.cfuller.gdxgame001.results.ResultsFlow;
import com.invertedlogic.componentsystem.CameraComponent;
import com.invertedlogic.componentsystem.ComponentFactory;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.gameobject.GameObjectFactory;
import com.invertedlogic.graphics.LayerManager;
import com.invertedlogic.scene.Scene;
import com.invertedlogic.scene.SceneFactory;
import com.invertedlogic.util.Assert;

public class GamePlay extends GameMode {
	enum State {
		Playing,
		Paused,
		Results,
	}
	
	HashMap<String, String> mLevelIds = new HashMap<String, String>();
	
	//HUD mHud;
	CameraComponent mCamera;
	CameraComponent mCamera2D;
	Pause mPause;
	ResultsFlow mResultsFlow;
	Level mLevel;
	Player mPlayer;
	State mState;
	GameObject mGameHUD;
	
	ObjectivesManager mObjectivesManager;
	
	public GamePlay() {
		ComponentFactory.RegisterComponentType(GameHUD.class);
		
		ComponentFactory.RegisterComponentType(Level.class);
		ComponentFactory.RegisterComponentType(LevelSection.class);
		ComponentFactory.RegisterComponentType(LevelBackground.class);
		ComponentFactory.RegisterComponentType(BackgroundSection.class);
		
		ComponentFactory.RegisterComponentType(Player.class);
		ComponentFactory.RegisterComponentType(PlayerController.class);

		ComponentFactory.RegisterComponentType(ObjectivesManager.class);
		ComponentFactory.RegisterComponentType(GuardManager.class);
		ComponentFactory.RegisterComponentType(Guard.class);
		
		ComponentFactory.RegisterComponentType(CoinManager.class);
		ComponentFactory.RegisterComponentType(Coin.class);
		
		ComponentFactory.RegisterComponentType(PowerupManager.class);
		ComponentFactory.RegisterComponentType(Powerup.class);
		ComponentFactory.RegisterComponentType(MagnetPowerup.class);
		ComponentFactory.RegisterComponentType(BoostPowerup.class);
		
		mLevelIds.put("gamescene", "xml/Game/Scenes/GameScene.xml");
	}
	
	@Override
	public void onCreate() {
		super.onCreate();

		// Create game data
		GameData.Create();
		
		// Create the HUD
		//mHud = new HUD();
		//EngineData.getEngine().getCamera().setHUD(mHud);
		
		// Create the camera and the scene
		//mCamera = new Camera(0.0f, 0.0f, CAMERA_WIDTH, CAMERA_HEIGHT);
		//mSceneObject = new SceneObject(new GameScene(mCamera));
		
		//mSceneObject.getScene().setBackground(new Background(new Color(0.2f, 0.2f, 0.2f)));
		
		// Load level library
		GameObjectFactory.LoadGameObjectLibraryFromXml("xml/Game/Levels/Library/LevelLibrary.xml");
		
		// Load the test level from Xml
		String levelFilename = "xml/Game/Scenes/GameScene.xml";//mLevelIds.get(GamePlayLauncher.Get().getGameDescriptor().mLevelName);
		
		Scene scene = SceneFactory.LoadSceneFromXml(levelFilename);
		Scene.SetCurrentScene(scene);
		//mSceneObject.getScene().setBackground(new Background(new Color(0.2f, 0.2f, 0.2f)));
		
		mLevel = (Level)Scene.FindGameObject("Level").getComponentOfType(Level.class);
		mLevel.getGameObject().setLayerMask(LayerManager.Get().getLayerMask("Default"));
		
		// Create the game Hud
		mGameHUD = GameObjectFactory.LoadGameObjectFromXml("xml/Game/HUD/Hud.xml", null);
		mGameHUD.setLayerMask(LayerManager.Get().getLayerMask("UI"));
		Scene.GetCurrentScene().addActor(mGameHUD.getTransform().getActor());
		//mGameHUD.attachToScene(EngineData.getEngine().getCamera().getHUD());
		//mGameHUD.setVisible(true);
		//mGameHud.setup();
		
		// Store a pointer to the player
		mPlayer = (Player)Scene.FindGameObject("player").getComponentOfType(Player.class);
		mPlayer.getGameObject().setLayerMask(LayerManager.Get().getLayerMask("Default"));
		
		// Store a pointer to the camera
		mCamera = (CameraComponent)Scene.FindGameObject("Main Camera").getComponentOfType(CameraComponent.class);
		mCamera.setLayersFilter(LayerManager.Get().getLayerMask("Default"));
		
		mCamera2D = (CameraComponent)Scene.FindGameObject("Camera 2D").getComponentOfType(CameraComponent.class);
		mCamera2D.setLayersFilter(LayerManager.Get().getLayerMask("UI"));
		
		// Results flow
		mResultsFlow = new ResultsFlow(this);
		
		// Pause menu
		mPause = new Pause(this);
		
		// Game state
		mState = State.Playing;
		//createGameType(GamePlayLauncher.Get().getGameDescriptor().mGameType);
		
		mObjectivesManager = (ObjectivesManager)Scene.FindGameObject("Objectives Manager").getComponentOfType(ObjectivesManager.class);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		// Make sure the results flow has ended
		Assert.assertFalse(mResultsFlow.isResultsFlowActive());
		
		mResultsFlow = null;
		mPause = null;
		
		//mCurrentGameType.onDestroy();
		//mCurrentGameType = null;
		
		mCamera = null;
		mLevel = null;
		mPlayer = null;
	}
	
	@Override
	public void update() {
		super.update();
		
		mGameHUD.update();
		
		switch (mState) {
		case Playing:
			// Update stats manager
			StatsManager.Get().onTimePlayedIncreased(Gdx.graphics.getDeltaTime());
			break;
			
		case Paused:
			mPause.update();
			if (!mPause.isPaused()) {
				mState = State.Playing;
			}
			break;
		case Results:
			mResultsFlow.update();
			break;
		}
	}
	
	@Override
	public void render() {
		super.render();
		
		mResultsFlow.render();
	}
	
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		/*if (pKeyCode == KeyEvent.KEYCODE_BACK
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if (mResultsFlow.isResultsFlowActive()) {
				getResultsFlow().endResultsFlow();
			}
			
			GameModeManager.Get().requestGameMode("Frontend");
			return true;
		} else if (pKeyCode == KeyEvent.KEYCODE_MENU
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			mState = State.Paused;
			mPause.beginPause();
			return true;
		}*/
		
		return false;
	}
	
	public Pause getPause() {
		return mPause;
	}
	
	@Override
	public void onPause() {
		mState = State.Paused;
		Scene.GetCurrentScene().Root().setEnabled(false);
		mGameHUD.setEnabled(false);
	}
	
	@Override
	public void onResume() {
		mState = State.Playing;
		
		Scene.GetCurrentScene().Root().setEnabled(true);
		mGameHUD.setEnabled(true);
	}
	
	public ResultsFlow getResultsFlow() {
		return mResultsFlow;
	}
	
	public void gameOver() {
		Assert.assertTrue(mState != State.Results);
		
		mState = State.Results;
		
		//GameObjectFactory.GetActiveSceneObject().setEnabled(false);
		mGameHUD.setEnabled(false);
		
		//if (mObjectivesManager.isAnyCurrentObjectiveComplete()) {
			mResultsFlow.beginResultsFlow("Objectives Menu");
		//} else {
			//mResultsFlow.beginResultsFlow("Progression Menu");
		//}
		
		ScoreData scoreData = GameData.Get().getScoreData();
		SaveData.Get().setHighscore(scoreData.getTotalScore());
	}
	
	public boolean isGameOver() {
		return mState == State.Results;
	}
	
	public boolean isPaused() {
		return mState == State.Paused;
	}
	
	public void play() {
		mState = State.Playing;
	}
}
