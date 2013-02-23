package de.taimos.cxf_renderer.model;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

public abstract class AbstractViewBodyWriter implements MessageBodyWriter<ViewModel> {

	private final List<MediaType> mediaTypes = this.getMediaTypes();

	@Override
	public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
		return ViewModel.class.isAssignableFrom(type) && this.mediaTypes.contains(mediaType);
	}

	@Override
	public long getSize(final ViewModel t, final Class<?> type, final Type genericType, final Annotation[] annotations,
			final MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(final ViewModel t, final Class<?> type, final Type genericType, final Annotation[] annotations,
			final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream)
			throws IOException, WebApplicationException {

		this.write(t, mediaType, entityStream);
	}

	protected abstract void write(ViewModel t, MediaType mediaType, OutputStream entityStream) throws IOException, WebApplicationException;

	protected abstract List<MediaType> getMediaTypes();

}
