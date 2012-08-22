package br.mikhas.reflector.config;

/**
 * Check availability and gets the Unsafe object from sun internal classes
 * 
 * @author Mikhail Domanoski
 * 
 */
@SuppressWarnings("restriction")
public class UnsafeGetter {

	/**
	 * Flags if the class is initialized
	 */
	private static boolean init = false;

	/**
	 * Flags if the Unsafe is available
	 */
	private static boolean available = false;

	/**
	 * Gets the unsafe object from sun internals
	 * 
	 * @return the Unsafe object or <code>null</code> if it is not available
	 */
	public static Unsafe getUnsafe() {
		if (isUnsafeAvailable())
			return Unsafe.getInstance();
		else
			return null;
	}

	public static sun.misc.Unsafe getRealUnsafe() {
		if (isUnsafeAvailable()) {
			return Unsafe.realUnsafe();
		}
		throw new UnsupportedOperationException(
				"The unsafe object is not available");
	}

	/**
	 * Check if the unsafe object is available
	 */
	public synchronized static boolean isUnsafeAvailable() {
		if (!init) {
			try {
				Class.forName("sun.misc.Unsafe");
				available = true;

			} catch (Throwable t) {
				available = false;
			}

			if (available) {
				try {
					Class.forName("org.apache.harmony.util.concurrent.Atomics");
					available = false;
				} catch (ClassNotFoundException e) {
					available = true;
				}
			}
			init = true;
		}

		return available;
	}

}
