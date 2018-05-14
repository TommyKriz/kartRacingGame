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

		SpriteComponent spriteComponent = new SpriteComponent("kart.png");
		Entity kart = new Entity();
		kart.add(new KeyInputComponent(new int[] { Input.Keys.A, Input.Keys.D,
				Input.Keys.W, Input.Keys.S, Input.Keys.SPACE }));
		Vector2 pos = new Vector2(x, y);
		kart.add(new PivotComponent(pos));
		kart.add(spriteComponent);

		kart.add(new Body2DComponent().setDynamic(true).setDamping(0.3f));

		kart.add(new ColliderComponent(new RectangleCollider(spriteComponent
				.getSprite().getWidth() / 2, spriteComponent.getSprite()
				.getHeight() / 2, 0, 0, 0)));

		Texture wheeltexture = new Texture("wheel.png");
		kart.add(new VehicleComponent().addWheel(
				new Wheel(1.8f, 2, false, true, wheeltexture)).addWheel(
				new Wheel(-1.8f, 2, true, false, wheeltexture)));

		return kart;

	}

	public static Entity buildBG() {
		Entity bg = new Entity();
		bg.add(new PivotComponent(new Vector2(0, 0)));
		bg.add(new SpriteComponent("BG.png"));
		return bg;
	}

	public static Entity buildRoadBlock(float x, float y) {
		SpriteComponent spriteComponent = new SpriteComponent("roadblock.png");
		Entity raodBlock = new Entity();
		raodBlock.add(new PivotComponent(new Vector2(x, y)));
		raodBlock.add(spriteComponent);
		raodBlock.add(new Body2DComponent().setDynamic(false));
		raodBlock.add(new ColliderComponent(new CircleCollider(spriteComponent
				.getSprite().getWidth() / 2, 0, 0, 0)));
		return raodBlock;
	}

}
