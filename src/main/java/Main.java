import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static final String URI =
            "https://api.nasa.gov/planetary/apod?api_key=" +
                    "hFCfg2MbTzAcD7ac3hzSPSEoaxQdbyEZBebxCVFo";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(URI);
        CloseableHttpResponse response = httpClient.execute(request);

        NasaObject answer = mapper.readValue(response.getEntity().getContent(), NasaObject.class);
        String hdurl = answer.getHdurl();
        System.out.println(hdurl);

        int lastIndex = hdurl.lastIndexOf("/");
        String targetFileName = hdurl.substring(lastIndex + 1);

        HttpGet requestImage = new HttpGet(hdurl);
        CloseableHttpResponse image = httpClient.execute(requestImage);

        HttpEntity entity = image.getEntity();

        try (FileOutputStream fos = new FileOutputStream(targetFileName);) {
            entity.writeTo(fos);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
