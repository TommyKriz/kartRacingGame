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

public class Map3 implements Screen {

	private GameMain game;

	private Engine engine;

	private Viewport viewport;
	public static float kartStartRotation = 0;
	public static float kartStartPositionX = 10;
	public static float kartStartPositionY = 20;

	public Map3(GameMain game, Engine engine) {

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
				game.getTexture(TexturePaths.MAP3), 190, 160));
		initRoadblocks();
	}

	private void initRoadblocks() {
		Texture roadblockTexture = game.getTexture(TexturePaths.ROADBLOCK);
		engine.addEntity(EntityBuilder.buildRoadBlock(-1.093988f, -42.954597f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(0.21500781f, -41.51727f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(0.7540053f, -39.669273f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(0.9850061f, -37.718616f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(1.6010034f, -35.87062f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(1.9859998f, -33.71463f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(2.9099991f, -31.866636f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(4.295993f, -30.42931f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(5.6049886f, -28.889315f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(6.9909825f, -27.554655f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(8.838978f, -27.04132f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-0.40099126f,
				-52.091896f, roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(1.3700025f, -51.373234f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(2.9099991f, -50.243904f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(4.295993f, -48.90924f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(5.758988f, -47.369247f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(6.451987f, -45.521255f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(6.9909825f, -43.570595f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(7.683981f, -41.51727f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(8.145979f, -39.56661f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(8.838978f, -37.718616f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(9.839972f, -36.07596f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(38.59604f, -56.01204f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(39.058037f, -57.86003f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(39.751038f, -59.708023f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(40.675034f, -61.55602f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(41.83003f, -63.096012f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(43.216022f, -64.43068f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(44.987015f, -65.14934f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(46.75801f, -66.07333f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(48.683002f, -66.484f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(48.914005f, -57.449368f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(47.14301f, -56.628036f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(46.142014f, -54.985374f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(66.93194f, -57.346703f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(68.47193f, -56.21737f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(68.548935f, -54.266712f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(67.03807f, -66.68933f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(68.96306f, -66.27867f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(70.65705f, -65.354675f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(72.12005f, -64.12267f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(73.12105f, -62.480015f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(74.045044f, -60.734688f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(75.20004f, -59.092026f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(75.81604f, -57.14137f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(76.27803f, -55.293373f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(76.58603f, -53.342716f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(18.246998f, 19.527998f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(20.171991f, 19.835997f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(21.711983f, 21.067991f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(23.25198f, 22.402655f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(24.175976f, 24.14798f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(24.86897f, 26.09864f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(25.40797f, 27.946632f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(26.023968f, 29.794628f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(26.716967f, 31.64262f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(27.024965f, 33.59328f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(28.410957f, 34.92794f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(28.410957f, 44.167908f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(26.408968f, 44.167908f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(24.714972f, 43.038578f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(23.40598f, 41.498585f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(22.250982f, 39.753258f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(21.249985f, 38.00793f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(20.47999f, 35.954605f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(19.786991f, 34.106613f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(19.324993f, 32.25862f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(19.170994f, 30.307959f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(18.015997f, 28.665298f,
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
		engine.addSystem(new CameraSystem(-1.5000056f, 0.7f));
		engine.addSystem(new CameraZoomSystem(19.299976f));
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