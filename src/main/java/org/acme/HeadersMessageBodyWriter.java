package org.acme;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class HeadersMessageBodyWriter implements MessageBodyWriter<String> {

    @Override
    public boolean isWriteable(Class aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return String.class.isAssignableFrom(aClass) && MediaType.TEXT_PLAIN_TYPE.isCompatible(mediaType);
    }

    @Override
    public void writeTo(String s, Class aClass, Type type, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        final String content = "Headers response: " + s;
        outputStream.write(content.getBytes());
    }
}
