package tomcom.kartGame.scenes;

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

public class TestLevel implements Screen {

	private GameMain game;

	private Engine engine;

	private Viewport viewport;
	public static float kartStartRotation = 0;
	public static float kartStartPositionX = 10;
	public static float kartStartPositionY = 20;

	public TestLevel(GameMain game, Engine engine) {

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
				game.getTexture(TexturePaths.MAP1), 190, 160));
		// engine.addEntity(EntityBuilder.buildKart(-19.5f, 36, -180,
		// game.getTexture(TexturePaths.KART)).add(
		// new CameraTargetComponent()));
		initRoadblocks();
	}

	private void initRoadblocks() {
		Texture roadblockTexture = game.getTexture(TexturePaths.ROADBLOCK);
		engine.addEntity(EntityBuilder.buildRoadBlock(-16.866934f, -11.457262f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-16.760935f, -9.549273f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-16.124939f, -7.711947f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-15.64794f, -5.73329f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-15.223942f, -3.8252983f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-14.587946f, -1.9879742f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-13.845949f,
				-0.07998252f, roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-12.573954f, 1.333344f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-11.0369625f, 2.4640055f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-9.4999695f, 3.6653318f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-7.8569775f, 4.654662f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-6.0019855f, 5.1493263f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-3.9349947f, 5.5026574f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-7.0619807f, -12.163927f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-6.8499823f, -10.255936f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-6.054985f, -8.41861f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-4.888992f, -6.863951f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(48.74507f, -7.005284f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(50.547062f, -6.2986207f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(52.243053f, -5.3092914f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(53.93905f, -4.39063f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(55.47604f, -3.189302f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(57.17203f, -2.2706394f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(58.550026f, -0.927979f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(59.66302f, 0.6266799f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(60.670017f, 2.5346718f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(61.359013f, 4.3719945f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(62.048008f, 6.2799864f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(62.20701f, 8.187978f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(48.215073f, 5.714656f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(49.911064f, 6.77465f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(51.50106f, 7.905313f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(52.296055f, 9.671969f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(61.88901f, 47.726776f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(61.46501f, 49.91743f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(61.14701f, 51.825424f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(60.458015f, 53.66275f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(59.61002f, 55.429405f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(58.550026f, 57.1254f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(57.967026f, 59.033386f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(57.225033f, 60.870712f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(56.00604f, 62.354706f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(54.363045f, 63.556034f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(52.508057f, 64.0507f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(50.600063f, 64.61603f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(48.58607f, 52.81475f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(50.282066f, 51.896088f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(51.23606f, 50.200096f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(52.137054f, 48.362774f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(18.59194f, 64.57994f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(16.73695f, 64.08528f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(14.8289585f, 63.16662f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(13.0799675f, 62.177288f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(11.595976f, 60.97596f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(9.952984f, 59.986633f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(8.945988f, 58.361305f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(8.0449915f, 56.38265f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(7.4089956f, 54.47466f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(6.6139984f, 52.637337f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(6.190001f, 50.729343f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(5.872f, 48.82135f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(5.3950043f, 46.842697f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(4.3880086f, 45.21737f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(2.7980165f, 44.086708f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(18.48594f, 52.637337f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(17.00195f, 51.36534f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(16.047953f, 49.38668f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(15.411957f, 47.54936f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(15.411957f, 45.5707f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(14.8289585f, 43.592045f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(14.245962f, 41.75472f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(13.556965f, 39.917393f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(12.549971f, 38.150734f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(11.489976f, 36.52541f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(9.793983f, 35.60675f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(8.150991f, 34.546753f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(6.2960005f, 33.981422f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(4.547008f, 32.85076f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(2.5860164f, 32.28543f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-42.44706f, 31.738636f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-44.196056f, 30.819975f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-45.203053f, 29.053314f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-45.41505f, 27.145325f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-43.29506f, 43.893246f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-44.99105f, 42.83325f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-47.16404f, 42.691917f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-48.86003f, 41.70259f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-49.867027f, 40.077263f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-51.139023f, 38.522606f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-52.252014f, 36.755943f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-53.47101f, 35.27195f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-54.319008f, 33.36396f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-55.008003f, 31.526636f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-55.273003f, 29.54798f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-55.485f, 27.569323f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-45.808155f, -11.241372f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-46.126152f, -13.22003f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-46.974148f, -15.128021f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-47.663147f, -17.03601f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-48.617138f, -18.944002f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-49.730133f, -20.781328f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-51.05513f, -22.406652f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-52.645123f, -23.81998f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-54.447113f, -24.667974f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-56.408104f, -24.879974f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-57.945095f, -26.151968f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-59.800087f, -26.646633f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-55.348106f, -9.192048f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-55.613106f, -11.170706f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-56.4611f, -12.937365f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-58.104095f, -13.997358f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-59.959087f, -14.562689f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-66.66416f, -26.84668f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-68.30716f, -27.836006f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-69.42015f, -29.461332f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-69.84415f, -31.369324f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-67.19416f, -14.692068f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-69.10215f, -15.186731f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-71.01014f, -15.610733f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-72.65314f, -16.812057f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-74.08413f, -18.084053f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-75.46212f, -19.426712f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-76.89311f, -20.910707f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-77.847115f, -22.606697f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-78.32411f, -24.51469f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-78.960106f, -26.352015f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-79.490105f, -28.33067f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-80.1791f, -30.167995f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-79.27811f, -31.863989f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-69.84311f, -47.902786f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-69.63111f, -50.09344f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-68.624115f, -51.860096f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-66.981125f, -52.99076f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-79.33006f, -48.185448f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-79.27707f, -50.16411f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-78.64107f, -52.00143f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-78.323074f, -53.909424f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-77.95207f, -55.958748f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-77.36908f, -57.79607f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-76.36208f, -59.49206f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-75.19608f, -61.046722f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-73.50009f, -61.965385f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-71.963104f, -63.096046f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-70.05511f, -63.59071f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-68.09412f, -63.944042f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-66.20701f, -63.661377f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-63.87502f, -63.802708f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-20.805965f, -64.79204f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-18.632975f, -64.50937f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-16.724985f, -63.873375f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-15.134992f, -62.742714f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-13.439001f, -61.82405f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-12.061007f, -60.410725f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-10.736012f, -58.92673f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-9.19902f, -57.79607f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-8.351025f, -56.02941f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-7.6620274f, -54.12142f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-7.4500284f, -52.213432f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-7.397028f, -50.23477f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-21.282963f, -52.566765f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-19.321972f, -52.354763f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-17.94398f, -51.012104f,
				roadblockTexture));
		engine.addEntity(EntityBuilder.buildRoadBlock(-16.777985f, -49.457443f,
				roadblockTexture));
		roadblockTexture = null;
	}

	private void initEvents() {
		if (engine.getSystem(ServerSystem.class) != null) { // Host use Input directly
			
			SpawnData spawnData = new SpawnData(0, kartStartPositionX, kartStartPositionY, kartStartRotation);
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

					 Gdx.app.log("TestLevel", "Received Spawn: "
					 +arg1.entityID+" x "+arg1.x+" y "+arg1.y+" rot "+arg1.rot);
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
		engine.addSystem(new CameraSystem(-8.199994f, -0.8999984f));
		engine.addSystem(new CameraZoomSystem(19.499977f));
		engine.addSystem(new CameraMoveSystem());
		engine.addSystem(new InputSystem());
		CheckpointSystem checkpointSystem = new CheckpointSystem();

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

	private void spawnCart(SpawnData spawnData) {
		Gdx.app.log("TestLevel", "spawning cart: " + spawnData.entityID +" at: " +spawnData.x+" " +spawnData.y+" "+ spawnData.rot);
		Texture cartTexture = null;
		if(spawnData.entityID == 0)
			cartTexture = game.getTexture(TexturePaths.KART_RED);
		if(spawnData.entityID == 1)
			cartTexture = game.getTexture(TexturePaths.KART_GRAY);
		if(spawnData.entityID == 2)
			cartTexture = game.getTexture(TexturePaths.KART_SILVER);
		if(spawnData.entityID == 3)
			cartTexture = game.getTexture(TexturePaths.KART_BLUE);
		engine.addEntity(EntityBuilder.buildKart(spawnData.entityID,
				spawnData.x, spawnData.y, spawnData.rot,
				cartTexture, spawnData.localControl)
				.add(new CameraTargetComponent()));
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