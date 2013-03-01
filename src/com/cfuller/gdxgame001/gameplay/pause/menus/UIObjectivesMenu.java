package com.cfuller.gdxgame001.gameplay.pause.menus;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.cfuller.gdxgame001.gamemode.GameModeManager;
import com.cfuller.gdxgame001.gameplay.GamePlay;
import com.cfuller.gdxgame001.gameplay.ObjectivesManager;
import com.cfuller.gdxgame001.ui.UIMenu;
import com.invertedlogic.componentsystem.TextComponent;
import com.invertedlogic.componentsystem.animation.Animation;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.gameobject.Transform;
import com.invertedlogic.scene.Scene;

public class UIObjectivesMenu extends UIMenu {

	ObjectivesManager mObjectivesManager;
	ArrayList<GameObject> mObjectivesText;
	
	enum ObjectiveState
	{
		Idle,
		SlideOff,
		SlideOn,
	}

	float mObjectiveTimer;
	int mCurrentObjective;
	ObjectiveState mObjectiveState;
	
	public UIObjectivesMenu(GameObject pGameObject) {
		super(pGameObject);
	}

	@Override
	public void setup() {
		super.setup();
		
		mObjectivesManager = (ObjectivesManager)Scene.FindGameObject("Objectives Manager").getComponentOfType(ObjectivesManager.class);
		mObjectivesText = new ArrayList<GameObject>();
		
		int i = 0;
		Transform t = null;
		
		do {
			t = mTransform.findChildTransform("objective" + String.format("%2s", i++).replace(' ', '0'));
			if (t != null) {
				mObjectivesText.add(t.getGameObject());
			}
		} while (t != null);
	}
	
	@Override
	protected void onEnterMenu() {
		// Reset states
		mObjectiveTimer = 0.0f;
		mObjectiveState = ObjectiveState.Idle;
		mCurrentObjective =-1;
		
		ArrayList<ObjectivesManager.Objective> objectives = mObjectivesManager.getCurrentObjectives();
		int objectiveCount = objectives.size();
		for (int i = 0; i < objectiveCount; ++i) {
			ObjectivesManager.Objective objective = objectives.get(i);
			GameObject go = mObjectivesText.get(i);
			
			TextComponent text = (TextComponent)go.findChild("text").getComponentOfType(TextComponent.class);
			TextComponent details = (TextComponent)go.findChild("details").getComponentOfType(TextComponent.class);
			
			text.setText(objective.getTitleText());
			details.setText(objective.getDescriptionText());
			
			if (objective.isComplete()
					&& mCurrentObjective ==-1) {
				mCurrentObjective = i;
			}
		}
	}
	
	@Override
	public void update() {
		super.update();

		// Check for completed objectives
		if (mCurrentObjective >-1) {
			ArrayList<ObjectivesManager.Objective> objectives = mObjectivesManager.getCurrentObjectives();
			ObjectivesManager.Objective objective = objectives.get(mCurrentObjective);
			
			if (objective.isComplete()) {
				GameObject go = mObjectivesText.get(mCurrentObjective);
				Animation anim = go.getAnimation();
				
				mObjectiveTimer += Gdx.graphics.getDeltaTime();
				
				switch (mObjectiveState) {
				case Idle:
					if (mObjectiveTimer > 1.0f) {
						anim.play("SlideOff", false);
						mObjectiveState = ObjectiveState.SlideOff;
					}
					break;
				case SlideOff:
					if (!anim.isPlaying("SlideOff")) {
						mObjectivesManager.updateCurrentObjectives();
						
						TextComponent text = (TextComponent)go.findChild("text").getComponentOfType(TextComponent.class);
						TextComponent details = (TextComponent)go.findChild("details").getComponentOfType(TextComponent.class);
						
						text.setText(objective.getTitleText());
						details.setText(objective.getDescriptionText());
						
						anim.play("SlideOn", false);
						mObjectiveState = ObjectiveState.SlideOn;
					}
					break;
				case SlideOn:
					if (!anim.isPlaying("SlideOn")) {
						mObjectiveState = ObjectiveState.Idle;
						mCurrentObjective++;
						if (mCurrentObjective >= mObjectivesText.size()) {
							mCurrentObjective =-1;
						}
					}
					break;
				}
			} else {
				mCurrentObjective++;
				if (mCurrentObjective >= mObjectivesText.size()) {
					mCurrentObjective =-1;
				}
			}
		}
		
		if (mDelayedAction != null) {
			if (mDelayedAction.equalsIgnoreCase("retry")) {
				GamePlay gamePlay = (GamePlay)GameModeManager.Get().getCurrentGameMode();
				gamePlay.getResultsFlow().endResultsFlow();
				
				GameModeManager.Get().requestGameMode("GamePlay");
			} else if (mDelayedAction.equalsIgnoreCase("reset")) {
				ObjectivesManager objectivesManager = (ObjectivesManager)Scene.FindGameObject("Objectives Manager").getComponentOfType(ObjectivesManager.class);
				objectivesManager.clearSaveData();
			}
			
			mDelayedAction = null;
		}
	}
	
	@Override
	protected String onOptionClicked(UIMenuOption pOption) {
		String destinationMenuId = super.onOptionClicked(pOption);
		
		mDelayedAction = pOption.getId();
		
		return destinationMenuId;
	}
}
