package de.taimos.cxf_renderer.velocity;

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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.Log4JLogChute;

import de.taimos.cxf_renderer.model.AbstractViewBodyWriter;
import de.taimos.cxf_renderer.model.ViewModel;

public abstract class VelocityBodyWriter extends AbstractViewBodyWriter {

	static {
		try {
			// Use ClasspathLoader
			Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
			Velocity.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			// Use UTF-8
			Velocity.setProperty("input.encoding", "UTF-8");
			Velocity.setProperty("output.encoding", "UTF-8");
			// Use log4j
			Velocity.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, Log4JLogChute.class.getCanonicalName());
			Velocity.setProperty("runtime.log.logsystem.log4j.logger", "org.apache.velocity");
			Velocity.init();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private static String evaluateVM(final String name, final Map<String, Object> variables) throws IOException {
		try {
			/* lets make a Context and put data into it */
			final VelocityContext context = new VelocityContext();

			final Set<Entry<String, Object>> entrySet = variables.entrySet();
			for (final Entry<String, Object> entry : entrySet) {
				context.put(entry.getKey(), entry.getValue());
			}

			final Template template = Velocity.getTemplate(name);
			final StringWriter w = new StringWriter();
			template.merge(context, w);
			return w.toString();
		} catch (final Exception e) {
			throw new InternalServerErrorException(e);
		}
	}

	@Override
	protected void write(final ViewModel t, final MediaType mediaType, final OutputStream entityStream) throws IOException,
			WebApplicationException {

		final String templateName = this.generateTemplateName(t.getViewName(), mediaType);
		final String evaluate = VelocityBodyWriter.evaluateVM(templateName, t.getModel());
		entityStream.write(evaluate.getBytes());

	}

	protected abstract String generateTemplateName(String viewName, MediaType mediaType);

	@Override
	protected List<MediaType> getMediaTypes() {
		final List<MediaType> r = new ArrayList<>();
		r.add(MediaType.TEXT_HTML_TYPE);
		r.add(MediaType.TEXT_PLAIN_TYPE);
		return r;
	}

}
