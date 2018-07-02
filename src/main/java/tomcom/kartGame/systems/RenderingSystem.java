package tomcom.kartGame.systems;

import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.components.SpriteComponent;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class RenderingSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(SpriteComponent.class,
			PivotComponent.class).get();

	private ComponentMapper<SpriteComponent> sc = ComponentMapper
			.getFor(SpriteComponent.class);
	private ComponentMapper<PivotComponent> pc = ComponentMapper
			.getFor(PivotComponent.class);

	private SpriteBatch batch;

	public RenderingSystem() {
		super(FAMILY);
		batch = new SpriteBatch();
	}

	@Override
	public void update(float deltaTime) {

		// TODO: dirty flag?
		batch.setProjectionMatrix(getEngine().getSystem(CameraSystem.class)
				.getProjectionMatrix());

		batch.begin();

		Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);

		super.update(deltaTime);

		batch.end();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		final Vector3 pivot = pc.get(entity).getPos();
		final Sprite sprite = sc.get(entity).getSprite();

		sprite.setPosition(pivot.x - sprite.getWidth() / 2,
				pivot.y - sprite.getHeight() / 2);

		sprite.setRotation(pivot.z);

		sprite.draw(batch);
	}

	public SpriteBatch getBatch() {
		// TODO Auto-generated method stub
		return batch;
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		batch.dispose();
	}

}
