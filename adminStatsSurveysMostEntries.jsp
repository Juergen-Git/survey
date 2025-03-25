<%@ include file="/globalAuthRequired.jsp" %><%
  if ( !pid.equals("admin") ) { response.sendRedirect ( "index.jsp" ); return; }
  if ( (String) request.getParameter ( "backToMainMenu" ) != null ) { response.sendRedirect("index.jsp"); return; }

  String pageTitle = Config.msg(34);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" onLoad="" />
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
 <input type="submit" name="backToMainMenu" value="<%=Config.msg(28)%>" class="button">
</form>
<table border="0" cellpadding="5" cellspacing="0">
  <tr class="menusectionheader">
    <td>&nbsp;</td>
    <td><b><%=Config.msg(35)%></b></td>
    <td><b><%=Config.msg(36)%></b></td>
    <td align="right"><b><%=Config.msg(37)%></b></td>
    <td><b><%=Config.msg(38)%></b></td>
  </tr>
<%
  String bgColor = "#eeeeee";

  SurveyMetaDataList surveyMetaDataList = new SurveyMetaDataList (Config.appDataDir);
  File surveyDataDirFile = new File ( Config.appDataDir + "surveys" + Config.FILE_SEPARATOR );
  java.io.File[] surveyFileList = surveyDataDirFile.listFiles( );
  for ( int i = 0; i < surveyFileList.length; i++ ) {
    if ( surveyFileList[i].isDirectory() ) {
      SurveyMetaData s = new SurveyMetaData ( Config.appDataDir, surveyFileList[i].getName() );
      if ( s.exists() ) {
        surveyMetaDataList.addSurvey(surveyFileList[i].getName(), s);
      }
    }
  }

  long i = 0;
  Iterator surveys = surveyMetaDataList.getSortedByEntries ();
  while (surveys.hasNext () ) {
    i++;
    SurveyMetaData s = (SurveyMetaData) surveys.next ();
    if ( bgColor.equals("#eeeeee") ) { bgColor = "#ffffff"; } else { bgColor = "#eeeeee"; }
    String surveyUrl = Config.urlScheme+"://" + Config.hostName + Config.rootURL + "previewEntryForm.jsp?surveyId=" + s.getId();
%>
  <tr>
    <td bgColor="<%=bgColor%>" align="right"><%=i%>.</td>
    <td bgColor="<%=bgColor%>"><a href="JavaScript:new_window('preview.jsp?surveyId=<%=surveyId%>&url=<%=java.net.URLEncoder.encode ( surveyUrl )%>')"><%=s.getName()%></a></td>
    <td bgColor="<%=bgColor%>"><%
  Iterator adminPIDs = s.getAdminsSorted ();
  int c = 0;
  while ( adminPIDs.hasNext () ) {
    if ( c > 0 ) { out.print(", "); }
    c++;
    out.print ( (String) adminPIDs.next () );
  }
    %></td>
    <td align="right" bgColor="<%=bgColor%>"><%=s.getNumEntries()%></td>
    <td bgColor="<%=bgColor%>"><%=s.getStatusAsText()%></td>
  </tr>
<%
  } // end: while (surveys.hasNext () )
%>
</table>
<survey:pageFooter />
