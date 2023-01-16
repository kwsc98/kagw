package pers.kagw.core.protocol.okhttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author kwsc98
 */
@Slf4j
public class OkHttpClientService {

    private final OkHttpClient okHttpClient;

    public OkHttpClientService() {
        okHttpClient = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).readTimeout(3000, TimeUnit.MILLISECONDS).writeTimeout(3000, TimeUnit.MILLISECONDS).build();
    }

    public Response execute(Request request) throws IOException {
        return okHttpClient.newCall(request).execute();
    }


}
