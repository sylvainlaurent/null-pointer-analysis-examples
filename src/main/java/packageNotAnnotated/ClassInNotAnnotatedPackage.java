package packageNotAnnotated;

/**
 * No nullability constraints is applied here.
 */
public class ClassInNotAnnotatedPackage {

    public void demo() {
        // no error reported
        echo(null);
    }

    public String echo(final String str) {
        // no warningabout useless check
        if (str == null) {
            return null;
        }
        return "hello " + str;
    }
}
