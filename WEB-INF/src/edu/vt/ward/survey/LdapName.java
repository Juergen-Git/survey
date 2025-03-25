package edu.vt.ward.survey;

import java.util.*;
import javax.naming.directory.SearchResult;
import javax.naming.directory.Attributes;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import edu.vt.middleware.ldap.LdapConfig;

public class LdapName extends edu.vt.middleware.ldap.Ldap {

  public LdapName () {
    super();
    LdapConfig config = new LdapConfig ( Config.ldapNameHost, Config.ldapNameBaseDN );
    config.setPort ( Config.ldapNamePort );
    config.setService ( Config.ldapNameServiceUser, Config.ldapNameServiceCredential);
    setLdapConfig(config);
  }

  public String getFullName ( String user ) {
    if ( user == null || user.equals("") ) {
      return "";
    }
    else {
      Iterator results = null;
      this.connect();
      try {
        String[] attribute = { Config.ldapNameFullNameAttribute };
        results = this.search("(" + Config.ldapNameUidAttribute + "=" + user + ")", attribute );
      } catch (javax.naming.NamingException e) {
        // e.printStackTrace();
        return "";
      }
      this.close();

      if (results.hasNext()) {
        return getFirstSearchResult(results);
      }
      else {
        return "";
      }
    }
  }

  public String getFullNameInParenthesis ( String user ) {
    String fullName = this.getFullName( user );
    if ( !fullName.equals("") ) { fullName = "(" + fullName + ")"; }
    return fullName;
  }

  // returns the first of the entries in results
  // adapted from printResults in edu.vt.middleware.ldap.Ldap.java
  public static String getFirstSearchResult ( Iterator results ) {
    while (results.hasNext()) {
      try {
        SearchResult sr = (SearchResult) results.next();
        Attributes attrs = sr.getAttributes();
        if (attrs != null) {
          NamingEnumeration ae = attrs.getAll();
          if (ae != null) {
            while (ae.hasMore()) {
              Attribute attr = (Attribute) ae.next();
              if (attr != null) {
                String attrName = attr.getID();
                NamingEnumeration e = attr.getAll();
                if (e != null) {
                  while (e.hasMore()) {
                    Object rawValue = e.next();
                    String value = "";
                    try {
                      value = (String) rawValue;
                    } catch (ClassCastException e1) {
                      try {
                        byte[] bytes = (byte[]) rawValue;
                        value = new String(bytes);
                      } catch (ClassCastException e2) {
                        return "";
                      }
                    }
                    return value;
                  }
                }
              }
            }
          }
        }
      } catch (Exception e) {
        return "";
      }
    }
    return "";
  }
}
