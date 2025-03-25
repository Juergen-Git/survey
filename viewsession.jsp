<%@ page import="java.util.*" %>
<p>Session Info:</p>
<%
 Enumeration e = session.getAttributeNames();

    while (e.hasMoreElements()) {
    	String name = (String) e.nextElement();
    	out.println(name + " ");
    	Object o = session.getAttribute(name);
    	if (o instanceof String) {
    		out.println(o);
    	} 
    	else if (o instanceof Hashtable) {
    		out.println(o.toString());
    	} 
    	else {
    		out.println("some other object");
    	}
    	out.println("<br/>\n");
    }
    
%>