/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletRequest;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.ServletRequestDataBinder;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletModelAttributeMethodProcessor
/*     */   extends ModelAttributeMethodProcessor
/*     */ {
/*     */   public ServletModelAttributeMethodProcessor(boolean annotationNotRequired) {
/*  60 */     super(annotationNotRequired);
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
/*     */   protected final Object createAttribute(String attributeName, MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest request) throws Exception {
/*  75 */     String value = getRequestValueForAttribute(attributeName, request);
/*  76 */     if (value != null) {
/*  77 */       Object attribute = createAttributeFromRequestValue(value, attributeName, parameter, binderFactory, request);
/*     */       
/*  79 */       if (attribute != null) {
/*  80 */         return attribute;
/*     */       }
/*     */     } 
/*     */     
/*  84 */     return super.createAttribute(attributeName, parameter, binderFactory, request);
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
/*     */   protected String getRequestValueForAttribute(String attributeName, NativeWebRequest request) {
/*  98 */     Map<String, String> variables = getUriTemplateVariables(request);
/*  99 */     String variableValue = variables.get(attributeName);
/* 100 */     if (StringUtils.hasText(variableValue)) {
/* 101 */       return variableValue;
/*     */     }
/* 103 */     String parameterValue = request.getParameter(attributeName);
/* 104 */     if (StringUtils.hasText(parameterValue)) {
/* 105 */       return parameterValue;
/*     */     }
/* 107 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final Map<String, String> getUriTemplateVariables(NativeWebRequest request) {
/* 112 */     Map<String, String> variables = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, 0);
/*     */     
/* 114 */     return (variables != null) ? variables : Collections.<String, String>emptyMap();
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
/*     */   @Nullable
/*     */   protected Object createAttributeFromRequestValue(String sourceValue, String attributeName, MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest request) throws Exception {
/* 135 */     WebDataBinder webDataBinder = binderFactory.createBinder(request, null, attributeName);
/* 136 */     ConversionService conversionService = webDataBinder.getConversionService();
/* 137 */     if (conversionService != null) {
/* 138 */       TypeDescriptor source = TypeDescriptor.valueOf(String.class);
/* 139 */       TypeDescriptor target = new TypeDescriptor(parameter);
/* 140 */       if (conversionService.canConvert(source, target)) {
/* 141 */         return webDataBinder.convertIfNecessary(sourceValue, parameter.getParameterType(), parameter);
/*     */       }
/*     */     } 
/* 144 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
/* 154 */     ServletRequest servletRequest = (ServletRequest)request.getNativeRequest(ServletRequest.class);
/* 155 */     Assert.state((servletRequest != null), "No ServletRequest");
/* 156 */     ServletRequestDataBinder servletBinder = (ServletRequestDataBinder)binder;
/* 157 */     servletBinder.bind(servletRequest);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/ServletModelAttributeMethodProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */