package com.cfuller.gdxgame001.ui.dialog;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.invertedlogic.scene.Scene;

public class DialogManager {
	static DialogManager smThis;
	
	Stack<Dialog> mDialogQueue = new Stack<Dialog>();
	ArrayList<Dialog> mDialogRemoveQueue = new ArrayList<Dialog>();
	Scene mScene;
	
	public static void Create() {
		smThis = new DialogManager();
	}
	
	public static DialogManager Get() {
		return smThis;
	}
	
	public DialogManager() {
		//mScene = new Scene();
		//mScene.setBackgroundEnabled(false);
		
		//mScene.registerUpdateHandler(this);
	}
	
	public void update() {
		Dialog dialog = mDialogQueue.peek();
		if (dialog != null
				&& !dialog.isActive()
				&& !mDialogRemoveQueue.contains(dialog)) {
			removeCurrentDialog();
		}
		
		Iterator<Dialog> iter = mDialogRemoveQueue.iterator();
		while (iter.hasNext()) {
			Dialog remove = (Dialog)iter.next();
			if (!remove.isVisible()) {
				//mScene.detachChild(remove.getTransform().getEntity());
				mDialogQueue.remove(remove);
				iter.remove();
				
				//EngineData.getActiveScene().back();
			}
		}
	}
	
	public void AddDialog(Dialog pDialog) {
		for (int i = 0; i < mDialogQueue.size(); ++i) {
			Dialog iter = mDialogQueue.get(i);
			
			if (iter.mPriority < pDialog.mPriority) {
				mDialogQueue.add(i, pDialog);
				
				// If this dialog has just been inserted activate it
				if (i == 0) {
					if (mDialogQueue.size() >= 2) {
						deactivateCurrentDialog();
					}
					
					activateCurrentDialog();
				}
				
				return;
			}
		}
		
		mDialogQueue.push(pDialog);
		if (mDialogQueue.size() == 1) {
			activateCurrentDialog();
		}
	}
	
	public void activateCurrentDialog() {
		Dialog dialog = mDialogQueue.peek();
		dialog.onActivate(mScene);
		
		//EngineData.getActiveScene().setChildScene(mScene, false, false, true);
	}
	
	public void deactivateCurrentDialog() {
		Dialog dialog = mDialogQueue.peek();
		dialog.onDeactivate(mScene);
		
		//EngineData.getActiveScene().back();
	}
	
	public void removeDialog(Dialog pDialog) {
		for (int i = 0; i < mDialogQueue.size(); ++i) {
			Dialog iter = mDialogQueue.get(i);
			
			if (iter == pDialog) {
				if (i == 0) {
					removeCurrentDialog();
				} else {
					mDialogQueue.remove(i);
				}
				
				break;
			}
		}
	}
	
	public void removeCurrentDialog() {
		deactivateCurrentDialog();
		mDialogRemoveQueue.add(mDialogQueue.peek());
		
		//displayNextDialog();
	}
	
	public boolean isDialogActive(Dialog pDialog) {
		return mDialogQueue.size() > 0 && mDialogQueue.peek() == pDialog;
	}
	
	public boolean isAnyDialogActive() {
		return mDialogQueue.size() > 0;
	}
	
	protected void displayNextDialog() {
		if (mDialogQueue.size() > 0) {
			activateCurrentDialog();
		}
	}
	
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		/*if (pKeyCode == KeyEvent.KEYCODE_MENU
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			Assert.assertTrue(mDialogQueue.size() > 0);
			removeCurrentDialog();
			
			return true;
		}
		*/
		return false;
	}
}
