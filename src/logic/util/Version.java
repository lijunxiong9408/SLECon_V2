package logic.util;

public final class Version {
    private final String controlCoreVersion;
    private final String guiVersion;
    private final String mcsContractVersion;
    private final String mcsFirmwareVersion;
    private final String mcsBoardVersion;
    private final String mcsSerialNumber;

    
    

    public Version ( String controlCoreVersion, String guiVersion, String mcsContractVersion, String mcsFirmwareVersion,
                     String mcsBoardVersion, String mcsSerialNumber ) {
        super();
        this.controlCoreVersion = controlCoreVersion;
        this.guiVersion         = guiVersion;
        this.mcsContractVersion = mcsContractVersion;
        this.mcsFirmwareVersion = mcsFirmwareVersion;
        this.mcsBoardVersion = mcsBoardVersion;
        this.mcsSerialNumber    = mcsSerialNumber;
    }


    public final String getControlCoreVersion () {
        return controlCoreVersion;
    }


    public final String getGuiVersion () {
        return guiVersion;
    }


    public final String getMcsContractVersion () {
        return mcsContractVersion;
    }


    public final String getMcsFirmwareVersion () {
        return mcsFirmwareVersion;
    }


    public final String getMcsBoardVersion () {
        return mcsBoardVersion;
    }


    public final String getMcsSerialNumber () {
        return mcsSerialNumber;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( controlCoreVersion == null ) ? 0 : controlCoreVersion.hashCode() );
        result = prime * result + ( ( guiVersion == null ) ? 0 : guiVersion.hashCode() );
        result = prime * result + ( ( mcsBoardVersion == null ) ? 0 : mcsBoardVersion.hashCode() );
        result = prime * result + ( ( mcsContractVersion == null ) ? 0 : mcsContractVersion.hashCode() );
        result = prime * result + ( ( mcsFirmwareVersion == null ) ? 0 : mcsFirmwareVersion.hashCode() );
        result = prime * result + ( ( mcsSerialNumber == null ) ? 0 : mcsSerialNumber.hashCode() );
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!( obj instanceof Version )) {
            return false;
        }
        Version other = (Version) obj;
        if (controlCoreVersion == null) {
            if (other.controlCoreVersion != null) {
                return false;
            }
        } else if (!controlCoreVersion.equals(other.controlCoreVersion)) {
            return false;
        }
        if (guiVersion == null) {
            if (other.guiVersion != null) {
                return false;
            }
        } else if (!guiVersion.equals(other.guiVersion)) {
            return false;
        }
        if (mcsBoardVersion == null) {
            if (other.mcsBoardVersion != null) {
                return false;
            }
        } else if (!mcsBoardVersion.equals(other.mcsBoardVersion)) {
            return false;
        }
        if (mcsContractVersion == null) {
            if (other.mcsContractVersion != null) {
                return false;
            }
        } else if (!mcsContractVersion.equals(other.mcsContractVersion)) {
            return false;
        }
        if (mcsFirmwareVersion == null) {
            if (other.mcsFirmwareVersion != null) {
                return false;
            }
        } else if (!mcsFirmwareVersion.equals(other.mcsFirmwareVersion)) {
            return false;
        }
        if (mcsSerialNumber == null) {
            if (other.mcsSerialNumber != null) {
                return false;
            }
        } else if (!mcsSerialNumber.equals(other.mcsSerialNumber)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "Version [controlCoreVersion=" + controlCoreVersion + ", guiVersion=" + guiVersion + ", mcsContractVersion=" + mcsContractVersion
                + ", mcsFirmwareVersion=" + mcsFirmwareVersion + ", mcsBoardVersion=" + mcsBoardVersion + ", mcsSerialNumber=" + mcsSerialNumber
                + "]";
    }
}