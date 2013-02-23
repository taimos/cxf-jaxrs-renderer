/**
 * 
 */
package de.taimos.cxf_renderer.velocity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;

import de.taimos.cxf_renderer.model.AbstractViewBodyWriter;
import de.taimos.cxf_renderer.model.ViewModel;

/**
 * @author thoeger
 * 
 *         Copyright 2012, Taimos GmbH
 * 
 */
public abstract class VelocityBodyWriter extends AbstractViewBodyWriter {

	static {
		try {
			/* first, we init the runtime engine. Defaults are fine. */
			Velocity.setProperty(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");
			Velocity.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogSystem");
			Velocity.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, "./templates");
			Velocity.init();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private static String evaluateVM(final String template, final Map<String, Object> variables) throws IOException {
		try {
			/* lets make a Context and put data into it */
			final VelocityContext context = new VelocityContext();

			final Set<Entry<String, Object>> entrySet = variables.entrySet();
			for (final Entry<String, Object> entry : entrySet) {
				context.put(entry.getKey(), entry.getValue());
			}

			final StringWriter w = new StringWriter();
			Velocity.evaluate(context, w, "generation of template", template);
			return w.toString();
		} catch (ParseErrorException | MethodInvocationException | ResourceNotFoundException e) {
			throw new InternalServerErrorException(e);
		}
	}

	private static String readContent(final String name) throws IOException {
		try (Scanner s = new Scanner(VelocityBodyWriter.class.getResourceAsStream(name))) {
			final StringBuilder sb = new StringBuilder();
			while (s.hasNextLine()) {
				sb.append(s.nextLine());
				sb.append("\n");
			}
			return sb.toString();
		}
	}

	@Override
	protected void write(final ViewModel t, final MediaType mediaType, final OutputStream entityStream) throws IOException,
			WebApplicationException {

		final String templateName = this.generateTemplateName(t.getViewName(), mediaType);
		final String content = VelocityBodyWriter.readContent(templateName);
		final String evaluate = VelocityBodyWriter.evaluateVM(content, t.getModel());
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
