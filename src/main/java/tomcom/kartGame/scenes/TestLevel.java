package tomcom.kartGame.scenes;

import tomcom.kartGame.components.InputComponent;
import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.components.SpriteComponent;
import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.game.GameMain;
import tomcom.kartGame.systems.Box2DPhysicsSystem;
import tomcom.kartGame.systems.Box2DRenderingSystem;
import tomcom.kartGame.systems.CameraSystem;
import tomcom.kartGame.systems.InputSystem;
import tomcom.kartGame.systems.PivotUpdateSystem;
import tomcom.kartGame.systems.RenderingSystem;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;

public class TestLevel implements Screen {

	private GameMain game;

	private Engine engine;

	public TestLevel(GameMain game) {
		this.game = game;

		// Cloud c = new Cloud(world);

		engine = new Engine();

		initSystems();

		initEntities();
	}

	private void initEntities() {
		addBG();
		addKart();
	}

	private void addBG() {
		Entity bg = new Entity();
		bg.add(new PivotComponent(new Vector2(0, 0)));
		bg.add(new SpriteComponent("BG.png"));
		engine.addEntity(bg);
	}

	private void addKart() {
		SpriteComponent spriteComponent = new SpriteComponent("kart.png");
		Entity kart = new Entity();
		kart.add(new InputComponent(new int[] { Input.Keys.A, Input.Keys.D,
				Input.Keys.W, Input.Keys.S, Input.Keys.SPACE }));
		kart.add(new PivotComponent(new Vector2(10, 10)));
		kart.add(spriteComponent);

		kart.add(new Body2DComponent().setDynamic(true));

		// TODO: add fixture Def (Collider Component)

		engine.addEntity(kart);
	}

	private void initSystems() {
		engine.addSystem(new InputSystem());
		engine.addSystem(new Box2DPhysicsSystem());
		engine.addSystem(new PivotUpdateSystem());

		engine.addSystem(new CameraSystem());
		engine.addSystem(new Box2DRenderingSystem());
		engine.addSystem(new RenderingSystem());
	}

	@Override
	public void show() {
		// init

	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.app.log("TestLevel ", "updateEngine");
		engine.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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
