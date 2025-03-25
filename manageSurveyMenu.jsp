<%@ include file="/globalAuthRequired.jsp" %><%
  if ( (String) request.getParameter ( "backToMainMenu" ) != null ) { response.sendRedirect("index.jsp"); return; }

//  if ( surveyId != null ) { 
//  	mysession.setAttribute(null,  "surveyId", surveyId ); 
//  }

  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );

  String pageTitle = survey.getName ();
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; " + survey.getName () );
%><survey:pageHeader title="<%=pageTitle%>" />
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>">
 <input type="hidden" name="surveyId" value="<%=surveyId%>">
 <input type="submit" name="backToMainMenu" value="<%=Config.msg(28)%>" class="button">
</form>
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
<table border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td class="menusectionheader" width="1"><img src="images/trans.gif" width="1" height="1" alt=""><br>
    </td>
    <td class="menusectionheader"><%=Config.msg(203)%></td>
  </tr>
  <tr>
    <td rowspan="6"><img src="images/trans.gif" width="1" height="1" alt=""><br>
    </td>
    <td>
      <%
  if ( survey.getOpened ().equals("") || !survey.getClosed ().equals("") ) {
    if ( survey.getNumEntries () == 0 ) {
      out.print( "<a href=\"editEntryForm.jsp?surveyId="+surveyId+"\">"+Config.msg(81)+"</a>" );
      if ( !entryForm.isReadyForOpen() ) { out.print(" "+Config.msg(204)); }
    }
    else {
%>
      <span class="menuiteminactive"><%=Config.msg(81)%>
      <%=Config.msg(205)%> <a href="deleteEntries.jsp?surveyId=<%=surveyId%>" class="menuiteminactive"><%=Config.msg(206)%></a>.<br>
      <%=Config.msg(207)%> <a href="createNewSurvey.jsp?templateSurveyId=<%=surveyId%>" class="menuiteminactive"><%=Config.msg(208)%></a> <%=Config.msg(209)%></span>
      <%
    }
  }
  else {
%>
      <span class="menuiteminactive"><%=Config.msg(81)%> <%=Config.msg(210)%></span>
      <%
  }%>
    </td>
  </tr>
  <tr>
    <td><a href="editExitPage.jsp?surveyId=<%=surveyId%>"><%=Config.msg(94)%></a>
      <%
    if ( entryForm.getExitPageText ().equals ("") ) { out.print(" "+Config.msg(211)); }
%>
    </td>
  </tr>
  <tr>
    <td><a href="editAdminEmail.jsp?surveyId=<%=surveyId%>"><%=Config.msg(74)%></a>
      <%
    if ( survey.getAdminEmail().equals("")) { out.print(Config.msg(212)); }
    else { out.print("(" + survey.getAdminEmail() + " "+Config.msg(213)+")"); }
    %>
    </td>
  </tr>
  <tr>
    <td><a href="manageAdmins.jsp?surveyId=<%=surveyId%>"><%=Config.msg(182)%></a></td>
  </tr>
  <tr>
    <td><a href="editEntryRestrictions.jsp?surveyId=<%=surveyId%>"><%=Config.msg(86)%></a></td>
  </tr>
  <tr>
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr>
    <td width="8" class="menusectionheader"><img src="images/trans.gif" width="1" height="1" alt=""><br>
    </td>
    <td class="menusectionheader"><%=Config.msg(214)%></td>
  </tr>
  <tr>
    <td rowspan="2"><img src="images/trans.gif" width="1" height="1" alt=""><br>
    </td>
    <td>
      <%
  if ( !entryForm.isReadyForOpen () ) {
%>
      <span class="menuiteminactive"><%=Config.msg(215)%> <%=Config.msg(216)%></span>
      <%
  }
  else if ( survey.isAcceptingDataEntry () ) {
    String surveyUrl = Config.urlScheme+"://" + Config.hostName + Config.rootURL + "entry.jsp?id=" + surveyId;
%>
      <span style="color : #009900"><b><%=Config.msg(217)%></b></span> <%=Config.msg(218)%>
      &quot;<a style="color : #000000" href="JavaScript:new_window('preview.jsp?surveyId=<%=surveyId%>&url=<%=java.net.URLEncoder.encode ( surveyUrl )%>')"><%=surveyUrl%></a>&quot;<br>
      <%
  }
  else {
%>
      <a href="openSurvey.jsp?surveyId=<%=surveyId%>"><%=Config.msg(215)%></a> <%=Config.msg(219)%>
      <%
  }
