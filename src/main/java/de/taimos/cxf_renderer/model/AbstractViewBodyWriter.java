package de.taimos.cxf_renderer.model;

/*
 * #%L
 * MVC Renderer for JAX-RS with CXF
 * %%
 * Copyright (C) 2012 - 2013 Taimos GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
