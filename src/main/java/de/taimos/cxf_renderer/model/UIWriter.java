package de.taimos.cxf_renderer.model;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

public class UIWriter implements MessageBodyWriter<RenderedUI> {

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return RenderedUI.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(RenderedUI t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return t.getContent().length();
	}

	@Override
	public void writeTo(RenderedUI t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
		entityStream.write(t.getContent().getBytes("UTF-8"));
	}
}