%>
    </td>
  </tr>
  <tr>
    <td>
      <%
  if ( !survey.getClosed ().equals("") && !survey.isAcceptingDataEntry () ) {
    %>
      <span class="menuiteminactive"><%=Config.msg(221)%> (<%=Config.msg(222)%> <%=survey.getClosed()%>)</span><br>
      <%
  }
  else if ( survey.getOpened().equals("") ) { // not opened yet
%>
      <span class="menuiteminactive"><%=Config.msg(221)%></span>
      <%
  }
  else {
%>
      <a href="closeSurvey.jsp?surveyId=<%=surveyId%>"><%=Config.msg(221)%></a> <%=Config.msg(223)%>
      <%
    if ( !survey.getClosed().equals("") ) { %>
      (<%=Config.msg(224)%> <%=survey.getClosed()%>)
      <% }
  }
%>
    </td>
  </tr>
  <tr>
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr>
    <td class="menusectionheader"><img src="images/trans.gif" width="1" height="1" alt=""><br>
    </td>
    <td class="menusectionheader"><%=Config.msg(225)%></td>
  </tr>
  <tr>
    <td><img src="images/trans.gif" width="1" height="1" alt=""><br>
    </td>
    <td><a href="editResultsPassword.jsp?surveyId=<%=surveyId%>"><%=Config.msg(114)%></a> (<%
    if ( survey.getAccessResultsRestriction().equals("owner")) { out.print( Config.msg(226) ); }
    else
    if ( survey.getAccessResultsRestriction().equals("anyone")) { out.print( Config.msg(227) ); }
    else
    if ( survey.getAccessResultsRestriction().equals("password")) { out.print( Config.msg(228) ); }
    %>)</td>
  </tr>
  <tr>
    <td><img src="images/trans.gif" width="1" height="1" alt=""><br>
    </td>
    <td>
      <%
  if ( !survey.getOpened ().equals("") ) {
    String surveyUrl = Config.urlScheme+"://" + Config.hostName + Config.rootURL + "viewResults.jsp?id=" + surveyId;
    %>
      <a href="viewResults.jsp?surveyId=<%=surveyId%>"><%=Config.msg(229)%></a> (<%=Config.msg(230)%> <%=surveyUrl%>)
      <%
  }
  else {
%>
      <span class="menuiteminactive"><%=Config.msg(229)%></span>
      <%
  }%>
    </td>
  </tr>
  <tr>
    <td><img src="images/trans.gif" width="1" height="1" alt=""><br></td>
    <td>
      <%
  if ( !survey.getOpened().equals("") ) { //survey.getNumEntries () > 0 ) {
    %>
      <a href="deleteEntries.jsp?surveyId=<%=surveyId%>"><%=Config.msg(58)%></a>
      <%
  }
  else {
%>
      <span class="menuiteminactive"><%=Config.msg(58)%></span>
      <%
  }%>
    </td>

  <tr>
    <td><img src="images/trans.gif" width="1" height="1" alt=""><br></td>
    <td><%
  if ( true ) { // survey.getNumEntries () > 0 ) {
    %><a href="exportResults.jsp?surveyId=<%=surveyId%>"><%=Config.msg(136)%></a> <%=Config.msg(231)%><%
  }
  else {
%><span class="menuiteminactive"><%=Config.msg(136)%> <%=Config.msg(231)%></span><%
  }%></td>
  </tr>
</table>
<survey:pageFooter />
