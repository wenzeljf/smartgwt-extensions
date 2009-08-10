/**
Copyright (c) <2009> <Pete Boysen>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.smartgwt.extensions.fileuploader.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;

/**
 * Provide a means to test interaction of widgets in hosted mode. Put the following in gwt.xml file:
 * 	<servlet path ="/test" class ="edu.iastate.its.thinkspace.gwt.server.TestServiceImpl"/>

 *
 * @author Pete Boysen (pboysen@iastate.edu)
 * @since May 9, 2009
 * @version May 9, 2009
 */
public class TestServiceImpl extends HttpServlet {

	public TestServiceImpl() {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		process(request,response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		process(request,response);
	}
	
	private void process(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				processFiles(request,response);
			} else {
				processQuery(request,response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private void processQuery(HttpServletRequest request, HttpServletResponse response) {
		try {
			System.out.println("--------------------------------------------");
			for (Enumeration<String> en = request.getParameterNames(); en.hasMoreElements();) {
				String name = en.nextElement();
				System.out.println(name+"="+request.getParameter(name));
			}
			// these are parameters I pass from my ProjectDataSource on all calls.  Change to meet your needs.
			String context = request.getParameter("context");
			String model = request.getParameter("model");
			String xq = request.getParameter("xq");
			String op = request.getParameter("_operationType");
			response.setContentType("text/xml");
			response.setHeader("Pragma", "No-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Cache-Control", "no-cache");

			PrintWriter out = response.getWriter();
			getResponse(out,context,model,xq,op);
			out.flush();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	private void processFiles(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String,String> args = new HashMap<String,String>();
		try {
			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iter = upload.getItemIterator(request);
			FileItemStream fileItem = null;
			// pick up parameters first and note actual FileItem
			while (iter.hasNext()) {
			    FileItemStream item = iter.next();
			    String name = item.getFieldName();
			    if (item.isFormField()) {
			        args.put(name,Streams.asString(item.openStream()));
			    } else {
			    	fileItem = item;
			    }
			}
			if (fileItem != null) {
		        args.put("contentType", fileItem.getContentType());
		        args.put("fileName", FileUtils.filename(fileItem.getName()));
		        System.out.println("uploading args "+args);
				String context = args.get("context");
				String model = args.get("model");
				String xq = args.get("xq");
				System.out.println(context+","+model+","+xq);
				File f = new File(args.get("fileName"));
				System.out.println(f.getAbsolutePath());
		        /*
		         * TODO: pboysen get the state, context and fileManager and store the
		         * stream in fileName.  Parameters should be passed to locate state 
		         * and conversion options.
		         */
				response.setContentType("text/html");
				response.setHeader("Pragma", "No-cache");
				response.setDateHeader("Expires", 0);
				response.setHeader("Cache-Control", "no-cache");
				PrintWriter out = response.getWriter();
				out.println("<html>");
				out.println("<body>");
				out.println("<script>");
				out.println("top.uploadComplete('"+args.get("fileName")+"');");
				out.println("</script>");
				out.println("</body>");
				out.println("</html>");
				out.flush();
			} else {
				//TODO: add error code
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * This files a test file that contains a response for all CRUD operations. See example
	 * profile.xq.xml file.
	 * @param out
	 * @param context
	 * @param model
	 * @param xq
	 * @param op
	 * @throws Exception
	 */
	private void getResponse(PrintWriter out, String context, String model, String xq, String op) 
		throws Exception {
		String path = "src/test/webapp/WEB-INF/models/"+model+"/"+xq+".xml";
		System.out.println("getting datafile "+path);
		LineNumberReader in = null;
		File f = new File(path);
		if (!f.exists()) {
			System.out.println("File not found:"+path);
			out.println("<response>");
			out.println("  <status>1</status");
			out.println("</response>");
			return;
		}
		try {
			String start = "<"+op+">";
			String end = "</"+op+">";
			in = new LineNumberReader(new FileReader(f));
			String line = null;
			boolean inOp = false;
			while ((line = in.readLine()) != null) {
				if (line.indexOf(start)> 0)
					inOp = true;
				else if (line.indexOf(end) > 0)
					inOp = false;
				else if (inOp)
					out.println(line);
				//System.out.println(line);
			}
		} catch (FileNotFoundException fnf) {
			throw new Exception(fnf);
		} finally {
			if (in != null)
				try { in.close(); } catch (Exception e){};
		}
	}
}