package test;

import static org.eclipse.jdt.annotation.DefaultLocation.FIELD;
import static org.eclipse.jdt.annotation.DefaultLocation.PARAMETER;
import static org.eclipse.jdt.annotation.DefaultLocation.RETURN_TYPE;
import static org.eclipse.jdt.annotation.DefaultLocation.TYPE_ARGUMENT;
import static org.eclipse.jdt.annotation.DefaultLocation.TYPE_BOUND;
import static org.eclipse.jdt.annotation.DefaultLocation.TYPE_PARAMETER;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public class EverythingNonNullByDefault {
    private String name; // considered as @NonNull
    private String address; // considered as @NonNull
    private @Nullable String city;

    // should report error about address not being initialized
    // should NOT report error about city not being initialized
    public EverythingNonNullByDefault(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        // should report an error because getCity() is not nullable
        return city;
    }

    public @Nullable String getNullableCity() {
        // no error, this method return a @Nullable String like this.city
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static <T> T typeArgument(List<T> l) {
        // no error, just a warning because the default @NonNullByDefault does
        // not include TYPE_PARAMETER location
        l.add(null);

        // ok to report an error since T may be nullable
        l.get(0).toString();
        return l.get(0);
    }

    @NonNullByDefault({ PARAMETER, RETURN_TYPE, FIELD, TYPE_BOUND, TYPE_ARGUMENT, TYPE_PARAMETER })
    public static <T> T typeArgumentNonNull(List<T> l) {
        l.add(null);
        // #Eclipse 4.6.3 bug?# we should have no error because
        // @NonNullByDefault with TYPE_PARAMETER should treat T as @Nonnull and
        // therefore l.get(0) too
        l.get(0).toString();
        return l.get(0);
    }

    public static <@NonNull T> T typeArgumentNonNullExplicit(List<T> l) {
        // it's correct to report an error here
        l.add(null);
        l.get(0).toString();
        return l.get(0);
    }

    public static <T extends CharSequence> T boundedTypeArgument(List<T> l) {
        // should report an error
        l.add(null);
        return l.get(0);
    }

    public static <@Nullable T extends @Nullable CharSequence> T explicitelyNullableBoundedType(List<T> l) {
        // should not report an error
        l.add(null);

        // ok to report an error
        l.get(0).toString();
        return l.get(0);
    }

    public static void apply() {
        // should be reported as error
        EverythingNonNullByDefault o = new EverythingNonNullByDefault(null);

        // should be reported as error
        o.setName(null);

        String name = o.getName();
        // should not report an error
        name.toString();

        if (o.city != null) {
            // "syntactic null analysis for fields" allows to test and then use
            // without complaints
            o.city.toString();
        }

        if (o.city != null) {
            o.setName("foo");
            // now "syntactic null analysis for fields" is not applied because
            // there was a satement between the test for nullability of the
            // field and its usage. Eclipse has no means to know that setName()
            // did not modify the city field
            o.city.toString();
        }

        // should report o.city as being potentially null
        o.city.toString();

        if (o.getNullableCity() != null) {
            // there's no syntactic null analysis for getters
            o.getNullableCity().toString();
        }
        // to avoid the error, a local variable must be used
        String nullableCity = o.getNullableCity();
        if (nullableCity != null) {
            // no error here
            nullableCity.toString();
        }
        // should report error
        nullableCity.toString();

        // eclipse glitch : it could report a warning about the test being
        // useless since the variable has been dereferenced just above
        assert nullableCity != null;

        // should not complain
        nullableCity.toString();

        List<String> list = new ArrayList<>();
        // considered as @NonNull List<@NonNull>

        // no error
        list.add("ok");

        // should report error
        list.add(null);

        List<@Nullable String> listOfNullable = new ArrayList<>();
        // should not complain
        String typeArgument = typeArgument(listOfNullable);
        // should report error
        typeArgument.toString();

        // should report error
        String boundedTypeArgument = boundedTypeArgument(listOfNullable);
        // should not complain
        boundedTypeArgument.toString();

        // should not complain
        String explicitelyNullableBoundedType = explicitelyNullableBoundedType(listOfNullable);
        // should report error
        explicitelyNullableBoundedType.toString();

        // should not complain. #Eclipse 4.6.3 bug?# : an error is reported
        String explicitelyNullableBoundedType2 = explicitelyNullableBoundedType(list);

        // should report error
        explicitelyNullableBoundedType2.toString();

        {
            String[] arrayOfUndeterminedNullabilityString = new String[2];
            // should not complain
            arrayOfUndeterminedNullabilityString[0].toString();
            // should not complain
            arrayOfUndeterminedNullabilityString[0] = null;

        }
        {
            @NonNull
            String[] arrayOfNonNullString = new @NonNull String[2];
            // should not complain
            arrayOfNonNullString[0].toString();
            // should report error
            arrayOfNonNullString[0] = null;
        }
        {
            @Nullable
            String[] arrayOfNullableString = new @Nullable String[2];
            // should report error
            arrayOfNullableString[0].toString();
            // should not complain
            arrayOfNullableString[0] = null;
        }
    }

}
