package io.quarkus.brotli4j;

import io.netty.handler.codec.compression.BrotliOptions;
import io.netty.handler.codec.compression.StandardCompressionOptions;
import io.quarkus.vertx.http.HttpServerOptionsCustomizer;
import io.vertx.core.http.HttpServerOptions;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Brotli4JHttpServerConfig implements HttpServerOptionsCustomizer {
    private static final int compressionLevel = 4;

    @Override
    public void customizeHttpServer(HttpServerOptions options) {
        options.addCompressor(getBrotliOptions(compressionLevel));
    }

    @Override
    public void customizeHttpsServer(HttpServerOptions options) {
        options.addCompressor(getBrotliOptions(compressionLevel));
    }

    private static BrotliOptions getBrotliOptions(int compressionLevel) {
        return StandardCompressionOptions.brotli();
    }
}
