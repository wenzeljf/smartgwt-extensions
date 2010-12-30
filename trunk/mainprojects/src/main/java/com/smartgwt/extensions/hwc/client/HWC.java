package com.smartgwt.extensions.hwc.client;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.SectionItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;

public class HWC extends Window implements EntryPoint, HWCConfig {

	public HWC() {
		setWidth(300);
		setHeight(370);
		setTitle("'Hello World!' Creator");
		setHeaderIcon("hwc/wizard.png");
		setAutoCenter(true);	
		addItem(new DynamicForm() {
			{
				setNumCols(2);
				setAction(GWT.getModuleBaseURL() + "HWCServlet");
				setCanSubmit(true);
				setItems(buildSectionItem("_secIntro", "Introduction", true, "intro"),
						new StaticTextItem("intro") {
							{
								setColSpan(2);
								setShowTitle(false);
								setValue("...");
							}
						}, 
						buildSectionItem("sectionApp", "Application", false, "projectName", "packageName", "entryName"),
						buildTextItem("projectName", "Project", 140),
						buildTextItem("entryName", "EntryPoint", 140),
						buildTextItem("packageName", "Package", 140),
						buildSectionItem("sectionMaven", "Maven Artifact", false, "artifactId", "groupId", "version"),
						buildTextItem("groupId", "Group Id", 130),
						buildTextItem("artifactId", "Artifact Id", 130),
						buildTextItem("version", "Version", 130),
						buildSectionItem("sectionLibs", "Dependencies", false, "versionGWT", "versionSmartGWT"),
						buildComboItem("versionGWT", "GWT",	120, VERSION_GWT_VALUES),
						buildComboItem("versionSmartGWT",	"SmartGWT", 120, VERSION_SMARTGWT_VALUES),
						buildSpacer(5),
						new ButtonItem("submit", "Generate ZIP...") {
							{
								setColSpan(2);
								setAlign(Alignment.CENTER);
								addClickHandler(new ClickHandler() {
									public void onClick(ClickEvent event) {
										if (validate())
											submitForm();
									}
								});
							}
						},
						buildSpacer(5));
			}
		});
	}

	public void onModuleLoad() {
		show();
	}

	public static ComboBoxItem buildComboItem(final String name, String label, final int width, final LinkedHashMap<String, String> values) {
		return new ComboBoxItem(name, label) {
			{
				setWidth(width);
				setType("select");
				setValueMap(values);
				setValue(CFG_DEFAULT.get(name)[1]);
				setRequired(true);
				setValidateOnChange(true);
				setValidators(new RegExpValidator() {
					{
						setExpression(CFG_DEFAULT.get(name)[0]);
					}
				});
			}
		};
	}

	public static SectionItem buildSectionItem(String name, final String label, final boolean canCollapse, final String... itemIds) {
		return new SectionItem(name) {
			{
				setDefaultValue(label);
				setItemIds(itemIds);
				setCanCollapse(canCollapse);
				if (canCollapse)
					setSectionExpanded(false);
			}
		};
	}

    public static TextItem buildTextItem(final String name, String label, final int width) {
        return new TextItem(name, label) {
            {
                setWidth(width);
                setValue(CFG_DEFAULT.get(name)[1]);
                setRequired(true);
                setValidators(new RegExpValidator() {
                    {
                        setExpression(CFG_DEFAULT.get(name)[0]);
                    }
                });
            }
        };
    }

    public static SpacerItem buildSpacer(final int height) {
        return new SpacerItem() {
            {
                setColSpan(2);
                setHeight(height);
            }
        };
    }

}
