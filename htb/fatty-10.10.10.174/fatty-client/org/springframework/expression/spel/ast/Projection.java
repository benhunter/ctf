/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Projection
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final boolean nullSafe;
/*     */   
/*     */   public Projection(boolean nullSafe, int pos, SpelNodeImpl expression) {
/*  50 */     super(pos, new SpelNodeImpl[] { expression });
/*  51 */     this.nullSafe = nullSafe;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  57 */     return getValueRef(state).getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/*  62 */     TypedValue op = state.getActiveContextObject();
/*     */     
/*  64 */     Object operand = op.getValue();
/*  65 */     boolean operandIsArray = ObjectUtils.isArray(operand);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     if (operand instanceof Map) {
/*  74 */       Map<?, ?> mapData = (Map<?, ?>)operand;
/*  75 */       List<Object> result = new ArrayList();
/*  76 */       for (Map.Entry<?, ?> entry : mapData.entrySet()) {
/*     */         try {
/*  78 */           state.pushActiveContextObject(new TypedValue(entry));
/*  79 */           state.enterScope();
/*  80 */           result.add(this.children[0].getValueInternal(state).getValue());
/*     */         } finally {
/*     */           
/*  83 */           state.popActiveContextObject();
/*  84 */           state.exitScope();
/*     */         } 
/*     */       } 
/*  87 */       return new ValueRef.TypedValueHolderValueRef(new TypedValue(result), this);
/*     */     } 
/*     */     
/*  90 */     if (operand instanceof Iterable || operandIsArray) {
/*     */       
/*  92 */       Iterable<?> data = (operand instanceof Iterable) ? (Iterable)operand : Arrays.asList(ObjectUtils.toObjectArray(operand));
/*     */       
/*  94 */       List<Object> result = new ArrayList();
/*  95 */       int idx = 0;
/*  96 */       Class<?> arrayElementType = null;
/*  97 */       for (Object element : data) {
/*     */         try {
/*  99 */           state.pushActiveContextObject(new TypedValue(element));
/* 100 */           state.enterScope("index", Integer.valueOf(idx));
/* 101 */           Object value = this.children[0].getValueInternal(state).getValue();
/* 102 */           if (value != null && operandIsArray) {
/* 103 */             arrayElementType = determineCommonType(arrayElementType, value.getClass());
/*     */           }
/* 105 */           result.add(value);
/*     */         } finally {
/*     */           
/* 108 */           state.exitScope();
/* 109 */           state.popActiveContextObject();
/*     */         } 
/* 111 */         idx++;
/*     */       } 
/*     */       
/* 114 */       if (operandIsArray) {
/* 115 */         if (arrayElementType == null) {
/* 116 */           arrayElementType = Object.class;
/*     */         }
/* 118 */         Object resultArray = Array.newInstance(arrayElementType, result.size());
/* 119 */         System.arraycopy(result.toArray(), 0, resultArray, 0, result.size());
/* 120 */         return new ValueRef.TypedValueHolderValueRef(new TypedValue(resultArray), this);
/*     */       } 
/*     */       
/* 123 */       return new ValueRef.TypedValueHolderValueRef(new TypedValue(result), this);
/*     */     } 
/*     */     
/* 126 */     if (operand == null) {
/* 127 */       if (this.nullSafe) {
/* 128 */         return ValueRef.NullValueRef.INSTANCE;
/*     */       }
/* 130 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROJECTION_NOT_SUPPORTED_ON_TYPE, new Object[] { "null" });
/*     */     } 
/*     */     
/* 133 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROJECTION_NOT_SUPPORTED_ON_TYPE, new Object[] { operand
/* 134 */           .getClass().getName() });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 139 */     return "![" + getChild(0).toStringAST() + "]";
/*     */   }
/*     */   
/*     */   private Class<?> determineCommonType(@Nullable Class<?> oldType, Class<?> newType) {
/* 143 */     if (oldType == null) {
/* 144 */       return newType;
/*     */     }
/* 146 */     if (oldType.isAssignableFrom(newType)) {
/* 147 */       return oldType;
/*     */     }
/* 149 */     Class<?> nextType = newType;
/* 150 */     while (nextType != Object.class) {
/* 151 */       if (nextType.isAssignableFrom(oldType)) {
/* 152 */         return nextType;
/*     */       }
/* 154 */       nextType = nextType.getSuperclass();
/*     */     } 
/* 156 */     for (Class<?> nextInterface : (Iterable<Class<?>>)ClassUtils.getAllInterfacesForClassAsSet(newType)) {
/* 157 */       if (nextInterface.isAssignableFrom(oldType)) {
/* 158 */         return nextInterface;
/*     */       }
/*     */     } 
/* 161 */     return Object.class;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/ast/Projection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */