package tomcom.kartGame.components.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Body2DComponent implements Body2D, Component {

	private Body body;

	private boolean dynamic;

	private float damping;

	public float getDamping() {
		return damping;
	}

	public Body2DComponent setDamping(float damping) {
		this.damping = damping;
		return this;
	}

	public Body2DComponent setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
		return this;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	@Override
	public Vector2 getVelocity(Vector2 p) {
		return body.getLinearVelocityFromWorldPoint(p);
	}

	@Override
	public Vector2 toLocalPoint(Vector2 wp) {
		return body.getLocalPoint(wp);
	}

	@Override
	public Vector2 toLocalVector(Vector2 wv) {
		return body.getLocalVector(wv);
	}

	@Override
	public void applyForce(Vector2 f) {
		body.applyForceToCenter(f, true);
	}

	@Override
	public void applyForce(Vector2 f, Vector2 p) {
		body.applyForce(f, p, true);
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

	/**
	 * USED in Box2DPhysicsSystem when body is destroyed
	 * 
	 * @return
	 */
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	@Override
	public Vector2 toWorldPoint(Vector2 lp) {
		return body.getWorldPoint(lp).cpy();
	}

	public float getAngleInDegrees() {
		return body.getAngle() / MathUtils.degreesToRadians;
	}

}
