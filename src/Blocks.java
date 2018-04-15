import com.nshirley.engine3d.math.Vector3f;
import com.nshirley.engine3d.math.Vector4f;

import blockdraw.AirBlock;
import blockdraw.BlockContainer;
import blockdraw.LowPolyBlock;
import blockdraw.NullBlock;
import blockdraw.UniformBlock;
import blockdraw.WaterBlock;


public class Blocks {
	public static void init() {
		//Set first block type to be air
		BlockContainer.blockTypes[0] = new AirBlock();
		//set up next 10 block types to be uniform
		for (int i = 0; i < 15; i++) {
			BlockContainer.blockTypes[i + 1] = new UniformBlock(i, 4, 4, new Vector4f(1, 1, 1, 1));
		}
		
		BlockContainer.blockTypes[BlockContainer.NUM_BLOCK_TYPES - 1] = new NullBlock();
		
		BlockContainer.blockTypes[7] = new WaterBlock(6, 4, 4, new Vector4f(1, .5f, .7f, .8f));
		BlockContainer.blockTypes[8] = new LowPolyBlock(7, 4, 4, new Vector4f(1, 1, 1, 1), new Vector3f(.3f, 0, .3f), new Vector3f(.7f, .6f, .7f), 0);
		BlockContainer.blockTypes[11] = new LowPolyBlock(10, 4, 4, new Vector4f(1, 1, 1, 1), new Vector3f(.3f, 0, .3f), new Vector3f(.7f, .6f, .7f), 0);
		BlockContainer.blockTypes[12] = new LowPolyBlock(11, 4, 4, new Vector4f(1, 1, 1, 1), 0);
		
		BlockContainer.blockTypes[10] = new UniformBlock(9, 4, 4, new Vector4f(0, .6f, .1f, 1));
		BlockContainer.blockTypes[13] = new UniformBlock(8, 4, 4, new Vector4f(.95f, .9f, .7f, 1));

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 4; k++) {
					BlockContainer.blockTypes[100 + (i * 16 + j * 4 + k)] = new UniformBlock(15, 4, 4, new Vector4f(i/3.0f, j/3.0f, k/3.0f, 1));
				}
			}
		}


	}
}
