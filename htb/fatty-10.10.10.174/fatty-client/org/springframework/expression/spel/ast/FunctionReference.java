/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.support.ReflectionHelper;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FunctionReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final String name;
/*     */   @Nullable
/*     */   private volatile Method method;
/*     */   
/*     */   public FunctionReference(String functionName, int pos, SpelNodeImpl... arguments) {
/*  61 */     super(pos, arguments);
/*  62 */     this.name = functionName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  68 */     TypedValue value = state.lookupVariable(this.name);
/*  69 */     if (value == TypedValue.NULL) {
/*  70 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.FUNCTION_NOT_DEFINED, new Object[] { this.name });
/*     */     }
/*  72 */     if (!(value.getValue() instanceof Method))
/*     */     {
/*  74 */       throw new SpelEvaluationException(SpelMessage.FUNCTION_REFERENCE_CANNOT_BE_INVOKED, new Object[] { this.name, value
/*  75 */             .getClass() });
/*     */     }
/*     */     
/*     */     try {
/*  79 */       return executeFunctionJLRMethod(state, (Method)value.getValue());
/*     */     }
/*  81 */     catch (SpelEvaluationException ex) {
/*  82 */       ex.setPosition(getStartPosition());
/*  83 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypedValue executeFunctionJLRMethod(ExpressionState state, Method method) throws EvaluationException {
/*  95 */     Object[] functionArgs = getArguments(state);
/*     */     
/*  97 */     if (!method.isVarArgs()) {
/*  98 */       int declaredParamCount = method.getParameterCount();
/*  99 */       if (declaredParamCount != functionArgs.length)
/* 100 */         throw new SpelEvaluationException(SpelMessage.INCORRECT_NUMBER_OF_ARGUMENTS_TO_FUNCTION, new Object[] {
/* 101 */               Integer.valueOf(functionArgs.length), Integer.valueOf(declaredParamCount)
/*     */             }); 
/*     */     } 
/* 104 */     if (!Modifier.isStatic(method.getModifiers())) {
/* 105 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.FUNCTION_MUST_BE_STATIC, new Object[] {
/* 106 */             ClassUtils.getQualifiedMethodName(method), this.name
/*     */           });
/*     */     }
/*     */     
/* 110 */     TypeConverter converter = state.getEvaluationContext().getTypeConverter();
/* 111 */     boolean argumentConversionOccurred = ReflectionHelper.convertAllArguments(converter, functionArgs, method);
/* 112 */     if (method.isVarArgs()) {
/* 113 */       functionArgs = ReflectionHelper.setupArgumentsForVarargsInvocation(method
/* 114 */           .getParameterTypes(), functionArgs);
/*     */     }
/* 116 */     boolean compilable = false;
/*     */     
/*     */     try {
/* 119 */       ReflectionUtils.makeAccessible(method);
/* 120 */       Object result = method.invoke(method.getClass(), functionArgs);
/* 121 */       compilable = !argumentConversionOccurred;
/* 122 */       return new TypedValue(result, (new TypeDescriptor(new MethodParameter(method, -1))).narrow(result));
/*     */     }
/* 124 */     catch (Exception ex) {
/* 125 */       throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_FUNCTION_CALL, new Object[] { this.name, ex
/* 126 */             .getMessage() });
/*     */     } finally {
/*     */       
/* 129 */       if (compilable) {
/* 130 */         this.exitTypeDescriptor = CodeFlow.toDescriptor(method.getReturnType());
/* 131 */         this.method = method;
/*     */       } else {
/*     */         
/* 134 */         this.exitTypeDescriptor = null;
/* 135 */         this.method = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 142 */     StringBuilder sb = (new StringBuilder("#")).append(this.name);
/* 143 */     sb.append("(");
/* 144 */     for (int i = 0; i < getChildCount(); i++) {
/* 145 */       if (i > 0) {
/* 146 */         sb.append(",");
/*     */       }
/* 148 */       sb.append(getChild(i).toStringAST());
/*     */     } 
/* 150 */     sb.append(")");
/* 151 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object[] getArguments(ExpressionState state) throws EvaluationException {
/* 160 */     Object[] arguments = new Object[getChildCount()];
/* 161 */     for (int i = 0; i < arguments.length; i++) {
/* 162 */       arguments[i] = this.children[i].getValueInternal(state).getValue();
/*     */     }
/* 164 */     return arguments;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 169 */     Method method = this.method;
/* 170 */     if (method == null) {
/* 171 */       return false;
/*     */     }
/* 173 */     int methodModifiers = method.getModifiers();
/* 174 */     if (!Modifier.isStatic(methodModifiers) || !Modifier.isPublic(methodModifiers) || 
/* 175 */       !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
/* 176 */       return false;
/*     */     }
/* 178 */     for (SpelNodeImpl child : this.children) {
/* 179 */       if (!child.isCompilable()) {
/* 180 */         return false;
/*     */       }
/*     */     } 
/* 183 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 188 */     Method method = this.method;
/* 189 */     Assert.state((method != null), "No method handle");
/* 190 */     String classDesc = method.getDeclaringClass().getName().replace('.', '/');
/* 191 */     generateCodeForArguments(mv, cf, method, this.children);
/* 192 */     mv.visitMethodInsn(184, classDesc, method.getName(), 
/* 193 */         CodeFlow.createSignatureDescriptor(method), false);
/* 194 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/ast/FunctionReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */