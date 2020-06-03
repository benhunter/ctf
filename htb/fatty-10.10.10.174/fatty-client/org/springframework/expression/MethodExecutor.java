package org.springframework.expression;

public interface MethodExecutor {
  TypedValue execute(EvaluationContext paramEvaluationContext, Object paramObject, Object... paramVarArgs) throws AccessException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/MethodExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */