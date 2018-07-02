package tomcom.kartGame.components.collision;

import com.badlogic.gdx.physics.box2d.Contact;

public interface CollisionListeningSystem {

	public void onBeginContact(Contact contact);

}
