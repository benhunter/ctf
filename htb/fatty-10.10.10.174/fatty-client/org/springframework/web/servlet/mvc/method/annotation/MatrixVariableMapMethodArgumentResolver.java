/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.annotation.MatrixVariable;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
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
/*     */ public class MatrixVariableMapMethodArgumentResolver
/*     */   implements HandlerMethodArgumentResolver
/*     */ {
/*     */   public boolean supportsParameter(MethodParameter parameter) {
/*  56 */     MatrixVariable matrixVariable = (MatrixVariable)parameter.getParameterAnnotation(MatrixVariable.class);
/*  57 */     return (matrixVariable != null && Map.class.isAssignableFrom(parameter.getParameterType()) && 
/*  58 */       !StringUtils.hasText(matrixVariable.name()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest request, @Nullable WebDataBinderFactory binderFactory) throws Exception {
/*  68 */     Map<String, MultiValueMap<String, String>> matrixVariables = (Map<String, MultiValueMap<String, String>>)request.getAttribute(HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE, 0);
/*     */ 
/*     */     
/*  71 */     if (CollectionUtils.isEmpty(matrixVariables)) {
/*  72 */       return Collections.emptyMap();
/*     */     }
/*     */     
/*  75 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/*  76 */     MatrixVariable ann = (MatrixVariable)parameter.getParameterAnnotation(MatrixVariable.class);
/*  77 */     Assert.state((ann != null), "No MatrixVariable annotation");
/*  78 */     String pathVariable = ann.pathVar();
/*     */     
/*  80 */     if (!pathVariable.equals("\n\t\t\n\t\t\n\n\t\t\t\t\n")) {
/*  81 */       MultiValueMap<String, String> mapForPathVariable = matrixVariables.get(pathVariable);
/*  82 */       if (mapForPathVariable == null) {
/*  83 */         return Collections.emptyMap();
/*     */       }
/*  85 */       linkedMultiValueMap.putAll((Map)mapForPathVariable);
/*     */     } else {
/*     */       
/*  88 */       for (MultiValueMap<String, String> vars : matrixVariables.values()) {
/*  89 */         vars.forEach((name, values) -> {
/*     */               for (String value : values) {
/*     */                 map.add(name, value);
/*     */               }
/*     */             });
/*     */       } 
/*     */     } 
/*     */     
/*  97 */     return isSingleValueMap(parameter) ? linkedMultiValueMap.toSingleValueMap() : linkedMultiValueMap;
/*     */   }
/*     */   
/*     */   private boolean isSingleValueMap(MethodParameter parameter) {
/* 101 */     if (!MultiValueMap.class.isAssignableFrom(parameter.getParameterType())) {
/* 102 */       ResolvableType[] genericTypes = ResolvableType.forMethodParameter(parameter).getGenerics();
/* 103 */       if (genericTypes.length == 2) {
/* 104 */         return !List.class.isAssignableFrom(genericTypes[1].toClass());
/*     */       }
/*     */     } 
/* 107 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/MatrixVariableMapMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */