package com.cfuller.gdxgame001.ui.dialog;

import java.util.ArrayList;

import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.scene.Scene;

public class Dialog extends GameObject {
	public static final int skDialogOptionOK = (1<<0);
	public static final int skDialogOptionCancel = (1<<1);
	
	public String mMessage;
	public String mTitle;
	public int mOptions;
	public int mPriority;
	EDialogResult mResult = EDialogResult.None;
	EDialogState mState = EDialogState.None;
	
	protected ArrayList<DialogOption> mDialogOptions = new ArrayList<DialogOption>();
	
	public Dialog() {
		super(null);
	}
	
	@Override
	public void setup() {
		
	}
	
	protected void addDialogOption(DialogOption pOption) {
		mDialogOptions.add(pOption);
	}
	/*
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		//Rectangle rect = (Rectangle)pTouchArea;
		//DialogOption clickedOption = (DialogOption)rect.getUserData();
		
		if (pSceneTouchEvent.isActionUp()) {
			mState = EDialogState.Closed;
		}
		
		return true;
	}
	*/
	public void onActivate(Scene pScene) {
		mState = EDialogState.Active;
		
		setVisible(true);
		
		//pScene.attachChild(getTransform().getEntity());
		//pScene.setOnAreaTouchListener(this);
		
		//for (DialogOption option : mDialogOptions) {
		//	pScene.registerTouchArea(option.getTouchArea());
		//}
	}
	
	public void onDeactivate(Scene pScene) {
		mState = EDialogState.Closed;
		
		setVisible(false);
		
		//pScene.setOnAreaTouchListener(null);
		
		//for (DialogOption option : mDialogOptions) {
		//	pScene.unregisterTouchArea(option.getTouchArea());
		//}
	}
	
	public boolean isActive() {
		return mState == EDialogState.Active;
	}
	
	public boolean isClosed() {
		return mState == EDialogState.Closed;
	}
	
	public boolean isCancelled() {
		return mState == EDialogState.Closed
				&& mResult == EDialogResult.Cancel;
	}
	
	public EDialogResult getResult() {
		return mResult;
	}
}
