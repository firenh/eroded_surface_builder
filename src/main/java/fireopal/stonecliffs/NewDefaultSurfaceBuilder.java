package fireopal.stonecliffs;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.DefaultSurfaceBuilder;

public class NewDefaultSurfaceBuilder {
    public static void generate(DefaultSurfaceBuilder self, Random random, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState fluidBlock, BlockState topBlock, BlockState underBlock, BlockState underwaterBlock, int seaLevel, int i) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
		int j = (int)(noise / 3.0D + 3.0D + random.nextDouble() * 0.25D);
      	int k;
		BlockState blockState5;

        BlockPos targetPos = new BlockPos(x, height - 1, z);
        BlockPos.Mutable newTargetPos;
        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        int validSlope = 0;
        boolean isOnChunkBorder = (x % 16 == 0 || x % 16 == 15) && (z % 16 == 0 || z % 16 == 15);

        for (Direction d : directions) {
            newTargetPos = targetPos.offset(d).mutableCopy();

            if (!(chunk.getBlockState(newTargetPos).isAir()) && chunk.getBlockState(newTargetPos.up()).isAir()) {
                validSlope += 3;
            } else if ((!(chunk.getBlockState(newTargetPos.up()).isAir()) && chunk.getBlockState(newTargetPos.up(2)).isAir())) {
                validSlope += 2;
            } else if ((!(chunk.getBlockState(newTargetPos.down()).isAir()) && chunk.getBlockState(newTargetPos).isAir())) {
                validSlope += 2;
            }
        }
        
        boolean canGenerate = isOnChunkBorder ? validSlope >= 1 : validSlope >= 6 || validSlope == 0;

        if (canGenerate) { 
            if (j == 0) {
                boolean bl = false;

                for (k = height; k >= i; --k) {
                    mutable.set(x, k, z);
                    BlockState blockState = chunk.getBlockState(mutable);
                    if (blockState.isAir()) {
                        bl = false;
                    } else if (blockState.isOf(defaultBlock.getBlock())) {
                        if (!bl) {
                            if (k >= seaLevel) {
                                blockState5 = Blocks.AIR.getDefaultState();
                            } else if (k == seaLevel - 1) {
                                blockState5 = biome.getTemperature(mutable) < 0.15F ? Blocks.ICE.getDefaultState() : fluidBlock;
                            } else {
                                blockState5 = underwaterBlock;
                            }

                            chunk.setBlockState(mutable, blockState5, false);
                        }

                        bl = true;
                    }
                }
            } else {
                BlockState blockState6 = underBlock;
                k = -1;

                for (int m = height; m >= i; --m) {
                    mutable.set(x, m, z);
                    blockState5 = chunk.getBlockState(mutable);

                    if (blockState5.isAir()) {
                        k = -1;
                    } else if (blockState5.isOf(defaultBlock.getBlock())) {
                        if (k == -1) {
                            k = j;
                            BlockState blockState12;

                            if (m >= seaLevel + 2) {
                                blockState12 = topBlock;
                            } else if (m >= seaLevel - 1) {
                                blockState6 = underBlock;
                                blockState12 = topBlock;
                            } else if (m >= seaLevel - (7 + j)) {
                                blockState12 = blockState6;
                            } else {
                                blockState6 = defaultBlock;
                                blockState12 = underwaterBlock;
                            }

                            chunk.setBlockState(mutable, blockState12, false);
      
                        } else if (k > 0) {
                            --k;
                            chunk.setBlockState(mutable, blockState6, false);

                            if (k == 0 && blockState6.isOf(Blocks.SAND) && j > 1) {
                                k = random.nextInt(4) + Math.max(0, m - seaLevel);
                                blockState6 = blockState6.isOf(Blocks.RED_SAND) ? Blocks.RED_SANDSTONE.getDefaultState() : Blocks.SANDSTONE.getDefaultState();
                            }
                        }
                    }
                }
            }
        }
    }
}
