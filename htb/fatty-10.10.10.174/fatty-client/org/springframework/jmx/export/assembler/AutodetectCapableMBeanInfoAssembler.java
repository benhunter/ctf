package org.springframework.jmx.export.assembler;

public interface AutodetectCapableMBeanInfoAssembler extends MBeanInfoAssembler {
  boolean includeBean(Class<?> paramClass, String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/assembler/AutodetectCapableMBeanInfoAssembler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */