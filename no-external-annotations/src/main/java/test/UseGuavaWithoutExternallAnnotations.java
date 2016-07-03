package test;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

public class UseGuavaWithoutExternallAnnotations {

	public void useGuava() {
		final String nullStr = null;
		// should not complain
		final String nonNull1 = MoreObjects.firstNonNull(nullStr, "foo");
		// should complain about useless test. Eclipse 4.6 does not report any
		// error because there's no annotation on Guava's code to indicate that
		// the return value is never null
		// see UseGuavaWithExternalAnnotations for correct analysis thanks to
		// external annotations
		if (nonNull1 != null) {
			nonNull1.toString();
		}

		final String nonNull2 = MoreObjects.firstNonNull(nullStr, "foo");
		// should not complain
		nonNull2.toString();

		final String nullableStr = Strings.emptyToNull("");
		// should report error, as the javax.annotation.Nullable is correctly
		// interpreted
		nullableStr.toString();
	}
}
