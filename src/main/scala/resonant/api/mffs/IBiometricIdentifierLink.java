package resonant.api.mffs;

import java.util.Set;

import resonant.api.mffs.security.IBiometricIdentifier;

/** Applied to TileEntities that can be linked with a Biometric Identifier.
 * 
 * @author Calclavia */
public interface IBiometricIdentifierLink
{
    public IBiometricIdentifier getBiometricIdentifier();

    public Set<IBiometricIdentifier> getBiometricIdentifiers();
}
