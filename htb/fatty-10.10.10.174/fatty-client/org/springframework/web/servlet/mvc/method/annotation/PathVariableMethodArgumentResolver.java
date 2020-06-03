/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.MissingPathVariableException;
/*     */ import org.springframework.web.bind.ServletRequestBindingException;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.method.support.UriComponentsContributor;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathVariableMethodArgumentResolver
/*     */   extends AbstractNamedValueMethodArgumentResolver
/*     */   implements UriComponentsContributor
/*     */ {
/*  68 */   private static final TypeDescriptor STRING_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(String.class);
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsParameter(MethodParameter parameter) {
/*  73 */     if (!parameter.hasParameterAnnotation(PathVariable.class)) {
/*  74 */       return false;
/*     */     }
/*  76 */     if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
/*  77 */       PathVariable pathVariable = (PathVariable)parameter.getParameterAnnotation(PathVariable.class);
/*  78 */       return (pathVariable != null && StringUtils.hasText(pathVariable.value()));
/*     */     } 
/*  80 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
/*  85 */     PathVariable ann = (PathVariable)parameter.getParameterAnnotation(PathVariable.class);
/*  86 */     Assert.state((ann != null), "No PathVariable annotation");
/*  87 */     return new PathVariableNamedValueInfo(ann);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
/*  94 */     Map<String, String> uriTemplateVars = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, 0);
/*     */     
/*  96 */     return (uriTemplateVars != null) ? uriTemplateVars.get(name) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleMissingValue(String name, MethodParameter parameter) throws ServletRequestBindingException {
/* 101 */     throw new MissingPathVariableException(name, parameter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleResolvedValue(@Nullable Object arg, String name, MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest request) {
/* 109 */     String key = View.PATH_VARIABLES;
/* 110 */     int scope = 0;
/* 111 */     Map<String, Object> pathVars = (Map<String, Object>)request.getAttribute(key, scope);
/* 112 */     if (pathVars == null) {
/* 113 */       pathVars = new HashMap<>();
/* 114 */       request.setAttribute(key, pathVars, scope);
/*     */     } 
/* 116 */     pathVars.put(name, arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void contributeMethodArgument(MethodParameter parameter, Object value, UriComponentsBuilder builder, Map<String, Object> uriVariables, ConversionService conversionService) {
/* 123 */     if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
/*     */       return;
/*     */     }
/*     */     
/* 127 */     PathVariable ann = (PathVariable)parameter.getParameterAnnotation(PathVariable.class);
/* 128 */     String name = (ann != null && StringUtils.hasLength(ann.value())) ? ann.value() : parameter.getParameterName();
/* 129 */     String formatted = formatUriValue(conversionService, new TypeDescriptor(parameter.nestedIfOptional()), value);
/* 130 */     uriVariables.put(name, formatted);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected String formatUriValue(@Nullable ConversionService cs, @Nullable TypeDescriptor sourceType, Object value) {
/* 135 */     if (value instanceof String) {
/* 136 */       return (String)value;
/*     */     }
/* 138 */     if (cs != null) {
/* 139 */       return (String)cs.convert(value, sourceType, STRING_TYPE_DESCRIPTOR);
/*     */     }
/*     */     
/* 142 */     return value.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class PathVariableNamedValueInfo
/*     */     extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo
/*     */   {
/*     */     public PathVariableNamedValueInfo(PathVariable annotation) {
/* 150 */       super(annotation.name(), annotation.required(), "\n\t\t\n\t\t\n\n\t\t\t\t\n");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/PathVariableMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */