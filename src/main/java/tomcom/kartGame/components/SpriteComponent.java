package tomcom.kartGame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component {

	private Sprite sprite;

	public SpriteComponent(Texture texture, float widthInWorldUnits,
			float heightInWorldUnits) {
		sprite = new Sprite(texture);
		sprite.setSize(widthInWorldUnits, heightInWorldUnits);
		sprite.setOriginCenter();
	}

	public Sprite getSprite() {
		return sprite;
	}

}
