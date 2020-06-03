/*     */ package org.springframework.remoting.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RemoteInvocation
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6876024250231820554L;
/*     */   private String methodName;
/*     */   private Class<?>[] parameterTypes;
/*     */   private Object[] arguments;
/*     */   private Map<String, Serializable> attributes;
/*     */   
/*     */   public RemoteInvocation(MethodInvocation methodInvocation) {
/*  70 */     this.methodName = methodInvocation.getMethod().getName();
/*  71 */     this.parameterTypes = methodInvocation.getMethod().getParameterTypes();
/*  72 */     this.arguments = methodInvocation.getArguments();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RemoteInvocation(String methodName, Class<?>[] parameterTypes, Object[] arguments) {
/*  82 */     this.methodName = methodName;
/*  83 */     this.parameterTypes = parameterTypes;
/*  84 */     this.arguments = arguments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RemoteInvocation() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMethodName(String methodName) {
/* 100 */     this.methodName = methodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodName() {
/* 107 */     return this.methodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterTypes(Class<?>[] parameterTypes) {
/* 115 */     this.parameterTypes = parameterTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?>[] getParameterTypes() {
/* 122 */     return this.parameterTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArguments(Object[] arguments) {
/* 130 */     this.arguments = arguments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getArguments() {
/* 137 */     return this.arguments;
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
/*     */   public void addAttribute(String key, Serializable value) throws IllegalStateException {
/* 153 */     if (this.attributes == null) {
/* 154 */       this.attributes = new HashMap<>();
/*     */     }
/* 156 */     if (this.attributes.containsKey(key)) {
/* 157 */       throw new IllegalStateException("There is already an attribute with key '" + key + "' bound");
/*     */     }
/* 159 */     this.attributes.put(key, value);
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
/*     */   public Serializable getAttribute(String key) {
/* 171 */     if (this.attributes == null) {
/* 172 */       return null;
/*     */     }
/* 174 */     return this.attributes.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttributes(@Nullable Map<String, Serializable> attributes) {
/* 185 */     this.attributes = attributes;
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
/*     */   public Map<String, Serializable> getAttributes() {
/* 197 */     return this.attributes;
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
/*     */   public Object invoke(Object targetObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 214 */     Method method = targetObject.getClass().getMethod(this.methodName, this.parameterTypes);
/* 215 */     return method.invoke(targetObject, this.arguments);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 221 */     return "RemoteInvocation: method name '" + this.methodName + "'; parameter types " + 
/* 222 */       ClassUtils.classNamesToString(this.parameterTypes);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/support/RemoteInvocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */