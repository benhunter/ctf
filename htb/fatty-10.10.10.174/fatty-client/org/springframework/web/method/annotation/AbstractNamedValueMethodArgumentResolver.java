/*     */ package org.springframework.web.method.annotation;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.ServletException;
/*     */ import org.springframework.beans.ConversionNotSupportedException;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*     */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.Scope;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.bind.ServletRequestBindingException;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.RequestScope;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractNamedValueMethodArgumentResolver
/*     */   implements HandlerMethodArgumentResolver
/*     */ {
/*     */   @Nullable
/*     */   private final ConfigurableBeanFactory configurableBeanFactory;
/*     */   @Nullable
/*     */   private final BeanExpressionContext expressionContext;
/*  72 */   private final Map<MethodParameter, NamedValueInfo> namedValueInfoCache = new ConcurrentHashMap<>(256);
/*     */ 
/*     */   
/*     */   public AbstractNamedValueMethodArgumentResolver() {
/*  76 */     this.configurableBeanFactory = null;
/*  77 */     this.expressionContext = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractNamedValueMethodArgumentResolver(@Nullable ConfigurableBeanFactory beanFactory) {
/*  87 */     this.configurableBeanFactory = beanFactory;
/*  88 */     this.expressionContext = (beanFactory != null) ? new BeanExpressionContext(beanFactory, (Scope)new RequestScope()) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
/*  98 */     NamedValueInfo namedValueInfo = getNamedValueInfo(parameter);
/*  99 */     MethodParameter nestedParameter = parameter.nestedIfOptional();
/*     */     
/* 101 */     Object resolvedName = resolveStringValue(namedValueInfo.name);
/* 102 */     if (resolvedName == null) {
/* 103 */       throw new IllegalArgumentException("Specified name must not resolve to null: [" + namedValueInfo
/* 104 */           .name + "]");
/*     */     }
/*     */     
/* 107 */     Object arg = resolveName(resolvedName.toString(), nestedParameter, webRequest);
/* 108 */     if (arg == null) {
/* 109 */       if (namedValueInfo.defaultValue != null) {
/* 110 */         arg = resolveStringValue(namedValueInfo.defaultValue);
/*     */       }
/* 112 */       else if (namedValueInfo.required && !nestedParameter.isOptional()) {
/* 113 */         handleMissingValue(namedValueInfo.name, nestedParameter, webRequest);
/*     */       } 
/* 115 */       arg = handleNullValue(namedValueInfo.name, arg, nestedParameter.getNestedParameterType());
/*     */     }
/* 117 */     else if ("".equals(arg) && namedValueInfo.defaultValue != null) {
/* 118 */       arg = resolveStringValue(namedValueInfo.defaultValue);
/*     */     } 
/*     */     
/* 121 */     if (binderFactory != null) {
/* 122 */       WebDataBinder binder = binderFactory.createBinder(webRequest, null, namedValueInfo.name);
/*     */       try {
/* 124 */         arg = binder.convertIfNecessary(arg, parameter.getParameterType(), parameter);
/*     */       }
/* 126 */       catch (ConversionNotSupportedException ex) {
/* 127 */         throw new MethodArgumentConversionNotSupportedException(arg, ex.getRequiredType(), namedValueInfo
/* 128 */             .name, parameter, ex.getCause());
/*     */       }
/* 130 */       catch (TypeMismatchException ex) {
/* 131 */         throw new MethodArgumentTypeMismatchException(arg, ex.getRequiredType(), namedValueInfo
/* 132 */             .name, parameter, ex.getCause());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 137 */     handleResolvedValue(arg, namedValueInfo.name, parameter, mavContainer, webRequest);
/*     */     
/* 139 */     return arg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NamedValueInfo getNamedValueInfo(MethodParameter parameter) {
/* 146 */     NamedValueInfo namedValueInfo = this.namedValueInfoCache.get(parameter);
/* 147 */     if (namedValueInfo == null) {
/* 148 */       namedValueInfo = createNamedValueInfo(parameter);
/* 149 */       namedValueInfo = updateNamedValueInfo(parameter, namedValueInfo);
/* 150 */       this.namedValueInfoCache.put(parameter, namedValueInfo);
/*     */     } 
/* 152 */     return namedValueInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract NamedValueInfo createNamedValueInfo(MethodParameter paramMethodParameter);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NamedValueInfo updateNamedValueInfo(MethodParameter parameter, NamedValueInfo info) {
/* 167 */     String name = info.name;
/* 168 */     if (info.name.isEmpty()) {
/* 169 */       name = parameter.getParameterName();
/* 170 */       if (name == null) {
/* 171 */         throw new IllegalArgumentException("Name for argument type [" + parameter
/* 172 */             .getNestedParameterType().getName() + "] not available, and parameter name information not found in class file either.");
/*     */       }
/*     */     } 
/*     */     
/* 176 */     String defaultValue = "\n\t\t\n\t\t\n\n\t\t\t\t\n".equals(info.defaultValue) ? null : info.defaultValue;
/* 177 */     return new NamedValueInfo(name, info.required, defaultValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object resolveStringValue(String value) {
/* 186 */     if (this.configurableBeanFactory == null) {
/* 187 */       return value;
/*     */     }
/* 189 */     String placeholdersResolved = this.configurableBeanFactory.resolveEmbeddedValue(value);
/* 190 */     BeanExpressionResolver exprResolver = this.configurableBeanFactory.getBeanExpressionResolver();
/* 191 */     if (exprResolver == null || this.expressionContext == null) {
/* 192 */       return value;
/*     */     }
/* 194 */     return exprResolver.evaluate(placeholdersResolved, this.expressionContext);
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
/*     */   protected abstract Object resolveName(String paramString, MethodParameter paramMethodParameter, NativeWebRequest paramNativeWebRequest) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleMissingValue(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
/* 221 */     handleMissingValue(name, parameter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleMissingValue(String name, MethodParameter parameter) throws ServletException {
/* 231 */     throw new ServletRequestBindingException("Missing argument '" + name + "' for method parameter of type " + parameter
/* 232 */         .getNestedParameterType().getSimpleName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object handleNullValue(String name, @Nullable Object value, Class<?> paramType) {
/* 240 */     if (value == null) {
/* 241 */       if (boolean.class.equals(paramType)) {
/* 242 */         return Boolean.FALSE;
/*     */       }
/* 244 */       if (paramType.isPrimitive()) {
/* 245 */         throw new IllegalStateException("Optional " + paramType.getSimpleName() + " parameter '" + name + "' is present but cannot be translated into a null value due to being declared as a primitive type. Consider declaring it as object wrapper for the corresponding primitive type.");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 250 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleResolvedValue(@Nullable Object arg, String name, MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class NamedValueInfo
/*     */   {
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */     
/*     */     private final boolean required;
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private final String defaultValue;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NamedValueInfo(String name, boolean required, @Nullable String defaultValue) {
/* 279 */       this.name = name;
/* 280 */       this.required = required;
/* 281 */       this.defaultValue = defaultValue;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/annotation/AbstractNamedValueMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */