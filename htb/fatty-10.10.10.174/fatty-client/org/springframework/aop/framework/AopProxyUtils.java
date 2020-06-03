/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import org.springframework.aop.SpringProxy;
/*     */ import org.springframework.aop.TargetClassAware;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.aop.target.SingletonTargetSource;
/*     */ import org.springframework.core.DecoratingProxy;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AopProxyUtils
/*     */ {
/*     */   @Nullable
/*     */   public static Object getSingletonTarget(Object candidate) {
/*  58 */     if (candidate instanceof Advised) {
/*  59 */       TargetSource targetSource = ((Advised)candidate).getTargetSource();
/*  60 */       if (targetSource instanceof SingletonTargetSource) {
/*  61 */         return ((SingletonTargetSource)targetSource).getTarget();
/*     */       }
/*     */     } 
/*  64 */     return null;
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
/*     */   public static Class<?> ultimateTargetClass(Object candidate) {
/*  78 */     Assert.notNull(candidate, "Candidate object must not be null");
/*  79 */     Object current = candidate;
/*  80 */     Class<?> result = null;
/*  81 */     while (current instanceof TargetClassAware) {
/*  82 */       result = ((TargetClassAware)current).getTargetClass();
/*  83 */       current = getSingletonTarget(current);
/*     */     } 
/*  85 */     if (result == null) {
/*  86 */       result = AopUtils.isCglibProxy(candidate) ? candidate.getClass().getSuperclass() : candidate.getClass();
/*     */     }
/*  88 */     return result;
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
/*     */   public static Class<?>[] completeProxiedInterfaces(AdvisedSupport advised) {
/* 102 */     return completeProxiedInterfaces(advised, false);
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
/*     */   static Class<?>[] completeProxiedInterfaces(AdvisedSupport advised, boolean decoratingProxy) {
/* 119 */     Class<?>[] specifiedInterfaces = advised.getProxiedInterfaces();
/* 120 */     if (specifiedInterfaces.length == 0) {
/*     */       
/* 122 */       Class<?> targetClass = advised.getTargetClass();
/* 123 */       if (targetClass != null) {
/* 124 */         if (targetClass.isInterface()) {
/* 125 */           advised.setInterfaces(new Class[] { targetClass });
/*     */         }
/* 127 */         else if (Proxy.isProxyClass(targetClass)) {
/* 128 */           advised.setInterfaces(targetClass.getInterfaces());
/*     */         } 
/* 130 */         specifiedInterfaces = advised.getProxiedInterfaces();
/*     */       } 
/*     */     } 
/* 133 */     boolean addSpringProxy = !advised.isInterfaceProxied(SpringProxy.class);
/* 134 */     boolean addAdvised = (!advised.isOpaque() && !advised.isInterfaceProxied(Advised.class));
/* 135 */     boolean addDecoratingProxy = (decoratingProxy && !advised.isInterfaceProxied(DecoratingProxy.class));
/* 136 */     int nonUserIfcCount = 0;
/* 137 */     if (addSpringProxy) {
/* 138 */       nonUserIfcCount++;
/*     */     }
/* 140 */     if (addAdvised) {
/* 141 */       nonUserIfcCount++;
/*     */     }
/* 143 */     if (addDecoratingProxy) {
/* 144 */       nonUserIfcCount++;
/*     */     }
/* 146 */     Class<?>[] proxiedInterfaces = new Class[specifiedInterfaces.length + nonUserIfcCount];
/* 147 */     System.arraycopy(specifiedInterfaces, 0, proxiedInterfaces, 0, specifiedInterfaces.length);
/* 148 */     int index = specifiedInterfaces.length;
/* 149 */     if (addSpringProxy) {
/* 150 */       proxiedInterfaces[index] = SpringProxy.class;
/* 151 */       index++;
/*     */     } 
/* 153 */     if (addAdvised) {
/* 154 */       proxiedInterfaces[index] = Advised.class;
/* 155 */       index++;
/*     */     } 
/* 157 */     if (addDecoratingProxy) {
/* 158 */       proxiedInterfaces[index] = DecoratingProxy.class;
/*     */     }
/* 160 */     return proxiedInterfaces;
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
/*     */   public static Class<?>[] proxiedUserInterfaces(Object proxy) {
/* 172 */     Class<?>[] proxyInterfaces = proxy.getClass().getInterfaces();
/* 173 */     int nonUserIfcCount = 0;
/* 174 */     if (proxy instanceof SpringProxy) {
/* 175 */       nonUserIfcCount++;
/*     */     }
/* 177 */     if (proxy instanceof Advised) {
/* 178 */       nonUserIfcCount++;
/*     */     }
/* 180 */     if (proxy instanceof DecoratingProxy) {
/* 181 */       nonUserIfcCount++;
/*     */     }
/* 183 */     Class<?>[] userInterfaces = new Class[proxyInterfaces.length - nonUserIfcCount];
/* 184 */     System.arraycopy(proxyInterfaces, 0, userInterfaces, 0, userInterfaces.length);
/* 185 */     Assert.notEmpty((Object[])userInterfaces, "JDK proxy must implement one or more interfaces");
/* 186 */     return userInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equalsInProxy(AdvisedSupport a, AdvisedSupport b) {
/* 195 */     return (a == b || (
/* 196 */       equalsProxiedInterfaces(a, b) && equalsAdvisors(a, b) && a.getTargetSource().equals(b.getTargetSource())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equalsProxiedInterfaces(AdvisedSupport a, AdvisedSupport b) {
/* 203 */     return Arrays.equals((Object[])a.getProxiedInterfaces(), (Object[])b.getProxiedInterfaces());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equalsAdvisors(AdvisedSupport a, AdvisedSupport b) {
/* 210 */     return Arrays.equals((Object[])a.getAdvisors(), (Object[])b.getAdvisors());
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
/*     */   static Object[] adaptArgumentsIfNecessary(Method method, @Nullable Object[] arguments) {
/* 224 */     if (ObjectUtils.isEmpty(arguments)) {
/* 225 */       return new Object[0];
/*     */     }
/* 227 */     if (method.isVarArgs()) {
/* 228 */       Class<?>[] paramTypes = method.getParameterTypes();
/* 229 */       if (paramTypes.length == arguments.length) {
/* 230 */         int varargIndex = paramTypes.length - 1;
/* 231 */         Class<?> varargType = paramTypes[varargIndex];
/* 232 */         if (varargType.isArray()) {
/* 233 */           Object varargArray = arguments[varargIndex];
/* 234 */           if (varargArray instanceof Object[] && !varargType.isInstance(varargArray)) {
/* 235 */             Object[] newArguments = new Object[arguments.length];
/* 236 */             System.arraycopy(arguments, 0, newArguments, 0, varargIndex);
/* 237 */             Class<?> targetElementType = varargType.getComponentType();
/* 238 */             int varargLength = Array.getLength(varargArray);
/* 239 */             Object newVarargArray = Array.newInstance(targetElementType, varargLength);
/* 240 */             System.arraycopy(varargArray, 0, newVarargArray, 0, varargLength);
/* 241 */             newArguments[varargIndex] = newVarargArray;
/* 242 */             return newArguments;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 247 */     return arguments;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/AopProxyUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */