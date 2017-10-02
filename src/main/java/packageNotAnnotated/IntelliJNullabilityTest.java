package packageNotAnnotated;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Created by slaurent on 16.05.17.
 */
public class IntelliJNullabilityTest {

    public static void test() {
        MyClass<String> my = new MyClass<>();

        my.echo(null);

        MyNonNullGenericClass<@NonNull String> toto = new MyNonNullGenericClass<>();
        System.out.println(toto);

        // should complain
        MyNonNullGenericClass<@Nullable String> titi = new MyNonNullGenericClass<>();
        System.out.println(titi);

        MyNullableGenericClass<String> t1 = new MyNullableGenericClass<>();
        MyNullableGenericClass<@NonNull String> t2 = new MyNullableGenericClass<>();
        MyNullableGenericClass<@Nullable String> t3 = new MyNullableGenericClass<>();

    }

    public static <T extends @Nullable CharSequence> void typeArgumentNonNullExplicit(T t) {
        System.out.println(t.hashCode());
        MyClass<T> my = new MyClass<>();

        my.echo(null);

    }

    static class MyClass<E> {
        private List<E> objs = new ArrayList<>();

        public void echo(E e) {
            objs.add(e);

            @NonNull
            List<@NonNull String> list = new ArrayList<>();
            list.add(null); // null warning here
        }

    }

    static class MyNonNullGenericClass<T extends @NonNull Object> {

    }

    static class MyNullableGenericClass<@Nullable T extends @Nullable Object> {

    }
}
