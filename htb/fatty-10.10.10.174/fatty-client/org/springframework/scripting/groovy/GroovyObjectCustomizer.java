package org.springframework.scripting.groovy;

import groovy.lang.GroovyObject;

@FunctionalInterface
public interface GroovyObjectCustomizer {
  void customize(GroovyObject paramGroovyObject);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/groovy/GroovyObjectCustomizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */