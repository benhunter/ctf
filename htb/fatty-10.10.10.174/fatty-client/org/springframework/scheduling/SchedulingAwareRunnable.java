package org.springframework.scheduling;

public interface SchedulingAwareRunnable extends Runnable {
  boolean isLongLived();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/SchedulingAwareRunnable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */