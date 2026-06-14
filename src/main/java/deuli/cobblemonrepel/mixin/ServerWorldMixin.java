package deuli.cobblemonrepel.mixin;

import com.cobblemon.mod.common.entity.npc.NPCEntity;
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


    @Inject(method = "spawnEntity", at = @At("HEAD"), cancellable = true)
    private void cobblemonrepel$checkRepelOnSpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        ServerWorld world = (ServerWorld) (Object) this;


        if (world.getGameRules().getInt(CobblemonRepel.REPEL_RANGE) == 0) return;
// test
        if (entity instanceof PokemonEntity pokemonEntity) {
            // Owned Pokémon = player's party being sent into battle
            if (pokemonEntity.getPokemon().getOwnerUUID() != null) return;

            BlockPos spawnPos = entity.getBlockPos();
            if (CobblemonRepel.isRepelNearby(world, spawnPos)) {
                entity.discard();
                cir.setReturnValue(false);
                cir.cancel();
            }

        } else if (entity instanceof NPCEntity npcEntity) {

            BlockPos spawnPos = entity.getBlockPos();
            if (CobblemonRepel.isRepelNearby(world, spawnPos)) {
                entity.discard();
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}