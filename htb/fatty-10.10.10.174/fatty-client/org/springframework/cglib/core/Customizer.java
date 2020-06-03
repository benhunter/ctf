package org.springframework.cglib.core;

import org.springframework.asm.Type;

public interface Customizer extends KeyFactoryCustomizer {
  void customize(CodeEmitter paramCodeEmitter, Type paramType);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cglib/core/Customizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */