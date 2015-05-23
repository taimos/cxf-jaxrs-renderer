package de.taimos.cxf_renderer.model;

/*
 * #%L
 * MVC Renderer for JAX-RS with CXF
 * %%
 * Copyright (C) 2012 - 2015 Taimos GmbH
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
