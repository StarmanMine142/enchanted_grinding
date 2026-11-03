package starman.enchantedgrinding;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import starman.enchantedgrinding.compat.EGPlatform;
import starman.enchantedgrinding.config.EGConfigManager;

public class EnchantedGrinding implements ModInitializer {
	public static final String MOD_ID = "enchanted_grinding";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		EGConfigManager.init(FabricLoader.getInstance().getConfigDir(), MOD_ID);
		EGPlatform.setModLoadedCheck(id -> FabricLoader.getInstance().isModLoaded(id));
		LOGGER.info("Enchanted Grinding says \"Hello Fabric world!\"");
	}
}
