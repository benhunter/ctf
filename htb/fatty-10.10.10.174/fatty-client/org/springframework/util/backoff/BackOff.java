package org.springframework.util.backoff;

@FunctionalInterface
public interface BackOff {
  BackOffExecution start();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/backoff/BackOff.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */