package edu.vt.ward.survey;

import javax.naming.NamingException;
import edu.vt.middleware.ldap.LdapConfig;
import edu.vt.middleware.ldap.Authenticator;

public class LdapAuth extends edu.vt.middleware.ldap.Ldap {
  private LdapConfig ldapConfig = null;
  private Authenticator authenticator = null;

  public LdapAuth () {
    super();
    ldapConfig = new LdapConfig ( Config.ldapAuthHost, Config.ldapAuthBaseDN );
    if (!Config.ldapAuthPort.equals("")) {
      ldapConfig.setPort(Config.ldapAuthPort);
    }
    if (!Config.ldapAuthServiceUser.equals("")) {
      ldapConfig.setServiceUser(Config.ldapAuthServiceUser);
    }
    if (!Config.ldapAuthServiceCredential.equals("")) {
      ldapConfig.setServiceCredential(Config.ldapAuthServiceCredential);
    }
    if (!Config.ldapAuthAuthType.equals("")) {
      ldapConfig.setAuthtype(Config.ldapAuthAuthType);
    }
    if (Config.ldapAuthTls) {
      ldapConfig.useTls(true);
    }
    if (!Config.ldapAuthSslSocketFactory.equals("")) {
      ldapConfig.setSslSocketFactory(Config.ldapAuthSslSocketFactory);
    }
    setLdapConfig(ldapConfig);
  }
  
  public boolean authenticate(String pid, String password) {
    if (authenticator == null) {
      authenticator = new Authenticator(ldapConfig);
      if (!Config.ldapAuthUidAttribute.equals("")) {
        authenticator.setUserfield(Config.ldapAuthUidAttribute);
      }
      if (Config.ldapAuthConstructDn) {
        authenticator.setConstructDn(true);
      }
    }
    
    try {
      return authenticator.authenticate(pid,password);
    }
    catch (Exception e) {
      return false;
    }
  }

  public boolean userExists ( String user ) {
    boolean exists = false;
    this.connect();

    try {
      String searchFilter = "(" + Config.ldapAuthUidAttribute + "=" + user + ")";
      String[] returnAttributes = { Config.ldapAuthUidAttribute };

      if (this.search (searchFilter, returnAttributes).hasNext()) { // user exist
        exists = true;
      }
    }
    catch ( NamingException e ) { }

    this.close();
    return exists;
  }

}
