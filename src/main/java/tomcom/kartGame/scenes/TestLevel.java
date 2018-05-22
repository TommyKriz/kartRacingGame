package tomcom.kartGame.scenes;

import tomcom.kartGame.components.CameraTargetComponent;
import tomcom.kartGame.game.GameMain;
import tomcom.kartGame.systems.Box2DPhysicsSystem;
import tomcom.kartGame.systems.Box2DRenderingSystem;
import tomcom.kartGame.systems.CameraSystem;
import tomcom.kartGame.systems.PivotUpdateSystem;
import tomcom.kartGame.systems.RenderingSystem;
import tomcom.kartGame.systems.input.KeyInputSystem;
import tomcom.kartGame.systems.input.MouseInputSystem;
import tomcom.kartGame.systems.vehicle.VehicleKeyInputSystem;
import tomcom.kartGame.systems.vehicle.WheelDebugRendererSystem;
import tomcom.kartGame.systems.vehicle.WheelRenderingSystem;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class TestLevel implements Screen {

	private GameMain game;

	private Engine engine;

	private StretchViewport viewport;

	public TestLevel(GameMain game) {
		this.game = game;

		engine = new Engine();

		initSystems();

		viewport = new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), engine.getSystem(CameraSystem.class)
						.getWorldCamera());

		initEntities();
	}

	private void initEntities() {
		engine.addEntity(EntityBuilder.buildKart(2, 2).add(
				new CameraTargetComponent()));
	}

	private void initSystems() {
		engine.addSystem(new Box2DPhysicsSystem());
		engine.addSystem(new PivotUpdateSystem());

		engine.addSystem(new CameraSystem());

		// engine.addSystem(new CameraFollowSystem());

		engine.addSystem(new RenderingSystem());
		engine.addSystem(new Box2DRenderingSystem());

		engine.addSystem(new KeyInputSystem());

		// engine.addSystem(new WheelRenderingSystem());
		engine.addSystem(new VehicleKeyInputSystem());
		engine.addSystem(new WheelDebugRendererSystem());

		engine.addSystem(new MouseInputSystem());
	}

	@Override
	public void show() {
		// init
	}

	//
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.app.log("TestLevel ", "updateEngine");
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