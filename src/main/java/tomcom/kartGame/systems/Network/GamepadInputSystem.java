package tomcom.kartGame.systems.Network;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class GamepadInputSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(
			XboxControllerInputComponent.class, ControllerInputComponent.class)
			.get();

	private ComponentMapper<ControllerInputComponent> cc = ComponentMapper
			.getFor(ControllerInputComponent.class);

	public GamepadInputSystem() {
		super(FAMILY);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		for (Controller controller : Controllers.getControllers()) {
			cc.get(entity).updateInput(controller.getAxis(1),
					controller.getAxis(4));
		}
	}
}