<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( (String) request.getParameter ( "done" ) != null ) {
    response.sendRedirect ( "manageSurveyMenu.jsp?surveyId="+surveyId ); return;
  }

  String errorMessage = "";

  if ( (String) request.getParameter ( "adminPID" ) != null ) {
    String adminPID = (String) request.getParameter ( "adminPID" );

    if ( !survey.isAdmin ( adminPID ) ) {
      User adminUser = new User ( Config.appDataDir, adminPID );

      if ( adminUser.exists() ) {
        // register this survey in the appropriate user file
        adminUser.addSurvey ( surveyId );
        adminUser.save ();

        survey.addAdminPID ( adminPID );
        survey.save ();
      }
      else {
        errorMessage = Config.msg(180)+" " + Config.serviceName +". "+Config.msg(181);
      }
    }
  }

  String pageTitle = Config.msg(182);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" onLoad="javascript:document.form.adminPID.focus()" />
<script language="JavaScript" type="text/javascript"><!--
function isIE4()
{ return( navigator.appName.indexOf("Microsoft") != -1 && (navigator.appVersion.charAt(0)=='4') ); }

function new_window(freshurl) {
  SmallWin = window.open(freshurl, 'Survey','scrollbars=yes,resizable=yes,toolbar=no,height=400,width=720');
  if (!isIE4())	{
    if (window.focus) { SmallWin.focus(); }
  }
  if (SmallWin.opener == null) SmallWin.opener = window;
  SmallWin.opener.name = "Main";
}
//-->
</script>
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>" name="form">
  <input type="hidden" name="surveyId" value="<%=surveyId%>">
<table border="0" cellspacing="0" cellpadding="3">
<%
  if ( !errorMessage.equals("") ) {
%>
  <tr>
    <td colspan="2"><%=Config.formatFeedback("error", errorMessage) %></td>
  </tr>
<%
  } // end: if ( !errorMessage.equals("") )
%>
  <tr>
    <td colspan="2"><b><%=Config.serviceProvider%> <%=Config.msg(183)%></b>
      <input type="text" name="adminPID" size="10" maxlength="8">
      <input type="submit" name="add" value="<%=Config.msg(184)%>" class="button">
    </td>
  </tr>
  <tr>
    <td colspan="2"><%=Config.msg(185)%></td>
  </tr>
  <tr>
    <td colspan="2"><img src="images/trans.gif" height="5" width="1" alt=""><br>
    </td>
  </tr>
  <tr class="menusectionheader">
    <td><%=Config.serviceProvider%> <%=Config.msg(186)%></td>
    <td align="right">&nbsp;</td>
  </tr>
<%
  edu.vt.ward.survey.LdapName ldapName = null;
  if ( !Config.authenticationMethodStandalone ) { ldapName = new edu.vt.ward.survey.LdapName(); }

  String bgColor="#eeeeee";
  Iterator adminPIDs = survey.getAdminsSorted ();
  while ( adminPIDs.hasNext () ) {
    String adminPID = (String) adminPIDs.next ();

    String fullName = "";
    if ( !Config.authenticationMethodStandalone ) {
      try {
        fullName = ldapName.getFullNameInParenthesis ( adminPID );
      }
      catch ( Exception e ) {}
    }

    if ( bgColor.equals("#eeeeee") ) { bgColor = "#ffffff"; } else { bgColor = "#eeeeee"; }
%>
  <tr>
    <td bgcolor="<%=bgColor%>"><%=adminPID%> <%=fullName%></td>
    <td bgcolor="<%=bgColor%>" align="right">
<%
  if ( survey.getNumAdmins() > 1 ) {
%>
      <a href="removeAdmin.jsp?surveyId=<%=surveyId%>&adminPID=<%=adminPID%>"><%=Config.msg(187)%></a>
<%
  }
%>&nbsp;</td>
  </tr>
<%
  } // end: while ( adminPIDs.hasNext () )
%>
</table>
<br>
<input type="submit" name="done" value="<%=Config.msg(82)%>" class="button">
</form>
<survey:pageFooter />