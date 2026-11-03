package starman.enchantedgrinding;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import starman.enchantedgrinding.compat.EGPlatform;
import starman.enchantedgrinding.config.ConfigIntegration;
import starman.enchantedgrinding.config.EGConfigManager;

@Mod(EnchantedGrinding.MOD_ID)
public class EnchantedGrinding {
	public static final String MOD_ID = "enchanted_grinding";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public EnchantedGrinding(IEventBus modBus) {
		EGConfigManager.init(FMLPaths.CONFIGDIR.get(), MOD_ID);
		ConfigIntegration.config();
		EGPlatform.setModLoadedCheck(ModList.get()::isLoaded);
		LOGGER.info("Enchanted Grinding says \"Hello NeoForge world!\"");
	}
}