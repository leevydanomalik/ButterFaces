package de.larmic.butterfaces.component.showcase;

import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.component.showcase.comboBox.Foo;
import de.larmic.butterfaces.component.showcase.comboBox.FooConverter;
import de.larmic.butterfaces.component.showcase.comboBox.FooType;

import javax.faces.view.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class RadioBoxShowcaseSingleCodeComponent extends AbstractInputShowcaseSingleCodeComponent implements Serializable {

	private ComboBoxValueType comboBoxValueType = ComboBoxValueType.STRING;
    private RadioBoxLayoutType radioBoxLayoutType = RadioBoxLayoutType.LINE_DIRECTION;

	private final List<SelectItem> foos = new ArrayList<SelectItem>();
	private final List<SelectItem> enums = new ArrayList<SelectItem>();
	private final List<SelectItem> strings = new ArrayList<SelectItem>();

	public RadioBoxShowcaseSingleCodeComponent() {
		this.initFoos();
		this.initStrings();
		this.initEnums();
	}

	@Override
	protected Object initValue() {
		return null;
	}

	@Override
	public Object getValue() {
		if (super.getValue() != null) {
			return super.getValue().toString();
		}

		return "(item is null)";
	}

	@Override
	public String getReadableValue() {
		if (super.getValue() != null) {
			if (super.getValue() instanceof Foo) {
				return ((Foo) super.getValue()).getValue();
			} else if (super.getValue() instanceof FooType) {
				return ((FooType) super.getValue()).label;
			}

			return (String) super.getValue();
		}

		return "(item is null)";
	}

	public List<SelectItem> getValues() {
		switch (this.comboBoxValueType) {
		case OBJECT:
			return this.foos;
		case ENUM:
			return this.enums;
		default:
			return this.strings;
		}
	}

	@Override
	public String getXHtml() {
		final StringBuilder sb = new StringBuilder();

        this.addXhtmlStart(sb);

		sb.append("        <b:radioBox id=\"input\"\n");
		sb.append("                    label=\"" + this.getLabel() + "\"\n");
		sb.append("                    value=\"" + this.getValue() + "\"\n");

        this.appendString("tooltip", this.getTooltip(), sb);

        this.appendBoolean("readonly", this.isReadonly(), sb);
        this.appendBoolean("required", this.isRequired(), sb);
        this.appendString("layout", radioBoxLayoutType.label, sb);
        this.appendString("styleClass", this.getStyleClass(), sb);
        this.appendString("inputStyleClass", this.getInputStyleClass(), sb);
        this.appendString("labelStyleClass", this.getLabelStyleClass(), sb);
        this.appendBoolean("rendered", this.isRendered(), sb, true);

		if (this.comboBoxValueType == ComboBoxValueType.STRING) {
			sb.append("            <f:selectItem itemValue=\"2000\" \n");
			sb.append("                          itemLabel=\"Year 2000\"/>\n");
			sb.append("            <f:selectItem itemValue=\"2010\" \n");
			sb.append("                          itemLabel=\"Year 2010\"/>\n");
			sb.append("            <f:selectItem itemValue=\"2020\" \n");
			sb.append("                          itemLabel=\"Year 2020\"/>\n");
		} else if (this.comboBoxValueType == ComboBoxValueType.ENUM) {
			sb.append("            <f:selectItems value=\"#{bean.fooEnums}\"/>\n");
		} else if (this.comboBoxValueType == ComboBoxValueType.OBJECT) {
			sb.append("            <f:selectItems value=\"#{bean.fooObjects}\"/>\n");
			sb.append("            <f:converter converterId=\"fooConverter\"/>\n");
		}

		this.createAjaxXhtml(sb, "change");

		sb.append("        </b:radioBox>");

		this.createOutputXhtml(sb);

        this.addXhtmlEnd(sb);

		return sb.toString();
	}

    @Override
    protected void addCss(StringBuilder sb) {
        if (!StringUtils.isEmpty(this.getStyleClass())) {
            sb.append(".some-demo-class {\n");
            sb.append("    background-color: red;\n");
            sb.append("}");
        }
    }

    protected String getEmptyDistanceString() {
        return "                    ";
    }

	public boolean isConverterActive() {
		return this.comboBoxValueType == ComboBoxValueType.OBJECT;
	}

	public List<SelectItem> getComboBoxTypes() {
		final List<SelectItem> items = new ArrayList<SelectItem>();

		for (final ComboBoxValueType type : ComboBoxValueType.values()) {
			items.add(new SelectItem(type, type.label));
		}
		return items;
	}

    public List<SelectItem> getRadioLayoutTypes() {
		final List<SelectItem> items = new ArrayList<SelectItem>();

		for (final RadioBoxLayoutType type : RadioBoxLayoutType.values()) {
			items.add(new SelectItem(type, type.label));
		}
		return items;
	}

	public ComboBoxValueType getComboBoxValueType() {
		return this.comboBoxValueType;
	}

	public void setComboBoxValueType(final ComboBoxValueType comboBoxValueType) {
		this.comboBoxValueType = comboBoxValueType;
	}

    public RadioBoxLayoutType getRadioBoxLayoutType() {
        return radioBoxLayoutType;
    }

    public void setRadioBoxLayoutType(RadioBoxLayoutType radioBoxLayoutType) {
        this.radioBoxLayoutType = radioBoxLayoutType;
    }

    private void initFoos() {
		for (final String key : FooConverter.fooMap.keySet()) {
			final Foo foo = FooConverter.fooMap.get(key);
			this.foos.add(new SelectItem(foo, foo.getKey()));
		}
	}

	private void initEnums() {
		for (final FooType fooType : FooType.values()) {
			this.enums.add(new SelectItem(fooType.label));
		}
	}

	private void initStrings() {
		this.strings.add(new SelectItem("2000", "Year 2000"));
		this.strings.add(new SelectItem("2010", "Year 2010"));
		this.strings.add(new SelectItem("2020", "Year 2020"));
	}
}