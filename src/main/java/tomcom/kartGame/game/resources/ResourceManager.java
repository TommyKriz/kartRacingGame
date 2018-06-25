package tomcom.kartGame.game.resources;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;

public class ResourceManager {

	private final static ResourceManager instance = new ResourceManager();

	private static HashMap<String, Texture> textures;

	private ResourceManager() {
		textures = new HashMap<>();
		initTextures();
	}

	private void initTextures() {
		textures.put(TextureKeys.KART, new Texture(TexturePaths.KART));
		textures.put(TextureKeys.WHEEL, new Texture(TexturePaths.WHEEL));
		textures.put(TextureKeys.ROADBLOCK, new Texture(TexturePaths.ROADBLOCK));
		textures.put(TextureKeys.MAP, new Texture(TexturePaths.MAP));
		textures.put(TextureKeys.VIENNA_MAP, new Texture(TexturePaths.VIENNA_MAP));
		textures.put(TextureKeys.LAMBO, new Texture(TexturePaths.LAMBO));
		textures.put(TextureKeys.HAGENBERG_MAP, new Texture(TexturePaths.HAGENBERG_MAP));
	}

	public static Texture getTexture(String key) {
		return textures.get(key);
	}

}
