package com.tfc.tetricadditions;

import com.tfc.tetricadditions.tools.ItemModularHelmet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.mickelus.tetra.blocks.forged.container.ForgedContainerTESR;
import se.mickelus.tetra.items.TetraItemGroup;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("tetric_additions")
public class TetricAdditions {
	
	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "tetric_additions";
	
	public TetricAdditions() {
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(
				Item.class,
				TetricAdditions::registerItems
		);
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(TetricAdditions::setup);
		
		if (FMLEnvironment.dist.isClient()) {
			FMLJavaModLoadingContext.get().getModEventBus().addListener(
					TetricAdditions::provideTextures
			);
		}
		MinecraftForge.EVENT_BUS.addListener(Server::onEntityDamaged);
	}
	
	public static void provideTextures(TextureStitchEvent.Pre event) {
		if (AtlasTexture.LOCATION_BLOCKS_TEXTURE.equals(event.getMap().getTextureLocation())) {
			Minecraft.getInstance().getResourceManager()
					.getAllResourceLocations(
							"textures/items/module",
							(s) -> s.endsWith(".png")
					).stream()
					.filter((resourceLocation) -> MOD_ID.equals(resourceLocation.getNamespace()))
					.map((rl) -> new ResourceLocation(rl.getNamespace(), rl.getPath().substring(9, rl.getPath().length() - 4)))
					.forEach(event::addSprite);
			event.addSprite(ForgedContainerTESR.material.getTextureLocation());
		}
//		Priority.LOWEST
//		Priority.LOWER
//		Priority.LOW
//		Priority.BASE
//		Priority.HIGH
//		Priority.HIGHER
//		Priority.HIGHEST
	}
	
	public static void setup(FMLLoadCompleteEvent event) {
//		System.out.println("Listing Schemas");
//		SchemaRegistry.instance.getAllSchemas().forEach(schema->{
//			System.out.println(schema.getName());
//			System.out.println(schema.getClass());
//		});
//		System.out.println("Done listing Schemas");

//		se.mickelus.tetra.data.MergingDataStore

//		ModuleRegistry moduleRegistry = ModuleRegistry.instance;
//		moduleRegistry.registerModuleType(
//				new ResourceLocation("tetric_additions","armor_binding"),
//				BasicArmorModule::new
//		);
//		SchemaRegistry.instance.registerSchema(
//				new UpgradeSchema() {
//					@Override
//					public String getKey() {
//						return "armor/binding";
//					}
//
//					@Override
//					public String getName() {
//						return "binding";
//					}
//
//					@Override
//					public String getDescription(@Nullable ItemStack itemStack) {
//						return "desc";
//					}
//
//					@Override
//					public int getNumMaterialSlots() {
//						return 1;
//					}
//
//					@Override
//					public String getSlotName(ItemStack itemStack, int i) {
//						return "binding";
//					}
//
//					@Override
//					public int getRequiredQuantity(ItemStack itemStack, int i, ItemStack itemStack1) {
//						return 1;
//					}
//
//					@Override
//					public boolean acceptsMaterial(ItemStack itemStack, String s, int i, ItemStack itemStack1) {
//						return true;
//					}
//
//					@Override
//					public boolean isMaterialsValid(ItemStack itemStack, String s, ItemStack[] itemStacks) {
//						return true;
//					}
//
//					@Override
//					public boolean isApplicableForItem(ItemStack itemStack) {
//						return true;
//					}
//
//					@Override
//					public boolean isApplicableForSlot(String s, ItemStack itemStack) {
//						return true;
//					}
//
//					@Override
//					public boolean canApplyUpgrade(PlayerEntity playerEntity, ItemStack itemStack, ItemStack[] itemStacks, String s, int[] ints) {
//						return true;
//					}
//
//					@Override
//					public boolean isIntegrityViolation(PlayerEntity playerEntity, ItemStack itemStack, ItemStack[] itemStacks, String s) {
//						return false;
//					}
//
//					@Override
//					public ItemStack applyUpgrade(ItemStack itemStack, ItemStack[] itemStacks, boolean b, String s, PlayerEntity playerEntity) {
//						return null;
//					}
//
//					@Override
//					public boolean checkCapabilities(ItemStack itemStack, ItemStack[] itemStacks, int[] ints) {
//						return false;
//					}
//
//					@Override
//					public Collection<Capability> getRequiredCapabilities(ItemStack itemStack, ItemStack[] itemStacks) {
//						return null;
//					}
//
//					@Override
//					public int getRequiredCapabilityLevel(ItemStack itemStack, ItemStack[] itemStacks, Capability capability) {
//						return 0;
//					}
//
//					@Override
//					public SchemaType getType() {
//						return null;
//					}
//
//					@Override
//					public GlyphData getGlyph() {
//						return null;
//					}
//
//					@Override
//					public OutcomePreview[] getPreviews(ItemStack itemStack, String s) {
//						return ;
//					}
//				}
//		);
	}
	
	public static void registerItems(RegistryEvent.Register<Item> itemRegistryEvent) {
		itemRegistryEvent.getRegistry().register(
				new ItemModularHelmet(
						new Item.Properties()
								.group(TetraItemGroup.instance)
								.maxStackSize(1)
				)
		);
	}
}
