/*     */ package org.springframework.web.method.support;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InvocableHandlerMethod
/*     */   extends HandlerMethod
/*     */ {
/*  46 */   private static final Object[] EMPTY_ARGS = new Object[0];
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private WebDataBinderFactory dataBinderFactory;
/*     */   
/*  52 */   private HandlerMethodArgumentResolverComposite resolvers = new HandlerMethodArgumentResolverComposite();
/*     */   
/*  54 */   private ParameterNameDiscoverer parameterNameDiscoverer = (ParameterNameDiscoverer)new DefaultParameterNameDiscoverer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InvocableHandlerMethod(HandlerMethod handlerMethod) {
/*  61 */     super(handlerMethod);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InvocableHandlerMethod(Object bean, Method method) {
/*  68 */     super(bean, method);
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
/*     */   public InvocableHandlerMethod(Object bean, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
/*  81 */     super(bean, methodName, parameterTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDataBinderFactory(WebDataBinderFactory dataBinderFactory) {
/*  91 */     this.dataBinderFactory = dataBinderFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandlerMethodArgumentResolvers(HandlerMethodArgumentResolverComposite argumentResolvers) {
/*  98 */     this.resolvers = argumentResolvers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
/* 107 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
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
/*     */   
/*     */   @Nullable
/*     */   public Object invokeForRequest(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {
/* 134 */     Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
/* 135 */     if (this.logger.isTraceEnabled()) {
/* 136 */       this.logger.trace("Arguments: " + Arrays.toString(args));
/*     */     }
/* 138 */     return doInvoke(args);
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
/*     */   protected Object[] getMethodArgumentValues(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {
/* 150 */     MethodParameter[] parameters = getMethodParameters();
/* 151 */     if (ObjectUtils.isEmpty((Object[])parameters)) {
/* 152 */       return EMPTY_ARGS;
/*     */     }
/*     */     
/* 155 */     Object[] args = new Object[parameters.length];
/* 156 */     for (int i = 0; i < parameters.length; i++) {
/* 157 */       MethodParameter parameter = parameters[i];
/* 158 */       parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
/* 159 */       args[i] = findProvidedArgument(parameter, providedArgs);
/* 160 */       if (args[i] == null) {
/*     */ 
/*     */         
/* 163 */         if (!this.resolvers.supportsParameter(parameter)) {
/* 164 */           throw new IllegalStateException(formatArgumentError(parameter, "No suitable resolver"));
/*     */         }
/*     */         try {
/* 167 */           args[i] = this.resolvers.resolveArgument(parameter, mavContainer, request, this.dataBinderFactory);
/*     */         }
/* 169 */         catch (Exception ex) {
/*     */           
/* 171 */           if (this.logger.isDebugEnabled()) {
/* 172 */             String exMsg = ex.getMessage();
/* 173 */             if (exMsg != null && !exMsg.contains(parameter.getExecutable().toGenericString())) {
/* 174 */               this.logger.debug(formatArgumentError(parameter, exMsg));
/*     */             }
/*     */           } 
/* 177 */           throw ex;
/*     */         } 
/*     */       } 
/* 180 */     }  return args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object doInvoke(Object... args) throws Exception {
/* 188 */     ReflectionUtils.makeAccessible(getBridgedMethod());
/*     */     try {
/* 190 */       return getBridgedMethod().invoke(getBean(), args);
/*     */     }
/* 192 */     catch (IllegalArgumentException ex) {
/* 193 */       assertTargetBean(getBridgedMethod(), getBean(), args);
/* 194 */       String text = (ex.getMessage() != null) ? ex.getMessage() : "Illegal argument";
/* 195 */       throw new IllegalStateException(formatInvokeError(text, args), ex);
/*     */     }
/* 197 */     catch (InvocationTargetException ex) {
/*     */       
/* 199 */       Throwable targetException = ex.getTargetException();
/* 200 */       if (targetException instanceof RuntimeException) {
/* 201 */         throw (RuntimeException)targetException;
/*     */       }
/* 203 */       if (targetException instanceof Error) {
/* 204 */         throw (Error)targetException;
/*     */       }
/* 206 */       if (targetException instanceof Exception) {
/* 207 */         throw (Exception)targetException;
/*     */       }
/*     */       
/* 210 */       throw new IllegalStateException(formatInvokeError("Invocation failure", args), targetException);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/support/InvocableHandlerMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */