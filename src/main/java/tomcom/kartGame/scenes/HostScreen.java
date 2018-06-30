package tomcom.kartGame.scenes;

import tomcom.kartGame.game.GameMain;
import tomcom.kartGame.game.resources.ResourceManager;
import tomcom.kartGame.game.resources.TextureKeys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * 
 * Choose between Maps and display IP-Address.
 * 
 * @author Tommy
 *
 */
public class HostScreen implements Screen {

	private Stage stage;

	private Sprite level1Preview = new Sprite(
			ResourceManager.getTexture(TextureKeys.LEVEL1_PREVIEW));
	private Sprite level2Preview = new Sprite(
			ResourceManager.getTexture(TextureKeys.LEVEL2_PREVIEW));
	private Sprite level3Preview = new Sprite(
			ResourceManager.getTexture(TextureKeys.LEVEL3_PREVIEW));

	private int selectedLevel = 1;

	public HostScreen(GameMain game) {
		stage = new Stage();

		Skin mySkin = ResourceManager.getSkin();

		Gdx.input.setInputProcessor(stage);

		TextButton lastLevel = new TextButton("<", mySkin);
		lastLevel.setBounds(220, 220, 100, 100);
		lastLevel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (selectedLevel - 1 >= 1) {
					selectedLevel--;
				}
			}
		});
		stage.addActor(lastLevel);

		TextButton nextLevel = new TextButton(">", mySkin);
		nextLevel.setBounds(620, 220, 100, 100);
		nextLevel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (selectedLevel + 1 <= 3) {
					selectedLevel++;
				}
			}
		});
		stage.addActor(nextLevel);

		TextButton back = new TextButton("Back", mySkin);
		back.setBounds(500, 60, 100, 100);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
			}
		});
		stage.addActor(back);

		initSprites();

	}

	private void initSprites() {
		level1Preview.setBounds(280, 220, 300, 300);
		level2Preview.setBounds(280, 220, 300, 300);
		level3Preview.setBounds(280, 220, 300, 300);
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		stage.act(delta);

		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.getBatch().begin();

		switch (selectedLevel) {
		case 1:
			level1Preview.draw(stage.getBatch());
			break;
		case 2:
			level2Preview.draw(stage.getBatch());
			break;
		case 3:
			level3Preview.draw(stage.getBatch());
			break;
		}

		stage.getBatch().end();

		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
