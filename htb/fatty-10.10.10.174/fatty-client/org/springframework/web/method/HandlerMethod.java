/*     */ package org.springframework.web.method;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.SynthesizingMethodParameter;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.annotation.ResponseStatus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HandlerMethod
/*     */ {
/*  65 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private final Object bean;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final BeanFactory beanFactory;
/*     */ 
/*     */   
/*     */   private final Class<?> beanType;
/*     */   
/*     */   private final Method method;
/*     */   
/*     */   private final Method bridgedMethod;
/*     */   
/*     */   private final MethodParameter[] parameters;
/*     */   
/*     */   @Nullable
/*     */   private HttpStatus responseStatus;
/*     */   
/*     */   @Nullable
/*     */   private String responseStatusReason;
/*     */   
/*     */   @Nullable
/*     */   private HandlerMethod resolvedFromHandlerMethod;
/*     */   
/*     */   @Nullable
/*     */   private volatile List<Annotation[][]> interfaceParameterAnnotations;
/*     */ 
/*     */   
/*     */   public HandlerMethod(Object bean, Method method) {
/*  97 */     Assert.notNull(bean, "Bean is required");
/*  98 */     Assert.notNull(method, "Method is required");
/*  99 */     this.bean = bean;
/* 100 */     this.beanFactory = null;
/* 101 */     this.beanType = ClassUtils.getUserClass(bean);
/* 102 */     this.method = method;
/* 103 */     this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
/* 104 */     this.parameters = initMethodParameters();
/* 105 */     evaluateResponseStatus();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethod(Object bean, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
/* 113 */     Assert.notNull(bean, "Bean is required");
/* 114 */     Assert.notNull(methodName, "Method name is required");
/* 115 */     this.bean = bean;
/* 116 */     this.beanFactory = null;
/* 117 */     this.beanType = ClassUtils.getUserClass(bean);
/* 118 */     this.method = bean.getClass().getMethod(methodName, parameterTypes);
/* 119 */     this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(this.method);
/* 120 */     this.parameters = initMethodParameters();
/* 121 */     evaluateResponseStatus();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethod(String beanName, BeanFactory beanFactory, Method method) {
/* 130 */     Assert.hasText(beanName, "Bean name is required");
/* 131 */     Assert.notNull(beanFactory, "BeanFactory is required");
/* 132 */     Assert.notNull(method, "Method is required");
/* 133 */     this.bean = beanName;
/* 134 */     this.beanFactory = beanFactory;
/* 135 */     Class<?> beanType = beanFactory.getType(beanName);
/* 136 */     if (beanType == null) {
/* 137 */       throw new IllegalStateException("Cannot resolve bean type for bean with name '" + beanName + "'");
/*     */     }
/* 139 */     this.beanType = ClassUtils.getUserClass(beanType);
/* 140 */     this.method = method;
/* 141 */     this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
/* 142 */     this.parameters = initMethodParameters();
/* 143 */     evaluateResponseStatus();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HandlerMethod(HandlerMethod handlerMethod) {
/* 150 */     Assert.notNull(handlerMethod, "HandlerMethod is required");
/* 151 */     this.bean = handlerMethod.bean;
/* 152 */     this.beanFactory = handlerMethod.beanFactory;
/* 153 */     this.beanType = handlerMethod.beanType;
/* 154 */     this.method = handlerMethod.method;
/* 155 */     this.bridgedMethod = handlerMethod.bridgedMethod;
/* 156 */     this.parameters = handlerMethod.parameters;
/* 157 */     this.responseStatus = handlerMethod.responseStatus;
/* 158 */     this.responseStatusReason = handlerMethod.responseStatusReason;
/* 159 */     this.resolvedFromHandlerMethod = handlerMethod.resolvedFromHandlerMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HandlerMethod(HandlerMethod handlerMethod, Object handler) {
/* 166 */     Assert.notNull(handlerMethod, "HandlerMethod is required");
/* 167 */     Assert.notNull(handler, "Handler object is required");
/* 168 */     this.bean = handler;
/* 169 */     this.beanFactory = handlerMethod.beanFactory;
/* 170 */     this.beanType = handlerMethod.beanType;
/* 171 */     this.method = handlerMethod.method;
/* 172 */     this.bridgedMethod = handlerMethod.bridgedMethod;
/* 173 */     this.parameters = handlerMethod.parameters;
/* 174 */     this.responseStatus = handlerMethod.responseStatus;
/* 175 */     this.responseStatusReason = handlerMethod.responseStatusReason;
/* 176 */     this.resolvedFromHandlerMethod = handlerMethod;
/*     */   }
/*     */   
/*     */   private MethodParameter[] initMethodParameters() {
/* 180 */     int count = this.bridgedMethod.getParameterCount();
/* 181 */     MethodParameter[] result = new MethodParameter[count];
/* 182 */     for (int i = 0; i < count; i++) {
/* 183 */       HandlerMethodParameter parameter = new HandlerMethodParameter(i);
/* 184 */       GenericTypeResolver.resolveParameterType((MethodParameter)parameter, this.beanType);
/* 185 */       result[i] = (MethodParameter)parameter;
/*     */     } 
/* 187 */     return result;
/*     */   }
/*     */   
/*     */   private void evaluateResponseStatus() {
/* 191 */     ResponseStatus annotation = getMethodAnnotation(ResponseStatus.class);
/* 192 */     if (annotation == null) {
/* 193 */       annotation = (ResponseStatus)AnnotatedElementUtils.findMergedAnnotation(getBeanType(), ResponseStatus.class);
/*     */     }
/* 195 */     if (annotation != null) {
/* 196 */       this.responseStatus = annotation.code();
/* 197 */       this.responseStatusReason = annotation.reason();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getBean() {
/* 206 */     return this.bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Method getMethod() {
/* 213 */     return this.method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getBeanType() {
/* 222 */     return this.beanType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Method getBridgedMethod() {
/* 230 */     return this.bridgedMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter[] getMethodParameters() {
/* 237 */     return this.parameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected HttpStatus getResponseStatus() {
/* 247 */     return this.responseStatus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getResponseStatusReason() {
/* 257 */     return this.responseStatusReason;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter getReturnType() {
/* 264 */     return (MethodParameter)new HandlerMethodParameter(-1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter getReturnValueType(@Nullable Object returnValue) {
/* 271 */     return (MethodParameter)new ReturnValueMethodParameter(returnValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVoid() {
/* 278 */     return void.class.equals(getReturnType().getParameterType());
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
/*     */   @Nullable
/*     */   public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType) {
/* 292 */     return (A)AnnotatedElementUtils.findMergedAnnotation(this.method, annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> boolean hasMethodAnnotation(Class<A> annotationType) {
/* 302 */     return AnnotatedElementUtils.hasAnnotation(this.method, annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public HandlerMethod getResolvedFromHandlerMethod() {
/* 311 */     return this.resolvedFromHandlerMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethod createWithResolvedBean() {
/* 319 */     Object handler = this.bean;
/* 320 */     if (this.bean instanceof String) {
/* 321 */       Assert.state((this.beanFactory != null), "Cannot resolve bean name without BeanFactory");
/* 322 */       String beanName = (String)this.bean;
/* 323 */       handler = this.beanFactory.getBean(beanName);
/*     */     } 
/* 325 */     return new HandlerMethod(this, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getShortLogMessage() {
/* 333 */     return getBeanType().getName() + "#" + this.method.getName() + "[" + this.method
/* 334 */       .getParameterCount() + " args]";
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Annotation[][]> getInterfaceParameterAnnotations() {
/* 339 */     List<Annotation[][]> parameterAnnotations = this.interfaceParameterAnnotations;
/* 340 */     if (parameterAnnotations == null) {
/* 341 */       parameterAnnotations = (List)new ArrayList<>();
/* 342 */       for (Class<?> ifc : this.method.getDeclaringClass().getInterfaces()) {
/* 343 */         for (Method candidate : ifc.getMethods()) {
/* 344 */           if (isOverrideFor(candidate)) {
/* 345 */             parameterAnnotations.add(candidate.getParameterAnnotations());
/*     */           }
/*     */         } 
/*     */       } 
/* 349 */       this.interfaceParameterAnnotations = parameterAnnotations;
/*     */     } 
/* 351 */     return parameterAnnotations;
/*     */   }
/*     */   
/*     */   private boolean isOverrideFor(Method candidate) {
/* 355 */     if (!candidate.getName().equals(this.method.getName()) || candidate
/* 356 */       .getParameterCount() != this.method.getParameterCount()) {
/* 357 */       return false;
/*     */     }
/* 359 */     Class<?>[] paramTypes = this.method.getParameterTypes();
/* 360 */     if (Arrays.equals((Object[])candidate.getParameterTypes(), (Object[])paramTypes)) {
/* 361 */       return true;
/*     */     }
/* 363 */     for (int i = 0; i < paramTypes.length; i++) {
/* 364 */       if (paramTypes[i] != 
/* 365 */         ResolvableType.forMethodParameter(candidate, i, this.method.getDeclaringClass()).resolve()) {
/* 366 */         return false;
/*     */       }
/*     */     } 
/* 369 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 375 */     if (this == other) {
/* 376 */       return true;
/*     */     }
/* 378 */     if (!(other instanceof HandlerMethod)) {
/* 379 */       return false;
/*     */     }
/* 381 */     HandlerMethod otherMethod = (HandlerMethod)other;
/* 382 */     return (this.bean.equals(otherMethod.bean) && this.method.equals(otherMethod.method));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 387 */     return this.bean.hashCode() * 31 + this.method.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 392 */     return this.method.toGenericString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected static Object findProvidedArgument(MethodParameter parameter, @Nullable Object... providedArgs) {
/* 400 */     if (!ObjectUtils.isEmpty(providedArgs)) {
/* 401 */       for (Object providedArg : providedArgs) {
/* 402 */         if (parameter.getParameterType().isInstance(providedArg)) {
/* 403 */           return providedArg;
/*     */         }
/*     */       } 
/*     */     }
/* 407 */     return null;
/*     */   }
/*     */   
/*     */   protected static String formatArgumentError(MethodParameter param, String message) {
/* 411 */     return "Could not resolve parameter [" + param.getParameterIndex() + "] in " + param
/* 412 */       .getExecutable().toGenericString() + (StringUtils.hasText(message) ? (": " + message) : "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void assertTargetBean(Method method, Object targetBean, Object[] args) {
/* 423 */     Class<?> methodDeclaringClass = method.getDeclaringClass();
/* 424 */     Class<?> targetBeanClass = targetBean.getClass();
/* 425 */     if (!methodDeclaringClass.isAssignableFrom(targetBeanClass)) {
/*     */ 
/*     */       
/* 428 */       String text = "The mapped handler method class '" + methodDeclaringClass.getName() + "' is not an instance of the actual controller bean class '" + targetBeanClass.getName() + "'. If the controller requires proxying (e.g. due to @Transactional), please use class-based proxying.";
/*     */       
/* 430 */       throw new IllegalStateException(formatInvokeError(text, args));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String formatInvokeError(String text, Object[] args) {
/* 439 */     String formattedArgs = IntStream.range(0, args.length).<CharSequence>mapToObj(i -> (args[i] != null) ? ("[" + i + "] [type=" + args[i].getClass().getName() + "] [value=" + args[i] + "]") : ("[" + i + "] [null]")).collect(Collectors.joining(",\n", " ", " "));
/* 440 */     return text + "\nController [" + 
/* 441 */       getBeanType().getName() + "]\nMethod [" + 
/* 442 */       getBridgedMethod().toGenericString() + "] with argument values:\n" + formattedArgs;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected class HandlerMethodParameter
/*     */     extends SynthesizingMethodParameter
/*     */   {
/*     */     @Nullable
/*     */     private volatile Annotation[] combinedAnnotations;
/*     */ 
/*     */ 
/*     */     
/*     */     public HandlerMethodParameter(int index) {
/* 456 */       super(HandlerMethod.this.bridgedMethod, index);
/*     */     }
/*     */     
/*     */     protected HandlerMethodParameter(HandlerMethodParameter original) {
/* 460 */       super(original);
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getContainingClass() {
/* 465 */       return HandlerMethod.this.getBeanType();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T extends Annotation> T getMethodAnnotation(Class<T> annotationType) {
/* 470 */       return HandlerMethod.this.getMethodAnnotation(annotationType);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T extends Annotation> boolean hasMethodAnnotation(Class<T> annotationType) {
/* 475 */       return HandlerMethod.this.hasMethodAnnotation(annotationType);
/*     */     }
/*     */ 
/*     */     
/*     */     public Annotation[] getParameterAnnotations() {
/* 480 */       Annotation[] anns = this.combinedAnnotations;
/* 481 */       if (anns == null) {
/* 482 */         anns = super.getParameterAnnotations();
/* 483 */         int index = getParameterIndex();
/* 484 */         if (index >= 0) {
/* 485 */           for (Annotation[][] ifcAnns : HandlerMethod.this.getInterfaceParameterAnnotations()) {
/* 486 */             if (index < ifcAnns.length) {
/* 487 */               Annotation[] paramAnns = ifcAnns[index];
/* 488 */               if (paramAnns.length > 0) {
/* 489 */                 List<Annotation> merged = new ArrayList<>(anns.length + paramAnns.length);
/* 490 */                 merged.addAll(Arrays.asList(anns));
/* 491 */                 for (Annotation paramAnn : paramAnns) {
/* 492 */                   boolean existingType = false;
/* 493 */                   for (Annotation ann : anns) {
/* 494 */                     if (ann.annotationType() == paramAnn.annotationType()) {
/* 495 */                       existingType = true;
/*     */                       break;
/*     */                     } 
/*     */                   } 
/* 499 */                   if (!existingType) {
/* 500 */                     merged.add(adaptAnnotation(paramAnn));
/*     */                   }
/*     */                 } 
/* 503 */                 anns = merged.<Annotation>toArray(new Annotation[0]);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/* 508 */         this.combinedAnnotations = anns;
/*     */       } 
/* 510 */       return anns;
/*     */     }
/*     */ 
/*     */     
/*     */     public HandlerMethodParameter clone() {
/* 515 */       return new HandlerMethodParameter(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ReturnValueMethodParameter
/*     */     extends HandlerMethodParameter
/*     */   {
/*     */     @Nullable
/*     */     private final Object returnValue;
/*     */ 
/*     */     
/*     */     public ReturnValueMethodParameter(Object returnValue) {
/* 529 */       super(-1);
/* 530 */       this.returnValue = returnValue;
/*     */     }
/*     */     
/*     */     protected ReturnValueMethodParameter(ReturnValueMethodParameter original) {
/* 534 */       super(original);
/* 535 */       this.returnValue = original.returnValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getParameterType() {
/* 540 */       return (this.returnValue != null) ? this.returnValue.getClass() : super.getParameterType();
/*     */     }
/*     */ 
/*     */     
/*     */     public ReturnValueMethodParameter clone() {
/* 545 */       return new ReturnValueMethodParameter(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/HandlerMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */