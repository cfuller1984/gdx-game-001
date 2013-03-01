package com.cfuller.gdxgame001.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.cfuller.gdxgame001.ui.UIMenu.UIMenuOption;
import com.invertedlogic.componentsystem.Component;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.gameobject.TransformGroup;
import com.invertedlogic.util.Assert;

public class UIMenuSystem extends Component {
	
	static public final String skInvalidMenu = "Invalid Menu";
	static public final String skNavigateBack = "Back";
	
	String mInitialMenuId;
	
	UIMenu mCurrentMenu;
	String mDestinationMenuId = skInvalidMenu;
	
	HashMap<String, UIMenu> mMenuMap = new HashMap<String, UIMenu>();
	Stack<UIMenu> mBackStack = new Stack<UIMenu>();
	
	int mFocusRefCount;
	
	public UIMenuSystem(GameObject pGameObject) {
		super(pGameObject, Component.skInstanceType_Single);
	}
	
	@Override
	public void onDestroy() {
		Assert.assertTrue(mFocusRefCount == 0);
	}

	@Override
	protected void parseFromXml(Element pXmlNode) {
		super.parseFromXml(pXmlNode);
		
		if (pXmlNode.hasChildNodes()) {
			// Parse Children
			NodeList list = pXmlNode.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element childNode = (Element)list.item(i);
					String nodeName = childNode.getNodeName();
					
					if (nodeName.equalsIgnoreCase("UIMenus")) {
						parseMenusFromXml(childNode);
					} else if (nodeName.equalsIgnoreCase("UITransitions")) {
						parseTransitionsFromXml(childNode);
					}
				}
			}
		}
	}
	
	@Override
	protected boolean parseAttributeFromXml(String pName, String pValue) {
		if (pName.equalsIgnoreCase("initial-menu")) {
			mInitialMenuId = pValue;
			return true;
		} else {
			return super.parseAttributeFromXml(pName, pValue);
		}
	}
	
	protected void parseMenusFromXml(Element pXmlNode) {
		NodeList list = pXmlNode.getElementsByTagName("UIMenu");
		
		for (int i = 0; i < list.getLength(); i++) {
			Element menuNode = (Element)list.item(i);
			
			NamedNodeMap attributes = menuNode.getAttributes();
			
			for (int j = 0; j < attributes.getLength(); ++j) {
				Node attribute = attributes.item(j);
				String name = attribute.getNodeName();
				
				if (name.equalsIgnoreCase("id")) {
					String id = attribute.getNodeValue();
					
					// Find the menu object associated with this menu component and make it invisible
					GameObject gameObject = mGameObject.findChild(id);
					
					// Add the menu component to the menu map
					UIMenu menu = (UIMenu)gameObject.getComponentOfType(UIMenu.class);
					menu.parseMenuFromXml(menuNode);
					
					//menu.setEnabled(false);
					menu.mMenuSystem = this;
					
					mMenuMap.put(id, menu);
				} else {
					Assert.fail("Unsupported attribute");
				}
			}
		}
	}
	
	protected void parseTransitionsFromXml(Element pXmlNode) {
		
	}

	@Override
	public void inheritFrom(Component pComponent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setup() {
		// Hide all menus
		
		for (Map.Entry<String, UIMenu> entry : mMenuMap.entrySet()) {
			UIMenu menu = entry.getValue();
			menu.getGameObject().setVisible(false);
		}
		
		// Setup the initial menu
		if (mInitialMenuId != null) {
			mDestinationMenuId = mInitialMenuId;
			internalNavigate();
		}
		
		mFocusRefCount = 1;
		loseFocus();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		if (mDestinationMenuId != null) {
			internalNavigate();
		}
	}
	
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		if (mCurrentMenu != null) {
			// Get the menu option that was touched
			TransformGroup group = (TransformGroup)event.getTarget();
			UIMenuOptionObject menuOptionObject = (UIMenuOptionObject)group.getGameObject().getComponentOfType(UIMenuOptionObject.class);
			
			if (menuOptionObject != null) {
				//UIMenuOption menuOption = mCurrentMenu.mOptionMap.get(menuOptionObject);
				
				//menuOptionObject.processTouchEvent(pSceneTouchEvent, pTouchArea, pTouchAreaLocalX, pTouchAreaLocalY);
				
				// Check for menu option presses
				//if (pSceneTouchEvent.isActionUp()) {
				//	mDestinationMenuId = mCurrentMenu.onOptionClicked(menuOption);
				//}
				
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		if (mCurrentMenu != null) {
			// Get the menu option that was touched
			TransformGroup group = (TransformGroup)event.getTarget();
			UIMenuOptionObject menuOptionObject = (UIMenuOptionObject)group.getGameObject().getComponentOfType(UIMenuOptionObject.class);
			
			if (menuOptionObject != null) {
				UIMenuOption menuOption = mCurrentMenu.mOptionMap.get(menuOptionObject);
				
				//menuOptionObject.processTouchEvent(pSceneTouchEvent, pTouchArea, pTouchAreaLocalX, pTouchAreaLocalY);
				
				// Check for menu option presses
				mDestinationMenuId = mCurrentMenu.onOptionClicked(menuOption);
			}
		}
	}
/*
	@Override
	public boolean onTouchEvent(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		
		if (mCurrentMenu != null) {
			// Get the touch area that was touched
			TouchArea touchArea = (TouchArea)pTouchArea;
			TouchCollider touchCollider = (TouchCollider)touchArea.getUserData();
			
			// Get the menu option that was touched
			UIMenuOptionObject menuOptionObject = (UIMenuOptionObject)touchCollider.getGameObject().getComponentOfType(UIMenuOptionObject.class);
			
			if (menuOptionObject != null) {
				UIMenuOption menuOption = mCurrentMenu.mOptionMap.get(menuOptionObject);
			
				menuOptionObject.processTouchEvent(pSceneTouchEvent, pTouchArea, pTouchAreaLocalX, pTouchAreaLocalY);
			
				// Check for menu option presses
				if (pSceneTouchEvent.isActionUp()) {
					mDestinationMenuId = mCurrentMenu.onOptionClicked(menuOption);
				}
				
				return true;
			}
		}
		
		return false;
	}
	*/
	protected void onBack() {
		mDestinationMenuId = skNavigateBack;
	}
	
	protected int getBackStackSize() {
		return mBackStack.size();
	}
	
	protected void clearBackStack() {
		mBackStack.clear();
	}
	
	public void navigateToMenu(String pMenuId) {
		mDestinationMenuId = pMenuId;
		internalNavigate();
	}
	
	public void gainFocus() {
		mFocusRefCount++;
		
		if (mFocusRefCount == 1) {
			for (Component component : mGameObject.getComponents()) {
				component.enable();
			}
		}
	}
	
	public void loseFocus() {
		Assert.assertTrue(mFocusRefCount > 0);
		mFocusRefCount--;
		
		if (mFocusRefCount == 0) {
			for (Component component : mGameObject.getComponents()) {
				component.disable();
			}
		}
	}
	
	public boolean hasFocus() {
		return mFocusRefCount > 0;
	}
	
	private void internalNavigate() {
		// Leave the current menu
		if (mCurrentMenu != null) {
			mCurrentMenu.onLeaveMenu();
			
			GameObject currentMenuObject = mCurrentMenu.getGameObject();
			currentMenuObject.setVisible(false);
			
			// If we're not going back to the previous menu add this menu
			// to the back stack
			if (!mDestinationMenuId.equalsIgnoreCase(UIMenuSystem.skNavigateBack)) {
				mBackStack.push(mCurrentMenu);
			}
			
			mCurrentMenu = null;
		}
		
		// If there is a destination menu
		if (!mDestinationMenuId.equalsIgnoreCase(UIMenuSystem.skInvalidMenu)) {
			// Navigate to the destination menu
			UIMenu menu = null;
			if (mDestinationMenuId.equalsIgnoreCase(UIMenuSystem.skNavigateBack)) {
				Assert.assertTrue("Back stack is empty.", mBackStack.size() > 0);
				menu = mBackStack.pop();
			} else {
				menu = mMenuMap.get(mDestinationMenuId);
				Assert.assertNotNull("Destination menu doesn't exist.", menu);
			}
			
			GameObject destinationMenuObject = menu.getGameObject();
			destinationMenuObject.setVisible(true);
			
			mCurrentMenu = menu;
			mCurrentMenu.onEnterMenu();
		}
		
		// Reset the destination menu Id
		mDestinationMenuId = null;
	}
}
