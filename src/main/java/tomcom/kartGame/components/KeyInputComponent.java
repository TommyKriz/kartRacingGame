package tomcom.kartGame.components;

import com.badlogic.ashley.core.Component;

public class KeyInputComponent implements Component {

	/**
	 * LEFT RIGHT UP DOWN NITRO
	 */
	private int[] keys;

	/**
	 * @param keys
	 *            LEFT RIGHT UP DOWN NITRO
	 */
	public KeyInputComponent(int[] keys) {
		this.keys = keys;
	}

	public int[] getKeys() {
		return keys;
	}

}
