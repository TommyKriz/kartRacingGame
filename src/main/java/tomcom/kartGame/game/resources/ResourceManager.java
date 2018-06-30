package tomcom.kartGame.game.resources;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ResourceManager {

	private final static ResourceManager instance = new ResourceManager();

	private static final String SKIN_FILEPATH = "skin/glassy-ui.json";

	private static HashMap<String, Texture> textures;

	private static Skin skin;

	private ResourceManager() {
		textures = new HashMap<>();
		initTextures();
		skin = new Skin(Gdx.files.internal(SKIN_FILEPATH));
	}

	private void initTextures() {
		textures.put(TextureKeys.KART, new Texture(TexturePaths.KART));
		textures.put(TextureKeys.WHEEL, new Texture(TexturePaths.WHEEL));
		textures.put(TextureKeys.ROADBLOCK, new Texture(TexturePaths.ROADBLOCK));
		textures.put(TextureKeys.MAP, new Texture(TexturePaths.MAP));
		textures.put(TextureKeys.VIENNA_MAP, new Texture(
				TexturePaths.VIENNA_MAP));
		textures.put(TextureKeys.LAMBO, new Texture(TexturePaths.LAMBO));
		textures.put(TextureKeys.HAGENBERG_MAP, new Texture(
				TexturePaths.HAGENBERG_MAP));
	}

	public static Texture getTexture(String key) {
		return textures.get(key);
	}

	public static Skin getSkin() {
		return skin;
	}
}
