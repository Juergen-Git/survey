<%@ include file="/globalNoAuthRequired.jsp" %><%
  out.print ( "<healthCheck version=\".9\">\n" );
  out.print ( "  <test importance=\"1\">\n" );
  out.print ( "    <name>" + Config.serviceName + "</name>\n" );
  out.print ( "    <status>OK</status>\n" );
  out.print ( "  </test>\n" );
  out.print ( "</healthCheck>\n" );
%>