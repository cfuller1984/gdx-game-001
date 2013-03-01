package com.cfuller.gdxgame001.ui.dialog;

import org.w3c.dom.Element;

import com.badlogic.gdx.math.Rectangle;
import com.invertedlogic.gameobject.GameObject;

public class DialogOption extends GameObject {
	Rectangle mTouchArea;
	
	public DialogOption() {
		super(null);
	}
	
	@Override
	public void setup() {
		super.setup();
		
		mTouchArea = new Rectangle(0.0f, 0.0f, mTransform.getSizeX(), mTransform.getSizeY());
		//mTouchArea.setVisible(false);
		//mTouchArea.setUserData(this);
		//mTransform.attachEntity(mTouchArea);
	}
	
	@Override
	protected void parseFromXml(Element pXmlNode) {
		super.parseFromXml(pXmlNode);
		
		GameObject parent = mTransform.getParent().getGameObject();
		while (parent != null) {
			if (Dialog.class.isAssignableFrom(parent.getClass())) {
				((Dialog)parent).addDialogOption(this);
				break;
			}
			
			parent = parent.getTransform().getParent().getGameObject();
		}
	}
	
	protected Rectangle getTouchArea() {
		return mTouchArea;
	}
}
