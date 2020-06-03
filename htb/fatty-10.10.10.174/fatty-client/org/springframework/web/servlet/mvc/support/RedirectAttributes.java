package org.springframework.web.servlet.mvc.support;

import java.util.Collection;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.springframework.ui.Model;

public interface RedirectAttributes extends Model {
  RedirectAttributes addAttribute(String paramString, @Nullable Object paramObject);
  
  RedirectAttributes addAttribute(Object paramObject);
  
  RedirectAttributes addAllAttributes(Collection<?> paramCollection);
  
  RedirectAttributes mergeAttributes(Map<String, ?> paramMap);
  
  RedirectAttributes addFlashAttribute(String paramString, @Nullable Object paramObject);
  
  RedirectAttributes addFlashAttribute(Object paramObject);
  
  Map<String, ?> getFlashAttributes();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/support/RedirectAttributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */