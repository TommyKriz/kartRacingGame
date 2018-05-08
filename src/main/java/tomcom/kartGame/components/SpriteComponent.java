package tomcom.kartGame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component {

	private Sprite sprite;

	public SpriteComponent(String textureName) {
		sprite = new Sprite(new Texture(textureName));
	}

	public Sprite getSprite() {
		return sprite;
	}

}
