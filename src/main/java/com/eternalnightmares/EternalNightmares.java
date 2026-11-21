package com.eternalnightmares;

import com.eternalnightmares.block.marker.MarkerBlock;
import com.eternalnightmares.block.marker.MarkerBlockEntity;
import com.eternalnightmares.block.triggers.staircasetrigger.StaircaseTrigger;
import com.eternalnightmares.entity.BlueWisp.BlueWispEntity;
import com.eternalnightmares.entity.Watcher.WatcherEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.function.Supplier;

@Mod(EternalNightmares.MODID)
public class EternalNightmares {
    public static final String MODID = "eternalnightmares";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock(
            "example_block",
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
    );

    public static final DeferredBlock<Block> darkness_block = BLOCKS.registerSimpleBlock(
            "darkness",
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(-1.0f, 3600000.0f)
    );

    public static final DeferredBlock<Block> staircasetrigger = BLOCKS.register(
            "staircasetrigger",
            () -> new StaircaseTrigger(BlockBehaviour.Properties.of()
                    .noCollission()
                    .noOcclusion()
                    .strength(-1.0f, 3600000.0f)
                    .noLootTable())
    );

    public static final DeferredBlock<Block> marker = BLOCKS.register(
            "marker",
            () -> new MarkerBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .noCollission()
                    .noOcclusion()
                    .strength(-1.0f, 3600000.0f)
                    .noLootTable())
    );

    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    public static final DeferredItem<BlockItem> MARKER_ITEM =
            ITEMS.registerSimpleBlockItem("marker", marker);

    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem(
            "example_item",
            new Item.Properties().food(new FoodProperties.Builder()
                    .alwaysEdible()
                    .nutrition(1)
                    .saturationModifier(2f)
                    .build())
    );

    public static final DeferredHolder<EntityType<?>, EntityType<WatcherEntity>> WATCHER = ENTITIES.register(
            "watcher",
            () -> EntityType.Builder.of(WatcherEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("watcher")
    );

    public static final DeferredHolder<EntityType<?>, EntityType<BlueWispEntity>> BLUE_WISP = ENTITIES.register(
            "bluewisp",
            () -> EntityType.Builder.of(BlueWispEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 0.6f)
                    .build("bluewisp")
    );

    public static final Supplier<BlockEntityType<MarkerBlockEntity>> marker_block_entity = BLOCK_ENTITY_TYPES.register(
            "marker",
            () -> BlockEntityType.Builder.of(MarkerBlockEntity::new, marker.get()).build(null)
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> void_ambience = sound("voidambience");
    public static final DeferredHolder<SoundEvent, SoundEvent> staircase_ambience = sound("staircaseambience");
    public static final DeferredHolder<SoundEvent, SoundEvent> sweden_reversed = sound("swedenreversed");
    public static final DeferredHolder<SoundEvent, SoundEvent> heartbeat = sound("heartbeat");
    public static final DeferredHolder<SoundEvent, SoundEvent> warp = sound("warp");

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register(
            "example_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.eternalnightmares"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> output.accept(EXAMPLE_ITEM.get()))
                    .build()
    );

    public EternalNightmares(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        ENTITIES.register(modEventBus);
        SOUNDS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private static DeferredHolder<SoundEvent, SoundEvent> sound(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(
                ResourceLocation.fromNamespaceAndPath(MODID, name)));
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(EXAMPLE_BLOCK_ITEM);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
