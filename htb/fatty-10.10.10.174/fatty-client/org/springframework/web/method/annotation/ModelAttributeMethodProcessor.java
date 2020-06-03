/*     */ package org.springframework.web.method.annotation;
/*     */ 
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.PropertyAccessException;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.validation.BindException;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.SmartValidator;
/*     */ import org.springframework.validation.Validator;
/*     */ import org.springframework.validation.annotation.Validated;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.bind.annotation.ModelAttribute;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.bind.support.WebRequestDataBinder;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelAttributeMethodProcessor
/*     */   implements HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler
/*     */ {
/*  78 */   private static final ParameterNameDiscoverer parameterNameDiscoverer = (ParameterNameDiscoverer)new DefaultParameterNameDiscoverer();
/*     */   
/*  80 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean annotationNotRequired;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelAttributeMethodProcessor(boolean annotationNotRequired) {
/*  92 */     this.annotationNotRequired = annotationNotRequired;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsParameter(MethodParameter parameter) {
/* 103 */     return (parameter.hasParameterAnnotation(ModelAttribute.class) || (this.annotationNotRequired && 
/* 104 */       !BeanUtils.isSimpleProperty(parameter.getParameterType())));
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
/*     */   public final Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
/* 121 */     Assert.state((mavContainer != null), "ModelAttributeMethodProcessor requires ModelAndViewContainer");
/* 122 */     Assert.state((binderFactory != null), "ModelAttributeMethodProcessor requires WebDataBinderFactory");
/*     */     
/* 124 */     String name = ModelFactory.getNameForParameter(parameter);
/* 125 */     ModelAttribute ann = (ModelAttribute)parameter.getParameterAnnotation(ModelAttribute.class);
/* 126 */     if (ann != null) {
/* 127 */       mavContainer.setBinding(name, ann.binding());
/*     */     }
/*     */     
/* 130 */     Object<?> attribute = null;
/* 131 */     BindingResult bindingResult = null;
/*     */     
/* 133 */     if (mavContainer.containsAttribute(name)) {
/* 134 */       attribute = (Object<?>)mavContainer.getModel().get(name);
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/* 139 */         attribute = (Object<?>)createAttribute(name, parameter, binderFactory, webRequest);
/*     */       }
/* 141 */       catch (BindException ex) {
/* 142 */         if (isBindExceptionRequired(parameter))
/*     */         {
/* 144 */           throw ex;
/*     */         }
/*     */         
/* 147 */         if (parameter.getParameterType() == Optional.class) {
/* 148 */           attribute = Optional.empty();
/*     */         }
/* 150 */         bindingResult = ex.getBindingResult();
/*     */       } 
/*     */     } 
/*     */     
/* 154 */     if (bindingResult == null) {
/*     */ 
/*     */       
/* 157 */       WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name);
/* 158 */       if (binder.getTarget() != null) {
/* 159 */         if (!mavContainer.isBindingDisabled(name)) {
/* 160 */           bindRequestParameters(binder, webRequest);
/*     */         }
/* 162 */         validateIfApplicable(binder, parameter);
/* 163 */         if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
/* 164 */           throw new BindException(binder.getBindingResult());
/*     */         }
/*     */       } 
/*     */       
/* 168 */       if (!parameter.getParameterType().isInstance(attribute)) {
/* 169 */         attribute = (Object<?>)binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType(), parameter);
/*     */       }
/* 171 */       bindingResult = binder.getBindingResult();
/*     */     } 
/*     */ 
/*     */     
/* 175 */     Map<String, Object> bindingResultModel = bindingResult.getModel();
/* 176 */     mavContainer.removeAttributes(bindingResultModel);
/* 177 */     mavContainer.addAllAttributes(bindingResultModel);
/*     */     
/* 179 */     return attribute;
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
/*     */   protected Object createAttribute(String attributeName, MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest webRequest) throws Exception {
/* 205 */     MethodParameter nestedParameter = parameter.nestedIfOptional();
/* 206 */     Class<?> clazz = nestedParameter.getNestedParameterType();
/*     */     
/* 208 */     Constructor<?> ctor = BeanUtils.findPrimaryConstructor(clazz);
/* 209 */     if (ctor == null) {
/* 210 */       Constructor[] arrayOfConstructor = (Constructor[])clazz.getConstructors();
/* 211 */       if (arrayOfConstructor.length == 1) {
/* 212 */         ctor = arrayOfConstructor[0];
/*     */       } else {
/*     */         
/*     */         try {
/* 216 */           ctor = clazz.getDeclaredConstructor(new Class[0]);
/*     */         }
/* 218 */         catch (NoSuchMethodException ex) {
/* 219 */           throw new IllegalStateException("No primary or default constructor found for " + clazz, ex);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 224 */     Object attribute = constructAttribute(ctor, attributeName, parameter, binderFactory, webRequest);
/* 225 */     if (parameter != nestedParameter) {
/* 226 */       attribute = Optional.of(attribute);
/*     */     }
/* 228 */     return attribute;
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
/*     */   protected Object constructAttribute(Constructor<?> ctor, String attributeName, MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest webRequest) throws Exception {
/* 249 */     Object constructed = constructAttribute(ctor, attributeName, binderFactory, webRequest);
/* 250 */     if (constructed != null) {
/* 251 */       return constructed;
/*     */     }
/*     */     
/* 254 */     if (ctor.getParameterCount() == 0)
/*     */     {
/* 256 */       return BeanUtils.instantiateClass(ctor, new Object[0]);
/*     */     }
/*     */ 
/*     */     
/* 260 */     ConstructorProperties cp = ctor.<ConstructorProperties>getAnnotation(ConstructorProperties.class);
/* 261 */     String[] paramNames = (cp != null) ? cp.value() : parameterNameDiscoverer.getParameterNames(ctor);
/* 262 */     Assert.state((paramNames != null), () -> "Cannot resolve parameter names for constructor " + ctor);
/* 263 */     Class<?>[] paramTypes = ctor.getParameterTypes();
/* 264 */     Assert.state((paramNames.length == paramTypes.length), () -> "Invalid number of parameter names: " + paramNames.length + " for constructor " + ctor);
/*     */ 
/*     */     
/* 267 */     Object[] args = new Object[paramTypes.length];
/* 268 */     WebDataBinder binder = binderFactory.createBinder(webRequest, null, attributeName);
/* 269 */     String fieldDefaultPrefix = binder.getFieldDefaultPrefix();
/* 270 */     String fieldMarkerPrefix = binder.getFieldMarkerPrefix();
/* 271 */     boolean bindingFailure = false;
/* 272 */     Set<String> failedParams = new HashSet<>(4);
/*     */     
/* 274 */     for (int i = 0; i < paramNames.length; i++) {
/* 275 */       String paramName = paramNames[i];
/* 276 */       Class<?> paramType = paramTypes[i];
/* 277 */       Object value = webRequest.getParameterValues(paramName);
/* 278 */       if (value == null) {
/* 279 */         if (fieldDefaultPrefix != null) {
/* 280 */           value = webRequest.getParameter(fieldDefaultPrefix + paramName);
/*     */         }
/* 282 */         if (value == null && fieldMarkerPrefix != null && 
/* 283 */           webRequest.getParameter(fieldMarkerPrefix + paramName) != null) {
/* 284 */           value = binder.getEmptyValue(paramType);
/*     */         }
/*     */       } 
/*     */       
/*     */       try {
/* 289 */         MethodParameter methodParam = new FieldAwareConstructorParameter(ctor, i, paramName);
/* 290 */         if (value == null && methodParam.isOptional()) {
/* 291 */           args[i] = (methodParam.getParameterType() == Optional.class) ? Optional.empty() : null;
/*     */         } else {
/*     */           
/* 294 */           args[i] = binder.convertIfNecessary(value, paramType, methodParam);
/*     */         }
/*     */       
/* 297 */       } catch (TypeMismatchException ex) {
/* 298 */         ex.initPropertyName(paramName);
/* 299 */         args[i] = value;
/* 300 */         failedParams.add(paramName);
/* 301 */         binder.getBindingResult().recordFieldValue(paramName, paramType, value);
/* 302 */         binder.getBindingErrorProcessor().processPropertyAccessException((PropertyAccessException)ex, binder.getBindingResult());
/* 303 */         bindingFailure = true;
/*     */       } 
/*     */     } 
/*     */     
/* 307 */     if (bindingFailure) {
/* 308 */       BindingResult result = binder.getBindingResult();
/* 309 */       for (int j = 0; j < paramNames.length; j++) {
/* 310 */         String paramName = paramNames[j];
/* 311 */         if (!failedParams.contains(paramName)) {
/* 312 */           Object value = args[j];
/* 313 */           result.recordFieldValue(paramName, paramTypes[j], value);
/* 314 */           validateValueIfApplicable(binder, parameter, ctor.getDeclaringClass(), paramName, value);
/*     */         } 
/*     */       } 
/* 317 */       throw new BindException(result);
/*     */     } 
/*     */     
/* 320 */     return BeanUtils.instantiateClass(ctor, args);
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
/*     */   @Deprecated
/*     */   @Nullable
/*     */   protected Object constructAttribute(Constructor<?> ctor, String attributeName, WebDataBinderFactory binderFactory, NativeWebRequest webRequest) throws Exception {
/* 334 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
/* 343 */     ((WebRequestDataBinder)binder).bind((WebRequest)request);
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
/*     */   protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
/* 357 */     for (Annotation ann : parameter.getParameterAnnotations()) {
/* 358 */       Object[] validationHints = determineValidationHints(ann);
/* 359 */       if (validationHints != null) {
/* 360 */         binder.validate(validationHints);
/*     */         break;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validateValueIfApplicable(WebDataBinder binder, MethodParameter parameter, Class<?> targetType, String fieldName, @Nullable Object value) {
/* 383 */     for (Annotation ann : parameter.getParameterAnnotations()) {
/* 384 */       Object[] validationHints = determineValidationHints(ann);
/* 385 */       if (validationHints != null) {
/* 386 */         for (Validator validator : binder.getValidators()) {
/* 387 */           if (validator instanceof SmartValidator) {
/*     */             try {
/* 389 */               ((SmartValidator)validator).validateValue(targetType, fieldName, value, (Errors)binder
/* 390 */                   .getBindingResult(), validationHints);
/*     */             }
/* 392 */             catch (IllegalArgumentException illegalArgumentException) {}
/*     */           }
/*     */         } 
/*     */         break;
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
/*     */   
/*     */   @Nullable
/*     */   private Object[] determineValidationHints(Annotation ann) {
/* 411 */     Validated validatedAnn = (Validated)AnnotationUtils.getAnnotation(ann, Validated.class);
/* 412 */     if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
/* 413 */       Object hints = (validatedAnn != null) ? validatedAnn.value() : AnnotationUtils.getValue(ann);
/* 414 */       if (hints == null) {
/* 415 */         return new Object[0];
/*     */       }
/* 417 */       (new Object[1])[0] = hints; return (hints instanceof Object[]) ? (Object[])hints : new Object[1];
/*     */     } 
/* 419 */     return null;
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
/*     */   protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
/* 431 */     return isBindExceptionRequired(parameter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isBindExceptionRequired(MethodParameter parameter) {
/* 441 */     int i = parameter.getParameterIndex();
/* 442 */     Class<?>[] paramTypes = parameter.getExecutable().getParameterTypes();
/* 443 */     boolean hasBindingResult = (paramTypes.length > i + 1 && Errors.class.isAssignableFrom(paramTypes[i + 1]));
/* 444 */     return !hasBindingResult;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsReturnType(MethodParameter returnType) {
/* 454 */     return (returnType.hasMethodAnnotation(ModelAttribute.class) || (this.annotationNotRequired && 
/* 455 */       !BeanUtils.isSimpleProperty(returnType.getParameterType())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
/* 465 */     if (returnValue != null) {
/* 466 */       String name = ModelFactory.getNameForReturnValue(returnValue, returnType);
/* 467 */       mavContainer.addAttribute(name, returnValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FieldAwareConstructorParameter
/*     */     extends MethodParameter
/*     */   {
/*     */     private final String parameterName;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private volatile Annotation[] combinedAnnotations;
/*     */ 
/*     */     
/*     */     public FieldAwareConstructorParameter(Constructor<?> constructor, int parameterIndex, String parameterName) {
/* 484 */       super(constructor, parameterIndex);
/* 485 */       this.parameterName = parameterName;
/*     */     }
/*     */ 
/*     */     
/*     */     public Annotation[] getParameterAnnotations() {
/* 490 */       Annotation[] anns = this.combinedAnnotations;
/* 491 */       if (anns == null) {
/* 492 */         anns = super.getParameterAnnotations();
/*     */         try {
/* 494 */           Field field = getDeclaringClass().getDeclaredField(this.parameterName);
/* 495 */           Annotation[] fieldAnns = field.getAnnotations();
/* 496 */           if (fieldAnns.length > 0) {
/* 497 */             List<Annotation> merged = new ArrayList<>(anns.length + fieldAnns.length);
/* 498 */             merged.addAll(Arrays.asList(anns));
/* 499 */             for (Annotation fieldAnn : fieldAnns) {
/* 500 */               boolean existingType = false;
/* 501 */               for (Annotation ann : anns) {
/* 502 */                 if (ann.annotationType() == fieldAnn.annotationType()) {
/* 503 */                   existingType = true;
/*     */                   break;
/*     */                 } 
/*     */               } 
/* 507 */               if (!existingType) {
/* 508 */                 merged.add(fieldAnn);
/*     */               }
/*     */             } 
/* 511 */             anns = merged.<Annotation>toArray(new Annotation[0]);
/*     */           }
/*     */         
/* 514 */         } catch (NoSuchFieldException|SecurityException noSuchFieldException) {}
/*     */ 
/*     */         
/* 517 */         this.combinedAnnotations = anns;
/*     */       } 
/* 519 */       return anns;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getParameterName() {
/* 524 */       return this.parameterName;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/annotation/ModelAttributeMethodProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */