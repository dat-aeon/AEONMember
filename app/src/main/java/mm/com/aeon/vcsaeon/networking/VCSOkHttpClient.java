package mm.com.aeon.vcsaeon.networking;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.OK_HTTP_WRITE_TIMEOUT;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.RETROFIT_CONNECT_TIMEOUT;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.RETROFIT_READ_TIMEOUT;

public class VCSOkHttpClient {

    private static OkHttpClient objectInstance;

    private VCSOkHttpClient() {
    }

    public static OkHttpClient getObjectInstance() {
        if (objectInstance == null) {
            objectInstance = new OkHttpClient.Builder()
                    .readTimeout(RETROFIT_READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(OK_HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(RETROFIT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .build();
        }
        return objectInstance;
    }
}
