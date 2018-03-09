import com.nshirley.engine3d.math.Vector3f;
import com.nshirley.engine3d.math.Vector3i;

import engine.SimEntity;
import engine.Simulator;
import physics.PhysRect;
import world.Raycast;

public class MoveEntity extends SimEntity {
	
	public PhysRect rect;
	public double timer = 0;
	public int dir = 0;
	public double life = 4000;
	
	public MoveEntity(Vector3f pos) {
		IslandSim.bunniesBorn++;
		rect = new PhysRect(new Vector3f(.5f, .5f, .5f), pos, null);
	}
	
	public void setUp(Simulator s) {
		s.physics.AddRect(rect);
	}
	
	public void tearDown(Simulator s) {
		IslandSim.bunniesDied++;
		s.physics.RemoveRect(rect);
	}
	
	public void update(Simulator s, float delta) {
		life -= delta;
		if (life < 0) {
			s.setRemoveFlag(this.getID());
			return;
		}
		timer -= delta;
		if (timer < 0) {
			timer += Math.random() * 100 + 50;
			dir = (int) (Math.random() * 4);
		}
		Vector3f v = rect.getVelocity();
		if (dir < 2) {
			v.z = 0;
			v.x = ((dir * 2) - 1) * .03f;
		} else {
			v.x = 0;
			v.z = (((dir % 2) * 2) - 1) * .03f;
		}
		
		Vector3f face = v.clone();
		face.y = 0;

		Raycast val = s.world.raycast(rect.getPosition(), face, 3);
		if (val != null) {
			if (val.position.sub(rect.getPosition()).mag() < .5) {
				v.y = .13f;
			}
		}
		rect.setVelocity(v);
		
		face.y = -Math.abs(face.x) - Math.abs(face.z);
		val = s.world.raycast(rect.getPosition(), face, 3, new int[] {0, 1});
		if (val != null) {
			if (val.position.sub(rect.getPosition()).mag() < 3) {
				if (s.world.getBlockValue(val.blockPosition.x, val.blockPosition.y, val.blockPosition.z) == 7) {
					if (dir < 2) {
						dir = 1 - dir;
					} else {
						dir = 5 - dir;
					}
				}
			}
		}
		
		if (life < 200) {
			Vector3f curPos = this.rect.getPosition();
			int px = (int) Math.floor(curPos.x);
			int py = (int) Math.floor(curPos.y);
			int pz = (int) Math.floor(curPos.z);
			
			if (s.world.getBlockValue(px, py, pz) == 12) {
				s.world.setBlockValue(new Vector3i(px, py, pz), (short) 0);
				IslandSim.grassEaten++;
				s.add(new MoveEntity(curPos.clone()));
				s.add(new MoveEntity(curPos.clone()));
				s.setRemoveFlag(this.getID());
				return;
			}
		}

		//System.out.println(rect.getVelocity() + " " + rect.getPosition());
	}
	
	public void render() {
		
	}
}
