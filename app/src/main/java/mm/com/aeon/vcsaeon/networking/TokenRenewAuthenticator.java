package mm.com.aeon.vcsaeon.networking;

import android.content.Context;
import androidx.annotation.NonNull;

import java.io.IOException;

import mm.com.aeon.vcsaeon.beans.LoginAccessTokenInfo;
import mm.com.aeon.vcsaeon.beans.LoginRequest;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import okhttp3.Authenticator;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_GRANT_TYPE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PASSWORD;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.CODE_TOKEN_TIMEOUT;

public class TokenRenewAuthenticator implements Authenticator {

    private Context context;
    private LoginRequest loginRequest;

    public TokenRenewAuthenticator(Context context, LoginRequest loginRequest) {
        this.context = context;
        this.loginRequest = loginRequest;
    }

    public TokenRenewAuthenticator(){}

    @Override
    public Request authenticate(Route route, @NonNull Response response) throws IOException {

        if (response.code() == CODE_TOKEN_TIMEOUT) {

            Service apiService = APIClient.getOAuthRetrofitRestClient();
            retrofit2.Response<BaseResponse<LoginAccessTokenInfo>> refreshTokenResponse = apiService.refreshToken(PASSWORD, PreferencesManager.getRefreshToken(context)).execute();
            if (refreshTokenResponse.isSuccessful()) {
                BaseResponse<LoginAccessTokenInfo> refreshTokenResult = refreshTokenResponse.body();
                assert refreshTokenResult != null;
                if (null != refreshTokenResult.getData()) {
                    // Save new token
                    //SharedPreferenceManager.saveSecurity(refreshTokenResult.getData());

                    LoginAccessTokenInfo loginAccessTokenInfo = (LoginAccessTokenInfo) refreshTokenResult.getData();

                    // retry the 'mainRequest' which encountered an authentication error
                    // add new token into 'mainRequest' and request again
                    Request original = response.request();
                    HttpUrl originalHttpUrl = original.url();

                    // Request customization: remove old token and add new to url
                    HttpUrl url = originalHttpUrl
                            .newBuilder()
                            .removeAllEncodedQueryParameters("access_token")
                            .addQueryParameter("access_token", loginAccessTokenInfo.getAccessToken())
                            .build();

                    Request.Builder requestBuilder = original
                            .newBuilder()
                            .url(url);

                    return requestBuilder.build();

                } else {//re-login again after refresh token expire

                    if(loginRequest!=null){
                        retrofit2.Response<BaseResponse<LoginAccessTokenInfo>> tokenResponse = apiService.doLogin(loginRequest.getUsername(),loginRequest.getPassword(),PARAM_GRANT_TYPE,"aaa").execute();
                        if (tokenResponse.isSuccessful()){
                            BaseResponse<LoginAccessTokenInfo> tokenResult = tokenResponse.body();

                            assert tokenResult != null;
                            if (null != tokenResult.getData()) {

                                // Save new token
                                //SharedPreferenceManager.saveSecurity(tokenResult.getData());

                                LoginAccessTokenInfo loginAccessTokenInfo = (LoginAccessTokenInfo) tokenResult.getData();

                                // retry the 'mainRequest' which encountered an authentication error
                                // add new token into 'mainRequest' and request again
                                Request original = response.request();
                                HttpUrl originalHttpUrl = original.url();

                                // Request customization: remove old token and add new to url
                                HttpUrl url = originalHttpUrl
                                        .newBuilder()
                                        .removeAllEncodedQueryParameters("access_token")
                                        .addQueryParameter("access_token", loginAccessTokenInfo.getAccessToken())
                                        .build();

                                Request.Builder requestBuilder = original
                                        .newBuilder()
                                        .url(url);

                                return requestBuilder.build();
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}

