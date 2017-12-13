package road.to.npefree.world;

import static org.eclipse.jdt.annotation.DefaultLocation.ARRAY_CONTENTS;
import static org.eclipse.jdt.annotation.DefaultLocation.PARAMETER;
import static org.eclipse.jdt.annotation.DefaultLocation.RETURN_TYPE;
import static org.eclipse.jdt.annotation.DefaultLocation.TYPE_ARGUMENT;
import static org.eclipse.jdt.annotation.DefaultLocation.TYPE_BOUND;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault({ PARAMETER, RETURN_TYPE, TYPE_BOUND, TYPE_ARGUMENT, ARRAY_CONTENTS })
public class Level2_5 {
    public void foo() {
        String nullStr = null;
        echo(nullStr);
        echoNonNullArrayOfNonNull(new String[] { "hello", null });
        echoTypeParameter(null);
        echoTypeBound(null);
        List<Number> list = Arrays.asList(3, null);
        echoTypeArgument(list);
    }

    public void echo(String str) {
        System.out.println(str);
    }

    public void echoNonNullArrayOfNonNull(String[] str) {
        System.out.println(str);
    }

    public <T> void echoTypeParameter(T t) {
        System.out.println(t);
    }

    public <T extends Number> void echoTypeBound(T t) {
        System.out.println(t);
    }

    public void echoTypeArgument(List<Number> list) {
        System.out.println(list);
    }
}
