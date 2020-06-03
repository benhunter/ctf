/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.aspectj.lang.JoinPoint;
/*     */ import org.aspectj.lang.ProceedingJoinPoint;
/*     */ import org.aspectj.lang.Signature;
/*     */ import org.aspectj.lang.reflect.MethodSignature;
/*     */ import org.aspectj.lang.reflect.SourceLocation;
/*     */ import org.aspectj.runtime.internal.AroundClosure;
/*     */ import org.springframework.aop.ProxyMethodInvocation;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ public class MethodInvocationProceedingJoinPoint
/*     */   implements ProceedingJoinPoint, JoinPoint.StaticPart
/*     */ {
/*  54 */   private static final ParameterNameDiscoverer parameterNameDiscoverer = (ParameterNameDiscoverer)new DefaultParameterNameDiscoverer();
/*     */ 
/*     */ 
/*     */   
/*     */   private final ProxyMethodInvocation methodInvocation;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object[] args;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Signature signature;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private SourceLocation sourceLocation;
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodInvocationProceedingJoinPoint(ProxyMethodInvocation methodInvocation) {
/*  76 */     Assert.notNull(methodInvocation, "MethodInvocation must not be null");
/*  77 */     this.methodInvocation = methodInvocation;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void set$AroundClosure(AroundClosure aroundClosure) {
/*  83 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object proceed() throws Throwable {
/*  88 */     return this.methodInvocation.invocableClone().proceed();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object proceed(Object[] arguments) throws Throwable {
/*  93 */     Assert.notNull(arguments, "Argument array passed to proceed cannot be null");
/*  94 */     if (arguments.length != (this.methodInvocation.getArguments()).length) {
/*  95 */       throw new IllegalArgumentException("Expecting " + (this.methodInvocation
/*  96 */           .getArguments()).length + " arguments to proceed, but was passed " + arguments.length + " arguments");
/*     */     }
/*     */     
/*  99 */     this.methodInvocation.setArguments(arguments);
/* 100 */     return this.methodInvocation.invocableClone(arguments).proceed();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getThis() {
/* 108 */     return this.methodInvocation.getProxy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getTarget() {
/* 117 */     return this.methodInvocation.getThis();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] getArgs() {
/* 122 */     if (this.args == null) {
/* 123 */       this.args = (Object[])this.methodInvocation.getArguments().clone();
/*     */     }
/* 125 */     return this.args;
/*     */   }
/*     */ 
/*     */   
/*     */   public Signature getSignature() {
/* 130 */     if (this.signature == null) {
/* 131 */       this.signature = (Signature)new MethodSignatureImpl();
/*     */     }
/* 133 */     return this.signature;
/*     */   }
/*     */ 
/*     */   
/*     */   public SourceLocation getSourceLocation() {
/* 138 */     if (this.sourceLocation == null) {
/* 139 */       this.sourceLocation = new SourceLocationImpl();
/*     */     }
/* 141 */     return this.sourceLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getKind() {
/* 146 */     return "method-execution";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getId() {
/* 152 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public JoinPoint.StaticPart getStaticPart() {
/* 157 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toShortString() {
/* 162 */     return "execution(" + getSignature().toShortString() + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public String toLongString() {
/* 167 */     return "execution(" + getSignature().toLongString() + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 172 */     return "execution(" + getSignature().toString() + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   private class MethodSignatureImpl
/*     */     implements MethodSignature
/*     */   {
/*     */     @Nullable
/*     */     private volatile String[] parameterNames;
/*     */ 
/*     */     
/*     */     private MethodSignatureImpl() {}
/*     */     
/*     */     public String getName() {
/* 186 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getModifiers() {
/* 191 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getModifiers();
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getDeclaringType() {
/* 196 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getDeclaringClass();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getDeclaringTypeName() {
/* 201 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getDeclaringClass().getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getReturnType() {
/* 206 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getReturnType();
/*     */     }
/*     */ 
/*     */     
/*     */     public Method getMethod() {
/* 211 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod();
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?>[] getParameterTypes() {
/* 216 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getParameterTypes();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String[] getParameterNames() {
/* 222 */       if (this.parameterNames == null) {
/* 223 */         this.parameterNames = MethodInvocationProceedingJoinPoint.parameterNameDiscoverer.getParameterNames(getMethod());
/*     */       }
/* 225 */       return this.parameterNames;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?>[] getExceptionTypes() {
/* 230 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getExceptionTypes();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toShortString() {
/* 235 */       return toString(false, false, false, false);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toLongString() {
/* 240 */       return toString(true, true, true, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 245 */       return toString(false, true, false, true);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private String toString(boolean includeModifier, boolean includeReturnTypeAndArgs, boolean useLongReturnAndArgumentTypeName, boolean useLongTypeName) {
/* 251 */       StringBuilder sb = new StringBuilder();
/* 252 */       if (includeModifier) {
/* 253 */         sb.append(Modifier.toString(getModifiers()));
/* 254 */         sb.append(" ");
/*     */       } 
/* 256 */       if (includeReturnTypeAndArgs) {
/* 257 */         appendType(sb, getReturnType(), useLongReturnAndArgumentTypeName);
/* 258 */         sb.append(" ");
/*     */       } 
/* 260 */       appendType(sb, getDeclaringType(), useLongTypeName);
/* 261 */       sb.append(".");
/* 262 */       sb.append(getMethod().getName());
/* 263 */       sb.append("(");
/* 264 */       Class<?>[] parametersTypes = getParameterTypes();
/* 265 */       appendTypes(sb, parametersTypes, includeReturnTypeAndArgs, useLongReturnAndArgumentTypeName);
/* 266 */       sb.append(")");
/* 267 */       return sb.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void appendTypes(StringBuilder sb, Class<?>[] types, boolean includeArgs, boolean useLongReturnAndArgumentTypeName) {
/* 273 */       if (includeArgs) {
/* 274 */         for (int size = types.length, i = 0; i < size; i++) {
/* 275 */           appendType(sb, types[i], useLongReturnAndArgumentTypeName);
/* 276 */           if (i < size - 1) {
/* 277 */             sb.append(",");
/*     */           }
/*     */         }
/*     */       
/*     */       }
/* 282 */       else if (types.length != 0) {
/* 283 */         sb.append("..");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void appendType(StringBuilder sb, Class<?> type, boolean useLongTypeName) {
/* 289 */       if (type.isArray()) {
/* 290 */         appendType(sb, type.getComponentType(), useLongTypeName);
/* 291 */         sb.append("[]");
/*     */       } else {
/*     */         
/* 294 */         sb.append(useLongTypeName ? type.getName() : type.getSimpleName());
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class SourceLocationImpl
/*     */     implements SourceLocation
/*     */   {
/*     */     private SourceLocationImpl() {}
/*     */ 
/*     */     
/*     */     public Class<?> getWithinType() {
/* 307 */       if (MethodInvocationProceedingJoinPoint.this.methodInvocation.getThis() == null) {
/* 308 */         throw new UnsupportedOperationException("No source location joinpoint available: target is null");
/*     */       }
/* 310 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getThis().getClass();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getFileName() {
/* 315 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getLine() {
/* 320 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public int getColumn() {
/* 326 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/MethodInvocationProceedingJoinPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */