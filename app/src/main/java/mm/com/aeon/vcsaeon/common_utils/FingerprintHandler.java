package mm.com.aeon.vcsaeon.common_utils;

import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;

public class FingerprintHandler {

    public FingerprintHandler(String message) { }

    public void doAuth(FingerprintManager manager,
                       FingerprintManager.CryptoObject obj) {
        CancellationSignal signal = new CancellationSignal();
    }
}
