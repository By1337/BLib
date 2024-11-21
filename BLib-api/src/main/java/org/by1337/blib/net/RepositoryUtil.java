package org.by1337.blib.net;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

public class RepositoryUtil {
    private static final boolean hasCloseMethod;

    public static Path downloadIfNotExist(String repo, String groupId, String artifactId, String version, Path folder) {
        // url/{groupId}/{artifactId}/{version}/{artifactId}-{version}.jar
        String fullUrl = repo + '/' +
                groupId.replace(".", "/") + '/' +
                artifactId + '/' +
                version + '/' +
                artifactId + "-" + version + ".jar";
        ;
        Path resultFolder = folder.resolve(groupId.replace(".", "/")).resolve(artifactId).resolve(version);
        resultFolder.toFile().mkdirs();
        Path out = resultFolder.resolve(artifactId + "-" + version + ".jar");
        if (out.toFile().exists()) {
            return out;
        }

        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fullUrl))
                    .build();

            HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(out));
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.statusCode());
            }
            return out;
        } catch (Exception t) {
            throw new RuntimeException(t);
        } finally {
            if (hasCloseMethod) {
                client.close(); // since 21
            }
        }
    }

    static {
        boolean hasCloseMethod1;
        try {
            HttpClient.class.getMethod("close");
            hasCloseMethod1 = true;
        } catch (NoSuchMethodException e) {
            hasCloseMethod1 = false;
        }
        hasCloseMethod = hasCloseMethod1;
    }
}
