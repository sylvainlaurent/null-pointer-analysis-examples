package test;

import java.util.Collection;

import org.springframework.util.CollectionUtils;

public class UseSpringWithExternalAnnotations {

    public void useSpring() {
        Collection<String> strs = null;
        CollectionUtils.isEmpty(strs);
    }
}
