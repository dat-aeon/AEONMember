package mm.com.aeon.vcsaeon.networking;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String url){
        OkHttpClient okHttpClient = VCSOkHttpClient.getObjectInstance();
        retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();
        return retrofit;
    }
}
