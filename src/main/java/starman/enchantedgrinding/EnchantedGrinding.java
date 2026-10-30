package starman.enchantedgrinding;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import starman.enchantedgrinding.config.EGConfigManager;

public class EnchantedGrinding implements ModInitializer {
	public static final String MOD_ID = "enchanted_grinding";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		EGConfigManager.load();
		LOGGER.info("Enchanted Grinding says \"Hello Fabric world!\"");
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
