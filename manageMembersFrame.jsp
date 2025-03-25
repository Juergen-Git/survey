<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { out.print ( Config.msg(188) ); return; }
%><html>
<head>
  <title><%=Config.msg(189)%></title>
  <meta http-equiv="Content-Type" content="text/html; charset=<%=Config.xmlEncoding%>">
  <link rel="stylesheet" href="<%=Config.rootURL%>styles/survey.css" type="text/css">
</head>
<body>
<%
LdapAuth ldapAuth = new LdapAuth ();
LdapName ldapName = new LdapName ();
String errorMessage = "";

String pids = (String) request.getParameter ( "pids" );
String addPIDError = "";
String addPIDAffirmation = "";

if ( (String) request.getParameter ( "add" ) != null && pids != null) {
  String pidsAdded = "";
  String pidsInvalid = "";

  StringTokenizer pidsTokens = new StringTokenizer( pids, " ,;\n\r\t" );
  while ( pidsTokens.hasMoreTokens() ) {
    String pidName = pidsTokens.nextToken();

    if ( !pidName.equals ( "" ) ) {
      if ( Pattern.matches( Config.regexValidPID, pidName ) && ldapAuth.userExists ( pidName ) ) {
        if ( !survey.isMember ( pidName ) ) {
          survey.addMemberPID ( pidName );
        }
        if ( !pidsAdded.equals ("") ) { pidsAdded += ", "; }
        pidsAdded += pidName;
      }
      else {
        if ( !pidsInvalid.equals ("") ) { pidsInvalid += ", "; }
        pidsInvalid += pidName;
      }
    } // end: if ( !pidName.equals ( "" ) )
  } // end: while ( pidsTokens.hasMoreTokens() )

  survey.save();

  // feedback message(s)
  if ( !pidsAdded.equals ("") ) {
	  if ( pidsAdded.indexOf ( "," ) > 0 ) { // more than one PID
      addPIDAffirmation = Config.msg(190)+" &quot;" + pidsAdded + "&quot; "+Config.msg(191);
    }
    else {
      addPIDAffirmation = Config.msg(192)+" &quot;" + pidsAdded + "&quot; "+Config.msg(193);
    }
  }
  if ( !pidsInvalid.equals ("") ) {
	  if ( pidsInvalid.indexOf ( "," ) > 0 ) { // more than one PID
      addPIDError = Config.msg(194)+" "+Config.msg(190)+" &quot;" + pidsInvalid + "&quot; "+Config.msg(195);
    }
    else {
      addPIDError = Config.msg(194)+" "+Config.msg(192)+" &quot;" + pidsInvalid + "&quot; "+Config.msg(196);
    }
  }
} // end: if ( (String) request.getParameter ( "add" ) != null )
else if ( (String) request.getParameter ( "delete" ) != null ) { //
  String[] surveyMembers = request.getParameterValues ( "surveyMembers" );
  if ( surveyMembers != null ) {
    for ( int i = 0; i < surveyMembers.length; i++ ) {
      if ( !surveyMembers[i].equals ( "" ) ) {
        survey.removeMemberPID( surveyMembers[i] );
      }
    }

    survey.save();
  } // end: if ( surveyMembers != null )
} // end: if ( (String) request.getParameter ( "delete" ) != null )
%>
<script language="JavaScript" type="text/javascript"><!--
function isIE4()
{ return( navigator.appName.indexOf("Microsoft") != -1 && (navigator.appVersion.charAt(0)=='4') ); }

function new_window(freshurl) {
SmallWin = window.open(freshurl, 'Peoplefinder','scrollbars=yes,resizable=yes,toolbar=no,height=390,width=700');
if (!isIE4())	{
  if (window.focus) { SmallWin.focus(); }
}
if (SmallWin.opener == null) SmallWin.opener = window;
SmallWin.opener.name = "Survey";
}
//-->
</script>
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>" name="form">
<input type="hidden" name="surveyId" value="<%=surveyId%>">
<%
if ( !addPIDAffirmation.equals ( "" ) || !addPIDError.equals ( "" ) ) {
  if ( !addPIDAffirmation.equals ( "" ) ) { %><%=Config.formatFeedback ( "affirmation", addPIDAffirmation )%><br><br><% }
  if ( !addPIDError.equals ( "" ) ) { %><%=Config.formatFeedback ( "error", addPIDError )%><br><br><% }
} // end: if ( !addPIDAffirmation.equals ( "" ) || !addPIDError.equals ( "" ) )
%>
<table border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td valign="top" align="left"> <b><%=Config.serviceProvider%> <%=Config.msg(197)%></b><br>
      <textarea name="pids" rows="8" cols="25"></textarea>
      <br>
      <font color="#999999"><%=Config.msg(198)%></font><br>
      <%=Config.msg(185)%>
      </td><td valign="top" align="center"><br>
      <br>
      <br>
      &nbsp;
      <input type="submit" name="add" value="<%=Config.msg(199)%>" class="button">
      &nbsp; </td><td align="left" valign="top"><b><%=Config.msg(200)%></b><br>
      <select name="surveyMembers" size="9" style="width : 200px" multiple>
<%
Iterator memberPIDs = survey.getMembersSorted ();
while ( memberPIDs.hasNext () ) {
  String memberPID = (String) memberPIDs.next ();

  out.print("<option value=\"" + memberPID + "\">");
  out.print( memberPID + " " + ldapName.getFullNameInParenthesis( memberPID ) );
  out.print("</option>\n");
}
%>
      </select>
      <br>
      <input type="submit" name="delete" value="<%=Config.msg(201)%>" class="button">
      <br>
      <%=Config.msg(202)%><br>
    </td></tr>
</table>
</form>

<form>
  <input type="button" name="done" onClick="JavaScript:top.window.close()" value="<%=Config.msg(82)%>" class="button">
</form>
</body>