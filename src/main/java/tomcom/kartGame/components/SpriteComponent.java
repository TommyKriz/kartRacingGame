package tomcom.kartGame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component {

	private Sprite sprite;

	public SpriteComponent(String textureName, float widthInWorldUnits,
			float heightInWorldUnits) {
		sprite = new Sprite(new Texture(Gdx.files.internal(textureName)));
		sprite.setSize(widthInWorldUnits, heightInWorldUnits);
	}

	public Sprite getSprite() {
		return sprite;
	}

}
