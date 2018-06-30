package tomcom.kartGame.scenes;

import tomcom.kartGame.components.IDComponent;
import tomcom.kartGame.components.InputComponent;
import tomcom.kartGame.components.PivotComponent;
import tomcom.kartGame.components.NetworkIdentityComponent;
import tomcom.kartGame.components.SpriteComponent;
import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.game.GameMain;
import tomcom.kartGame.systems.Box2DPhysicsSystem;
import tomcom.kartGame.systems.Box2DRenderingSystem;
import tomcom.kartGame.systems.CameraSystem;
import tomcom.kartGame.systems.EntityManagerSystem;
import tomcom.kartGame.systems.InputSystem;
import tomcom.kartGame.systems.PivotUpdateSystem;
import tomcom.kartGame.systems.RenderingSystem;
import tomcom.kartGame.systems.Network.GameControllerSystem;
import tomcom.kartGame.systems.Network.NetworkingSystem;
import tomcom.kartGame.systems.Network.DataContainer.SpawnData;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.quantumreboot.ganet.Peer;


public class TestLevel implements Screen {

	private final boolean  IS_SERVER = false;
	
	private GameMain game;

	private Engine engine;

	public TestLevel(GameMain game) {
		this.game = game;

		engine = new Engine();

		initSystems();

		initEntities();
	}

	private void initEntities() {
		
		addBG();
		if(IS_SERVER) {
			addKart(true);
		}
	}

	private void addBG() {
		Entity bg = new Entity();
		bg.add(new PivotComponent(new Vector2(0, 0)));
		bg.add(new SpriteComponent("BG.png"));
		engine.addEntity(bg);
	}
	
	private SpawnData addKart(boolean localPlayer) {
		SpriteComponent spriteComponent = new SpriteComponent("kart.png");
		Entity kart = new Entity();
		
		kart.add(spriteComponent);
		IDComponent idcomp = new IDComponent();
		kart.add(idcomp);
		PivotComponent pivot = new PivotComponent(new Vector2(10+100*idcomp.id, 10));
		kart.add(pivot);
		kart.add(new NetworkIdentityComponent());
		if(localPlayer) {
			kart.add(new InputComponent(new int[] { Input.Keys.A, Input.Keys.D,
					Input.Keys.W, Input.Keys.S, Input.Keys.SPACE }));
		}
		Body2DComponent body =new Body2DComponent().setDynamic(true);
		
		kart.add(body);

		engine.addEntity(kart);
		SpawnData data = new SpawnData(idcomp.id,pivot.getPos().x, pivot.getPos().y);
		return data;
	}
	
	private void addKart(boolean localPlayer,int entityId, float x,float y) {
		SpriteComponent spriteComponent = new SpriteComponent("kart.png");
		Entity kart = new Entity();
		
		kart.add(new PivotComponent(new Vector2(x, y)));
		kart.add(spriteComponent);
		kart.add(new IDComponent(entityId));
		kart.add(new NetworkIdentityComponent());
		if(localPlayer) {
		kart.add(new InputComponent(new int[] { Input.Keys.A, Input.Keys.D,
				Input.Keys.W, Input.Keys.S, Input.Keys.SPACE }));
		}
		Body2DComponent body =new Body2DComponent().setDynamic(true);
		body.setPosition(new Vector2(x,y));
		kart.add(body);
		

		engine.addEntity(kart);
	}

	private List<SpawnData> getAllSpawnedCarts(){
		ImmutableArray<Entity> carts = engine.getEntitiesFor(Family.all(Body2DComponent.class)
				.get());
		List<SpawnData> data = new ArrayList<SpawnData>();
		Gdx.app.log("Server","CARTs" + carts.size());
		
		for(Entity cart: carts) {
			
			int id = cart.getComponent(IDComponent.class).id;
			PivotComponent pivot = cart.getComponent(PivotComponent.class);
			float x = pivot.getPos().x;
			float y = pivot.getPos().y;
			data.add(new SpawnData(id,x,y));
		}
		return data;
		
	}
	private void initSystems() {
		engine.addSystem(new EntityManagerSystem());
		
		engine.addSystem(new InputSystem());
		engine.addSystem(new Box2DPhysicsSystem());
		engine.addSystem(new GameControllerSystem());
		if(IS_SERVER)
			engine.addSystem(new PivotUpdateSystem());
		
		NetworkingSystem ns = new NetworkingSystem(IS_SERVER);
		if(IS_SERVER) {
			ns.onSpawnOnServer.add(new Listener<Peer>(){

				@Override
				public void receive(Signal<Peer> arg0, Peer arg1) {
					Gdx.app.log("Server","Spawned Karts:" +getAllSpawnedCarts().size());
					for(SpawnData data : getAllSpawnedCarts())
						ns.sendSpawnInfo(null, data);
					SpawnData newSpawnedCar= addKart(false);
					ns.sendSpawnInfo(arg1, newSpawnedCar);
					
				}
				
			});
		}
		else {
			ns.onSpawnOnClient.add(new Listener<SpawnData>(){
				@Override
				public void receive(Signal<SpawnData> arg0, SpawnData arg1) {
					addKart(arg1.localControl,arg1.entityID, arg1.x,arg1.y );
					
				}
				
			});
		}
		
		engine.addSystem(ns);
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
		//Gdx.app.log("TestLevel ", "updateEngine");
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
