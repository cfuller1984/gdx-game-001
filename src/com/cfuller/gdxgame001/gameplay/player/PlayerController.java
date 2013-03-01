package com.cfuller.gdxgame001.gameplay.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.cfuller.gdxgame001.gamemode.GameModeManager;
import com.cfuller.gdxgame001.gameplay.GamePlay;
import com.cfuller.gdxgame001.gameplay.ai.Guard;
import com.cfuller.gdxgame001.gameplay.ai.GuardManager;
import com.cfuller.gdxgame001.gameplay.level.Level;
import com.invertedlogic.componentsystem.CameraComponent;
import com.invertedlogic.componentsystem.Component;
import com.invertedlogic.gameobject.GameObject;
import com.invertedlogic.physics.PhysicsConstants;
import com.invertedlogic.physics.PhysicsManager;
import com.invertedlogic.physics.RigidBody;
import com.invertedlogic.scene.Scene;
import com.invertedlogic.util.MathHelper;

public class PlayerController extends Component implements PhysicsConstants {
	enum Action
	{
		None,
		Jump,
		Slide,
	}
	
	enum MoveSpeed
	{
		Idle,
		Walking,
		Jogging,
		Running,
		Sprinting,
	}
	
	final float kForwards = 1.0f;
	final float kBackwards =-1.0f;
	
	boolean mTouching;
	boolean mInitialTouch;
	float mInitialTouchX;
	float mInitialTouchY;
	float mTouchX;
	float mTouchY;
	float mTouchSpeedX;
	float mTouchSpeedY;
	float mTouchMovementX;
	float mTouchMovementY;
	
	float mVelocity;
	float mBoostVelocity;
	float mBoostTimer;
	RigidBody mRigidBody;
	
	Vector2 mPreviousPosition;
	
	Player mPlayer;
	Level mLevel;
	CameraComponent mCamera;
	
	GamePlay mGamePlay;
	
	boolean mOnGround;
	
	float initialSpeed;
	float minSpeed;
	float maxSpeed;
	float jumpHeight;
	
	float boostSpeed;
	float boostAccelerationTime;
	
	float maxTouchSpeed;
	
	float mSpeedIncreaseMultiplier;
	boolean mBoost;
	
	Action mQueuedAction;
	Action mAction;
	
	public PlayerController(GameObject pGameObject) {
		super(pGameObject, Component.skInstanceType_Single);
		
		mVelocity = 0.0f;
		mSpeedIncreaseMultiplier = 0.1f;
		
		mQueuedAction = Action.None;
		mAction = Action.None;
	}

	@Override
	public void setup() {
		mGamePlay = (GamePlay)GameModeManager.Get().getCurrentGameMode();
		
		mRigidBody = (RigidBody)mGameObject.getComponentOfType(RigidBody.class);
		mLevel = (Level)Scene.FindGameObject("Level").getComponentOfType(Level.class);
		mPlayer = (Player)mGameObject.getComponentOfType(Player.class);
		mCamera = (CameraComponent)Scene.FindGameObject("Main Camera").getComponentOfType(CameraComponent.class);
		
		mAnimation = mGameObject.getAnimation();
		mPreviousPosition = new Vector2(mTransform.getWorldX(), mTransform.getWorldY());
		
		reset();
	}

	@Override
	public void reset() {
		mTouching = false;
		mInitialTouch = false;
		
		mOnGround = true;
		mVelocity = initialSpeed;
		mBoostVelocity = 0.0f;
		mBoostTimer = 0.0f;
		mBoost = false;
		
		mQueuedAction = Action.None;
		mAction = Action.None;
		
		mAnimation.play("run", false);
	}
	
	@Override
	public void inheritFrom(Component pComponent) {
		super.inheritFrom(pComponent);
		
		PlayerController playerController = (PlayerController)pComponent;
		initialSpeed = playerController.initialSpeed;
		minSpeed = playerController.minSpeed;
		maxSpeed = playerController.maxSpeed;
		jumpHeight = playerController.jumpHeight;
		maxTouchSpeed = playerController.maxTouchSpeed;
		boostSpeed = playerController.boostSpeed;
		boostAccelerationTime = playerController.boostAccelerationTime;
	}

