package tomcom.kartGame.components;

import com.badlogic.ashley.core.Component;

/**
 * 
 * @author keppi
 *
 */
public class IDComponent implements Component{

	public final int id;
	public static int idCounter = 0;
	
	public IDComponent() {
		id = idCounter++;
	}
	public IDComponent(int id) {
		this.id=id;
//		id = idCounter++;
	}
	
	
}
