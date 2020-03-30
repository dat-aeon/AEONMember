package mm.com.aeon.vcsaeon.delegates;

public interface AccessPermissionResultDelegate {

    void onAccessRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults);
}
