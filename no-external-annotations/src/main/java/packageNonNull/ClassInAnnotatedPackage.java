package packageNonNull;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * since the whole package is annotated with {@link NonNullByDefault},
 * nullability constraints also apply to this class.
 */
public class ClassInAnnotatedPackage {

	public void demo() {
		// should report error
		echo(null);
	}

	public String echo(final String str) {
		// should warn about useless check / dead code
		if (str == null) {
			return null;
		}
		return "hello " + str;
	}
}
