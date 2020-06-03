package org.springframework.cglib.core;

import org.springframework.asm.Type;

public interface HashCodeCustomizer extends KeyFactoryCustomizer {
  boolean customize(CodeEmitter paramCodeEmitter, Type paramType);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cglib/core/HashCodeCustomizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */