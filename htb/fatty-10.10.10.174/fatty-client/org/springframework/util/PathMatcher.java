package org.springframework.util;

import java.util.Comparator;
import java.util.Map;

public interface PathMatcher {
  boolean isPattern(String paramString);
  
  boolean match(String paramString1, String paramString2);
  
  boolean matchStart(String paramString1, String paramString2);
  
  String extractPathWithinPattern(String paramString1, String paramString2);
  
  Map<String, String> extractUriTemplateVariables(String paramString1, String paramString2);
  
  Comparator<String> getPatternComparator(String paramString);
  
  String combine(String paramString1, String paramString2);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/PathMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */