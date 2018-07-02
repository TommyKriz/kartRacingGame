package tomcom.kartGame.game.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetManagerHolder {

	private static final String SKIN_FILEPATH = "skin/uiskin.json";

	private AssetManager manager;

	public AssetManagerHolder() {
		manager = new AssetManager();
		loadSkin();
		loadTextures();
		manager.finishLoading();
	}

	private void loadSkin() {
		manager.load(SKIN_FILEPATH, Skin.class);
	}

	public Skin getSkin() {
		return manager.get(SKIN_FILEPATH, Skin.class);
	}

	private void loadTextures() {
		loadTexture(TexturePaths.MAIN_MENU_BG);
		loadTexture(TexturePaths.KART);
		loadTexture(TexturePaths.WHEEL);
		loadTexture(TexturePaths.ROADBLOCK);
		loadTexture(TexturePaths.MAP);
		loadTexture(TexturePaths.VIENNA_MAP);
		loadTexture(TexturePaths.LAMBO);
		loadTexture(TexturePaths.HAGENBERG_MAP);
		loadTexture(TexturePaths.LEVEL2_PREVIEW);
		loadTexture(TexturePaths.LEVEL3_PREVIEW);
	}

	public Texture getTexture(String filePath) {
		return manager.get(filePath, Texture.class);
	}

	private void loadTexture(String filePath) {
		manager.load(filePath, Texture.class);
	}

	public void dispose() {
		manager.clear();
	}

}
