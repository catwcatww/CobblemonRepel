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
     * Intercepts direct world.spawnEntity() calls to catch mods like CobbleBosses
     * that bypass CobblemonEvents.POKEMON_ENTITY_SPAWN entirely.
     *
     * We skip any PokemonEntity that has an owner UUID set — those are party Pokémon
     * being sent out into a battle, which should never be blocked by repels.
     * Ownerless entities are wild spawns (including boss spawns) and are fair game.
     */
    @Inject(method = "spawnEntity", at = @At("HEAD"), cancellable = true)
    private void cobblemonrepel$checkRepelOnSpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!(entity instanceof PokemonEntity pokemonEntity)) return;

        // Owned Pokémon = player's party being sent into battle. Never block these.
        if (pokemonEntity.getPokemon().getOwnerUUID() != null) return;

        ServerWorld world = (ServerWorld) (Object) this;

        // Respect the gamerule kill-switch (range == 0 means repels disabled)
        if (world.getGameRules().getInt(CobblemonRepel.REPEL_RANGE) == 0) return;

        BlockPos spawnPos = entity.getBlockPos();
        if (CobblemonRepel.isRepelNearby(world, spawnPos)) {
            entity.discard();
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}