package de.taimos.cxf_renderer.model;

import java.util.HashMap;
import java.util.Map;

public class ViewModel {

	private final String viewName;

	private final Map<String, Object> model = new HashMap<>();

	/**
	 * @param viewName
	 *            the logical view name
	 */
	public ViewModel(final String viewName) {
		this.viewName = viewName;
	}

	public String getViewName() {
		return this.viewName;
	}

	public Map<String, Object> getModel() {
		return this.model;
	}

	/**
	 * @param name
	 *            the key of the entry
	 * @param value
	 *            the value of the model entry
	 */
	public void addModel(final String name, final Object value) {
		this.model.put(name, value);
	}

}
