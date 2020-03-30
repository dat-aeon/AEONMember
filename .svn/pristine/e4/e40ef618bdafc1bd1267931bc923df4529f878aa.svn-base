package mm.com.aeon.vcsaeon.networking;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import mm.com.aeon.vcsaeon.BuildConfig;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.AUTH_PASSWORD;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.AUTH_USERNAME;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.AUTH_ATTRIBUTE;

public class APIClient {

    private static Service oAuthApiService;

    public static Service getUserService() {
        return RetrofitClient.getClient(BuildConfig.API_URL).create(Service.class);
    }

    public static Service getApplicationRegisterService() {
        return RetrofitClient.getClient(BuildConfig.API_DIGITAL_APPLICATION_URL).create(Service.class);
    }

    public static Service getAuthUserService(){
        OkHttpClient okHttpClient = initOkHttp(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(Service.class);
    }

    private static OkHttpClient initOkHttp(boolean isAuth) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        String credential = Credentials.basic(AUTH_USERNAME,AUTH_PASSWORD);

        OkHttpClient.Builder httpClient = VCSOkHttpClient.getObjectInstance().newBuilder();
        
        if (isAuth) {
            httpClient.authenticator(new TokenRenewAuthenticator());
            httpClient.addInterceptor(interceptor).addInterceptor(new AuthenticationInterceptor(credential));
        }

        return httpClient.build();
    }

    static class AuthenticationInterceptor implements Interceptor {
        private String authToken;
        AuthenticationInterceptor(String token) {
            this.authToken = token;
        }
        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request original = chain.request();
            Request.Builder builder = original.newBuilder()
                    .header(AUTH_ATTRIBUTE, authToken);
            Request request = builder.build();
            return chain.proceed(request);
        }
    }

    static Service getOAuthRetrofitRestClient() {
        if (oAuthApiService == null) {
            OkHttpClient okHttpClient = initOkHttp(true);
            Retrofit retrofit = initRetrofit(okHttpClient);
            oAuthApiService = retrofit.create(Service.class);
        }
        return oAuthApiService;
    }

    private static Retrofit initRetrofit(OkHttpClient okHttpClient) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}