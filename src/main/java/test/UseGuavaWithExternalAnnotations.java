package test;

import com.google.common.base.MoreObjects;

public class UseGuavaWithExternalAnnotations {

    public void useGuava() {
        final String nullStr = null;
        // should not complain because the guava method has been externally
        // annotated
        final String nonNull1 = MoreObjects.firstNonNull(nullStr, "foo");
        // eclipse warns about useless null-check
        if (nonNull1 != null) {
            nonNull1.toString();
        }

        final String nonNull2 = MoreObjects.firstNonNull(nullStr, "foo");
        // should not complain
        nonNull2.toString();
    }
}
