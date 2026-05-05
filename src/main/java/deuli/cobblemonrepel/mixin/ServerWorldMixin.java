package deuli.cobblemonrepel.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import deuli.cobblemonrepel.CobblemonRepel;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    /**
     * Intercepts all direct world.spawnEntity() calls.
     * CobbleBosses (and anything else that manually places a PokemonEntity)
     * bypasses CobblemonEvents.POKEMON_ENTITY_SPAWN entirely, so we catch it
     * here instead. If the spawn position is inside a repel radius, we discard
     * the entity and cancel the spawn.
     */
    @Inject(method = "spawnEntity", at = @At("HEAD"), cancellable = true)
    private void cobblemonrepel$checkRepelOnSpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!(entity instanceof PokemonEntity)) return;

        ServerWorld world = (ServerWorld) (Object) this;

        // Respect the gamerule kill-switch
        if (world.getGameRules().getInt(CobblemonRepel.REPEL_RANGE) == 0) return;

        BlockPos spawnPos = entity.getBlockPos();
        if (CobblemonRepel.isRepelNearby(world, spawnPos)) {
            entity.discard();
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}