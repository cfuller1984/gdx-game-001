package com.cfuller.gdxgame001.ui;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.invertedlogic.componentsystem.Component;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.util.Assert;

public class UIMenuOptionObject extends Component {
	String mId;
	//TouchCollider mTouchCollider;
	
	public UIMenuOptionObject(GameObject pGameObject) {
		super(pGameObject, Component.skInstanceType_Single);
	}
	
	@Override
	public void onDestroy() {
		
	}

	@Override
	protected void parseFromXml(Element pXmlNode) {
		
	}

	@Override
	public void inheritFrom(Component pComponent) {
		UIMenuOptionObject inheritOption = (UIMenuOptionObject)pComponent;
		
		mId = inheritOption.mId;
	}

	@Override
	public void setup() {
		//mTouchCollider = (TouchCollider)mGameObject.getComponentOfType(TouchCollider.class);
		/* UIMenu menu = null;
		
		Transform transform = mTransform.getParent();
		while (transform != null) {
			menu = (UIMenu)transform.getGameObject().getComponentOfType(UIMenu.class);
			if (menu != null) {
				break;
			} else {
				transform = transform.getParent();
			}
		}
		
		Assert.assertNotNull("No menu found in the hierarchy above this menu option.", menu);
		menu.addOptionObject(mGameObject);*/
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	protected void parseMenuOptionFromXml(Element pXmlNode) {
		NamedNodeMap attributes = pXmlNode.getAttributes();
		
		for (int j = 0; j < attributes.getLength(); ++j) {
			Node attribute = attributes.item(j);
			String name = attribute.getNodeName();
			
			if (name.equalsIgnoreCase("id")) {
				// Id
				mId = attribute.getNodeValue();
			} else {
				Assert.fail("Unsupported attribute");
			}
		}
	}
	/*
	public void processTouchEvent(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		//TouchArea touchArea = (TouchArea)pTouchArea;
		//TouchCollider touchCollider = (TouchCollider)touchArea.getUserData();
		
		//Assert.assertTrue(touchCollider == mTouchCollider);
		
		if (pSceneTouchEvent.isActionDown()) {
			mGameObject.playAnimation("HoverOn", true);
		} else if (pSceneTouchEvent.isActionUp()) {
			mGameObject.playAnimation("HoverOff", true);
		}
	}
*/
	public String getId() {
		return mId;
	}
}
