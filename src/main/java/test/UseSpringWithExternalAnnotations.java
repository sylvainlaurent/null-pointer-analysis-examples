package test;

import static org.eclipse.jdt.annotation.DefaultLocation.TYPE_PARAMETER;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.springframework.util.CollectionUtils;

public class UseSpringWithExternalAnnotations {

    public void useSpring() {
        Collection<String> strs = null;
        CollectionUtils.isEmpty(strs);
    }

    @NonNullByDefault({ TYPE_PARAMETER })
    public static <T> void typeArgumentNonNull(List<T> l) {
        l.get(0);
        l.get(0).toString();
    }

    class X {
        @NonNullByDefault(TYPE_PARAMETER)
        <T> T identity(T t) {
            return null;
        }
    }

}
