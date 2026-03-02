package skipthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Some mod stacks can crash at startup in SkindexRegistry.processRegistrants().
 * Keep startup alive by isolating that call and suppressing its failure.
 */
@SpirePatch2(
        cls = "skindex.registering.SkindexRegistry",
        method = "processRegistrants",
        optional = true
)
public class SafeSkindexRegistryPatch {
    private static final ThreadLocal<Boolean> REENTRY = ThreadLocal.withInitial(() -> false);
    private static volatile Method processRegistrantsMethod;

    @SpirePrefixPatch
    public static SpireReturn<Void> prefix() {
        // Inner call should run the original method without wrapping again.
        if (Boolean.TRUE.equals(REENTRY.get())) {
            return SpireReturn.Continue();
        }

        REENTRY.set(true);
        try {
            invokeOriginalProcessRegistrants();
        } catch (Throwable t) {
            Throwable root = unwrap(t);
            System.err.println("[SkipTheSpire] Suppressed Skindex startup crash in processRegistrants: " + root);
            root.printStackTrace();
        } finally {
            REENTRY.set(false);
        }

        return SpireReturn.Return();
    }

    private static void invokeOriginalProcessRegistrants() throws ReflectiveOperationException {
        Method method = processRegistrantsMethod;
        if (method == null) {
            Class<?> registryClass = Class.forName("skindex.registering.SkindexRegistry");
            method = registryClass.getDeclaredMethod("processRegistrants");
            method.setAccessible(true);
            processRegistrantsMethod = method;
        }
        method.invoke(null);
    }

    private static Throwable unwrap(Throwable t) {
        Throwable current = t;
        while (current instanceof InvocationTargetException
                && ((InvocationTargetException) current).getTargetException() != null) {
            current = ((InvocationTargetException) current).getTargetException();
        }
        return current;
    }
}
