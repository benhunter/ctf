/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class MethodInvoker
/*     */ {
/*     */   @Nullable
/*     */   protected Class<?> targetClass;
/*     */   @Nullable
/*     */   private Object targetObject;
/*     */   @Nullable
/*     */   private String targetMethod;
/*     */   @Nullable
/*     */   private String staticMethod;
/*     */   @Nullable
/*     */   private Object[] arguments;
/*     */   @Nullable
/*     */   private Method methodObject;
/*     */   
/*     */   public void setTargetClass(@Nullable Class<?> targetClass) {
/*  69 */     this.targetClass = targetClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getTargetClass() {
/*  77 */     return this.targetClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetObject(@Nullable Object targetObject) {
/*  88 */     this.targetObject = targetObject;
/*  89 */     if (targetObject != null) {
/*  90 */       this.targetClass = targetObject.getClass();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getTargetObject() {
/*  99 */     return this.targetObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetMethod(@Nullable String targetMethod) {
/* 110 */     this.targetMethod = targetMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getTargetMethod() {
/* 118 */     return this.targetMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStaticMethod(String staticMethod) {
/* 129 */     this.staticMethod = staticMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArguments(Object... arguments) {
/* 137 */     this.arguments = arguments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getArguments() {
/* 144 */     return (this.arguments != null) ? this.arguments : new Object[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() throws ClassNotFoundException, NoSuchMethodException {
/* 155 */     if (this.staticMethod != null) {
/* 156 */       int lastDotIndex = this.staticMethod.lastIndexOf('.');
/* 157 */       if (lastDotIndex == -1 || lastDotIndex == this.staticMethod.length()) {
/* 158 */         throw new IllegalArgumentException("staticMethod must be a fully qualified class plus method name: e.g. 'example.MyExampleClass.myExampleMethod'");
/*     */       }
/*     */ 
/*     */       
/* 162 */       String className = this.staticMethod.substring(0, lastDotIndex);
/* 163 */       String methodName = this.staticMethod.substring(lastDotIndex + 1);
/* 164 */       this.targetClass = resolveClassName(className);
/* 165 */       this.targetMethod = methodName;
/*     */     } 
/*     */     
/* 168 */     Class<?> targetClass = getTargetClass();
/* 169 */     String targetMethod = getTargetMethod();
/* 170 */     Assert.notNull(targetClass, "Either 'targetClass' or 'targetObject' is required");
/* 171 */     Assert.notNull(targetMethod, "Property 'targetMethod' is required");
/*     */     
/* 173 */     Object[] arguments = getArguments();
/* 174 */     Class<?>[] argTypes = new Class[arguments.length];
/* 175 */     for (int i = 0; i < arguments.length; i++) {
/* 176 */       argTypes[i] = (arguments[i] != null) ? arguments[i].getClass() : Object.class;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 181 */       this.methodObject = targetClass.getMethod(targetMethod, argTypes);
/*     */     }
/* 183 */     catch (NoSuchMethodException ex) {
/*     */       
/* 185 */       this.methodObject = findMatchingMethod();
/* 186 */       if (this.methodObject == null) {
/* 187 */         throw ex;
/*     */       }
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
/*     */   
/*     */   protected Class<?> resolveClassName(String className) throws ClassNotFoundException {
/* 201 */     return ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Method findMatchingMethod() {
/* 213 */     String targetMethod = getTargetMethod();
/* 214 */     Object[] arguments = getArguments();
/* 215 */     int argCount = arguments.length;
/*     */     
/* 217 */     Class<?> targetClass = getTargetClass();
/* 218 */     Assert.state((targetClass != null), "No target class set");
/* 219 */     Method[] candidates = ReflectionUtils.getAllDeclaredMethods(targetClass);
/* 220 */     int minTypeDiffWeight = Integer.MAX_VALUE;
/* 221 */     Method matchingMethod = null;
/*     */     
/* 223 */     for (Method candidate : candidates) {
/* 224 */       if (candidate.getName().equals(targetMethod)) {
/* 225 */         Class<?>[] paramTypes = candidate.getParameterTypes();
/* 226 */         if (paramTypes.length == argCount) {
/* 227 */           int typeDiffWeight = getTypeDifferenceWeight(paramTypes, arguments);
/* 228 */           if (typeDiffWeight < minTypeDiffWeight) {
/* 229 */             minTypeDiffWeight = typeDiffWeight;
/* 230 */             matchingMethod = candidate;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 236 */     return matchingMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Method getPreparedMethod() throws IllegalStateException {
/* 248 */     if (this.methodObject == null) {
/* 249 */       throw new IllegalStateException("prepare() must be called prior to invoke() on MethodInvoker");
/*     */     }
/* 251 */     return this.methodObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrepared() {
/* 259 */     return (this.methodObject != null);
/*     */   }
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
/*     */   @Nullable
/*     */   public Object invoke() throws InvocationTargetException, IllegalAccessException {
/* 274 */     Object targetObject = getTargetObject();
/* 275 */     Method preparedMethod = getPreparedMethod();
/* 276 */     if (targetObject == null && !Modifier.isStatic(preparedMethod.getModifiers())) {
/* 277 */       throw new IllegalArgumentException("Target method must not be non-static without a target");
/*     */     }
/* 279 */     ReflectionUtils.makeAccessible(preparedMethod);
/* 280 */     return preparedMethod.invoke(targetObject, getArguments());
/*     */   }
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
/*     */   public static int getTypeDifferenceWeight(Class<?>[] paramTypes, Object[] args) {
/* 305 */     int result = 0;
/* 306 */     for (int i = 0; i < paramTypes.length; i++) {
/* 307 */       if (!ClassUtils.isAssignableValue(paramTypes[i], args[i])) {
/* 308 */         return Integer.MAX_VALUE;
/*     */       }
/* 310 */       if (args[i] != null) {
/* 311 */         Class<?> paramType = paramTypes[i];
/* 312 */         Class<?> superClass = args[i].getClass().getSuperclass();
/* 313 */         while (superClass != null) {
/* 314 */           if (paramType.equals(superClass)) {
/* 315 */             result += 2;
/* 316 */             superClass = null; continue;
/*     */           } 
/* 318 */           if (ClassUtils.isAssignable(paramType, superClass)) {
/* 319 */             result += 2;
/* 320 */             superClass = superClass.getSuperclass();
/*     */             continue;
/*     */           } 
/* 323 */           superClass = null;
/*     */         } 
/*     */         
/* 326 */         if (paramType.isInterface()) {
/* 327 */           result++;
/*     */         }
/*     */       } 
/*     */     } 
/* 331 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/MethodInvoker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */