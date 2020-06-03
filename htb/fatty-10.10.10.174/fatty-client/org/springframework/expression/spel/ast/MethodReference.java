/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.ExpressionInvocationTargetException;
/*     */ import org.springframework.expression.MethodExecutor;
/*     */ import org.springframework.expression.MethodResolver;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.support.ReflectiveMethodExecutor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ public class MethodReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final String name;
/*     */   private final boolean nullSafe;
/*     */   @Nullable
/*     */   private String originalPrimitiveExitTypeDescriptor;
/*     */   @Nullable
/*     */   private volatile CachedMethodExecutor cachedExecutor;
/*     */   
/*     */   public MethodReference(boolean nullSafe, String methodName, int pos, SpelNodeImpl... arguments) {
/*  68 */     super(pos, arguments);
/*  69 */     this.name = methodName;
/*  70 */     this.nullSafe = nullSafe;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/*  75 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/*  80 */     Object[] arguments = getArguments(state);
/*  81 */     if (state.getActiveContextObject().getValue() == null) {
/*  82 */       throwIfNotNullSafe(getArgumentTypes(arguments));
/*  83 */       return ValueRef.NullValueRef.INSTANCE;
/*     */     } 
/*  85 */     return new MethodValueRef(state, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  90 */     EvaluationContext evaluationContext = state.getEvaluationContext();
/*  91 */     Object value = state.getActiveContextObject().getValue();
/*  92 */     TypeDescriptor targetType = state.getActiveContextObject().getTypeDescriptor();
/*  93 */     Object[] arguments = getArguments(state);
/*  94 */     TypedValue result = getValueInternal(evaluationContext, value, targetType, arguments);
/*  95 */     updateExitTypeDescriptor();
/*  96 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private TypedValue getValueInternal(EvaluationContext evaluationContext, @Nullable Object value, @Nullable TypeDescriptor targetType, Object[] arguments) {
/* 102 */     List<TypeDescriptor> argumentTypes = getArgumentTypes(arguments);
/* 103 */     if (value == null) {
/* 104 */       throwIfNotNullSafe(argumentTypes);
/* 105 */       return TypedValue.NULL;
/*     */     } 
/*     */     
/* 108 */     MethodExecutor executorToUse = getCachedExecutor(evaluationContext, value, targetType, argumentTypes);
/* 109 */     if (executorToUse != null) {
/*     */       try {
/* 111 */         return executorToUse.execute(evaluationContext, value, arguments);
/*     */       }
/* 113 */       catch (AccessException ex) {
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
/* 125 */         throwSimpleExceptionIfPossible(value, ex);
/*     */ 
/*     */ 
/*     */         
/* 129 */         this.cachedExecutor = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 134 */     executorToUse = findAccessorForMethod(argumentTypes, value, evaluationContext);
/* 135 */     this.cachedExecutor = new CachedMethodExecutor(executorToUse, (value instanceof Class) ? (Class)value : null, targetType, argumentTypes);
/*     */     
/*     */     try {
/* 138 */       return executorToUse.execute(evaluationContext, value, arguments);
/*     */     }
/* 140 */     catch (AccessException ex) {
/*     */       
/* 142 */       throwSimpleExceptionIfPossible(value, ex);
/* 143 */       throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_METHOD_INVOCATION, new Object[] { this.name, value
/*     */             
/* 145 */             .getClass().getName(), ex.getMessage() });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void throwIfNotNullSafe(List<TypeDescriptor> argumentTypes) {
/* 150 */     if (!this.nullSafe)
/* 151 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.METHOD_CALL_ON_NULL_OBJECT_NOT_ALLOWED, new Object[] {
/*     */             
/* 153 */             FormatHelper.formatMethodForMessage(this.name, argumentTypes)
/*     */           }); 
/*     */   }
/*     */   
/*     */   private Object[] getArguments(ExpressionState state) {
/* 158 */     Object[] arguments = new Object[getChildCount()];
/* 159 */     for (int i = 0; i < arguments.length; i++) {
/*     */ 
/*     */       
/* 162 */       try { state.pushActiveContextObject(state.getScopeRootContextObject());
/* 163 */         arguments[i] = this.children[i].getValueInternal(state).getValue();
/*     */ 
/*     */         
/* 166 */         state.popActiveContextObject(); } finally { state.popActiveContextObject(); }
/*     */     
/*     */     } 
/* 169 */     return arguments;
/*     */   }
/*     */   
/*     */   private List<TypeDescriptor> getArgumentTypes(Object... arguments) {
/* 173 */     List<TypeDescriptor> descriptors = new ArrayList<>(arguments.length);
/* 174 */     for (Object argument : arguments) {
/* 175 */       descriptors.add(TypeDescriptor.forObject(argument));
/*     */     }
/* 177 */     return Collections.unmodifiableList(descriptors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private MethodExecutor getCachedExecutor(EvaluationContext evaluationContext, Object value, @Nullable TypeDescriptor target, List<TypeDescriptor> argumentTypes) {
/* 184 */     List<MethodResolver> methodResolvers = evaluationContext.getMethodResolvers();
/* 185 */     if (methodResolvers.size() != 1 || !(methodResolvers.get(0) instanceof org.springframework.expression.spel.support.ReflectiveMethodResolver))
/*     */     {
/* 187 */       return null;
/*     */     }
/*     */     
/* 190 */     CachedMethodExecutor executorToCheck = this.cachedExecutor;
/* 191 */     if (executorToCheck != null && executorToCheck.isSuitable(value, target, argumentTypes)) {
/* 192 */       return executorToCheck.get();
/*     */     }
/* 194 */     this.cachedExecutor = null;
/* 195 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MethodExecutor findAccessorForMethod(List<TypeDescriptor> argumentTypes, Object targetObject, EvaluationContext evaluationContext) throws SpelEvaluationException {
/* 201 */     AccessException accessException = null;
/* 202 */     List<MethodResolver> methodResolvers = evaluationContext.getMethodResolvers();
/* 203 */     for (MethodResolver methodResolver : methodResolvers) {
/*     */       try {
/* 205 */         MethodExecutor methodExecutor = methodResolver.resolve(evaluationContext, targetObject, this.name, argumentTypes);
/*     */         
/* 207 */         if (methodExecutor != null) {
/* 208 */           return methodExecutor;
/*     */         }
/*     */       }
/* 211 */       catch (AccessException ex) {
/* 212 */         accessException = ex;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 217 */     String method = FormatHelper.formatMethodForMessage(this.name, argumentTypes);
/* 218 */     String className = FormatHelper.formatClassNameForMessage((targetObject instanceof Class) ? (Class)targetObject : targetObject
/* 219 */         .getClass());
/* 220 */     if (accessException != null) {
/* 221 */       throw new SpelEvaluationException(
/* 222 */           getStartPosition(), accessException, SpelMessage.PROBLEM_LOCATING_METHOD, new Object[] { method, className });
/*     */     }
/*     */     
/* 225 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.METHOD_NOT_FOUND, new Object[] { method, className });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void throwSimpleExceptionIfPossible(Object value, AccessException ex) {
/* 234 */     if (ex.getCause() instanceof java.lang.reflect.InvocationTargetException) {
/* 235 */       Throwable rootCause = ex.getCause().getCause();
/* 236 */       if (rootCause instanceof RuntimeException) {
/* 237 */         throw (RuntimeException)rootCause;
/*     */       }
/* 239 */       throw new ExpressionInvocationTargetException(getStartPosition(), "A problem occurred when trying to execute method '" + this.name + "' on object of type [" + value
/*     */           
/* 241 */           .getClass().getName() + "]", rootCause);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateExitTypeDescriptor() {
/* 246 */     CachedMethodExecutor executorToCheck = this.cachedExecutor;
/* 247 */     if (executorToCheck != null && executorToCheck.get() instanceof ReflectiveMethodExecutor) {
/* 248 */       Method method = ((ReflectiveMethodExecutor)executorToCheck.get()).getMethod();
/* 249 */       String descriptor = CodeFlow.toDescriptor(method.getReturnType());
/* 250 */       if (this.nullSafe && CodeFlow.isPrimitive(descriptor)) {
/* 251 */         this.originalPrimitiveExitTypeDescriptor = descriptor;
/* 252 */         this.exitTypeDescriptor = CodeFlow.toBoxedDescriptor(descriptor);
/*     */       } else {
/*     */         
/* 255 */         this.exitTypeDescriptor = descriptor;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 262 */     StringBuilder sb = new StringBuilder(this.name);
/* 263 */     sb.append("(");
/* 264 */     for (int i = 0; i < getChildCount(); i++) {
/* 265 */       if (i > 0) {
/* 266 */         sb.append(",");
/*     */       }
/* 268 */       sb.append(getChild(i).toStringAST());
/*     */     } 
/* 270 */     sb.append(")");
/* 271 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 280 */     CachedMethodExecutor executorToCheck = this.cachedExecutor;
/* 281 */     if (executorToCheck == null || executorToCheck.hasProxyTarget() || 
/* 282 */       !(executorToCheck.get() instanceof ReflectiveMethodExecutor)) {
/* 283 */       return false;
/*     */     }
/*     */     
/* 286 */     for (SpelNodeImpl child : this.children) {
/* 287 */       if (!child.isCompilable()) {
/* 288 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 292 */     ReflectiveMethodExecutor executor = (ReflectiveMethodExecutor)executorToCheck.get();
/* 293 */     if (executor.didArgumentConversionOccur()) {
/* 294 */       return false;
/*     */     }
/* 296 */     Class<?> clazz = executor.getMethod().getDeclaringClass();
/* 297 */     if (!Modifier.isPublic(clazz.getModifiers()) && executor.getPublicDeclaringClass() == null) {
/* 298 */       return false;
/*     */     }
/*     */     
/* 301 */     return true;
/*     */   }
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/*     */     String classDesc;
/* 306 */     CachedMethodExecutor executorToCheck = this.cachedExecutor;
/* 307 */     if (executorToCheck == null || !(executorToCheck.get() instanceof ReflectiveMethodExecutor)) {
/* 308 */       throw new IllegalStateException("No applicable cached executor found: " + executorToCheck);
/*     */     }
/*     */     
/* 311 */     ReflectiveMethodExecutor methodExecutor = (ReflectiveMethodExecutor)executorToCheck.get();
/* 312 */     Method method = methodExecutor.getMethod();
/* 313 */     boolean isStaticMethod = Modifier.isStatic(method.getModifiers());
/* 314 */     String descriptor = cf.lastDescriptor();
/*     */     
/* 316 */     Label skipIfNull = null;
/* 317 */     if (descriptor == null && !isStaticMethod)
/*     */     {
/* 319 */       cf.loadTarget(mv);
/*     */     }
/* 321 */     if ((descriptor != null || !isStaticMethod) && this.nullSafe) {
/* 322 */       mv.visitInsn(89);
/* 323 */       skipIfNull = new Label();
/* 324 */       Label continueLabel = new Label();
/* 325 */       mv.visitJumpInsn(199, continueLabel);
/* 326 */       CodeFlow.insertCheckCast(mv, this.exitTypeDescriptor);
/* 327 */       mv.visitJumpInsn(167, skipIfNull);
/* 328 */       mv.visitLabel(continueLabel);
/*     */     } 
/* 330 */     if (descriptor != null && isStaticMethod)
/*     */     {
/* 332 */       mv.visitInsn(87);
/*     */     }
/*     */     
/* 335 */     if (CodeFlow.isPrimitive(descriptor)) {
/* 336 */       CodeFlow.insertBoxIfNecessary(mv, descriptor.charAt(0));
/*     */     }
/*     */ 
/*     */     
/* 340 */     if (Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
/* 341 */       classDesc = method.getDeclaringClass().getName().replace('.', '/');
/*     */     } else {
/*     */       
/* 344 */       Class<?> publicDeclaringClass = methodExecutor.getPublicDeclaringClass();
/* 345 */       Assert.state((publicDeclaringClass != null), "No public declaring class");
/* 346 */       classDesc = publicDeclaringClass.getName().replace('.', '/');
/*     */     } 
/*     */     
/* 349 */     if (!isStaticMethod && (descriptor == null || !descriptor.substring(1).equals(classDesc))) {
/* 350 */       CodeFlow.insertCheckCast(mv, "L" + classDesc);
/*     */     }
/*     */     
/* 353 */     generateCodeForArguments(mv, cf, method, this.children);
/* 354 */     mv.visitMethodInsn(isStaticMethod ? 184 : 182, classDesc, method.getName(), 
/* 355 */         CodeFlow.createSignatureDescriptor(method), method.getDeclaringClass().isInterface());
/* 356 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */     
/* 358 */     if (this.originalPrimitiveExitTypeDescriptor != null)
/*     */     {
/*     */       
/* 361 */       CodeFlow.insertBoxIfNecessary(mv, this.originalPrimitiveExitTypeDescriptor);
/*     */     }
/* 363 */     if (skipIfNull != null) {
/* 364 */       mv.visitLabel(skipIfNull);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class MethodValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final EvaluationContext evaluationContext;
/*     */     
/*     */     @Nullable
/*     */     private final Object value;
/*     */     
/*     */     @Nullable
/*     */     private final TypeDescriptor targetType;
/*     */     private final Object[] arguments;
/*     */     
/*     */     public MethodValueRef(ExpressionState state, Object[] arguments) {
/* 382 */       this.evaluationContext = state.getEvaluationContext();
/* 383 */       this.value = state.getActiveContextObject().getValue();
/* 384 */       this.targetType = state.getActiveContextObject().getTypeDescriptor();
/* 385 */       this.arguments = arguments;
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue getValue() {
/* 390 */       TypedValue result = MethodReference.this.getValueInternal(this.evaluationContext, this.value, this.targetType, this.arguments);
/*     */       
/* 392 */       MethodReference.this.updateExitTypeDescriptor();
/* 393 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValue(@Nullable Object newValue) {
/* 398 */       throw new SpelEvaluationException(0, SpelMessage.NOT_ASSIGNABLE, new Object[] { MethodReference.access$200(this.this$0) });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWritable() {
/* 403 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CachedMethodExecutor
/*     */   {
/*     */     private final MethodExecutor methodExecutor;
/*     */     
/*     */     @Nullable
/*     */     private final Class<?> staticClass;
/*     */     
/*     */     @Nullable
/*     */     private final TypeDescriptor target;
/*     */     
/*     */     private final List<TypeDescriptor> argumentTypes;
/*     */ 
/*     */     
/*     */     public CachedMethodExecutor(MethodExecutor methodExecutor, @Nullable Class<?> staticClass, @Nullable TypeDescriptor target, List<TypeDescriptor> argumentTypes) {
/* 423 */       this.methodExecutor = methodExecutor;
/* 424 */       this.staticClass = staticClass;
/* 425 */       this.target = target;
/* 426 */       this.argumentTypes = argumentTypes;
/*     */     }
/*     */     
/*     */     public boolean isSuitable(Object value, @Nullable TypeDescriptor target, List<TypeDescriptor> argumentTypes) {
/* 430 */       return ((this.staticClass == null || this.staticClass == value) && 
/* 431 */         ObjectUtils.nullSafeEquals(this.target, target) && this.argumentTypes.equals(argumentTypes));
/*     */     }
/*     */     
/*     */     public boolean hasProxyTarget() {
/* 435 */       return (this.target != null && Proxy.isProxyClass(this.target.getType()));
/*     */     }
/*     */     
/*     */     public MethodExecutor get() {
/* 439 */       return this.methodExecutor;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/ast/MethodReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */