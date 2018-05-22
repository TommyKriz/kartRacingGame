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
import tomcom.kartGame.game.GameConfig;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class EntityBuilder {

	private static final float KART_WIDTH = 2.5f;

	private static final float KART_HEIGHT = 4f;

	private static final float ROADBLOCK_R = 1.5f;

	public static Entity buildKart(float x, float y) {

		SpriteComponent spriteComponent = new SpriteComponent(
				TexturePaths.KART_TEXTURE, KART_WIDTH, KART_HEIGHT);
		
		Entity kart = new Entity();
		kart.add(new KeyInputComponent(new int[] { Input.Keys.A, Input.Keys.D,
				Input.Keys.W, Input.Keys.S, Input.Keys.SPACE }));
		Vector2 pos = new Vector2(x, y);
		kart.add(new PivotComponent(pos));
		kart.add(spriteComponent);

		kart.add(new Body2DComponent().setDynamic(true).setDamping(22.3f));

		kart.add(new ColliderComponent(new RectangleCollider(spriteComponent
				.getSprite().getWidth() / 2, spriteComponent.getSprite()
				.getHeight() / 2, 0, 0, 0)));

		Texture wheeltexture = new Texture(TexturePaths.WHEEL);
		kart.add(new VehicleComponent().addWheel(
				new Wheel(1.8f, 2, false, true, wheeltexture)).addWheel(
				new Wheel(-1.8f, 2, true, false, wheeltexture)));

		return kart;

	}

	public static Entity buildRoadBlock(float x, float y) {
		SpriteComponent spriteComponent = new SpriteComponent(
				TexturePaths.ROADBLOCK, ROADBLOCK_R, ROADBLOCK_R);
		Entity raodBlock = new Entity();
		raodBlock.add(new PivotComponent(new Vector2(x, y)));
		raodBlock.add(spriteComponent);
		raodBlock.add(new Body2DComponent().setDynamic(false));
		raodBlock.add(new ColliderComponent(new CircleCollider(spriteComponent
				.getSprite().getWidth() / 2, 0, 0, 0)));
		return raodBlock;
	}

}
