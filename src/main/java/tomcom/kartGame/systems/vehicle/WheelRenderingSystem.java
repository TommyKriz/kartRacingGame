package tomcom.kartGame.systems.vehicle;

import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.components.vehicle.VehicleComponent;
import tomcom.kartGame.components.vehicle.Wheel;
import tomcom.kartGame.scenes.EntityConfig;
import tomcom.kartGame.scenes.TexturePaths;
import tomcom.kartGame.systems.RenderingSystem;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class WheelRenderingSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(VehicleComponent.class,
			Body2DComponent.class).get();

	ComponentMapper<VehicleComponent> vm = ComponentMapper
			.getFor(VehicleComponent.class);
	ComponentMapper<Body2DComponent> bm = ComponentMapper
			.getFor(Body2DComponent.class);

	private SpriteBatch batch;

	private Sprite wheelSprite;

	public WheelRenderingSystem() {
		super(FAMILY);
		wheelSprite = new Sprite(new Texture(
				Gdx.files.internal(TexturePaths.WHEEL)));
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

		Vector2 entityPivot = bm.get(entity).getPosition();

		float wx;
		float wy;

		for (Wheel w : vehicle.getDrivenWheels()) {

			// TODO: replace with chassis.toWorldPoint(xOff,yOff) for rotation !
			wx = entityPivot.x + w.xOffsetFromPivot;
			wy = entityPivot.y + w.yOffsetFromPivot;

			wheelSprite.setRotation(w.orientation);
			wheelSprite.setPosition(wx - EntityConfig.WHEEL_WIDTH / 2, wy
					- EntityConfig.WHEEL_HEIGHT / 2);
			wheelSprite.draw(batch);

		}
		for (Wheel w : vehicle.getSteerableWheels()) {

			// TODO: replace with chassis.toWorldPoint(xOff,yOff) for rotation !
			wx = entityPivot.x + w.xOffsetFromPivot;
			wy = entityPivot.y + w.yOffsetFromPivot;

			wheelSprite.setRotation(w.orientation - 90);
			wheelSprite.setPosition(wx - EntityConfig.WHEEL_WIDTH / 2, wy
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
