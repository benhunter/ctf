package org.springframework.expression.spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.lang.Nullable;

public abstract class CompiledExpression {
  public abstract Object getValue(@Nullable Object paramObject, @Nullable EvaluationContext paramEvaluationContext) throws EvaluationException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/CompiledExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */