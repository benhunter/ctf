/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.MissingMatrixVariableException;
/*     */ import org.springframework.web.bind.ServletRequestBindingException;
/*     */ import org.springframework.web.bind.annotation.MatrixVariable;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
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
/*     */ public class MatrixVariableMethodArgumentResolver
/*     */   extends AbstractNamedValueMethodArgumentResolver
/*     */ {
/*     */   public MatrixVariableMethodArgumentResolver() {
/*  53 */     super(null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsParameter(MethodParameter parameter) {
/*  59 */     if (!parameter.hasParameterAnnotation(MatrixVariable.class)) {
/*  60 */       return false;
/*     */     }
/*  62 */     if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
/*  63 */       MatrixVariable matrixVariable = (MatrixVariable)parameter.getParameterAnnotation(MatrixVariable.class);
/*  64 */       return (matrixVariable != null && StringUtils.hasText(matrixVariable.name()));
/*     */     } 
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
/*  71 */     MatrixVariable ann = (MatrixVariable)parameter.getParameterAnnotation(MatrixVariable.class);
/*  72 */     Assert.state((ann != null), "No MatrixVariable annotation");
/*  73 */     return new MatrixVariableNamedValueInfo(ann);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
/*  81 */     Map<String, MultiValueMap<String, String>> pathParameters = (Map<String, MultiValueMap<String, String>>)request.getAttribute(HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE, 0);
/*  82 */     if (CollectionUtils.isEmpty(pathParameters)) {
/*  83 */       return null;
/*     */     }
/*     */     
/*  86 */     MatrixVariable ann = (MatrixVariable)parameter.getParameterAnnotation(MatrixVariable.class);
/*  87 */     Assert.state((ann != null), "No MatrixVariable annotation");
/*  88 */     String pathVar = ann.pathVar();
/*  89 */     List<String> paramValues = null;
/*     */     
/*  91 */     if (!pathVar.equals("\n\t\t\n\t\t\n\n\t\t\t\t\n")) {
/*  92 */       if (pathParameters.containsKey(pathVar)) {
/*  93 */         paramValues = (List<String>)((MultiValueMap)pathParameters.get(pathVar)).get(name);
/*     */       }
/*     */     } else {
/*     */       
/*  97 */       boolean found = false;
/*  98 */       paramValues = new ArrayList<>();
/*  99 */       for (MultiValueMap<String, String> params : pathParameters.values()) {
/* 100 */         if (params.containsKey(name)) {
/* 101 */           if (found) {
/* 102 */             String paramType = parameter.getNestedParameterType().getName();
/* 103 */             throw new ServletRequestBindingException("Found more than one match for URI path parameter '" + name + "' for parameter type [" + paramType + "]. Use 'pathVar' attribute to disambiguate.");
/*     */           } 
/*     */ 
/*     */           
/* 107 */           paramValues.addAll((Collection<? extends String>)params.get(name));
/* 108 */           found = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 113 */     if (CollectionUtils.isEmpty(paramValues)) {
/* 114 */       return null;
/*     */     }
/* 116 */     if (paramValues.size() == 1) {
/* 117 */       return paramValues.get(0);
/*     */     }
/*     */     
/* 120 */     return paramValues;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleMissingValue(String name, MethodParameter parameter) throws ServletRequestBindingException {
/* 126 */     throw new MissingMatrixVariableException(name, parameter);
/*     */   }
/*     */   
/*     */   private static final class MatrixVariableNamedValueInfo
/*     */     extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo
/*     */   {
/*     */     private MatrixVariableNamedValueInfo(MatrixVariable annotation) {
/* 133 */       super(annotation.name(), annotation.required(), annotation.defaultValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/MatrixVariableMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */