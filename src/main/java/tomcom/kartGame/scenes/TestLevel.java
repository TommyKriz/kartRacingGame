package tomcom.kartGame.scenes;

import tomcom.kartGame.components.CameraTargetComponent;
import tomcom.kartGame.config.GameConfig;
import tomcom.kartGame.entities.EntityBuilder;
import tomcom.kartGame.game.GameMain;
import tomcom.kartGame.systems.Box2DPhysicsSystem;
import tomcom.kartGame.systems.Box2DRenderingSystem;
import tomcom.kartGame.systems.CameraMoveSystem;
import tomcom.kartGame.systems.CameraSystem;
import tomcom.kartGame.systems.CameraZoomSystem;
import tomcom.kartGame.systems.PivotUpdateSystem;
import tomcom.kartGame.systems.RenderingSystem;
import tomcom.kartGame.systems.TrackEditorSystem;
import tomcom.kartGame.systems.vehicle.VehicleGamepadInputDebugRendererSystem;

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
		// viewport.apply();
		// TODO: why is only this necessary () ?
		viewport.setWorldSize(GameConfig.WORLD_WIDTH_SEEN_THROUGH_CAMERA,
				GameConfig.WORLD_WIDTH_SEEN_THROUGH_CAMERA
						* (GameConfig.SCREEN_WIDTH / GameConfig.SCREEN_HEIGHT));

		initEntities();
	}

	private void initEntities() {
		engine.addEntity(EntityBuilder.buildMap());
		engine.addEntity(EntityBuilder.buildFinishLine(2, 2));
		engine.addEntity(EntityBuilder.buildKart(2, 2).add(
				new CameraTargetComponent()));
		initRoadblocks();
	}

	private void initRoadblocks() {
		engine.addEntity(EntityBuilder.buildRoadBlock(-16.866934f, -11.457262f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-16.760935f, -9.549273f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-16.124939f, -7.711947f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-15.64794f, -5.73329f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-15.223942f, -3.8252983f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-14.587946f, -1.9879742f));
		engine.addEntity(EntityBuilder
				.buildRoadBlock(-13.845949f, -0.07998252f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-12.573954f, 1.333344f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-11.0369625f, 2.4640055f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-9.4999695f, 3.6653318f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-7.8569775f, 4.654662f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-6.0019855f, 5.1493263f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-3.9349947f, 5.5026574f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-7.0619807f, -12.163927f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-6.8499823f, -10.255936f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-6.054985f, -8.41861f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-4.888992f, -6.863951f));
		engine.addEntity(EntityBuilder.buildRoadBlock(48.74507f, -7.005284f));
		engine.addEntity(EntityBuilder.buildRoadBlock(50.547062f, -6.2986207f));
		engine.addEntity(EntityBuilder.buildRoadBlock(52.243053f, -5.3092914f));
		engine.addEntity(EntityBuilder.buildRoadBlock(53.93905f, -4.39063f));
		engine.addEntity(EntityBuilder.buildRoadBlock(55.47604f, -3.189302f));
		engine.addEntity(EntityBuilder.buildRoadBlock(57.17203f, -2.2706394f));
		engine.addEntity(EntityBuilder.buildRoadBlock(58.550026f, -0.927979f));
		engine.addEntity(EntityBuilder.buildRoadBlock(59.66302f, 0.6266799f));
		engine.addEntity(EntityBuilder.buildRoadBlock(60.670017f, 2.5346718f));
		engine.addEntity(EntityBuilder.buildRoadBlock(61.359013f, 4.3719945f));
		engine.addEntity(EntityBuilder.buildRoadBlock(62.048008f, 6.2799864f));
		engine.addEntity(EntityBuilder.buildRoadBlock(62.20701f, 8.187978f));
		engine.addEntity(EntityBuilder.buildRoadBlock(48.215073f, 5.714656f));
		engine.addEntity(EntityBuilder.buildRoadBlock(49.911064f, 6.77465f));
		engine.addEntity(EntityBuilder.buildRoadBlock(51.50106f, 7.905313f));
		engine.addEntity(EntityBuilder.buildRoadBlock(52.296055f, 9.671969f));
		engine.addEntity(EntityBuilder.buildRoadBlock(61.88901f, 47.726776f));
		engine.addEntity(EntityBuilder.buildRoadBlock(61.46501f, 49.91743f));
		engine.addEntity(EntityBuilder.buildRoadBlock(61.14701f, 51.825424f));
		engine.addEntity(EntityBuilder.buildRoadBlock(60.458015f, 53.66275f));
		engine.addEntity(EntityBuilder.buildRoadBlock(59.61002f, 55.429405f));
		engine.addEntity(EntityBuilder.buildRoadBlock(58.550026f, 57.1254f));
		engine.addEntity(EntityBuilder.buildRoadBlock(57.967026f, 59.033386f));
		engine.addEntity(EntityBuilder.buildRoadBlock(57.225033f, 60.870712f));
		engine.addEntity(EntityBuilder.buildRoadBlock(56.00604f, 62.354706f));
		engine.addEntity(EntityBuilder.buildRoadBlock(54.363045f, 63.556034f));
		engine.addEntity(EntityBuilder.buildRoadBlock(52.508057f, 64.0507f));
		engine.addEntity(EntityBuilder.buildRoadBlock(50.600063f, 64.61603f));
		engine.addEntity(EntityBuilder.buildRoadBlock(48.58607f, 52.81475f));
		engine.addEntity(EntityBuilder.buildRoadBlock(50.282066f, 51.896088f));
		engine.addEntity(EntityBuilder.buildRoadBlock(51.23606f, 50.200096f));
		engine.addEntity(EntityBuilder.buildRoadBlock(52.137054f, 48.362774f));
		engine.addEntity(EntityBuilder.buildRoadBlock(18.59194f, 64.57994f));
		engine.addEntity(EntityBuilder.buildRoadBlock(16.73695f, 64.08528f));
		engine.addEntity(EntityBuilder.buildRoadBlock(14.8289585f, 63.16662f));
		engine.addEntity(EntityBuilder.buildRoadBlock(13.0799675f, 62.177288f));
		engine.addEntity(EntityBuilder.buildRoadBlock(11.595976f, 60.97596f));
		engine.addEntity(EntityBuilder.buildRoadBlock(9.952984f, 59.986633f));
		engine.addEntity(EntityBuilder.buildRoadBlock(8.945988f, 58.361305f));
		engine.addEntity(EntityBuilder.buildRoadBlock(8.0449915f, 56.38265f));
		engine.addEntity(EntityBuilder.buildRoadBlock(7.4089956f, 54.47466f));
		engine.addEntity(EntityBuilder.buildRoadBlock(6.6139984f, 52.637337f));
		engine.addEntity(EntityBuilder.buildRoadBlock(6.190001f, 50.729343f));
		engine.addEntity(EntityBuilder.buildRoadBlock(5.872f, 48.82135f));
		engine.addEntity(EntityBuilder.buildRoadBlock(5.3950043f, 46.842697f));
		engine.addEntity(EntityBuilder.buildRoadBlock(4.3880086f, 45.21737f));
		engine.addEntity(EntityBuilder.buildRoadBlock(2.7980165f, 44.086708f));
		engine.addEntity(EntityBuilder.buildRoadBlock(18.48594f, 52.637337f));
		engine.addEntity(EntityBuilder.buildRoadBlock(17.00195f, 51.36534f));
		engine.addEntity(EntityBuilder.buildRoadBlock(16.047953f, 49.38668f));
		engine.addEntity(EntityBuilder.buildRoadBlock(15.411957f, 47.54936f));
		engine.addEntity(EntityBuilder.buildRoadBlock(15.411957f, 45.5707f));
		engine.addEntity(EntityBuilder.buildRoadBlock(14.8289585f, 43.592045f));
		engine.addEntity(EntityBuilder.buildRoadBlock(14.245962f, 41.75472f));
		engine.addEntity(EntityBuilder.buildRoadBlock(13.556965f, 39.917393f));
		engine.addEntity(EntityBuilder.buildRoadBlock(12.549971f, 38.150734f));
		engine.addEntity(EntityBuilder.buildRoadBlock(11.489976f, 36.52541f));
		engine.addEntity(EntityBuilder.buildRoadBlock(9.793983f, 35.60675f));
		engine.addEntity(EntityBuilder.buildRoadBlock(8.150991f, 34.546753f));
		engine.addEntity(EntityBuilder.buildRoadBlock(6.2960005f, 33.981422f));
		engine.addEntity(EntityBuilder.buildRoadBlock(4.547008f, 32.85076f));
		engine.addEntity(EntityBuilder.buildRoadBlock(2.5860164f, 32.28543f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-42.44706f, 31.738636f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-44.196056f, 30.819975f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-45.203053f, 29.053314f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-45.41505f, 27.145325f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-43.29506f, 43.893246f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-44.99105f, 42.83325f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-47.16404f, 42.691917f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-48.86003f, 41.70259f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-49.867027f, 40.077263f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-51.139023f, 38.522606f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-52.252014f, 36.755943f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-53.47101f, 35.27195f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-54.319008f, 33.36396f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-55.008003f, 31.526636f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-55.273003f, 29.54798f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-55.485f, 27.569323f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-45.808155f, -11.241372f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-46.126152f, -13.22003f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-46.974148f, -15.128021f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-47.663147f, -17.03601f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-48.617138f, -18.944002f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-49.730133f, -20.781328f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-51.05513f, -22.406652f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-52.645123f, -23.81998f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-54.447113f, -24.667974f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-56.408104f, -24.879974f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-57.945095f, -26.151968f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-59.800087f, -26.646633f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-55.348106f, -9.192048f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-55.613106f, -11.170706f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-56.4611f, -12.937365f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-58.104095f, -13.997358f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-59.959087f, -14.562689f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-66.66416f, -26.84668f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-68.30716f, -27.836006f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-69.42015f, -29.461332f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-69.84415f, -31.369324f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-67.19416f, -14.692068f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-69.10215f, -15.186731f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-71.01014f, -15.610733f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-72.65314f, -16.812057f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-74.08413f, -18.084053f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-75.46212f, -19.426712f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-76.89311f, -20.910707f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-77.847115f, -22.606697f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-78.32411f, -24.51469f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-78.960106f, -26.352015f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-79.490105f, -28.33067f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-80.1791f, -30.167995f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-79.27811f, -31.863989f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-69.84311f, -47.902786f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-69.63111f, -50.09344f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-68.624115f, -51.860096f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-66.981125f, -52.99076f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-79.33006f, -48.185448f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-79.27707f, -50.16411f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-78.64107f, -52.00143f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-78.323074f, -53.909424f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-77.95207f, -55.958748f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-77.36908f, -57.79607f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-76.36208f, -59.49206f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-75.19608f, -61.046722f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-73.50009f, -61.965385f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-71.963104f, -63.096046f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-70.05511f, -63.59071f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-68.09412f, -63.944042f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-66.20701f, -63.661377f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-63.87502f, -63.802708f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-20.805965f, -64.79204f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-18.632975f, -64.50937f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-16.724985f, -63.873375f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-15.134992f, -62.742714f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-13.439001f, -61.82405f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-12.061007f, -60.410725f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-10.736012f, -58.92673f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-9.19902f, -57.79607f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-8.351025f, -56.02941f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-7.6620274f, -54.12142f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-7.4500284f, -52.213432f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-7.397028f, -50.23477f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-21.282963f, -52.566765f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-19.321972f, -52.354763f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-17.94398f, -51.012104f));
		engine.addEntity(EntityBuilder.buildRoadBlock(-16.777985f, -49.457443f));
	}

	private void initSystems() {

		engine.addSystem(new CameraSystem(-8.199994f, -0.8999984f));
		engine.addSystem(new CameraZoomSystem(19.499977f));
		engine.addSystem(new CameraMoveSystem());

		engine.addSystem(new Box2DPhysicsSystem());
		engine.addSystem(new PivotUpdateSystem());

		engine.addSystem(new RenderingSystem());
		engine.addSystem(new Box2DRenderingSystem());

		// TODO: !!!
		// engine.addSystem(new WheelSystem());
		// engine.addSystem(new WheelRenderingSystem());
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