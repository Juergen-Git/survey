<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( (String) request.getParameter ( "cancel" ) != null ) {
    response.sendRedirect ( "manageSurveyMenu.jsp?surveyId="+surveyId ); return;
  }

  String errorMessage = "";

  String entryRestriction = (String) request.getParameter("entryRestriction");
  if ( entryRestriction == null ) { entryRestriction = survey.getEntryRestriction(); }
  String entryPassword = (String) request.getParameter("entryPassword");
  if ( entryPassword == null ) { entryPassword = survey.getEntryPassword(); }

  boolean oneEntryOnly = false;
  String oneEntryOnlyStr = (String) request.getParameter("oneEntryOnly");

  // security checks
  if ( !(entryRestriction.equals("public") || entryRestriction.equals("password") || entryRestriction.equals("vt") || entryRestriction.equals("members"))) {
    entryRestriction = "public";
  }

  if ( !entryPassword.equals("") && !Pattern.matches( Config.regexValidEntryPassword, entryPassword ) ) {
    errorMessage = Config.msg(84);
  }

  if ( entryRestriction.equals("password") && entryPassword.equals("") ) {
    errorMessage = Config.msg(85);
  }


  if ( (String) request.getParameter ( "ok" ) != null && errorMessage.equals("") ) {
    survey.setEntryRestriction ( entryRestriction );
    survey.setEntryPassword ( entryPassword );

    if ( oneEntryOnlyStr != null && oneEntryOnlyStr.equals("1") ) { oneEntryOnly = true; }
    else { oneEntryOnly = false; }
    if ( entryRestriction.equals("public") || entryRestriction.equals("password")) { oneEntryOnly = false; }
    survey.setOneEntryOnly ( oneEntryOnly );

    survey.save();
    response.sendRedirect ( "manageSurveyMenu.jsp?surveyId="+surveyId ); return;
  }
  else {
    oneEntryOnly = survey.getOneEntryOnly();
    if ( entryRestriction.equals("public") || entryRestriction.equals("password")) { oneEntryOnly = false; }
  }

  String pageTitle = Config.msg(86);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" onLoad="changedRestrictions()" />
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
<script language="JavaScript" type="text/javascript"><!--
function changedRestrictions() {
  var f = document.form;
  var i;
  var selectedRestriction;
  var public = 0;
  var password = 1;
  var vt = 2;
  var members = 3;

  for (i = 0; i < f.entryRestriction.length; i++) {
    if (f.entryRestriction[i].checked == true) {
      selectedRestriction = i;
    }
  }

<%
  if ( !Config.authenticationMethodStandalone ) {
%>
  if ( selectedRestriction == public ) {
    document.form.oneEntryOnly.checked = false;
    document.form.oneEntryOnly.disabled = true;
  }
  else if ( selectedRestriction == password ) {
    document.form.oneEntryOnly.checked = false;
    document.form.oneEntryOnly.disabled = true;
  }
  else if ( selectedRestriction == vt ) {
    document.form.oneEntryOnly.disabled = false;
  }
  else if ( selectedRestriction == members ) {
    document.form.oneEntryOnly.disabled = false;
  }
<%
  } // end: if ( !Config.authenticationMethodStandalone )
%>
}

//-->
</script>

<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>" name="form">
 <input type="hidden" name="surveyId" value="<%=surveyId%>">
<%
  if ( !errorMessage.equals("")) { out.print ( Config.formatFeedback ( "error", errorMessage ) + "<br><br>" ); }
%>
  <table border="0" cellspacing="0" cellpadding="2">
    <tr>
      <td align="center">
        <input type="radio" name="entryRestriction" value="public" onClick="changedRestrictions()" <%=entryRestriction.equals("public")?" checked":""%>>
      </td>
      <td>&nbsp;</td>
      <td><%=Config.msg(87)%></td>
    </tr>
    <tr>
      <td align="center">
        <input type="radio" name="entryRestriction" value="password" onClick="changedRestrictions()" <%=entryRestriction.equals("password")?" checked":""%>>
      </td>
      <td>&nbsp;</td>
      <td nowrap><%=Config.msg(88)%>
        <input type="text" size="15" maxlength="<%=Config.maxLenEntryPassword%>" name="entryPassword" value="<%=entryPassword%>">
      </td>
    </tr>
<%
  if ( !Config.authenticationMethodStandalone ) {
%>
    <tr>
      <td align="center">
        <input type="radio" name="entryRestriction" value="vt" onClick="changedRestrictions()" <%=entryRestriction.equals("vt")?" checked":""%>>
      </td>
      <td>&nbsp;</td>
      <td><%=Config.msg(89)%> <%=Config.serviceProvider%> <%=Config.msg(90)%></td>
    </tr>
    <tr>
      <td align="center">
        <input type="radio" name="entryRestriction" value="members" onClick="changedRestrictions()" <%=entryRestriction.equals("members")?" checked":""%>>
      </td>
      <td>&nbsp;</td>
      <td><%=Config.msg(91)%> (<a href="JavaScript:new_window('manageMembersFrameset.jsp?surveyId=<%=surveyId%>')"><%=Config.msg(92)%></a>)</td>
    </tr>
    <tr><td colspan="3">&nbsp;</td></tr>
    <tr>
      <td align="center">
        <input type="checkbox" name="oneEntryOnly" value="1" <%=oneEntryOnly?" checked":""%>>
      </td>
      <td>&nbsp;</td>
      <td><%=Config.msg(93)%></td>
    </tr>
<%
  } // end: if ( !Config.authenticationMethodStandalone )
%>
  </table>
  <p>
    <input type="submit" name="ok" value="<%=Config.msg(23)%>" class="button">
    &nbsp;&nbsp;
    <input type="submit" name="cancel" value="<%=Config.msg(24)%>" class="button">
  </p>
</form>
<survey:pageFooter />