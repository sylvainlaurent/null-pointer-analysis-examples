package test;

import com.google.common.base.MoreObjects;

public class UseGuava {

	public void useGuava() {
		final String nullStr = null;
		// should not complain
		final String nonNull1 = MoreObjects.firstNonNull(nullStr, "foo");
		// should complain about useless less. Eclipse 4.6 does not report any
		// error because it does not honor the @ParametersAreNonnullByDefault on
		// the com.google.common.base package. see
		// UseGuavaWithExternalAnnotations for correct analysis thanks to
		// external annotations
		if (nonNull1 != null) {
			nonNull1.toString();
		}

		final String nonNull2 = MoreObjects.firstNonNull(nullStr, "foo");
		// should not complain
		nonNull2.toString();
	}
}
