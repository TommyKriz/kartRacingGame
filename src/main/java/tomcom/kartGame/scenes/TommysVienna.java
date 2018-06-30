package tomcom.kartGame.scenes;

import tomcom.kartGame.components.CameraTargetComponent;
import tomcom.kartGame.config.GameConfig;
import tomcom.kartGame.entities.EntityBuilder;
import tomcom.kartGame.game.GameMain;
import tomcom.kartGame.game.resources.ResourceManager;
import tomcom.kartGame.game.resources.TextureKeys;
import tomcom.kartGame.systems.Box2DPhysicsSystem;
import tomcom.kartGame.systems.Box2DRenderingSystem;
import tomcom.kartGame.systems.CameraFollowSystem;
import tomcom.kartGame.systems.CameraMoveSystem;
import tomcom.kartGame.systems.CameraSystem;
import tomcom.kartGame.systems.CameraZoomSystem;
import tomcom.kartGame.systems.PivotUpdateSystem;
import tomcom.kartGame.systems.RenderingSystem;
import tomcom.kartGame.systems.TrackEditorSystem;
import tomcom.kartGame.systems.vehicle.VehicleGamepadInputDebugRendererSystem;
import tomcom.kartGame.systems.vehicle.WheelRenderingSystem;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class TommysVienna implements Screen {

	private GameMain game;

	private Engine engine;

	private FitViewport viewport;

	public TommysVienna(GameMain game) {
		this.game = game;

		engine = new Engine();

		initSystems();

		viewport = new FitViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), engine.getSystem(CameraSystem.class)
						.getWorldCamera());
		// viewport.apply();
		// TODO: why is only this necessary () ?
		viewport.setWorldSize(GameConfig.WORLD_WIDTH_SEEN_THROUGH_CAMERA,
				GameConfig.WORLD_WIDTH_SEEN_THROUGH_CAMERA
						* (GameConfig.SCREEN_WIDTH / GameConfig.SCREEN_HEIGHT));

		initEntities();
	}

	private void initEntities() {
		engine.addEntity(EntityBuilder.buildMapVienna());
		engine.addEntity(EntityBuilder.buildLamborghini(2, 2).add(
				new CameraTargetComponent()));
	}

	private void initSystems() {

		engine.addSystem(new CameraSystem(-8.199994f, -0.8999984f));
		engine.addSystem(new CameraZoomSystem(19.499977f));
		engine.addSystem(new CameraMoveSystem());

		engine.addSystem(new CameraFollowSystem());

		engine.addSystem(new Box2DPhysicsSystem());
		engine.addSystem(new PivotUpdateSystem());

		engine.addSystem(new RenderingSystem());
		engine.addSystem(new Box2DRenderingSystem());

		engine.addSystem(new WheelRenderingSystem(ResourceManager
				.getTexture(TextureKeys.WHEEL)));
		engine.addSystem(new VehicleGamepadInputDebugRendererSystem());

		engine.addSystem(new TrackEditorSystem());
	}

	@Override
	public void show() {
		// init
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		engine.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO: ??? should i dispose?
	}

}