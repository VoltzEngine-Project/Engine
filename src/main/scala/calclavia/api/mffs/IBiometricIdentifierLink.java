package calclavia.api.mffs;

import java.util.Set;

import calclavia.api.mffs.security.IBiometricIdentifier;

/** Applied to TileEntities that can be linked with a Biometric Identifier.
 * 
 * @author Calclavia */
public interface IBiometricIdentifierLink
{
    public IBiometricIdentifier getBiometricIdentifier();

    public Set<IBiometricIdentifier> getBiometricIdentifiers();
}
