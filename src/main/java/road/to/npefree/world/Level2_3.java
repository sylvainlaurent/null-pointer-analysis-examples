package road.to.npefree.world;

import org.eclipse.jdt.annotation.NonNull;

public class Level2_3 {
    public void foo() {
        String nullStr = null;
        echo(nullStr);
        echoArrayOfNonNullString(null);// this is actually correct to have no error
        echoNonNullArrayOfNonNull(new String[] { "hello", null });
    }

    public void echo(@NonNull String str) {
        System.out.println(str);
    }

    public void echoArrayOfNonNullString(@NonNull String[] str) {
        System.out.println(str);
    }

    public void echoNonNullArrayOfNonNull(@NonNull String @NonNull [] str) {
        System.out.println(str);
    }
}
