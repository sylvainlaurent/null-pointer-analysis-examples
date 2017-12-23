package road.to.npefree.world;

import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.Contract;

public class Level5_1 {
    private static boolean isEmpty(@Nullable String str) {
        return str == null || str.isEmpty();
    }

    public void foo(@Nullable String str) {
        if (!isEmpty(str)) {
            System.out.println(str.toLowerCase());
        }
    }
}
