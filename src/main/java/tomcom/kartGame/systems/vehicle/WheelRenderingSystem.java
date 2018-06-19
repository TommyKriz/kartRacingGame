package tomcom.kartGame.systems.vehicle;

import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.components.vehicle.VehicleComponent;
import tomcom.kartGame.components.vehicle.Wheel;
import tomcom.kartGame.config.EntityConfig;
import tomcom.kartGame.systems.RenderingSystem;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class WheelRenderingSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(VehicleComponent.class,
			Body2DComponent.class).get();

	ComponentMapper<VehicleComponent> vm = ComponentMapper
			.getFor(VehicleComponent.class);
	ComponentMapper<Body2DComponent> bm = ComponentMapper
			.getFor(Body2DComponent.class);

	private SpriteBatch batch;

	private Sprite wheelSprite;

	public WheelRenderingSystem(Texture wheelTexture) {
		super(FAMILY);
		wheelSprite = new Sprite(wheelTexture);
		wheelSprite
				.setSize(EntityConfig.WHEEL_WIDTH, EntityConfig.WHEEL_HEIGHT);
		wheelSprite.setOriginCenter();
	}

	@Override
	public void update(float deltaTime) {
		batch.begin();
		super.update(deltaTime);
		batch.end();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		VehicleComponent vehicle = vm.get(entity);

		Body2DComponent chassis = bm.get(entity);

		Vector2 wheelPivot;

		for (Wheel w : vehicle.getDrivenWheels()) {

			wheelPivot = chassis.toWorldPoint(w.offsetFromPivot);

			wheelSprite.setRotation(w.orientation);
			wheelSprite.setPosition(
					wheelPivot.x - EntityConfig.WHEEL_WIDTH / 2, wheelPivot.y
							- EntityConfig.WHEEL_HEIGHT / 2);
			wheelSprite.draw(batch);

		}
		for (Wheel w : vehicle.getSteerableWheels()) {

			wheelPivot = chassis.toWorldPoint(w.offsetFromPivot);

			wheelSprite.setRotation(w.orientation - 90);
			wheelSprite.setPosition(
					wheelPivot.x - EntityConfig.WHEEL_WIDTH / 2, wheelPivot.y
							- EntityConfig.WHEEL_HEIGHT / 2);
			wheelSprite.draw(batch);

		}

	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		batch = engine.getSystem(RenderingSystem.class).getBatch();
	}

}
