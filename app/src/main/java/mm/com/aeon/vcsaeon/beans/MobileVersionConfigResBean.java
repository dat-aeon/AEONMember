package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class MobileVersionConfigResBean implements Serializable {

	private int id;
	private String forceUpdFlag;
	private String versionUpdateInfo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getForceUpdFlag() {
		return forceUpdFlag;
	}

	public void setForceUpdFlag(String forceUpdFlag) {
		this.forceUpdFlag = forceUpdFlag;
	}

	public String getVersionUpdateInfo() {
		return versionUpdateInfo;
	}

	public void setVersionUpdateInfo(String versionUpdateInfo) {
		this.versionUpdateInfo = versionUpdateInfo;
	}
}
