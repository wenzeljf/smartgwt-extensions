package com.smartgwt.extensions.hwc.client;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public interface HWCConfig {

	public static final String TEMPLATE_ROOT = "/com/smartgwt/extensions/hwc/server/template";
	
	public static final Map<String, String[]> CFG_DEFAULT = new HashMap<String, String[]>() {
		{
			put("projectName", new String[] { "^ *[^ ]+ *$", "HelloWorld" });
			put("entryName", new String[] { "^ *[a-zA-Z0-9_]+ *$", "HelloWorldApp" });
			put("packageName", new String[] { "^ *([a-zA-Z0-9_]+\\.)*[a-zA-Z0-9_]+ *$", "com.acme.helloworld" });
			put("groupId", new String[] { "^ *[^ ]+ *$", "com.acme" });
			put("artifactId", new String[] { "^ *[^ ]+ *$", "helloworld" });
			put("version", new String[] { "^ *[^ ]+ *$", "1.0-SNAPSHOT" });
			put("versionGWT", new String[] { "^ *[^ ]+ *$", VERSION_GWT_DEFAULT });
			put("versionSmartGWT", new String[] { "^ *[^ ]+ *$", VERSION_SMARTGWT_DEFAULT });
		}
	};
	
	public static final String VERSION_GWT_DEFAULT = "1.7.0";
	
	public static final LinkedHashMap<String, String> VERSION_GWT_VALUES = new LinkedHashMap<String, String>() {
		{
			put("1.5.3", "1.5.3");
			put("1.6.4", "1.6.4");
			put("1.7.0", "1.7.0");
		}
	};
	
	public static final String VERSION_SMARTGWT_DEFAULT = "1.1";
	
	public static final LinkedHashMap<String, String> VERSION_SMARTGWT_VALUES = new LinkedHashMap<String, String>() {
		{
			//put("1.0b2", "1.0b2 (beta)");
			put("1.1", "1.1 (release)");
			put("1.2-SNAPSHOT", "1.2-SNAPSHOT");
		}
	};
	
}
