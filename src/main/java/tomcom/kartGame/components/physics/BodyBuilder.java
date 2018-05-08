package tomcom.kartGame.components.physics;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class BodyBuilder {
	public static BodyDef createDynamicBodyDef(float x, float y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		return bodyDef;
	}

	public static FixtureDef rectangularFixtureDef(float density, float w,
			float h) {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(w, h);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = density; // mass
		return fixtureDef;
		// TODO: shaoe.dispose()?
	}
}
