package org.owasp.webgoat.lessons.xmlread;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author nbaars
 * @since 4/8/17.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "comment")
@XmlType
@ToString
public class Comment {
  private String user;
  private String dateTime;
  private String text;
}
