package de.larmic.butterfaces.component.showcase.tooltip;

public enum TooltipType {
	A("<a />"), COMMAND_LINK("<b:commandLink />");
	public final String label;

	private TooltipType(final String label) {
		this.label = label;
	}
}