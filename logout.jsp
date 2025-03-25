<%@ include file="/globalNoAuthRequired.jsp" %><%
  session.invalidate ();
  response.sendRedirect ( "login.jsp?r=1" ); return;
%>