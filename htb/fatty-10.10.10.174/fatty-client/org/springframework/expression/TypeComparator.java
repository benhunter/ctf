package org.springframework.expression;

import org.springframework.lang.Nullable;

public interface TypeComparator {
  boolean canCompare(@Nullable Object paramObject1, @Nullable Object paramObject2);
  
  int compare(@Nullable Object paramObject1, @Nullable Object paramObject2) throws EvaluationException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/TypeComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */