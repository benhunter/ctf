package org.springframework.aop;

public interface IntroductionAdvisor extends Advisor, IntroductionInfo {
  ClassFilter getClassFilter();
  
  void validateInterfaces() throws IllegalArgumentException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/IntroductionAdvisor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */