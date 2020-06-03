/*     */ package org.springframework.scripting.bsh;
/*     */ 
/*     */ import bsh.EvalError;
/*     */ import bsh.Interpreter;
/*     */ import bsh.Primitive;
/*     */ import bsh.XThis;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import org.springframework.core.NestedRuntimeException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ public abstract class BshScriptUtils
/*     */ {
/*     */   public static Object createBshObject(String scriptSource) throws EvalError {
/*  53 */     return createBshObject(scriptSource, null, null);
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
/*     */   public static Object createBshObject(String scriptSource, @Nullable Class<?>... scriptInterfaces) throws EvalError {
/*  72 */     return createBshObject(scriptSource, scriptInterfaces, ClassUtils.getDefaultClassLoader());
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
/*     */   public static Object createBshObject(String scriptSource, @Nullable Class<?>[] scriptInterfaces, @Nullable ClassLoader classLoader) throws EvalError {
/*  92 */     Object result = evaluateBshScript(scriptSource, scriptInterfaces, classLoader);
/*  93 */     if (result instanceof Class) {
/*  94 */       Class<?> clazz = (Class)result;
/*     */       try {
/*  96 */         return ReflectionUtils.accessibleConstructor(clazz, new Class[0]).newInstance(new Object[0]);
/*     */       }
/*  98 */       catch (Throwable ex) {
/*  99 */         throw new IllegalStateException("Could not instantiate script class: " + clazz.getName(), ex);
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     return result;
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
/*     */   @Nullable
/*     */   static Class<?> determineBshObjectType(String scriptSource, @Nullable ClassLoader classLoader) throws EvalError {
/* 120 */     Assert.hasText(scriptSource, "Script source must not be empty");
/* 121 */     Interpreter interpreter = new Interpreter();
/* 122 */     if (classLoader != null) {
/* 123 */       interpreter.setClassLoader(classLoader);
/*     */     }
/* 125 */     Object result = interpreter.eval(scriptSource);
/* 126 */     if (result instanceof Class) {
/* 127 */       return (Class)result;
/*     */     }
/* 129 */     if (result != null) {
/* 130 */       return result.getClass();
/*     */     }
/*     */     
/* 133 */     return null;
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
/*     */   static Object evaluateBshScript(String scriptSource, @Nullable Class<?>[] scriptInterfaces, @Nullable ClassLoader classLoader) throws EvalError {
/* 156 */     Assert.hasText(scriptSource, "Script source must not be empty");
/* 157 */     Interpreter interpreter = new Interpreter();
/* 158 */     interpreter.setClassLoader(classLoader);
/* 159 */     Object result = interpreter.eval(scriptSource);
/* 160 */     if (result != null) {
/* 161 */       return result;
/*     */     }
/*     */ 
/*     */     
/* 165 */     if (ObjectUtils.isEmpty((Object[])scriptInterfaces)) {
/* 166 */       throw new IllegalArgumentException("Given script requires a script proxy: At least one script interface is required.\nScript: " + scriptSource);
/*     */     }
/*     */     
/* 169 */     XThis xt = (XThis)interpreter.eval("return this");
/* 170 */     return Proxy.newProxyInstance(classLoader, scriptInterfaces, new BshObjectInvocationHandler(xt));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class BshObjectInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     private final XThis xt;
/*     */ 
/*     */ 
/*     */     
/*     */     public BshObjectInvocationHandler(XThis xt) {
/* 183 */       this.xt = xt;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 189 */       if (ReflectionUtils.isEqualsMethod(method)) {
/* 190 */         return Boolean.valueOf(isProxyForSameBshObject(args[0]));
/*     */       }
/* 192 */       if (ReflectionUtils.isHashCodeMethod(method)) {
/* 193 */         return Integer.valueOf(this.xt.hashCode());
/*     */       }
/* 195 */       if (ReflectionUtils.isToStringMethod(method)) {
/* 196 */         return "BeanShell object [" + this.xt + "]";
/*     */       }
/*     */       try {
/* 199 */         Object result = this.xt.invokeMethod(method.getName(), args);
/* 200 */         if (result == Primitive.NULL || result == Primitive.VOID) {
/* 201 */           return null;
/*     */         }
/* 203 */         if (result instanceof Primitive) {
/* 204 */           return ((Primitive)result).getValue();
/*     */         }
/* 206 */         return result;
/*     */       }
/* 208 */       catch (EvalError ex) {
/* 209 */         throw new BshScriptUtils.BshExecutionException(ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean isProxyForSameBshObject(Object other) {
/* 214 */       if (!Proxy.isProxyClass(other.getClass())) {
/* 215 */         return false;
/*     */       }
/* 217 */       InvocationHandler ih = Proxy.getInvocationHandler(other);
/* 218 */       return (ih instanceof BshObjectInvocationHandler && this.xt
/* 219 */         .equals(((BshObjectInvocationHandler)ih).xt));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class BshExecutionException
/*     */     extends NestedRuntimeException
/*     */   {
/*     */     private BshExecutionException(EvalError ex) {
/* 231 */       super("BeanShell script execution failed", (Throwable)ex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/bsh/BshScriptUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */