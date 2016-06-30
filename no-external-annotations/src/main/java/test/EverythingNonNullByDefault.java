package test;

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
	public EverythingNonNullByDefault(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public String getCity() {
		// should report an error because getCity() is not nullable
		return city;
	}

	public @Nullable String getNullableCity() {
		// should report an error because getCity() is not nullable
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public static <T> T typeArgument(final List<T> l) {
		// no error
		l.add(null);
		return l.get(0);
	}

	public static <T extends CharSequence> T boundedTypeArgument(final List<T> l) {
		// should report an error
		l.add(null);
		return l.get(0);
	}

	public static <@Nullable T extends CharSequence> T explicitelyNullableBoundedType(final List<T> l) {
		// should not report an error
		l.add(null);
		return l.get(0);
	}

	public static void apply() {
		// should be reported as error
		final EverythingNonNullByDefault o = new EverythingNonNullByDefault(null);

		// should be reported as error
		o.setName(null);

		final String name = o.getName();
		// should not report an error
		name.toString();

		// should report o.city as being potentially null
		o.city.toString();

		if (o.city != null) {
			// "syntactic null analysis for fields" allows to test and then use
			// without complaints
			o.city.toString();
		}
		if (o.getNullableCity() != null) {
			// there's no syntactic null analysis for getters
			o.getNullableCity().toString();
		}
		// to avoid the error, a local variable must be used
		final String nullableCity = o.getNullableCity();
		if (nullableCity != null) {
			// no error here
			nullableCity.toString();
		}
		// should report error
		nullableCity.toString();

		assert nullableCity != null;
		// should not complain
		nullableCity.toString();

		final List<String> list = new ArrayList<>();
		// considered as @NonNull List<@NonNull>

		// no error
		list.add("ok");

		// should report error
		list.add(null);

		final List<@Nullable String> listOfNullable = new ArrayList<>();
		// should not complain
		final String typeArgument = typeArgument(listOfNullable);
		// should report error
		typeArgument.toString();

		// should report error
		final String boundedTypeArgument = boundedTypeArgument(listOfNullable);
		// should not complain
		boundedTypeArgument.toString();

		// should not complain
		final String explicitelyNullableBoundedType = explicitelyNullableBoundedType(listOfNullable);
		// should report error
		explicitelyNullableBoundedType.toString();

		// should not complain. Eclipse 4.6 bug : an error is reported
		final String explicitelyNullableBoundedType2 = explicitelyNullableBoundedType(list);

		// should report error
		explicitelyNullableBoundedType2.toString();

		{
			final String[] arrayOfUndeterminedNullabilityString = new String[2];
			// should not complain
			arrayOfUndeterminedNullabilityString[0].toString();
			// should not complain
			arrayOfUndeterminedNullabilityString[0] = null;

		}
		{
			final @NonNull String[] arrayOfNonNullString = new @NonNull String[2];
			// should not complain
			arrayOfNonNullString[0].toString();
			// should report error
			arrayOfNonNullString[0] = null;
		}
		{
			final @Nullable String[] arrayOfNullableString = new @Nullable String[2];
			// should report error
			arrayOfNullableString[0].toString();
			// should not complain
			arrayOfNullableString[0] = null;
		}
	}

}
