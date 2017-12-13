package road.to.npefree.world;

import org.eclipse.jdt.annotation.Nullable;

public class Level2_2 {
    public void foo() {
        @Nullable
        String str = nullStr();

        System.out.println("String length:" + str.length());
    }

    public @Nullable String nullStr() {
        return null;
    }
}
