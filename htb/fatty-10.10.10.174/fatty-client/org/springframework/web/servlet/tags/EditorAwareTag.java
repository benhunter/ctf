package org.springframework.web.servlet.tags;

import java.beans.PropertyEditor;
import javax.servlet.jsp.JspException;
import org.springframework.lang.Nullable;

public interface EditorAwareTag {
  @Nullable
  PropertyEditor getEditor() throws JspException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/EditorAwareTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */