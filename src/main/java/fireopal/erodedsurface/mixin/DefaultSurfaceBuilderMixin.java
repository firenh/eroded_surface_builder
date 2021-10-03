package fireopal.erodedsurface.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.DefaultSurfaceBuilder;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import fireopal.erodedsurface.NewDefaultSurfaceBuilder;

@Mixin(DefaultSurfaceBuilder.class)
abstract class DefaultSurfaceBuilderMixin {
	@Redirect(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/surfacebuilder/DefaultSurfaceBuilder;generate(Ljava/util/Random;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/biome/Biome;IIIDLnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;II)V"))
    private void redirectThisGenerate(DefaultSurfaceBuilder self, Random random, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState fluidBlock, BlockState topBlock, BlockState underBlock, BlockState underwaterBlock, int seaLevel, int i) {
		NewDefaultSurfaceBuilder.generate(self, random, chunk, biome, x, z, height, noise, defaultBlock, fluidBlock, topBlock, underBlock, underwaterBlock, seaLevel, i);
    }
}
