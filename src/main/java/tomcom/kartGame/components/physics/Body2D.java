package tomcom.kartGame.components.physics;

import com.badlogic.gdx.math.Vector2;

public interface Body2D {

	Vector2 getPosition();

	Vector2 getVelocity(Vector2 p);

	Vector2 toWorldPoint(Vector2 lp);

	Vector2 toLocalPoint(Vector2 wp);

	Vector2 toLocalVector(Vector2 wv);

	void applyForce(Vector2 f); // kraft in world space angeben

	void applyForce(Vector2 f, Vector2 p);

}
