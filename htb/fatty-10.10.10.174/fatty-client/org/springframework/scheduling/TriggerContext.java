package org.springframework.scheduling;

import java.util.Date;
import org.springframework.lang.Nullable;

public interface TriggerContext {
  @Nullable
  Date lastScheduledExecutionTime();
  
  @Nullable
  Date lastActualExecutionTime();
  
  @Nullable
  Date lastCompletionTime();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/TriggerContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */