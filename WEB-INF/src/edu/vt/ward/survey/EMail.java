package edu.vt.ward.survey;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import org.apache.log4j.*;

public abstract class EMail {
  static Category log = Category.getInstance(EMail.class.getName());

  public static boolean send ( String recipient, String subject, String message ) {
    try {
      // create some properties and get the default Session
      Properties props = new Properties();
      props.put("mail.smtp.host", Config.smtpHost );
      Session s = Session.getDefaultInstance(props, null);

      // create a message
      MimeMessage newMessage = new MimeMessage(s);
      newMessage.setFrom(new InternetAddress(Config.emailFrom, Config.serviceName ));
      newMessage.setRecipients(Message.RecipientType.TO, recipient );
      newMessage.setSubject( subject );
      newMessage.setSentDate(new Date ());
      newMessage.setText(message);

      // Send newMessage
      Transport.send(newMessage);
      log.debug("E-mail has been sent via "+Config.smtpHost+".");
    }
    catch ( Exception e ) {
      log.error("Failed to send e-mail.");
      return false;
    }

    return true;
  }

}