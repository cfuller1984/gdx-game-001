package com.cfuller.gdxgame001.gameplay;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Preferences;
import com.invertedlogic.componentsystem.Component;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.util.Assert;

public class ObjectivesManager extends Component {

	final int kCurrentObjectives = 3;
	
	public abstract class Objective
	{
		protected String mId;
		
		public Objective(String pId) {
			mId = pId;
		}
		
		public String getId() {
			return mId;
		}
		
		public abstract boolean isComplete();
		public abstract String getTitleText();
		public abstract String getDescriptionText();
		public abstract float getProgress();
		
		public void onCoinCollected(Coin pCoin) {}
		
		public abstract void save();
		public abstract void load();
		public final void clearSaveData() {
			Preferences prefs = SaveData.Get().beginSave(mId);
			prefs.clear();
			SaveData.Get().finaliseSave(prefs);
		}
	}
	
	public class CollectCoinsObjective extends Objective
	{
		public int mCoinsRequired;
		public int mCoinsCollected;
		
		public CollectCoinsObjective(int pCoinsRequired) {
			super("Collect " + pCoinsRequired + " coins");
			
			mCoinsRequired = pCoinsRequired;
			mCoinsCollected = 0;
		}
		
		@Override
		public boolean isComplete() {
			return mCoinsCollected >= mCoinsRequired;
		}

		@Override
		public String getTitleText() {
			return "Collect " + mCoinsRequired + " coins.";
		}

		@Override
		public String getDescriptionText() {
			return "You've collected " + mCoinsCollected + " / " + mCoinsRequired + " coins.";
		}

		@Override
		public float getProgress() {
			return mCoinsCollected / (float)mCoinsRequired;
		}
		
		@Override
		public void onCoinCollected(Coin pCoin) {
			mCoinsCollected += pCoin.getCoinValue();
			if (mCoinsCollected > mCoinsRequired) {
				mCoinsCollected = mCoinsRequired;
			}
		}
		
		@Override
		public void save() {
			Preferences prefs = SaveData.Get().beginSave(mId);
			
			prefs.putInteger("mCoinsCollected", mCoinsCollected);
			
			SaveData.Get().finaliseSave(prefs);
		}
		
		@Override
		public void load() {
			Preferences prefs = SaveData.Get().beginLoad(mId);
			
			mCoinsCollected = prefs.getInteger("mCoinsCollected");
		}
	}
	
	ArrayList<Objective> mObjectives;
	ArrayList<Objective> mCurrentObjectives;
	
	public ObjectivesManager(GameObject pGameObject) {
		super(pGameObject, Component.skInstanceType_Single);
		
		mObjectives = new ArrayList<Objective>();
		mObjectives.add(new CollectCoinsObjective(10));
		mObjectives.add(new CollectCoinsObjective(20));
		mObjectives.add(new CollectCoinsObjective(50));
		mObjectives.add(new CollectCoinsObjective(100));
		mObjectives.add(new CollectCoinsObjective(200));
		mObjectives.add(new CollectCoinsObjective(250));
		mObjectives.add(new CollectCoinsObjective(300));
		mObjectives.add(new CollectCoinsObjective(500));
		mObjectives.add(new CollectCoinsObjective(1000));
		
		mCurrentObjectives = new ArrayList<Objective>(kCurrentObjectives);
	}

	@Override
	public void setup() {
		load();
	}

	@Override
	public void reset() {
	}
	
	public void clearSaveData() {
		//SharedPreferences.Editor editor = SaveData.Get().beginSave("Objectives");
		//editor.clear();
		//editor.commit();
		
		for (Objective objective : mObjectives) {
			objective.clearSaveData();
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Objective> getCurrentObjectives() {
		return mCurrentObjectives;
	}
	
	public boolean isAnyCurrentObjectiveComplete() {
		for (Objective objective : mCurrentObjectives) {
			if (objective.isComplete()) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean allObjectivesComplete() {
		for (Objective objective : mObjectives) {
			if (!objective.isComplete()) {
				return false;
			}
		}
		
		return true;
	}
	
	public void completeObjective(Objective pObjective) {
		mCurrentObjectives.remove(pObjective);
	}
	
	public void updateCurrentObjectives() {
		// If there aren't enough current objectives
		// keep adding them until there are enough
		boolean addedObjective = false;
		do {
			int index = 0;
			for (Iterator<Objective> it = mCurrentObjectives.iterator(); it.hasNext(); index++) {
				Objective objective = it.next();
				
				if (objective.isComplete()) {
					it.remove();
					break;
				}
			}
			
			if (mCurrentObjectives.size() < 3) {
				for (Objective objective : mObjectives) {
					if (!objective.isComplete()
							&& !mCurrentObjectives.contains(objective)) {
						mCurrentObjectives.add(index, objective);
						addedObjective = true;
						break;
					}
				}
			}
		} while (mCurrentObjectives.size() < 3 && addedObjective);
	}
	
	public void save() {
		int index = 0;
		Preferences prefs = SaveData.Get().beginSave("Objectives");
		
		for (Iterator<Objective> it = mCurrentObjectives.iterator(); it.hasNext(); index++) {
			Objective currentObjective = it.next();
			
			// Iterate all objectives looking for a match
			for (int i = 0; i < mObjectives.size(); ++i) {
				Objective objective = mObjectives.get(i);
				
				if (objective == currentObjective) {
					prefs.putInteger("objective" + index, i);
					break;
				}
			}
		}
			
		SaveData.Get().finaliseSave(prefs);
		
		// Save each objective state
		for (Objective objective : mObjectives) {
			objective.save();
		}
	}
	
	public void load() {
		mCurrentObjectives.clear();
		
		// Load each objective state
		for (Objective objective : mObjectives) {
			objective.load();
		}

		Preferences prefs = SaveData.Get().beginLoad("Objectives");
		
		int currentObjectiveCount = kCurrentObjectives;
		for (int i = 0; i < currentObjectiveCount; ++i) {
			String key = "objective" + i;
			if (prefs.contains(key)) {
				int index = prefs.getInteger(key, -1);
				Assert.assertTrue(index >-1 && index < mObjectives.size());
				
				Objective objective = mObjectives.get(index);
				Assert.assertNotNull(objective);
				
				mCurrentObjectives.add(objective);
			}
		}
		
		updateCurrentObjectives();
	}
	
	public void onCoinCollected(Coin pCoin) {
		for (Objective objective : mCurrentObjectives) {
			objective.onCoinCollected(pCoin);
		}
	}
}
