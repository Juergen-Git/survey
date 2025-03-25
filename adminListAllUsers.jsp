<%@ include file="/globalAuthRequired.jsp" %><%
  if ( !pid.equals("admin") ) { response.sendRedirect ( "index.jsp" ); return; }
  if ( (String) request.getParameter ( "backToMainMenu" ) != null ) { response.sendRedirect("index.jsp"); return; }

  String pageTitle = Config.msg(27);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" onLoad="" />
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>">
 <input type="submit" name="backToMainMenu" value="<%=Config.msg(28)%>" class="button">
</form>
<%
  File userDataDirFile = new File ( Config.appDataDir + "users" + Config.FILE_SEPARATOR );
  java.io.File[] userFileList = userDataDirFile.listFiles( );
  for ( int i = 0; i < userFileList.length; i++ ) {
    if ( !userFileList[i].getName().equals("admin") ) { // don't show "admin"
      if ( userFileList[i].isDirectory() ) {
        out.print(userFileList[i].getName() + "<br>\n");
      }
    }
  }
%>
<survey:pageFooter />
