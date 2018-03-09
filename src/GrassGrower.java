import com.nshirley.engine3d.math.Vector3f;
import com.nshirley.engine3d.math.Vector3i;

import engine.SimEntity;
import engine.Simulator;
import physics.PhysRect;
import world.Raycast;

public class GrassGrower extends SimEntity {
	
	
	public GrassGrower() {
	}
	
	public void setUp(Simulator s) {
	}
	
	public void tearDown(Simulator s) {
	}
	
	public void update(Simulator s, float delta) {
		int x = (int) (Math.random() * 200 - 100);
		int z = (int) (Math.random() * 200 - 100);
		
		Raycast rc = s.world.raycast(new Vector3f(x, 30, z), new Vector3f(0, -1, 0), 31, new int[] {0, 1, 2});
		
		if (rc == null) {
			return;
		}
		
		short bv = s.world.getBlockValue(rc.blockPosition.x, rc.blockPosition.y, rc.blockPosition.z);
		if (bv == 12) {
			for (int i = 0; i < 3; i++) {
				//big grass, yay
				int px = x + (int) (Math.random() * 11 - 5);
				int pz = z + (int) (Math.random() * 11 - 5);
				
				rc = s.world.raycast(new Vector3f(px, 30, pz), new Vector3f(0, -1, 0), 31, new int[] {0, 1, 2});
				if (rc == null) {
					continue;
				}
				bv = s.world.getBlockValue(rc.blockPosition.x, rc.blockPosition.y, rc.blockPosition.z);
	
				if (bv == 10) {
					//grass block, yay
					rc.blockPosition.y++;
					s.world.setBlockValue(rc.blockPosition, (short) 12);
					IslandSim.grassAdded++;
				}
			}
		}
	}
	
	public void render() {
	}
}
