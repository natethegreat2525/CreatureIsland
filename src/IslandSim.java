

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFW;

import world.ChunkBuilderThread;
import world.Player;
import world.Raycast;
import world.World;
import chunks.ChunkViewport;

import com.nshirley.engine3d.N3D;
import com.nshirley.engine3d.entities.Camera3d;
import com.nshirley.engine3d.entities.Mesh;
import com.nshirley.engine3d.entities.shapes.Shape;
import com.nshirley.engine3d.graphics.Texture;
import com.nshirley.engine3d.math.Matrix4f;
import com.nshirley.engine3d.math.Vector3f;
import com.nshirley.engine3d.math.Vector3i;
import com.nshirley.engine3d.window.Input;
import com.nshirley.engine3d.window.Mouse;
import com.nshirley.engine3d.window.Window;

import drawentity.ChunkEntity;
import drawentity.FadeEntity;
import engine.PlayerEntity;
import engine.Simulator;
import engine.StaticEntityManager;
import physics.PhysSim;
import physics.Rect;
import voxels.VoxelData;
import voxels.VoxelDrawBuilder;

public class IslandSim {
	
	public static int grassAdded, grassEaten;
	public static int bunniesBorn, bunniesDied;

	public static int WIDTH = 1024, HEIGHT = 768;

	public static void main(String[] args) throws InterruptedException {
		Window win = new Window(WIDTH, HEIGHT, "Player Test");
		win.setCursorMode(GLFW.GLFW_CURSOR_DISABLED);

		N3D.init();
		ChunkEntity.loadShader();
		FadeEntity.loadShader();

		Texture tx = new Texture("res/blocks_a.png");
		Texture blank = new Texture("res/blank.png");

		Blocks.init();

		Mesh bunny = VoxelDrawBuilder.generateChunkEntity(
				VoxelData.fromStream(IslandSim.class.getClassLoader().getResourceAsStream("bunny.vox")),
				tx
				);
		
		Camera3d c = new Camera3d((float) Math.toRadians(100), WIDTH, HEIGHT,
				.1f, 1000);
		
		Mesh box = new Mesh(Shape.cube(), blank);

		World world = new World(new IslandBuilder(10, 80, 300, 0));
		ChunkViewport cv = new ChunkViewport(new Vector3i(), new Vector3i(5, 3, 5), world, tx);		
		Simulator sim = new Simulator(world, cv, new Vector3f(0, -.01f, 0), box, new StaticEntityManager());
		
		Raycast playerStart = world.raycast(new Vector3f(.5f, 30, .5f), new Vector3f(0, -1, 0), 30);
		playerStart.position.y += .5;

		PlayerEntity player = new PlayerEntity(box, playerStart.position, new Vector3f(.5f, 1.5f, .5f), c);
		sim.em.add(player);
		
		sim.em.add(new GrassGrower());
		
		FadeEntity fade = new FadeEntity();
		
		long deltaTime = System.currentTimeMillis();
		float delta = 1;
		glClearColor(.7f, 1, 1, 1);
		
		int timer = 0;
		boolean fast = false;
		
		while (!win.shouldClose()) {
			long newDelta = System.currentTimeMillis();
			delta = (newDelta - deltaTime) / (1000 / 60.0f);
			long diff = newDelta - deltaTime;

			deltaTime = newDelta;
			delta = Math.min(delta, 4);
			
			//System.out.println(diff);
			if (diff < 15) {
				Thread.sleep(15 - diff);
			}
			
			if (timer >= 5*60) {
				timer -= 5*60;
				int bunnies = IslandSim.bunniesBorn - IslandSim.bunniesDied;
				int grass = IslandSim.grassAdded - IslandSim.grassEaten;
				System.out.println(bunnies + ", " + grass);
			}

			win.clear();
			win.pollEvents();
			
			if (Input.isKeyHit(GLFW.GLFW_KEY_M)) {
				sim.em.add(new MoveEntity(player.headPos.clone(), bunny));
			}
			
			if (Input.isKeyHit(GLFW.GLFW_KEY_F))
				fast = !fast;
			
			int iterations = 1;
			if (fast)
				iterations = 60;
			
			timer += iterations;
			for (int i = 0; i < iterations; i++)
				sim.update(1);
			
			N3D.pushMatrix();
			N3D.multMatrix(c.getTotalMatrix());
			
			if (Input.isKeyDown(GLFW.GLFW_KEY_Q)) {
				glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
			} else {
				glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
			}
			
			sim.render(c.getPosition(), c.getLookDir());

			N3D.popMatrix();
			
			Vector3f pos = player.player.getPosition();
			boolean underwater = world.getBlockValue((int) Math.floor(pos.x), (int) Math.floor(pos.y + 1), (int) Math.floor(pos.z)) == 7;
			if (underwater)
				fade.render(0.0f, 0.0f, 0.5f, 0.5f);
			
			
			int i = glGetError();
			if (i != GL_NO_ERROR) {
				System.out.println(i);
			}
			
			win.flip();
			if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE))
				break;
		}
		sim.finish();
	}
}