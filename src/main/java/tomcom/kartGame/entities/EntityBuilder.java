package tomcom.kartGame.entities;

import tomcom.kartGame.components.GamepadInputComponent;
import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.components.SpriteComponent;
import tomcom.kartGame.components.collision.CircleCollider;
import tomcom.kartGame.components.collision.ColliderComponent;
import tomcom.kartGame.components.collision.RectangleCollider;
import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.components.vehicle.VehicleComponent;
import tomcom.kartGame.components.vehicle.Wheel;
import tomcom.kartGame.config.EntityConfig;
import tomcom.kartGame.game.resources.ResourceManager;
import tomcom.kartGame.game.resources.TextureKeys;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

public class EntityBuilder {

	public static Entity buildKart(float x, float y) {

		Entity kart = new Entity();

		kart.add(new GamepadInputComponent());

		Vector2 pos = new Vector2(x, y);
		kart.add(new PivotComponent(pos));

		SpriteComponent spriteComponent = new SpriteComponent(
				ResourceManager.getTexture(TextureKeys.KART),
				EntityConfig.KART_WIDTH, EntityConfig.KART_HEIGHT);
		kart.add(spriteComponent);

		kart.add(new Body2DComponent().setDynamic(true).setDamping(0f));

		kart.add(new ColliderComponent(new RectangleCollider(
				EntityConfig.KART_WIDTH, EntityConfig.KART_HEIGHT,
				EntityConfig.KART_MASS
						/ (EntityConfig.KART_WIDTH * EntityConfig.KART_HEIGHT),
				0, 0)));

		float xWheelOffset = EntityConfig.KART_WIDTH / 2
				+ EntityConfig.WHEEL_WIDTH / 2;
		float yWheelOffset = EntityConfig.KART_HEIGHT / 2
				- EntityConfig.WHEEL_HEIGHT;
		kart.add(new VehicleComponent()
				.addWheel(new Wheel(xWheelOffset, yWheelOffset, true))
				.addWheel(new Wheel(-xWheelOffset, yWheelOffset, true))
				.addWheel(new Wheel(xWheelOffset, -yWheelOffset, false))
				.addWheel(new Wheel(-xWheelOffset, -yWheelOffset, false)));

		return kart;
	}

	public static Entity buildRoadBlock(float x, float y) {
		SpriteComponent spriteComponent = new SpriteComponent(
				ResourceManager.getTexture(TextureKeys.ROADBLOCK),
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

	public static Entity buildMap() {
		Entity bg = new Entity();
		bg.add(new PivotComponent(new Vector2(0, 0)));
		bg.add(new SpriteComponent(ResourceManager.getTexture(TextureKeys.MAP),
				190, 160));
		return bg;
	}

	public static Entity buildFinishLine(float f, float y) {
		Entity roadBlock = new Entity();
		roadBlock.add(new PivotComponent(new Vector2(f, y)));
		roadBlock.add(new Body2DComponent().setDynamic(false));
		roadBlock.add(new ColliderComponent(new RectangleCollider(1, 10, 0, 0,
				0).setSensor(true).setUserData(
				EntityConfig.FINISH_LINE_COLLIDER)));
		return roadBlock;
	}

}
