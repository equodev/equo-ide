package dev.equo.solstice;

import java.util.Optional;
import org.osgi.framework.Bundle;
import org.osgi.framework.connect.FrameworkUtilHelper;

/** Equinox will sometimes use this to determine what bundle a class comes from. */
public class SolsticeFrameworkUtilHelper implements FrameworkUtilHelper {
	private static Solstice owner;

	public static void initialize(Solstice owner) {
		SolsticeFrameworkUtilHelper.owner = owner;
	}

	@Override
	public Optional<Bundle> getBundle(Class<?> classFromBundle) {
		var domain = classFromBundle.getProtectionDomain();
		var source = domain.getCodeSource();
		if (source == null) {
			return Optional.of(owner.systemBundle);
		}
		var location = source.getLocation();
		return Optional.of(owner.bundleForURL(location));
	}
}
