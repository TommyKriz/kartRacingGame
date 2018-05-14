package tomcom.kartGame.systems.vehicle;

import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.components.vehicle.VehicleComponent;
import tomcom.kartGame.components.vehicle.Wheel;
import tomcom.kartGame.game.GameConfig;
import tomcom.kartGame.systems.CameraSystem;
import tomcom.kartGame.systems.RenderingSystem;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class VehicleSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(VehicleComponent.class,
			Body2DComponent.class).get();

	ComponentMapper<VehicleComponent> vm = ComponentMapper
			.getFor(VehicleComponent.class);
	ComponentMapper<Body2DComponent> bm = ComponentMapper
			.getFor(Body2DComponent.class);

	private SpriteBatch batch;
	private OrthographicCamera worldCam;

	// private Sprite wheelTexture;

	public VehicleSystem() {
		super(FAMILY);
		// wheelTexture = new Sprite(new
		// Texture(Gdx.files.internal("wheel.png")));
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		Body2DComponent chassis = bm.get(entity);

		VehicleComponent vehicle = vm.get(entity);

		Array<Wheel> wheels = vehicle.getWheels();

		// float normalForce = calcNormalForce(chassis, wheels.size);

		// TODO: warum überflüssig?
		// batch.setProjectionMatrix(worldCam.combined);

		batch.begin();

		for (Wheel w : wheels) {

			// chassis.applyForce(new Vector2(1, 1), chassis
			// .toWorldPoint(new Vector2(w.xOffsetFromPivot,
			// w.yOffsetFromPivot)));

			// worldWheelPos = chassis.toWorldPoint(new Vector2(chassis
			// .getPosition().x + w.xOffsetFromPivot, chassis
			// .getPosition().y + w.yOffsetFromPivot));

			//

			//
			//
			// batch.draw(wheelTexture, worldWheelPos.x- wheelTexture.getWidth()
			// / 2,
			// worldWheelPos.y - wheelTexture.getHeight() / 2);

			// batch.draw(wheelTexture, x, y, originX, originY, width, height,
			// scaleX, scaleY, rotation, clockwise);

			// Gdx.app.log("VehicleSystem",
			// "WHEEL CENTER POINT: " + w.centerPoint.toString());

			// wheelTexture.setPosition(w.pos.x -
			// wheelTexture.getWidth()/2,w.pos.y);

			// wheelTexture.setRotation(w.orientation);
			// wheelTexture.draw(batch);

			w.wheelTexture.setPosition(w.pos.x, w.pos.y);
			w.wheelTexture.draw(batch);

		}

		batch.end();

	}

	private float calcNormalForce(Body2DComponent chassis, int numberOfWheels) {
		return (chassis.getDamping() / numberOfWheels) * 9.81f;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		batch = engine.getSystem(RenderingSystem.class).getBatch();
		worldCam = engine.getSystem(CameraSystem.class).getWorldCamera();
	}

}
