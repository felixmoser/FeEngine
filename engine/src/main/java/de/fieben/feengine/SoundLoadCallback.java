package de.fieben.feengine;

/**
 * This callback interface is used to get notified when a sound resource is loaded.
 * 
 * @author Felix Moser - felix.ernesto.moser@googlemail.com
 */
public interface SoundLoadCallback {
	/**
	 * @param success true if the sound resource is successfully loaded, false otherwise.
	 */
	void soundLoaded(final boolean success);
}