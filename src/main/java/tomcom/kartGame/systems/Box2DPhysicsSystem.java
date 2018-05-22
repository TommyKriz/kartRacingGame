package tomcom.kartGame.systems;

import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.components.collision.CircleCollider;
import tomcom.kartGame.components.collision.Collider;
import tomcom.kartGame.components.collision.ColliderComponent;
import tomcom.kartGame.components.collision.RectangleCollider;
import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.game.GameConfig;

import com.badlogic.ashley.core.ComponentMapper;
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

	private static final Family FAMILY = Family.all(Body2DComponent.class,
			PivotComponent.class).get();

	ComponentMapper<PivotComponent> pm = ComponentMapper
			.getFor(PivotComponent.class);
	ComponentMapper<Body2DComponent> bm = ComponentMapper
			.getFor(Body2DComponent.class);
	ComponentMapper<ColliderComponent> cm = ComponentMapper
			.getFor(ColliderComponent.class);

	private static float STEP_SIZE = 1 / 60f;

	private static final int WORLD_POSITION_ITERATIONS = 4;

	private static final int WORLD_VELOCITY_ITERATIONS = 4;

	private World world;

	private float timeAccumulator = 0f;

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
			world.step(STEP_SIZE, WORLD_VELOCITY_ITERATIONS,
					WORLD_POSITION_ITERATIONS);
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
				Body2DComponent body = bm.get(entity);
				world.destroyBody(body.getBody());
				body = null;
			}

			@Override
			public void entityAdded(Entity entity) {
				Gdx.app.log("Box2DPhysicsSystem", "Entity added to System");

				Body2DComponent body = bm.get(entity);

				BodyDef bodyDef = new BodyDef();
				if (body.isDynamic()) {
					bodyDef.type = BodyDef.BodyType.DynamicBody;
				} else {
					bodyDef.type = BodyDef.BodyType.StaticBody;
				}

				// TODO: prevent null ?
				bodyDef.linearDamping = body.getDamping();

				PivotComponent pivot = pm.get(entity);

				bodyDef.position.set(pivot.getPos().x, pivot.getPos().y);

				body.setBody(world.createBody(bodyDef));

				ColliderComponent colliderComponent = cm.get(entity);

				if (colliderComponent != null) {
					buildColliders(body, pivot, colliderComponent.getCollider());
				}

			}

			private void buildColliders(Body2DComponent body,
					PivotComponent pivot, Collider collider) {

				FixtureDef fixtureDef = new FixtureDef();

				fixtureDef.density = collider.getDensity();
				fixtureDef.friction = collider.getFriction();
				fixtureDef.restitution = collider.getRestitution();

				Shape shape = null;

				if (collider instanceof RectangleCollider) {
					Gdx.app.log("Box2DPhysicsSystem", "Rectangle Collider");
					RectangleCollider rect = (RectangleCollider) collider;
					PolygonShape rectangle = new PolygonShape();
					rectangle.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
					shape = rectangle;
				} else if (collider instanceof CircleCollider) {
					Gdx.app.log("Box2DPhysicsSystem", "Circle Collider");
					CircleCollider circ = (CircleCollider) collider;
					CircleShape circle = new CircleShape();
					circle.setRadius(circ.getRadius());
					shape = circle;
				}
				fixtureDef.shape = shape;
				body.getBody().createFixture(fixtureDef);
				shape.dispose();
			}
		});
	}
}
