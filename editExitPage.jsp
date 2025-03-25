<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( request.getParameter ( "saveSurvey" ) != null ) { response.sendRedirect("manageSurveyMenu.jsp?surveyId="+surveyId); return; }

  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );

  String pageTitle = Config.msg(94);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; " + pageTitle );

  String bgColor = "#ffffff";
  if ( !entryForm.getBgColor ().equals ( "" ) )
    bgColor = entryForm.getBgColor ();
  String style = "";
  if ( !entryForm.getFont ().equals ( "" ) )
    style = "font-family : " + entryForm.getFont ();
  if ( !entryForm.getBgColor ().equals ( "" ) )
    style += "; background-color : " + entryForm.getBgColor ();
%><survey:pageHeader title="<%=pageTitle%>" />
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
 <input type="hidden" name="surveyId" value="<%=surveyId%>">
 <input type="submit" name="saveSurvey" value="<%=Config.msg(82)%>" class="button">&nbsp;&nbsp;
 <input type="button" name="preview" value="<%=Config.msg(83)%>" class="button" onClick="JavaScript:new_window('preview.jsp?surveyId=<%=surveyId%>&url=<%=java.net.URLEncoder.encode("previewExitPage.jsp?surveyId=" + surveyId)%>')">
</form>
<%
    out.print ( entryForm.getExitPageHTML ( SurveyEntryForm.modeEdit ) );
%>
<survey:pageFooter />