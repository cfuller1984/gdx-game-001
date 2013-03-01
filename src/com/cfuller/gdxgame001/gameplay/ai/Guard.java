package com.cfuller.gdxgame001.gameplay.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.invertedlogic.componentsystem.Component;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.physics.RigidBody;

public class Guard extends Component {
	RigidBody mRigidBody;
	float mVelocity;
	
	public Guard(GameObject pGameObject) {
		super(pGameObject, Component.skInstanceType_Single);
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		mRigidBody = (RigidBody)mGameObject.getComponentOfType(RigidBody.class);
		mVelocity = MathUtils.random(-2.0f, -4.0f);
	}

	@Override
	public void inheritFrom(Component pComponent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		mRigidBody.setLinearVelocity(mVelocity, mRigidBody.getLinearVelocity().y);
	}

	@Override
	public void onDestroy() {
	}
	
	@Override
	public void onCollision(RigidBody pRigidBody, Contact pContact) {
		Vector2 normal = pContact.getWorldManifold().getNormal();
		if (Math.abs(normal.x) > 0.5f) {
			// change direction
			mVelocity *=-1.0f;
		}
	}
}
