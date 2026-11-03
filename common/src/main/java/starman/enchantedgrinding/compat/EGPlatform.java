package starman.enchantedgrinding.compat;

import java.util.function.Predicate;

public final class EGPlatform {
    private static Predicate<String> modLoadedCheck = id -> false;

    private EGPlatform() {}

    public static void setModLoadedCheck(Predicate<String> check) {
        modLoadedCheck = check;
    }

    public static boolean isModLoaded(String modId) {
        return modLoadedCheck.test(modId);
    }
}