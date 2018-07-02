package tomcom.kartGame.entities;

import tomcom.kartGame.components.CheckpointCounterComponent;
import tomcom.kartGame.components.CheckpointComponent;
import tomcom.kartGame.components.GamepadInputComponent;
import tomcom.kartGame.components.IDComponent;
import tomcom.kartGame.components.NetworkIdentityComponent;
import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.components.SpriteComponent;
import tomcom.kartGame.components.collision.CircleCollider;
import tomcom.kartGame.components.collision.ColliderComponent;
import tomcom.kartGame.components.collision.RectangleCollider;
import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.components.vehicle.VehicleComponent;
import tomcom.kartGame.components.vehicle.Wheel;
import tomcom.kartGame.config.EntityConfig;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class EntityBuilder {

	public static Entity buildKart(int entityID, float x, float y, float angle,
			Texture kartTexture, boolean control) {

		Entity kart = new Entity();
		if (control)
			kart.add(new GamepadInputComponent());

		Vector2 pos = new Vector2(x, y);
		kart.add(new PivotComponent(pos, angle));

		SpriteComponent spriteComponent = new SpriteComponent(kartTexture,
				EntityConfig.KART_WIDTH, EntityConfig.KART_HEIGHT);
		kart.add(spriteComponent);
		kart.add(new IDComponent(entityID));
		kart.add(new NetworkIdentityComponent());
		kart.add(new Body2DComponent().setDynamic(true).setDamping(0f));

		CheckpointCounterComponent checkpointCounter = new CheckpointCounterComponent();

		kart.add(checkpointCounter);

		kart.add(new ColliderComponent(new RectangleCollider(
				EntityConfig.KART_WIDTH, EntityConfig.KART_HEIGHT,
				EntityConfig.KART_MASS
						/ (EntityConfig.KART_WIDTH * EntityConfig.KART_HEIGHT),
				0, 0).setUserData(checkpointCounter)));

		float xWheelOffset = EntityConfig.KART_WIDTH / 2
				+ EntityConfig.WHEEL_WIDTH / 2;
		float yWheelOffset = EntityConfig.KART_HEIGHT / 2
				- EntityConfig.WHEEL_HEIGHT;
		kart.add(new VehicleComponent()
				.addWheel(new Wheel(xWheelOffset, yWheelOffset, true, false))
				.addWheel(new Wheel(-xWheelOffset, yWheelOffset, true, false))
				.addWheel(new Wheel(xWheelOffset, -yWheelOffset, false, true))
				.addWheel(new Wheel(-xWheelOffset, -yWheelOffset, false, true)));

		return kart;
	}

	public static Entity buildRoadBlock(float x, float y,
			Texture roadblockTexture) {
		SpriteComponent spriteComponent = new SpriteComponent(roadblockTexture,
				EntityConfig.ROADBLOCK_R * 2, EntityConfig.ROADBLOCK_R * 2);
		Entity roadBlock = new Entity();
		roadBlock.add(new PivotComponent(new Vector2(x, y)));
		roadBlock.add(spriteComponent);
		roadBlock.add(new Body2DComponent().setDynamic(false));
		roadBlock.add(new ColliderComponent(new CircleCollider(spriteComponent
				.getSprite().getWidth() / 2, 0, 0, 0)));
		return roadBlock;
	}

	public static Entity buildInvisibleRoadBlock(float x, float y) {
		Entity roadBlock = new Entity();
		roadBlock.add(new PivotComponent(new Vector2(x, y)));
		roadBlock.add(new Body2DComponent().setDynamic(false));
		roadBlock.add(new ColliderComponent(new CircleCollider(
				EntityConfig.ROADBLOCK_R, 0, 0, 0)));
		return roadBlock;
	}

	/**
	 * 
	 * for map1: 190,160 for hagenberg: 700,700 for vienna: 2580,1868
	 * 
	 */
	public static Entity buildMap(Texture mapTexture, int w, int h) {
		Entity bg = new Entity();
		bg.add(new PivotComponent(new Vector2(0, 0)));
		bg.add(new SpriteComponent(mapTexture, w, h));
		return bg;
	}

	public static Entity buildCheckpoint(float x, float y, float angle,
			int number) {
		Entity checkpoint = new Entity();
		checkpoint.add(new PivotComponent(new Vector2(x, y), angle));
		checkpoint.add(new Body2DComponent().setDynamic(false));

		CheckpointComponent checkpointNr = new CheckpointComponent(number);

		checkpoint.add(checkpointNr);

		checkpoint.add(new ColliderComponent(new RectangleCollider(1, 10, 0, 0,
				0).setSensor(true).setUserData(checkpointNr)));

		return checkpoint;
	}

	public static Entity buildLamborghini(float x, float y, Texture lamboTexture) {
		Entity kart = new Entity();

		kart.add(new GamepadInputComponent());

		Vector2 pos = new Vector2(x, y);
		kart.add(new PivotComponent(pos));

		SpriteComponent spriteComponent = new SpriteComponent(lamboTexture,
				EntityConfig.LAMBO_WIDTH, EntityConfig.LAMBO_HEIGHT);
		kart.add(spriteComponent);

		kart.add(new Body2DComponent().setDynamic(true).setDamping(0f));

		kart.add(new ColliderComponent(
				new RectangleCollider(
						EntityConfig.LAMBO_WIDTH,
						EntityConfig.LAMBO_HEIGHT,
						EntityConfig.KART_MASS
								/ (EntityConfig.LAMBO_WIDTH * EntityConfig.LAMBO_HEIGHT),
						0, 0)));

		float xWheelOffset = EntityConfig.LAMBO_WIDTH / 2
				+ EntityConfig.WHEEL_WIDTH / 2;
		float yWheelOffset = EntityConfig.LAMBO_HEIGHT / 2
				- EntityConfig.WHEEL_HEIGHT;
		kart.add(new VehicleComponent()
				.addWheel(new Wheel(xWheelOffset, yWheelOffset, true, false))
				.addWheel(new Wheel(-xWheelOffset, yWheelOffset, true, false))
				.addWheel(new Wheel(xWheelOffset, -yWheelOffset, false, true))
				.addWheel(new Wheel(-xWheelOffset, -yWheelOffset, false, true)));

		return kart;
	}

}
