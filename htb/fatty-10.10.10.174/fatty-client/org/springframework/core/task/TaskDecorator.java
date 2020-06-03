package org.springframework.core.task;

@FunctionalInterface
public interface TaskDecorator {
  Runnable decorate(Runnable paramRunnable);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/task/TaskDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */