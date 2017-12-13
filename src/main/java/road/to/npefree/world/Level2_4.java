package road.to.npefree.world;

import org.eclipse.jdt.annotation.NonNull;

public class Level2_4<E extends @NonNull Object> {
    E e;

    public Level2_4(E e) {
        this.e = e;
    }

    public void foo() {
        Level2_4<Object> l1 = new Level2_4<>(null);
        Level2_4<String> l2 = new Level2_4<>("hello");
        Level2_4<@NonNull String> l3 = new Level2_4<>("hello");
        Level2_4<@Nullable String> l4 = new Level2_4<>("hello");
    }

}
