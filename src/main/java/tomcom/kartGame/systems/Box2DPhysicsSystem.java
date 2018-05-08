package tomcom.kartGame.systems;

import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.components.collision.CircleColliderComponent;
import tomcom.kartGame.components.collision.ColliderComponent;
import tomcom.kartGame.components.collision.RectangleColliderComponent;
import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.game.GameConfig;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Box2DPhysicsSystem extends EntitySystem {

	private static final Family FAMILY = Family.all(Body2DComponent.class)
			.get();

	private World world;

	private float timeAccumulator = 0f;

	private static float STEP_SIZE = 1 / 60f;

	public Box2DPhysicsSystem() {
		world = new World(new Vector2(GameConfig.WORLD_GRAVITY_X,
				GameConfig.WORLD_GRAVITY_Y), true);
		world.setAutoClearForces(false);
	}

	public World getWorld() {
		return world;
	}

	@Override
	public void update(float deltaTime) {
		timeAccumulator += deltaTime;
		while (timeAccumulator > STEP_SIZE) {
			world.step(STEP_SIZE, 4, 4);
			timeAccumulator -= STEP_SIZE;
		}
		world.clearForces();
		super.update(deltaTime);
	}

	@Override
	public void addedToEngine(Engine engine) {
		engine.addEntityListener(FAMILY, new EntityListener() {

			@Override
			public void entityRemoved(Entity entity) {
				Gdx.app.log("Box2DPhysicsSystem", "Entity removed from System");
				Body2DComponent body = entity
						.getComponent(Body2DComponent.class);

				world.destroyBody(body.getBody());
				body = null;
			}

			@Override
			public void entityAdded(Entity entity) {
				Gdx.app.log("Box2DPhysicsSystem", "Entity added to System");

				Body2DComponent body = entity
						.getComponent(Body2DComponent.class);

				BodyDef bodyDef = new BodyDef();
				if (body.isDynamic()) {
					bodyDef.type = BodyDef.BodyType.DynamicBody;
				} else {
					bodyDef.type = BodyDef.BodyType.StaticBody;
				}

				PivotComponent pivot = entity
						.getComponent(PivotComponent.class);

				bodyDef.position.set(pivot.getPos().x, pivot.getPos().y);

				body.setBody(world.createBody(bodyDef));

				ColliderComponent collider = entity
						.getComponent(ColliderComponent.class);

				if (collider != null) {
					FixtureDef fixtureDef = new FixtureDef();

					fixtureDef.density = collider.getDensity();
					fixtureDef.friction = collider.getFriction();
					fixtureDef.restitution = collider.getRestitution();

					Shape shape = null;

					if (collider instanceof RectangleColliderComponent) {
						RectangleColliderComponent rect = (RectangleColliderComponent) collider;
						PolygonShape rectangle = new PolygonShape();
						rectangle.setAsBox(rect.getWidth(), rect.getHeight());
						shape = rectangle;
						rectangle.dispose();
					} else if (collider instanceof CircleColliderComponent) {
						CircleColliderComponent circ = (CircleColliderComponent) collider;
						CircleShape circle = new CircleShape();
						// TODO: is his necessary?
						circle.setPosition(pivot.getPos());
						circle.setRadius(circ.getRadius());
						shape = circle;
						circle.dispose();
					}

					fixtureDef.shape = shape;

					body.getBody().createFixture(fixtureDef);

					shape.dispose();
				}

			}
		});
	}
}
