package http;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author liuteng
 * @version 1.0
 * @date 2020/7/6
 */
public class OkHttpClientTest {
    static OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) throws IOException {
        String video = "http://play.vidcube.gm825.com/video/294461.mp4?sign=732e797383b43d76541b2d86f127996a&id=4854225&cpid=UGmKkZ&code=540p&por=1&classifaction=1";
        String result = run(video);
        System.out.println(result);
    }
    public static String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

}
