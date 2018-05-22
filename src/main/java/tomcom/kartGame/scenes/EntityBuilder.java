package tomcom.kartGame.scenes;

import tomcom.kartGame.components.KeyInputComponent;
import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.components.SpriteComponent;
import tomcom.kartGame.components.collision.CircleCollider;
import tomcom.kartGame.components.collision.ColliderComponent;
import tomcom.kartGame.components.collision.RectangleCollider;
import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.components.vehicle.VehicleComponent;
import tomcom.kartGame.components.vehicle.Wheel;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class EntityBuilder {

	public static Entity buildKart(float x, float y) {

		Entity kart = new Entity();

		kart.add(new KeyInputComponent(new int[] { Input.Keys.A, Input.Keys.D,
				Input.Keys.W, Input.Keys.S, Input.Keys.SPACE }));

		Vector2 pos = new Vector2(x, y);
		kart.add(new PivotComponent(pos));

		SpriteComponent spriteComponent = new SpriteComponent(
				TexturePaths.KART_TEXTURE, EntityConfig.KART_WIDTH,
				EntityConfig.KART_HEIGHT);
		kart.add(spriteComponent);

		kart.add(new Body2DComponent().setDynamic(true).setDamping(22.3f));

		kart.add(new ColliderComponent(new RectangleCollider(
				EntityConfig.KART_WIDTH, EntityConfig.KART_HEIGHT, 0, 0, 0)));

		float xWheelOffset = EntityConfig.KART_WIDTH / 2
				+ EntityConfig.WHEEL_WIDTH / 2;
		float yWheelOffset = EntityConfig.KART_HEIGHT / 2
				- EntityConfig.WHEEL_HEIGHT;
		kart.add(new VehicleComponent()
				.addWheel(new Wheel(xWheelOffset, yWheelOffset, false, true))
				.addWheel(new Wheel(-xWheelOffset, yWheelOffset, false, true))
				.addWheel(new Wheel(xWheelOffset, -yWheelOffset, true, false))
				.addWheel(new Wheel(-xWheelOffset, -yWheelOffset, true, false)));

		return kart;

	}

	public static Entity buildRoadBlock(float x, float y) {
		SpriteComponent spriteComponent = new SpriteComponent(
				TexturePaths.ROADBLOCK, EntityConfig.ROADBLOCK_R,
				EntityConfig.ROADBLOCK_R);
		Entity raodBlock = new Entity();
		raodBlock.add(new PivotComponent(new Vector2(x, y)));
		raodBlock.add(spriteComponent);
		raodBlock.add(new Body2DComponent().setDynamic(false));
		raodBlock.add(new ColliderComponent(new CircleCollider(spriteComponent
				.getSprite().getWidth() / 2, 0, 0, 0)));
		return raodBlock;
	}

}
