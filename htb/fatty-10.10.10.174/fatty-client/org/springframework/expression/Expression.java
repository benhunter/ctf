package org.springframework.expression;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;

public interface Expression {
  String getExpressionString();
  
  @Nullable
  Object getValue() throws EvaluationException;
  
  @Nullable
  <T> T getValue(@Nullable Class<T> paramClass) throws EvaluationException;
  
  @Nullable
  Object getValue(Object paramObject) throws EvaluationException;
  
  @Nullable
  <T> T getValue(Object paramObject, @Nullable Class<T> paramClass) throws EvaluationException;
  
  @Nullable
  Object getValue(EvaluationContext paramEvaluationContext) throws EvaluationException;
  
  @Nullable
  Object getValue(EvaluationContext paramEvaluationContext, Object paramObject) throws EvaluationException;
  
  @Nullable
  <T> T getValue(EvaluationContext paramEvaluationContext, @Nullable Class<T> paramClass) throws EvaluationException;
  
  @Nullable
  <T> T getValue(EvaluationContext paramEvaluationContext, Object paramObject, @Nullable Class<T> paramClass) throws EvaluationException;
  
  @Nullable
  Class<?> getValueType() throws EvaluationException;
  
  @Nullable
  Class<?> getValueType(Object paramObject) throws EvaluationException;
  
  @Nullable
  Class<?> getValueType(EvaluationContext paramEvaluationContext) throws EvaluationException;
  
  @Nullable
  Class<?> getValueType(EvaluationContext paramEvaluationContext, Object paramObject) throws EvaluationException;
  
  @Nullable
  TypeDescriptor getValueTypeDescriptor() throws EvaluationException;
  
  @Nullable
  TypeDescriptor getValueTypeDescriptor(Object paramObject) throws EvaluationException;
  
  @Nullable
  TypeDescriptor getValueTypeDescriptor(EvaluationContext paramEvaluationContext) throws EvaluationException;
  
  @Nullable
  TypeDescriptor getValueTypeDescriptor(EvaluationContext paramEvaluationContext, Object paramObject) throws EvaluationException;
  
  boolean isWritable(Object paramObject) throws EvaluationException;
  
  boolean isWritable(EvaluationContext paramEvaluationContext) throws EvaluationException;
  
  boolean isWritable(EvaluationContext paramEvaluationContext, Object paramObject) throws EvaluationException;
  
  void setValue(Object paramObject1, @Nullable Object paramObject2) throws EvaluationException;
  
  void setValue(EvaluationContext paramEvaluationContext, @Nullable Object paramObject) throws EvaluationException;
  
  void setValue(EvaluationContext paramEvaluationContext, Object paramObject1, @Nullable Object paramObject2) throws EvaluationException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/Expression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */