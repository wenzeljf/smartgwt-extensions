package com.smartgwt.extensions.hwc.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.smartgwt.extensions.hwc.client.HWCConfig;

public class HWCServlet extends HttpServlet implements HWCConfig {

	private static final long serialVersionUID = 1L;

	private static void addFile(Map<String, String> cfg, ZipOutputStream out, String project, String name, String template) throws IOException {

		String line = null;
		InputStream in = null;
		StringBuffer sb = new StringBuffer();
		try {
			in = getRes(TEMPLATE_ROOT + "/" + template + ".hwct");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while (null != (line = br.readLine()))
				sb.append(line + "\n");
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (Exception ignored) {
			}
		}

		String s = sb.toString();
		for (Object key : cfg.keySet())
			s = s.replaceAll("@@@" + key + "@@@", cfg.get(key));

		ZipEntry ze = new ZipEntry(project + "/" + name);
		out.putNextEntry(ze);
		byte[] b = s.getBytes("UTF-8");
		out.write(b, 0, b.length);
		out.flush();
	}

	public static String getParm(HttpServletRequest req, String name) throws ServletException {

		String v = req.getParameter(name);
		v = (v == null) ? "" : v.trim();
		if (!v.matches(CFG_DEFAULT.get(name)[0]))
			throw new ServletException("bad parameter (" + name + "): " + v);

		return v;
	}

	public static InputStream getRes(String name) throws IOException {

		InputStream in = HWCServlet.class.getResourceAsStream(name);
		if (in == null)
			throw new IOException("missing resource: " + name);

		return in;
	}

	protected void service(HttpServletRequest req, HttpServletResponse res)	throws ServletException, IOException {

		Map<String, String> cfg = new HashMap<String, String>();

		String project = getParm(req, "projectName");
		cfg.put("projectName", project);

		String entry = getParm(req, "entryName");
		cfg.put("entryName", entry);

		String pkg = getParm(req, "packageName");
		cfg.put("packageName", pkg);

		cfg.put("groupId", getParm(req, "groupId"));
		cfg.put("artifactId", getParm(req, "artifactId"));
		cfg.put("version", getParm(req, "version"));
		
		cfg.put("versionGWT", getParm(req, "versionGWT"));
		cfg.put("versionSmartGWT", getParm(req, "versionSmartGWT"));

		res.setContentType("application/octet-stream");
		res.addHeader("content-disposition", "attachment;filename=" + project + ".zip");

		ZipOutputStream zipOut = null;
		try {
			String packagePath = pkg.replace('.', '/');
			zipOut = new ZipOutputStream(res.getOutputStream());
			addFile(cfg, zipOut, project, "pom.xml", "pom.xml");
			addFile(cfg, zipOut, project, "src/main/resources/" + packagePath + "/" + entry + ".gwt.xml", "Module.gwt.xml");
			addFile(cfg, zipOut, project, "src/main/java/" + packagePath + "/client/" + entry + ".java", "Module.java");
			addFile(cfg, zipOut, project, "src/main/resources/"	+ packagePath + "/public/" + entry + ".html", "Module.html");
			addFile(cfg, zipOut, project, "src/main/resources/"	+ packagePath + "/public/" + entry + ".css", "Module.css");
			addFile(cfg, zipOut, project, "src/main/webapp/index.jsp", "webapp/index.jsp");
			addFile(cfg, zipOut, project, "src/main/webapp/WEB-INF/web.xml", "webapp/WEB-INF/web.xml");
			zipOut.finish();
			zipOut.flush();
		} finally {
			try {
				if (zipOut != null)
					zipOut.close();
			} catch (Exception ignored) {
			}
		}
	}

}
