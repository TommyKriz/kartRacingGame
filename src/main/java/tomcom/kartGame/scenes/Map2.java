package tomcom.kartGame.scenes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import tomcom.kartGame.components.CameraTargetComponent;
import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.config.GameConfig;
import tomcom.kartGame.entities.EntityBuilder;
import tomcom.kartGame.game.GameMain;
import tomcom.kartGame.game.resources.TexturePaths;
import tomcom.kartGame.systems.AudioSystem;
import tomcom.kartGame.systems.Box2DPhysicsSystem;
import tomcom.kartGame.systems.Box2DRenderingSystem;
import tomcom.kartGame.systems.CameraMoveSystem;
import tomcom.kartGame.systems.CameraSystem;
import tomcom.kartGame.systems.CameraZoomSystem;
import tomcom.kartGame.systems.CheckpointSystem;
import tomcom.kartGame.systems.EntityManagerSystem;
import tomcom.kartGame.systems.InputSystem;
import tomcom.kartGame.systems.PivotUpdateSystem;
import tomcom.kartGame.systems.RenderingSystem;
import tomcom.kartGame.systems.TrackEditorSystem;
import tomcom.kartGame.systems.Network.ClientCommands;
import tomcom.kartGame.systems.Network.ServerCommands;
import tomcom.kartGame.systems.Network.ServerSystem;
import tomcom.kartGame.systems.Network.DataContainer.CarData;
import tomcom.kartGame.systems.Network.DataContainer.InputData;
import tomcom.kartGame.systems.Network.DataContainer.SpawnData;
import tomcom.kartGame.systems.vehicle.VehicleDebugRendererSystem;
import tomcom.kartGame.systems.vehicle.WheelRenderingSystem;

public class Map2 implements Screen {

	private GameMain game;

	private Engine engine;

	private Viewport viewport;
	public static float kartStartRotation = 0;
	public static float kartStartPositionX = 10;
	public static float kartStartPositionY = 20;

