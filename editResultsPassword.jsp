<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( (String) request.getParameter ( "cancel" ) != null ) { 
  	response.sendRedirect("manageSurveyMenu.jsp?surveyId="+surveyId); return; 
  }

  String password1;
  if ( request.getParameter ( "password1" ) != null ) { password1 = request.getParameter ( "password1" ); }
  else { password1 = survey.getResultsPassword (); }
  String password2;
  if ( request.getParameter ( "password2" ) != null ) { password2 = request.getParameter ( "password2" ); }
  else { password2 = survey.getResultsPassword (); }
  String accessResultsRestriction = "";

  boolean invalidResultsPassword = false;

  if ( request.getParameter ( "save" ) != null ) {
    // check if name is valid (valid characters & not taken yet) otherwise forward back to survey creation UI
//    Regex r = new Regex( Config.regexValidResultsPassword );

    if ( password1.equals (password2) && ( password1.equals("") || Pattern.matches ( Config.regexValidResultsPassword, password1 ) ) ) {
      accessResultsRestriction = (String) request.getParameter ( "accessResultsRestriction" );
//      if ( accessResultsRestriction == null ) { accessResultsRestriction = "owner"; }
      if ( accessResultsRestriction.equals( "owner" ) ) { survey.setAccessResultsRestriction ( "owner" ); }
      if ( accessResultsRestriction.equals( "anyone" ) ) { survey.setAccessResultsRestriction ( "anyone" ); }
      if ( accessResultsRestriction.equals( "password" ) ) { survey.setAccessResultsRestriction ( "password" ); }
      survey.setResultsPassword ( password1 );
      survey.save ();

      response.sendRedirect("manageSurveyMenu.jsp?surveyId="+surveyId);
      return;
    }
    else {
      invalidResultsPassword = true;
    }
  }

  accessResultsRestriction = survey.getAccessResultsRestriction();
  if ( accessResultsRestriction == null || accessResultsRestriction.equals("") ) {
    accessResultsRestriction = "owner";
  }

  String pageTitle = Config.msg(114);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; " + pageTitle );

  String onLoad = "";
  if ( !accessResultsRestriction.equals( "password" ) ) {
    onLoad = "disablePasswords()";
  }
%><survey:pageHeader title="<%=pageTitle%>" onLoad="<%=onLoad%>" />
<%
  String surveyUrl = Config.urlScheme+"://" + Config.hostName + Config.rootURL + "viewResults.jsp?id=" + surveyId;
%>
<script language="JavaScript" type="text/javascript"><!--
function checkRestriction ( o ) {
  if ( o.value == "password" ) {
    enablePasswords ();
  }
  else {
    disablePasswords ();
  }
}
function enablePasswords () {
  document.form.password1.disabled = false;
  document.form.password1.style.backgroundColor = "#ffffff";
  document.form.password2.disabled = false;
  document.form.password2.style.backgroundColor = "#ffffff";
}

function disablePasswords () {
  document.form.password1.disabled = true;
  document.form.password1.style.backgroundColor = "#eeeeee";
  document.form.password2.disabled = true;
  document.form.password2.style.backgroundColor = "#eeeeee";
}

//-->
</script>
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>" name="form">
<input type="hidden" name="surveyId" value="<%=surveyId%>">
<table border="0" cellspacing="3" cellpadding="3">
  <tr>
    <td valign="top" align="right">
      <input type="radio" name="accessResultsRestriction" value="owner"<%
        if ( accessResultsRestriction.equals("owner") ) { out.print( " checked" ); }
      %> onClick="checkRestriction(this)">
    </td>
    <td><%=Config.msg(115)%></td>
  </tr>
  <tr>
    <td valign="top" align="right">
      <input type="radio" name="accessResultsRestriction" value="anyone"<%
        if ( accessResultsRestriction.equals("anyone") ) { out.print( " checked" ); }
      %> onClick="checkRestriction(this)">
    </td>
    <td><%=Config.msg(116)%></td>
  </tr>
  <tr>
    <td valign="top" align="right">
      <input type="radio" name="accessResultsRestriction" value="password"<%
        if ( accessResultsRestriction.equals("password") ) { out.print( " checked" ); }
      %> onClick="checkRestriction(this)">
    </td>
    <td valign="top"><%=Config.msg(117)%><br>
<%
  if ( invalidResultsPassword ) {
    out.println ( "<br><font color=\"red\"><b>"+Config.msg(118)+"</b></font><br>" );
  }
%>
      <table border="0" cellspacing="3" cellpadding="3">
        <tr>
          <td align="right"><%=Config.msg(119)%></td>
          <td>
            <input type="password" name="password1" value="<%=password1%>" size="25" maxlength="<%=Config.maxLenResultsPassword%>">
          </td>
        </tr>
        <tr>
          <td><%=Config.msg(120)%></td>
          <td>
            <input type="password" name="password2" value="<%=password2%>" size="25" maxlength="<%=Config.maxLenResultsPassword%>">
          </td>
        </tr>
      </table>
      <%=Config.msg(121)%><br>
<%=surveyUrl%><br>
<%=Config.msg(122)%> <br>
    </td>
  </tr>
</table>
<br>
&nbsp;&nbsp;<input type="submit" name="save" value="<%=Config.msg(23)%>" class="button">&nbsp;&nbsp;<input type="submit" name="cancel" value="<%=Config.msg(24)%>" class="button">
</form>
<survey:pageFooter />
