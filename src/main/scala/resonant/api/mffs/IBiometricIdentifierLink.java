package resonant.api.mffs;

import resonant.api.mffs.security.IBiometricIdentifier;

import java.util.Set;

/**
 * Applied to TileEntities that can be linked with a Biometric Identifier.
 *
 * @author Calclavia
 */
public interface IBiometricIdentifierLink
{
	public IBiometricIdentifier getBiometricIdentifier();

	public Set<IBiometricIdentifier> getBiometricIdentifiers();
}
