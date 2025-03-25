<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( (String) request.getParameter ( "cancel" ) != null ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( request.getParameter ( "deleteSurvey" ) != null ) {
    Iterator adminPIDs = survey.getAdminsSorted ();
    while ( adminPIDs.hasNext () ) {
      String adminPID = (String) adminPIDs.next ();

      // remove this survey from all the user who were administrators
      try {
        User user = new User ( Config.appDataDir, adminPID );
        user.removeSurvey ( surveyId );
        user.save ();
      }
      catch ( Exception e ) {} // the user may not exist anymore
    }
    survey.delete ();

    mysession.removeAttribute ( null, "surveyId" );
    request.getRequestDispatcher ( "/index.jsp" ).forward ( request, response );
  }

  String pageTitle = Config.msg(66) +" &quot;" + survey.getName () + "&quot;";
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" />
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>">
    <font color="#ff0000"><b><%=Config.msg(61)%></b></font> <%=Config.msg(67)%><br>
    <br>
<%
  if ( surveyId != null )
    out.print ( "<input type=\"hidden\" name=\"surveyId\" value=\"" + surveyId + "\">" );
%>
<input type="submit" name="deleteSurvey" value="<%=Config.msg(68)%>" class="button">&nbsp;
<input type="submit" name="cancel" value="<%=Config.msg(24)%>" class="button">
</form>
<survey:pageFooter />
