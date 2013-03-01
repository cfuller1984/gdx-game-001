package com.cfuller.gdxgame001.results.menus;

import java.util.ArrayList;

import com.invertedlogic.componentsystem.TextComponent;
import com.cfuller.gdxgame001.gameplay.StatsManager;
import com.cfuller.gdxgame001.ui.UIMenu;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.gameobject.Transform;

public class UIStatsMenu extends UIMenu {

	StatsManager mStatsManager;
	ArrayList<GameObject> mStatsText;
	
	public UIStatsMenu(GameObject pGameObject) {
		super(pGameObject);
	}

	@Override
	public void setup() {
		super.setup();
		
		mStatsManager = StatsManager.Get();
		mStatsText = new ArrayList<GameObject>();
		
		int i = 0;
		Transform t = null;
		
		do {
			t = mTransform.findChildTransform("stat" + String.format("%2s", i++).replace(' ', '0'));
			if (t != null) {
				mStatsText.add(t.getGameObject());
			}
		} while (t != null);
	}
	
	@Override
	protected void onEnterMenu() {
		/*ArrayList<ObjectivesManager.Objective> objectives = mObjectivesManager.getCurrentObjectives();
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
		}*/
		
		// Distance travelled
		{
			GameObject go = mTransform.findChildTransform("stat_distance").getGameObject();
			
			TextComponent title = (TextComponent)go.findChild("title").getComponentOfType(TextComponent.class);
			TextComponent details = (TextComponent)go.findChild("details").getComponentOfType(TextComponent.class);
			
			title.setText("Distance Run");
			details.setText(Math.round(StatsManager.Get().getDistanceRun()) + "m");
		}
		
		// Coins collected
		{
			GameObject go = mTransform.findChildTransform("stat_coins").getGameObject();
			
			TextComponent title = (TextComponent)go.findChild("title").getComponentOfType(TextComponent.class);
			TextComponent details = (TextComponent)go.findChild("details").getComponentOfType(TextComponent.class);
			
			title.setText("Coins Collected");
			details.setText(StatsManager.Get().getCoinsCollected() + " coins.");
		}
		
		// Time played
		{
			GameObject go = mTransform.findChildTransform("stat_time").getGameObject();
			
			TextComponent title = (TextComponent)go.findChild("title").getComponentOfType(TextComponent.class);
			TextComponent details = (TextComponent)go.findChild("details").getComponentOfType(TextComponent.class);
			
			title.setText("Time Played");
			int timeInSeconds = Math.round(StatsManager.Get().getTimePlayed());
			int totalMinutes = timeInSeconds / 60;
			int hours = totalMinutes / 60;
			int minutes = totalMinutes % 60;
			int seconds = timeInSeconds % 60;
			details.setText(hours + " hours, " + minutes + " mins, " + seconds + " seconds.");
		}
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	protected String onOptionClicked(UIMenuOption pOption) {
		String destinationMenuId = super.onOptionClicked(pOption);
		
		mDelayedAction = pOption.getId();
		
		return destinationMenuId;
	}
}
