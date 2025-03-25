<%@ include file="/globalAuthRequired.jsp" %><%
  if ( (String) request.getParameter ( "createNewSurvey" ) != null ) { response.sendRedirect("createNewSurvey.jsp"); return; }

  // read all survey meta data (parameter "true")
  User user = new User ( Config.appDataDir, pid );
  if ( Config.demoModeEnabled && !user.exists() ) {
    session.invalidate ();
    response.sendRedirect("demoPeriodExpired.jsp");
    return;
  }
  SurveyMetaDataList surveyMetaDataList = new SurveyMetaDataList ( Config.appDataDir, user.getSurveyIdList () );

  String pageTitle = Config.msg(8);
  mysession.setAttribute(surveyId,  "cookietrail", pageTitle );
%>
<survey:pageHeader title="<%=pageTitle%>" />
<script language="JavaScript" type="text/javascript"><!--
function isIE4()
{ return( navigator.appName.indexOf("Microsoft") != -1 && (navigator.appVersion.charAt(0)=='4') ); }

function new_window(freshurl) {
  SmallWin = window.open(freshurl, 'Survey','scrollbars=yes,resizable=yes,toolbar=no,height=400,width=600');
  if (!isIE4())	{
    if (window.focus) { SmallWin.focus(); }
  }
  if (SmallWin.opener == null) SmallWin.opener = window;
  SmallWin.opener.name = "Main";
}
//-->
</script>
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>">
 <input type="submit" name="createNewSurvey" value="<%=Config.msg(151)%>" class="button">
</form>
<%
Iterator surveys = surveyMetaDataList.getSortedByStatus ();

if (surveys.hasNext () ) {
%>
<%=Config.msg(152)%><br>
<br>
<table border="0" cellpadding="5" cellspacing="0">
  <tr>
    <td class="menusectionheader" align="left"><b><%=Config.msg(153)%></b></td>
    <td class="menusectionheader" align="left"><b><%=Config.msg(154)%></b></td>
    <td class="menusectionheader" align="right"><b><%=Config.msg(155)%></b></td>
    <td class="menusectionheader">&nbsp;</td>
  </tr>
  <%
  String bgColor = "#eeeeee";
  while (surveys.hasNext () ) {
    SurveyMetaData s = (SurveyMetaData) surveys.next ();
    if ( bgColor.equals ( "#eeeeee" ) ) { bgColor = "#ffffff"; }
    else { bgColor = "#eeeeee"; }
%>
  <tr>
    <td bgcolor="<%=bgColor%>" nowrap><a href="manageSurveyMenu.jsp?surveyId=<%=s.getId ()%>"><%=s.getName ()%></a></td>
<% String surveyUrl = Config.urlScheme+"://" + Config.hostName + Config.rootURL + "entry.jsp?id=" + s.getId ();%>
    <td bgcolor="<%=bgColor%>" nowrap><%=s.getStatusAsText ()%></td>
    <td align="right" bgcolor="<%=bgColor%>" nowrap><% if ( !s.getOpened().equals ("") ) { out.print ( s.getNumEntries () ); } %>&nbsp;&nbsp;&nbsp;</td>
    <td align="right" bgcolor="<%=bgColor%>" nowrap> <a href="createNewSurvey.jsp?templateSurveyId=<%=s.getId ()%>" title="<%=Config.msg(156)%>"><%=Config.msg(157)%></a>
      &nbsp;<a href="renameSurvey.jsp?surveyId=<%=s.getId ()%>" title="<%=Config.msg(158)%>"><%=Config.msg(159)%></a>
      &nbsp;<a href="deleteSurvey.jsp?surveyId=<%=s.getId ()%>" title="<%=Config.msg(160)%>"><%=Config.msg(161)%></a>
    </td>
  </tr>
  <%
  } // end: while (surveys.hasNext () )
%>
</table>
<br>
<%
} // end: if (surveys.hasNext () )
%>
<%
  if ( Config.authenticationMethodStandalone || pid.equals("admin") ) {
%>
<br>
<table border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="menusectionheader" align="left">
      <table border="0" cellspacing="2" cellpadding="5">
        <tr>
          <td bgcolor="#ffffff"><a href="changeMyLoginPassword.jsp"><%=Config.msg(45)%></a></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<br>
<%
  } // end:
%>
<%
  if ( pid.equals ("admin") ) {
%>
<br>
<table border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td class="menusectionheader" width="1"><img src="images/trans.gif" width="1" height="1" alt=""><br>
    </td>
    <td class="menusectionheader"><%=Config.msg(162)%></td>
  </tr>
  <tr>
    <td rowspan="4"><img src="images/trans.gif" width="1" height="1" alt=""><br>
    </td>
    <td><a href="adminAddNewUser.jsp"><%=Config.msg(18)%></a><br></td>
  </tr>
  <tr>
    <td><a href="adminRemoveUser.jsp"><%=Config.msg(30)%></a><br></td>
  </tr>
<%
  if ( Config.authenticationMethodStandalone ) {
%>
  <tr>
    <td><a href="adminChangeUserPassword.jsp"><%=Config.msg(26)%></a><br></td>
  </tr>
<%
  } // end:
%>
  <tr>
    <td><a href="adminListAllUsers.jsp"><%=Config.msg(27)%></a><br></td>
  </tr>
  <tr>
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr>
    <td class="menusectionheader" width="1"><img src="images/trans.gif" width="1" height="1" alt=""><br>
    </td>
    <td class="menusectionheader"><%=Config.msg(163)%> &nbsp;<span style="font-weight:normal">(<%=Config.msg(164)%> <%
    if ( Config.statsLastUpdated == 0 ) { out.print(Config.msg(165)); }
    else { out.print( DateUtil.formatDate ( DateUtil.ISO4601DateFormat, new Date (Config.statsLastUpdated)) ); }
    %>)</span></td>
  </tr>
  <tr>
    <td rowspan="11"><img src="images/trans.gif" width="1" height="1" alt=""><br>
    </td>
    <td><a href="adminStatsUpdateNow.jsp"><%=Config.msg(39)%></a>
<%
  if ( Config.enableAutoUpdateStats ) { out.print ( Config.msg(166) ); }
%>
    </td>
  </tr>
  <tr>
    <td><%=Config.statsRegisteredUsers%> <%=Config.msg(167)%></td>
  </tr>
  <tr>
    <td><%=Config.statsUsersWithRespectableSurvey%> <%=Config.msg(168)%></td>
  </tr>
  <tr>
    <td><%=Config.statsNewUsers7days%> <%=Config.msg(169)%></td>
  </tr>
  <tr>
    <td><%=Config.statsNewUsers24hours%> <%=Config.msg(170)%></td>
  </tr>
  <tr>
    <td><a href="adminStatsSurveysMostEntries.jsp"><%=Config.msg(34)%></a></td>
  </tr>
  <tr>
    <td><%=Config.statsSurveys%> <%=Config.msg(172)%></td>
  </tr>
  <tr>
    <td><%=Config.statsRespectableSurveys%> <%=Config.msg(173)%></td>
  </tr>
  <tr>
    <td><%=Config.statsOpenSurveys%> <%=Config.msg(174)%></td>
  </tr>
  <tr>
    <td><%=Config.statsNewEntries7days%> <%=Config.msg(175)%></td>
  </tr>
  <tr>
    <td><%=Config.statsNewEntries24hours%> <%=Config.msg(176)%></td>
  </tr>
</table>
<%
  } // end: if ( pid.equals ("admin") )
%>
<survey:pageFooter />