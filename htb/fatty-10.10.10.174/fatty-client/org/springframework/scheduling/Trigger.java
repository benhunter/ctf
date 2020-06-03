package org.springframework.scheduling;

import java.util.Date;
import org.springframework.lang.Nullable;

public interface Trigger {
  @Nullable
  Date nextExecutionTime(TriggerContext paramTriggerContext);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/Trigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */