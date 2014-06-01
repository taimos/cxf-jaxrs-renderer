package de.taimos.cxf_renderer.model;

/*
 * #%L MVC Renderer for JAX-RS with CXF %% Copyright (C) 2012 - 2013 Taimos GmbH %% Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License. #L%
 */

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.InternalServerErrorException;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.Log4JLogChute;

public abstract class ViewModel {

	private final String viewName;

	private final Map<String, Object> model = new HashMap<>();

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
	
	
	private static String evaluateVM(final String name, final Map<String, Object> variables) {
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

	/**
	 * @param viewName the logical view name
	 */
	public ViewModel(final String viewName) {
		this.viewName = viewName;
	}

	public final String getViewName() {
		return this.viewName;
	}

	public final Map<String, Object> getModel() {
		return this.model;
	}

	/**
	 * @param name the key of the entry
	 * @param value the value of the model entry
	 */
	public final void addModel(final String name, final Object value) {
		this.model.put(name, value);
	}
	
	protected abstract String generateTemplateName();

	public final RenderedUI render() {
		RenderedUI ui = new RenderedUI();
		final String templateName = this.generateTemplateName();
		String evaluateVM = ViewModel.evaluateVM(templateName, this.getModel());
		ui.setContent(evaluateVM);
		return ui;
	}

}
