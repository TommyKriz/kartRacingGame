package tomcom.kartGame.systems;

import tomcom.kartGame.components.IDComponent;
import tomcom.kartGame.components.InputComponent;
import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.systems.Network.DataContainer.ForceInputData;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;


public class InputSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(InputComponent.class).get();
	
	public Signal<ForceInputData> onApplyForceInput = new Signal<ForceInputData>();

	private ComponentMapper<InputComponent> ic = ComponentMapper
			.getFor(InputComponent.class);
	private ComponentMapper<IDComponent> idcomp = ComponentMapper
			.getFor(IDComponent.class);

	public InputSystem() {
		super(FAMILY);
	}
	

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		int[] keys = ic.get(entity).getKeys();
		int id = idcomp.get(entity).id;

		if (Gdx.input.isKeyPressed(keys[0])) {
			onApplyForceInput.dispatch(new ForceInputData(id, -1f,0f));
		}
		if (Gdx.input.isKeyPressed(keys[1])) {
			onApplyForceInput.dispatch(new ForceInputData(id,1f,0f));
		}
		if (Gdx.input.isKeyPressed(keys[2])) {
			onApplyForceInput.dispatch(new ForceInputData(id,0f,1f));
		}
		if (Gdx.input.isKeyPressed(keys[3])) {
			onApplyForceInput.dispatch(new ForceInputData(id,0f,-1f));
		}


	}
}
