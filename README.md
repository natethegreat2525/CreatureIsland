# CreatureIsland

Depends on LWJGL, 3dEngine and BlockGame repos

Creatures (bunnies) are simulated on an island with limited resources (grass).

Simulation rules:
Bunnies eat grass to reproduce
When a bunny eats grass, it dies and is replaced by 2 new bunnies.
A bunny must wait some number of frames to eat grass after it is born. (prevents bunnies from eating all grass immediately)
Each bunny has a maximum lifespan. If the bunny does not find grass in its life it will die.
Bunnies can move in the 4 cardinal directions and jump up blocks in front of them.
Bunnies avoid water by turning around if they detect water.
Every frame, a random x,y coordinate is chosen in a 200x200 square around the origin. If that square contains grass then some number of points around that grass block are chosen. Each point that grass can grow (is above water and is green) will get a new grass block.

