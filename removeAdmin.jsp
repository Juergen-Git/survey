<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }
  if ( (String) request.getParameter ("cancel") != null ) { response.sendRedirect ( "manageAdmins.jsp?surveyId="+surveyId ); return; }

  String adminPID = (String) request.getParameter ( "adminPID" );
  boolean confirmed = false;
  if ( (String) request.getParameter ("ok") != null ) { confirmed = true; }
  if ( !adminPID.equals( pid ) ) { confirmed = true; }

  if ( confirmed ) {
    if ( adminPID != null ) {
      survey.removeAdminPID ( adminPID );
      survey.save ();

      // remove this survey from the user file
      User adminUser = new User ( Config.appDataDir, adminPID );
      if ( adminUser.exists() ) {
        adminUser.removeSurvey ( surveyId );
        adminUser.save ();
      }

      if ( adminPID.equals( pid ) ) {
      	mysession.removeAttribute ( null, "surveyId" );
        response.sendRedirect ( "index.jsp" ); return;
      }

    }
    response.sendRedirect ( "manageAdmins.jsp?surveyId="+surveyId ); return;
  }

  String pageTitle = Config.msg(182);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" />
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>" name="form">
  <input type="hidden" name="surveyId" value="<%=surveyId%>">
<br>
<span style="color:#ff0000"><b><%=Config.msg(32)%></b></span>
<%=Config.msg(236)%>
<br>
<br>
<%
  if ( adminPID != null )
    out.print ( "<input type=\"hidden\" name=\"adminPID\" value=\"" + adminPID + "\">\n" );
%>
<input type="submit" name="ok" value="<%=Config.msg(23)%>" class="button">&nbsp;&nbsp;<input type="submit" name="cancel" value="<%=Config.msg(24)%>" class="button">
</form>
<survey:pageFooter />