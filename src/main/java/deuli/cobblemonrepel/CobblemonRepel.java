package deuli.cobblemonrepel;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.spawning.position.FishingSpawnablePosition;
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition;
import kotlin.Unit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;

public class CobblemonRepel implements ModInitializer {

    public static final String MOD_ID = "cobblemonrepel";

    public static final GameRules.Key<GameRules.IntRule> REPEL_RANGE = GameRuleRegistry.register("repelRange", GameRules.Category.SPAWNING, GameRuleFactory.createIntRule(32, 0, 512));
    public static final GameRules.Key<GameRules.IntRule> SUPER_REPEL_RANGE_MULTIPLIER = GameRuleRegistry.register("superRepelRangeMultiplier", GameRules.Category.SPAWNING, GameRuleFactory.createIntRule(2, 1, 10));
    public static final GameRules.Key<GameRules.IntRule> MAX_REPEL_RANGE_MULTIPLIER = GameRuleRegistry.register("maxRepelRangeMultiplier", GameRules.Category.SPAWNING, GameRuleFactory.createIntRule(3, 1, 10));

    public static final String REPEL_TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTcyNDg1ODY1ODExMywKICAicHJvZmlsZUlkIiA6ICIxNTUyNmU1OGZhOWE0NjBmODhhNmZhNjk1M2RlNjgzNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJQaWVkcml0YTE3IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzkzNWJmZmExN2ZmYWM4Yzk4ZjIyODM0ZjFkZjM3NGMyNDlmY2FlNzhlNGI4MDAwMWE1OThhZmI4N2M4MDU5YyIKICAgIH0KICB9Cn0=";
    public static final RepelBlock REPEL_BLOCK = Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, "repel"), new RepelBlock(REPEL_TEXTURE, 1));
    public static final RepelBlockItem REPEL_BLOCK_ITEM = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "repel"), new RepelBlockItem(REPEL_BLOCK, "repel"));

    public static final String SUPER_REPEL_TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTcyOTAxMTEwMzE4MywKICAicHJvZmlsZUlkIiA6ICIzYjBmNTM5MmRlNzM0YmZjYmJkOTMxYzMxYmFkODMxMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJjYXRhbmRCIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzU2MGIxNmZmYzlkMzk3MTY5MjY4YWFiOGQ5ZTdmMmU3MzlhNTQyODE5NjY5MzlhN2Q3MGZiZjM1ZWNjOGQwOGIiCiAgICB9CiAgfQp9";
    public static final RepelBlock SUPER_REPEL_BLOCK = Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, "super_repel"), new RepelBlock(SUPER_REPEL_TEXTURE, 2));
    public static final RepelBlockItem SUPER_REPEL_BLOCK_ITEM = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "super_repel"), new RepelBlockItem(SUPER_REPEL_BLOCK, "super_repel"));

    public static final String MAX_REPEL_TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTcyOTAxMTE4MzA2NywKICAicHJvZmlsZUlkIiA6ICI5OWY1MzhjMDhlN2E0NTg3YmU4MGJjNGVmNzU0ZmQyMSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTb2xvV1MyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzE0M2VjMTQ3ZGFjOTdjN2NlMzA2OWU2MTdiOWNmODU5ZTlkMTljOGNjODAxNmExM2VkYTEyMjc0ZDY4MTFmNzQiCiAgICB9CiAgfQp9";
    public static final RepelBlock MAX_REPEL_BLOCK = Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, "max_repel"), new RepelBlock(MAX_REPEL_TEXTURE, 3));
    public static final RepelBlockItem MAX_REPEL_BLOCK_ITEM = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "max_repel"), new RepelBlockItem(MAX_REPEL_BLOCK, "max_repel"));

    public static final PointOfInterestType REPEL_POI = PointOfInterestHelper.register(Identifier.of(MOD_ID, "repel"), 0, 1, REPEL_BLOCK, SUPER_REPEL_BLOCK, MAX_REPEL_BLOCK);
    public static final RegistryKey<PointOfInterestType> REPEL_POI_REGISTRY = RegistryKey.of(Registries.POINT_OF_INTEREST_TYPE.getKey(), Identifier.of(MOD_ID, "repel"));

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(g -> {
            g.add(REPEL_BLOCK_ITEM);
            g.add(SUPER_REPEL_BLOCK_ITEM);
            g.add(MAX_REPEL_BLOCK_ITEM);
        });

        ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> {
            RepelBlockItem.RANGE = minecraftServer.getGameRules().getInt(REPEL_RANGE);
            RepelBlockItem.MULTIPLIERS.put("super_repel", minecraftServer.getGameRules().getInt(SUPER_REPEL_RANGE_MULTIPLIER));
            RepelBlockItem.MULTIPLIERS.put("max_repel", minecraftServer.getGameRules().getInt(MAX_REPEL_RANGE_MULTIPLIER));
        });

        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.HIGHEST, event -> {
            SpawnablePosition spawnablePosition = event.getSpawnablePosition();
            ServerWorld world = spawnablePosition.getWorld();

            // Only skip fishing spawns and the gamerule kill-switch.
            // Pokésnack spawns (poke_snack_* spawner name) are intentionally
            // NOT excluded here — they should still be blocked by repels.
            // CobbleBosses spawns bypass this event entirely and are handled
            // by ServerWorldMixin instead.
            if (event.isCanceled() ||
                    world.getGameRules().getInt(REPEL_RANGE) == 0 ||
                    spawnablePosition instanceof FishingSpawnablePosition
            ) return Unit.INSTANCE;

            BlockPos spawnPos = spawnablePosition.getPosition();
            if (isRepelNearby(world, spawnPos)) {
                event.cancel();
            }

//            Debug.handle(event);

            return Unit.INSTANCE;
        });
    }

    public static boolean isRepelNearby(ServerWorld world, BlockPos pos) {
        int repelRange = world.getGameRules().getInt(REPEL_RANGE);
        int superMultiplier = world.getGameRules().getInt(SUPER_REPEL_RANGE_MULTIPLIER);
        int maxMultiplier = world.getGameRules().getInt(MAX_REPEL_RANGE_MULTIPLIER);
        int maxRange = repelRange * Math.max(superMultiplier, maxMultiplier);

        return world.getPointOfInterestStorage().getInSquare(
                poi -> poi.matchesKey(RegistryKey.of(Registries.POINT_OF_INTEREST_TYPE.getKey(), Identifier.of(MOD_ID, "repel"))),
                pos,
                maxRange,
                PointOfInterestStorage.OccupationStatus.ANY
        ).map(PointOfInterest::getPos).anyMatch(matchPos -> {
            if (world.getBlockState(matchPos).getBlock() instanceof RepelBlock repelBlock) {
                int repelLevel = repelBlock.getRepelLevel();
                int xRange = Math.abs(matchPos.getX() - pos.getX());
                int yRange = Math.abs(matchPos.getY() - pos.getY());
                int zRange = Math.abs(matchPos.getZ() - pos.getZ());
                if (repelLevel >= 1 && xRange < repelRange && yRange < repelRange && zRange < repelRange) {
                    return true;
                } else {
                    int superRepelRange = repelRange * superMultiplier;
                    if (repelLevel >= 2 && xRange < superRepelRange && yRange < superRepelRange && zRange < superRepelRange) {
                        return true;
                    } else {
                        return repelLevel >= 3;
                    }
                }
            }

            return false;
        });
    }
}
