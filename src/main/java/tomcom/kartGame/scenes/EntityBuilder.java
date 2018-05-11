package tomcom.kartGame.scenes;

import tomcom.kartGame.components.KeyInputComponent;
import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.components.SpriteComponent;
import tomcom.kartGame.components.physics.Body2DComponent;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class EntityBuilder {

	public static Entity buildKart(float x, float y) {

		SpriteComponent spriteComponent = new SpriteComponent("kart.png");
		Entity kart = new Entity();
		kart.add(new KeyInputComponent(new int[] { Input.Keys.A, Input.Keys.D,
				Input.Keys.W, Input.Keys.S, Input.Keys.SPACE }));
		kart.add(new PivotComponent(new Vector2(x, y)));
		kart.add(spriteComponent);

		kart.add(new Body2DComponent().setDynamic(true).setDamping(0.3f));

		// TODO: add fixture Def (Collider Component)

		// kart.add(new CameraTargetComponent());

		return kart;

	}

	public static Entity buildBG() {
		Entity bg = new Entity();
		bg.add(new PivotComponent(new Vector2(0, 0)));
		bg.add(new SpriteComponent("BG.png"));
		return bg;
	}

}
