package pkg;

import java.net.URL;
import java.util.Optional;
import org.osgi.framework.Bundle;
import org.osgi.framework.connect.FrameworkUtilHelper;

/** Equinox will sometimes use this to determine what bundle a class comes from. */
public class ShimFrameworkUtilHelper implements FrameworkUtilHelper {
	private static OsgiShim owner;

	public static void initialize(OsgiShim owner) {
		ShimFrameworkUtilHelper.owner = owner;
	}

	@Override
	public Optional<Bundle> getBundle(Class<?> classFromBundle) {
		URL source = classFromBundle.getProtectionDomain().getCodeSource().getLocation();
		return Optional.of(owner.bundleForURL(source));
	}
}