	public Map2(GameMain game, Engine engine) {

		this.game = game;

		this.engine = engine;

		initSystems();

		viewport = new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), engine.getSystem(CameraSystem.class)
						.getWorldCamera());
		// viewport.apply();
		// TODO: why is only this necessary () ?
		viewport.setWorldSize(GameConfig.WORLD_WIDTH_SEEN_THROUGH_CAMERA,
				GameConfig.WORLD_WIDTH_SEEN_THROUGH_CAMERA
						* (GameConfig.SCREEN_WIDTH / GameConfig.SCREEN_HEIGHT));

		initEntities();
		initEvents();
	}

	private void initEntities() {
		engine.addEntity(EntityBuilder.buildMap(
				game.getTexture(TexturePaths.MAP2), 190, 160));
		initRoadblocks();
	}

	private void initRoadblocks() {
		Texture roadblockTexture = game.getTexture(TexturePaths.ROADBLOCK);
		engine.addEntity(EntityBuilder.buildRoadBlock(-29.309938f, 34.47194f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-28.68894f, 32.539948f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-27.791943f, 30.791954f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-26.687946f, 29.227959f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-25.721952f, 27.571966f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-24.065958f, 26.375969f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-22.409966f, 25.363974f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-20.546972f, 24.719976f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-18.821978f, 32.539948f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-20.753971f, 33.09194f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-21.995964f, 34.563942f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(76.78109f, -10.231967f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(78.07709f, -8.719972f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(78.56309f, -6.775981f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(85.93406f, -7.315978f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(85.61006f, -9.259972f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(85.12406f, -11.203964f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(84.55706f, -13.039958f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(83.50407f, -14.875952f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(82.28907f, -16.387945f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(80.99308f, -17.89994f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(79.13008f, -18.547937f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(77.26709f, -18.979935f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(57.58416f, -10.015967f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(55.55917f, -10.555965f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(53.858173f, -11.419963f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(52.31918f, -12.715958f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(51.347183f, -14.443952f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(50.456184f, -16.279945f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(49.88919f, -18.331938f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(49.56519f, -20.275932f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(48.99819f, -22.759922f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(58.556156f, -19.087934f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(56.855164f, -20.275932f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(56.045166f, -22.111925f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(23.363981f, -65.931984f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(21.581985f, -65.067986f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(19.718994f, -64.41999f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(18.098999f, -63.339996f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(16.479004f, -62.044f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(15.588009f, -60.316006f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(14.697011f, -58.588013f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(14.373013f, -56.64402f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(13.563016f, -54.916023f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(12.42902f, -53.296032f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(23.606977f, -58.26401f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(22.067987f, -56.968018f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(21.095987f, -55.240025f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(20.44799f, -53.296032f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(19.637995f, -51.244038f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(18.665997f, -49.516045f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(17.451002f, -47.89605f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(16.236006f, -46.276054f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(14.535011f, -45.304058f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(12.42902f, -44.98006f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-37.86601f, -74.18799f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-39.972f, -73.97199f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-41.753994f, -73.215996f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-42.80699f, -71.596f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-44.264984f, -70.3f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-45.803978f, -68.03201f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-46.532978f, -66.19602f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-46.694977f, -64.25203f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-47.34297f, -62.308033f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-48.962967f, -61.22804f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-36.57001f, -66.19602f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-38.028008f, -64.900024f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-39.405003f, -63.49603f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-39.972f, -61.552036f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-40.458f, -59.608044f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-41.510994f, -57.77205f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-42.80699f, -56.368053f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-44.102985f, -54.964058f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-45.39898f, -53.560066f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-47.180973f, -52.58807f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-49.12497f, -52.264072f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-51.285927f, 4.1559505f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-49.503933f, 3.1839504f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-48.450935f, 1.4559584f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-41.727962f, 2.211955f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-42.37596f, 4.1559505f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-42.861958f, 6.0999413f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-43.67195f, 7.8279343f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-44.886948f, 9.447929f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-46.749943f, 10.959923f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-48.450935f, 11.931919f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-49.989933f, 13.119917f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(57.290085f, 9.740021f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(59.153076f, 10.292019f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(60.80907f, 11.488014f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(61.982067f, 13.052009f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(62.741062f, 14.892002f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(63.50006f, 16.639996f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(64.46606f, 18.387987f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(64.60406f, 20.41198f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(64.67306f, 22.343975f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(64.46606f, 24.367966f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(63.50006f, 26.48396f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(62.396065f, 28.04795f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(61.085068f, 29.427948f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(59.360077f, 30.715942f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(57.566082f, 31.727936f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(55.84109f, 32.647934f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(56.738087f, 18.295986f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(57.773083f, 20.043982f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(58.32508f, 21.883974f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(57.42808f, 23.723969f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(55.77209f, 24.827963f,
				roadblockTexture));
		roadblockTexture = null;
	}

	private void initEvents() {
		if (engine.getSystem(ServerSystem.class) != null) { // Host use Input
															// directly

			SpawnData spawnData = new SpawnData(0, kartStartPositionX,
					kartStartPositionY, kartStartRotation);
			spawnData.localControl = true;
			engine.getSystem(ServerSystem.class).spawnTransform = spawnData;
			spawnCart(spawnData);
			ServerCommands.onSpawn.add(new Listener<SpawnData>() {

				@Override
				public void receive(Signal<SpawnData> arg0, SpawnData arg1) {

					spawnCart(arg1);

				}

			});
			engine.getSystem(ServerSystem.class).sendSpawnServerOnly();
			InputSystem.onInputReceived.add(new Listener<Vector2>() {

				@Override
				public void receive(Signal<Vector2> arg0, Vector2 arg1) {
					VehicleDebugRendererSystem.onInputReceived
							.dispatch(new InputData(0, arg1.x, arg1.y));
				}
			});
			Timer.schedule(new Task() {

				@Override
				public void run() {
					ServerCommands.onStartRace.dispatch(null);

				}

			}, 2);// 3 Seconds to Start

		} else {// Client send Input over Network
			InputSystem.onInputReceived.add(new Listener<Vector2>() {

				@Override
				public void receive(Signal<Vector2> arg0, Vector2 arg1) {
					// Gdx.app.log("VehicleDebugRendererSystem",
					// "receiveing Input");
					ClientCommands.onInputReceived.dispatch(arg1);
				}
			});
			ClientCommands.onSpawn.add(new Listener<SpawnData>() {

				@Override
				public void receive(Signal<SpawnData> arg0, SpawnData arg1) {

					Gdx.app.log("TestLevel", "Received Spawn: " + arg1.entityID
							+ " x " + arg1.x + " y " + arg1.y + " rot "
							+ arg1.rot);
					spawnCart(arg1);

				}

			});
			ClientCommands.onCarDataReceived.add(new Listener<CarData>() {

				@Override
				public void receive(Signal<CarData> signal, CarData object) {
					Entity entity = engine.getSystem(EntityManagerSystem.class)
							.GetEntity(object.entityID);
					// Gdx.app.log("TESTLEVEL", "Receiving Car Data " +
					// object.entityID + " "+object.xPos+" " +
					// object.yPos+" "+object.rot);
					if (entity != null) {
						PivotComponent pivot = entity
								.getComponent(PivotComponent.class);
						pivot.setPos(new Vector2(object.xPos, object.yPos),
								object.rot);
					}

				}

			});
		}
	}

	private void initSystems() {

		engine.addSystem(new EntityManagerSystem());
		engine.addSystem(new CameraSystem(-0.599993f, -3.3000004f));
		engine.addSystem(new CameraZoomSystem(22.69999f));
		engine.addSystem(new CameraMoveSystem());
		engine.addSystem(new InputSystem());

		CheckpointSystem checkpointSystem = new CheckpointSystem(
				buildCheckpoints());

		engine.addSystem(new Box2DPhysicsSystem(checkpointSystem));
		if (engine.getSystem(ServerSystem.class) != null)// only on server
			engine.addSystem(new PivotUpdateSystem());

		engine.addSystem(new RenderingSystem());
		if (engine.getSystem(ServerSystem.class) != null)
			engine.addSystem(new Box2DRenderingSystem());

		engine.addSystem(new WheelRenderingSystem(game
				.getTexture(TexturePaths.WHEEL)));
		if (engine.getSystem(ServerSystem.class) != null)
			engine.addSystem(new VehicleDebugRendererSystem());

		engine.addSystem(checkpointSystem);

		if (engine.getSystem(ServerSystem.class) != null)
			engine.addSystem(new TrackEditorSystem(game
					.getTexture(TexturePaths.ROADBLOCK)));

		engine.addSystem(new AudioSystem(game.getBackgroundMusic()));
	}

	private List<Vector2> buildCheckpoints() {
		List<Vector2> checkpoints = new ArrayList<>();
		// checkpoints.add(new Vector2(-50.6f, 14.5f));
		// checkpoints.add(new Vector2(-63.9f, -20.6f));
		// checkpoints.add(new Vector2(-39.7f, -58.5f));
		// checkpoints.add(new Vector2(-12.2f, -22.1f));
		// checkpoints.add(new Vector2(25, -1.3f));
		// checkpoints.add(new Vector2(56.4f, 30.9f));
		// checkpoints.add(new Vector2(29.7f, 58.2f));
		// checkpoints.add(new Vector2(-19.5f, 38));
		return checkpoints;
	}

	private void spawnCart(SpawnData spawnData) {
		Gdx.app.log("TestLevel", "spawning cart: " + spawnData.entityID
				+ " at: " + spawnData.x + " " + spawnData.y + " "
				+ spawnData.rot);
		Texture cartTexture = null;
		if (spawnData.entityID == 0)
			cartTexture = game.getTexture(TexturePaths.KART_RED);
		if (spawnData.entityID == 1)
			cartTexture = game.getTexture(TexturePaths.KART_GRAY);
		if (spawnData.entityID == 2)
			cartTexture = game.getTexture(TexturePaths.KART_SILVER);
		if (spawnData.entityID == 3)
			cartTexture = game.getTexture(TexturePaths.KART_BLUE);
		engine.addEntity(EntityBuilder.buildKart(spawnData.entityID,
				spawnData.x, spawnData.y, spawnData.rot, cartTexture,
				spawnData.localControl).add(new CameraTargetComponent()));
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