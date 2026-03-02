package skipthespire.patches;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.screens.splash.SplashScreen;

import java.lang.reflect.Field;

@SpirePatch2(clz = SplashScreen.class, method = "update")
public class SkipSplashPatch {
    private static final Field IMG_FIELD = resolveImgField();

    @SpirePrefixPatch
    public static SpireReturn<Void> prefix(SplashScreen __instance) {
        disposeSplashTexture(__instance);
        __instance.isDone = true;
        return SpireReturn.Return();
    }

    private static void disposeSplashTexture(SplashScreen splashScreen) {
        if (IMG_FIELD == null) {
            return;
        }

        try {
            Object value = IMG_FIELD.get(splashScreen);
            if (value instanceof Texture) {
                ((Texture) value).dispose();
            }
            IMG_FIELD.set(splashScreen, null);
        } catch (IllegalAccessException ignored) {
        }
    }

    private static Field resolveImgField() {
        try {
            Field field = SplashScreen.class.getDeclaredField("img");
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            System.err.println("[SkipTheSpire] SplashScreen.img not found, skip-intro will still run but may leak one texture.");
            return null;
        }
    }
}
