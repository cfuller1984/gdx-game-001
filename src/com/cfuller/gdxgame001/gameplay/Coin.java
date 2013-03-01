package com.cfuller.gdxgame001.gameplay;

import com.invertedlogic.componentsystem.Component;
import com.invertedlogic.gameobject.GameObject;

public class Coin extends Component {
	int value;
	
	public Coin(GameObject pGameObject) {
		super(pGameObject, Component.skInstanceType_Single);
	}

	@Override
	public void setup() {
	}

	@Override
	public void inheritFrom(Component pComponent) {
		Coin coin = (Coin)pComponent;
		value = coin.value;
	}

	@Override
	public void reset() {
	}

	@Override
	public void update() {
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
	
	public int getCoinValue() {
		return value;
	}
}
