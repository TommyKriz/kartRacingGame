package tomcom.kartGame.systems;

import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.components.SpriteComponent;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class RenderingSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(SpriteComponent.class,
			PivotComponent.class).get();

	private ComponentMapper<SpriteComponent> sc = ComponentMapper
			.getFor(SpriteComponent.class);
	private ComponentMapper<PivotComponent> pc = ComponentMapper
			.getFor(PivotComponent.class);

	// TODO: Where to dispose?
	private SpriteBatch batch;

	private Array<Entity> renderQueue;

	public RenderingSystem() {
		super(FAMILY);
		batch = new SpriteBatch();
		renderQueue = new Array<>();
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);


//		batch.setProjectionMatrix(getEngine().getSystem(CameraSystem.class)
//				.getProjectionMatrix());

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		batch.setProjectionMatrix(getEngine().getSystem(CameraSystem.class)
				.getProjectionMatrix());
		
		batch.begin();

		Gdx.gl.glClearColor(0, 1, 0, 1);

		for (Entity entity : renderQueue) {
			SpriteComponent visual = sc.get(entity);
			PivotComponent pivot = pc.get(entity);

			final Sprite sprite = visual.getSprite();
			// // pivot is seen in the middle of the sprite
			sprite.setPosition(pivot.getPos().x - sprite.getWidth() / 2,
					pivot.getPos().y - sprite.getHeight() / 2);

			sprite.draw(batch);
		}

		batch.end();
		renderQueue.clear();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		renderQueue.add(entity);
	}

	public SpriteBatch getBatch() {
		// TODO Auto-generated method stub
		return batch;
	}

}
