<%
  if ( edu.vt.ward.survey.Config.appDataDir.equals ( "" ) ) {
    edu.vt.ward.survey.Config.setInitParams ( application );
  }

  String rootURL = edu.vt.ward.survey.Config.rootURL;
  MySession mysession = new MySession(session);
%>