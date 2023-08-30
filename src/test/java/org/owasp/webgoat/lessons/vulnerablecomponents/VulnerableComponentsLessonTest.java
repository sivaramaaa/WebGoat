

package org.owasp.webgoat.lessons.vulnerablecomponents;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class VulnerableComponentsLessonTest {

  String strangeContact =
      "<contact class='dynamic-proxy'>\n"
          + "<interface>org.owasp.webgoat.vulnerablecomponents.Contact</interface>\n"
          + "  <handler class='java.beans.EventHandler'>\n"
          + "    <target class='java.lang.ProcessBuilder'>\n"
          + "      <command>\n"
          + "        <string>calc.exe</string>\n"
          + "      </command>\n"
          + "    </target>\n"
          + "    <action>start</action>\n"
          + "  </handler>\n"
          + "</contact>";
  String contact = "<contact>\n" + "</contact>";

  @Test
  public void testTransformation() throws Exception {
    XStream xstream = new XStream();
    xstream.setClassLoader(Contact.class.getClassLoader());
    xstream.alias("contact", ContactImpl.class);
    xstream.ignoreUnknownElements();
    assertNotNull(xstream.fromXML(contact));
  }

  @Test
  @Disabled
  public void testIllegalTransformation() throws Exception {
    XStream xstream = new XStream();
    xstream.setClassLoader(Contact.class.getClassLoader());
    xstream.alias("contact", ContactImpl.class);
    xstream.ignoreUnknownElements();
    Exception e =
        assertThrows(
            RuntimeException.class,
            () -> ((Contact) xstream.fromXML(strangeContact)).getFirstName());
    assertTrue(e.getCause().getMessage().contains("calc.exe"));
  }

  @Test
  public void testIllegalPayload() throws Exception {
    XStream xstream = new XStream();
    xstream.setClassLoader(Contact.class.getClassLoader());
    xstream.alias("contact", ContactImpl.class);
    xstream.ignoreUnknownElements();
    Exception e =
        assertThrows(
            StreamException.class, () -> ((Contact) xstream.fromXML("bullssjfs")).getFirstName());
    assertTrue(e.getCause().getMessage().contains("START_DOCUMENT"));
  }
}
