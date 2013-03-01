package com.cfuller.gdxgame001.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.invertedlogic.componentsystem.Component;
import com.invertedlogic.componentsystem.TextComponent;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.gameobject.Transform;
import com.invertedlogic.util.Assert;

public class UIMenu extends Component {
	protected class UIMenuOption {
		String mId;
		String mText;
		String mTransitionTo = null;
		
		protected void parseFromXml(Element pXmlNode) {
			NamedNodeMap attributes = pXmlNode.getAttributes();
			
			for (int j = 0; j < attributes.getLength(); ++j) {
				Node attribute = attributes.item(j);
				String name = attribute.getNodeName();
				
				if (name.equalsIgnoreCase("id")) {
					// Id
					mId = attribute.getNodeValue();
				} else if (name.equalsIgnoreCase("text")) {
					// Display text
					mText = attribute.getNodeValue();
					/*if (mGameObject.childExists("text")) {
						GameObject gameObject = mGameObject.findChild("text");
						TextComponent textComponent = (TextComponent)gameObject.getComponentOfType(TextComponent.class);
						textComponent.setText(mText);
					}*/
				} else if (name.equalsIgnoreCase("transition-to")) {
					// The menu this option should transition to
					mTransitionTo = attribute.getNodeValue();
				} else {
					Assert.fail("Unsupported attribute");
				}
			}
		}
		
		protected void inheritFrom(UIMenuOption pMenuOption) {
			
		}
		
		public String getId() {
			return mId;
		}
		
		public String getText() {
			return mText;
		}
		
		public String getTransitionTo() {
			return mTransitionTo;
		}
	}
	
	UIMenuSystem mMenuSystem;
	
	UIMenuOption[] mOptions;
	ArrayList<UIMenuOptionObject> mOptionObjects = new ArrayList<UIMenuOptionObject>();
	HashMap<UIMenuOptionObject, UIMenuOption> mOptionMap = new HashMap<UIMenuOptionObject, UIMenuOption>();
	
	protected String mDelayedAction;
	
	public UIMenu(GameObject pGameObject) {
		super(pGameObject, Component.skInstanceType_Single);
	}
	
	@Override
	public void onDestroy() {
		
	}

	@Override
	protected void parseFromXml(Element pXmlNode) {
		super.parseFromXml(pXmlNode);
	}

	@Override
	public void inheritFrom(Component pComponent) {
		UIMenu inheritMenu = (UIMenu)pComponent;
		
		if (inheritMenu.mOptions != null) {
			mOptions = new UIMenuOption[inheritMenu.mOptions.length];
			
			for (int i = 0; i < inheritMenu.mOptions.length; ++i) {
				mOptions[i].inheritFrom(inheritMenu.mOptions[i]);
			}
		}
		
		mOptionObjects.addAll(inheritMenu.mOptionObjects);
	}

	@Override
	public void setup() {
		collectMenuOptionObjects(mGameObject);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		mOptionMap.clear();
		
		refreshMenuOptions();
	}
	
	@Override
	public void onEnabled() {
		for (UIMenuOptionObject optionObject : mOptionObjects) {
			optionObject.enable();
		}
		//for (UIMenuOption option : mOptions) {
		//	option.enable();
		//}
	}
	
	@Override
	public void onDisabled() {
		for (UIMenuOptionObject optionObject : mOptionObjects) {
			optionObject.disable();
		}
		//for (UIMenuOption option : mOptions) {
		//	option.disable();
		//}
	}
	
	protected void refreshMenuOption(int pOptionObject, int pOptionIndex) {
		UIMenuOptionObject optionObject = mOptionObjects.get(pOptionObject);
		optionObject.getGameObject().setVisible(true);
		
		GameObject gameObject = optionObject.getGameObject().findChild("text");
		TextComponent textComponent = (TextComponent)gameObject.getComponentOfType(TextComponent.class);
		
		textComponent.setText(mOptions[pOptionIndex].getText());
		
		mOptionMap.put(optionObject, mOptions[pOptionIndex]);
	}
	
	protected void refreshMenuOptions() {
		for (UIMenuOptionObject optionObject : mOptionObjects) {
			optionObject.getGameObject().setVisible(false);
		}
		
		for (int i = 0; i < mOptions.length; ++i) {
			refreshMenuOption(i, i);
		}
	}
	
	protected void onEnterMenu() {
		refreshMenuOptions();
	}
	
	protected void onLeaveMenu() {
		
	}
	
	public void onBack() {
		
	}

	protected void parseMenuFromXml(Element pXmlNode) {
		// Parse menu options
		NodeList list = pXmlNode.getElementsByTagName("UIMenuOption");
		
		if (list.getLength() > 0) {
			mOptions = new UIMenuOption[list.getLength()];
			
			for (int i = 0; i < list.getLength(); i++) {
				Element optionNode = (Element)list.item(i);

				mOptions[i] = new UIMenuOption();//(UIMenuOption)mOptionObjects.get(i).getComponentOfType(UIMenuOption.class);
				mOptions[i].parseFromXml(optionNode);
			}
		}
	}
	
	private void collectMenuOptionObjects(GameObject pGameObject) {
		if (pGameObject.getTransform().hasChildTransforms()) {
			for (Transform transform : pGameObject.getTransform().getChildTransforms()) {
				GameObject gameObject = transform.getGameObject();
				UIMenuOptionObject menuOption = (UIMenuOptionObject)gameObject.getComponentOfType(UIMenuOptionObject.class);
				if (menuOption != null) {
					addOptionObject(menuOption);
				} else {
					collectMenuOptionObjects(gameObject);
				}
			}
		}
	}
	
	protected void addOptionObject(UIMenuOptionObject pOptionObject) {
		mOptionObjects.add(pOptionObject);
	}
	
	protected String onOptionClicked(UIMenuOption pOption) {
		return pOption.getTransitionTo();
	}
	
	protected void setOptionText(int pOption, String pText) {
		mOptions[pOption].mText = pText;
	}
}