	@Override
	public void update() {
		if (!mGamePlay.isPaused()
				&& !mGamePlay.isGameOver())
		{
			//float boostAcceleration = 10.0f * pSecondsElapsed;
			
			//mVelocity += mSpeedIncreaseMultiplier * pSecondsElapsed;
			
			if (mBoost) {
				mBoostTimer += Gdx.graphics.getDeltaTime();
				if (mBoostTimer > boostAccelerationTime) {
					mBoostTimer = boostAccelerationTime;
				}
				float time = mBoostTimer / boostAccelerationTime;
				time = MathHelper.easeIn(time);
				
				mBoostVelocity = MathHelper.lerp(0.0f, boostSpeed, time);
				
				if (mBoostVelocity > boostSpeed) {
					mBoostVelocity = boostSpeed;
				}
			} else {
				mBoostTimer -= Gdx.graphics.getDeltaTime();
				if (mBoostTimer < 0.0f) {
					mBoostTimer = 0.0f;
				}
				float time = mBoostTimer / boostAccelerationTime;
				time = MathHelper.easeIn(time);
				
				mBoostVelocity = MathHelper.lerp(0.0f, boostSpeed, time);
				
				if (mBoostVelocity < 0.0f) {
					mBoostVelocity = 0.0f;
				}
			}

			float velocity = mVelocity + mBoostVelocity;
			mRigidBody.setLinearVelocity(velocity, mRigidBody.getLinearVelocity().y);
			
			//if (Float.compare(mTransform.getWorldX(), mPreviousPosition.x) == 0) {
			//	mPlayer.die();
			//}
			
			mPreviousPosition = new Vector2(mTransform.getWorldX(), mTransform.getWorldY());
			
			if (mTouching)
			{
				if (mTouchMovementY <-100.0f)
				{
					mQueuedAction = Action.Jump;
					
					endTouch();
				} else if (mTouchMovementY > 100.0f)
				{
					mQueuedAction = Action.Slide;
					
					endTouch();
				}
			}
			
			if (mOnGround) {
				if (mQueuedAction != Action.None)
				{
					switch (mQueuedAction) {
						case Jump:
							jump(jumpHeight);
							break;
						case Slide:
							slide();
						default:
							break;
					}
					
					mAction = mQueuedAction;
					mQueuedAction = Action.None;
				}
			}
			
			switch (mAction) {
			case Jump:
				if (mOnGround) {
					mAction = Action.None;
				}
				break;
			case Slide:
				if (!mAnimation.isPlaying("slide")) {
					mAnimation.play("run", false);
					mAction = Action.None;
				}
				break;
			}
		}
		
		mInitialTouch = false;
	}
	
	@Override
	public void render() {
		super.render();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBeginCollision(RigidBody pRigidBody, Contact pContact) {
		processCollision(pRigidBody, pContact, true);
	}

	@Override
	public void onEndCollision(RigidBody pRigidBody, Contact pContact) {
		mOnGround = false;
	}

	@Override
	public void onCollision(RigidBody pRigidBody, Contact pContact) {
		processCollision(pRigidBody, pContact, false);
	}
	
	private void processCollision(RigidBody pRigidBody, Contact pContact, boolean pInitialContact) {
		RigidBody rigidBody = (RigidBody)mGameObject.getComponentOfType(RigidBody.class);
		Vector2 velocity = rigidBody.getLinearVelocity();
		Vector2 normal = pContact.getWorldManifold().getNormal();
		
		if (Math.abs(normal.y) > 0.5f
				&& velocity.y >= 0.0f) {
			mOnGround = true;
		}

		GameObject gameObject = pRigidBody.getGameObject();
		
		if (mBoost) {
			if (!gameObject.getTag().equalsIgnoreCase("floor")) {
				pContact.setEnabled(false);
			}
		} else {
			if (gameObject.getTag().equalsIgnoreCase("deadzone")) {
				mPlayer.die();
			} else if (gameObject.getTag().equalsIgnoreCase("guard")) {
				pContact.setEnabled(false);
				
				if (pInitialContact) {
					Guard guard = (Guard)gameObject.getComponentOfType(Guard.class);
					GuardManager guardManager = (GuardManager)Scene.FindGameObject("Guard Manager").getComponentOfType(GuardManager.class);
					guardManager.killGuard(guard);
				}
			} else if (gameObject.getTag().equalsIgnoreCase("floor")) {
				// On ground
			} else {
				// Collisions with boxes, etc.
				if (!pRigidBody.isSensor()) {
					if (Math.abs(normal.y) < 1.0f) {
						//rigidBody.setLinearVelocity(0.0f, velocity.y);
						mPlayer.die();
					}
				}
			}
		}
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (!mGamePlay.isGameOver()) {
			// Store the initial touch position
			mInitialTouchX = screenX;
			mInitialTouchY = screenY;
			
			mInitialTouch = true;
			
			// Touch speed is zero
			mTouchSpeedX = 0.0f;
			mTouchSpeedY = 0.0f;
			
			mTouchMovementX = 0.0f;
			mTouchMovementY = 0.0f;
			
			mTouchX = screenX;
			mTouchY = screenY;
			
			mTouching = true;
		}
		
		return false;
	}
	
	public void touchDragged(int screenX, int screenY, int pointer) {
		// Calculate the touch speed
		mTouchSpeedX = screenX - mTouchX;
		mTouchSpeedY = screenY - mTouchY;
		
		mTouchMovementX += mTouchSpeedX;
		mTouchMovementY += mTouchSpeedY;
		
		mTouchX = screenX;
		mTouchY = screenY;
		
		mInitialTouch = false;
	}
	
	public void touchUp(int screenX, int screenY, int pointer, int button) {
		endTouch();
	}
	
	void endTouch()
	{
		mTouchMovementX = 0.0f;
		mTouchMovementY = 0.0f;
		
		mTouchSpeedX = 0.0f;
		mTouchSpeedY = 0.0f;
		
		mTouching = false;
	}
	
	public void beginBoost() {
		mBoost = true;
	}
	
	public void endBoost() {
		mBoost = false;
	}
	
	private float calculateJumpVelocity(float pHeight) {
		return (float) -Math.sqrt(2.0f * (pHeight/PIXEL_TO_METER_RATIO_DEFAULT) * PhysicsManager.Get().getPhysicsWorld().getGravity().y);
	}
	
	public void jump(float pHeight) {
		if (mOnGround) {
			mOnGround = false;
			
			Vector2 velocity = mRigidBody.getLinearVelocity();
			mRigidBody.setLinearVelocity(velocity.x, calculateJumpVelocity(pHeight));
		}
	}
	
	public void slide() {
		mGameObject.getAnimation().play("slide", false);
	}
}
