/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.aspectj.lang.annotation.After;
/*     */ import org.aspectj.lang.annotation.AfterReturning;
/*     */ import org.aspectj.lang.annotation.AfterThrowing;
/*     */ import org.aspectj.lang.annotation.Around;
/*     */ import org.aspectj.lang.annotation.Aspect;
/*     */ import org.aspectj.lang.annotation.Before;
/*     */ import org.aspectj.lang.annotation.Pointcut;
/*     */ import org.aspectj.lang.reflect.AjType;
/*     */ import org.aspectj.lang.reflect.AjTypeSystem;
/*     */ import org.aspectj.lang.reflect.PerClauseKind;
/*     */ import org.springframework.aop.framework.AopConfigException;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractAspectJAdvisorFactory
/*     */   implements AspectJAdvisorFactory
/*     */ {
/*     */   private static final String AJC_MAGIC = "ajc$";
/*  62 */   private static final Class<?>[] ASPECTJ_ANNOTATION_CLASSES = new Class[] { Pointcut.class, Around.class, Before.class, After.class, AfterReturning.class, AfterThrowing.class };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  69 */   protected final ParameterNameDiscoverer parameterNameDiscoverer = new AspectJAnnotationParameterNameDiscoverer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAspect(Class<?> clazz) {
/*  80 */     return (hasAspectAnnotation(clazz) && !compiledByAjc(clazz));
/*     */   }
/*     */   
/*     */   private boolean hasAspectAnnotation(Class<?> clazz) {
/*  84 */     return (AnnotationUtils.findAnnotation(clazz, Aspect.class) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean compiledByAjc(Class<?> clazz) {
/*  95 */     for (Field field : clazz.getDeclaredFields()) {
/*  96 */       if (field.getName().startsWith("ajc$")) {
/*  97 */         return true;
/*     */       }
/*     */     } 
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(Class<?> aspectClass) throws AopConfigException {
/* 106 */     if (aspectClass.getSuperclass().getAnnotation(Aspect.class) != null && 
/* 107 */       !Modifier.isAbstract(aspectClass.getSuperclass().getModifiers())) {
/* 108 */       throw new AopConfigException("[" + aspectClass.getName() + "] cannot extend concrete aspect [" + aspectClass
/* 109 */           .getSuperclass().getName() + "]");
/*     */     }
/*     */     
/* 112 */     AjType<?> ajType = AjTypeSystem.getAjType(aspectClass);
/* 113 */     if (!ajType.isAspect()) {
/* 114 */       throw new NotAnAtAspectException(aspectClass);
/*     */     }
/* 116 */     if (ajType.getPerClause().getKind() == PerClauseKind.PERCFLOW) {
/* 117 */       throw new AopConfigException(aspectClass.getName() + " uses percflow instantiation model: This is not supported in Spring AOP.");
/*     */     }
/*     */     
/* 120 */     if (ajType.getPerClause().getKind() == PerClauseKind.PERCFLOWBELOW) {
/* 121 */       throw new AopConfigException(aspectClass.getName() + " uses percflowbelow instantiation model: This is not supported in Spring AOP.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected static AspectJAnnotation<?> findAspectJAnnotationOnMethod(Method method) {
/* 133 */     for (Class<?> clazz : ASPECTJ_ANNOTATION_CLASSES) {
/* 134 */       AspectJAnnotation<?> foundAnnotation = findAnnotation(method, clazz);
/* 135 */       if (foundAnnotation != null) {
/* 136 */         return foundAnnotation;
/*     */       }
/*     */     } 
/* 139 */     return null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static <A extends Annotation> AspectJAnnotation<A> findAnnotation(Method method, Class<A> toLookFor) {
/* 144 */     Annotation annotation = AnnotationUtils.findAnnotation(method, toLookFor);
/* 145 */     if (annotation != null) {
/* 146 */       return new AspectJAnnotation<>((A)annotation);
/*     */     }
/*     */     
/* 149 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected enum AspectJAnnotationType
/*     */   {
/* 160 */     AtPointcut, AtAround, AtBefore, AtAfter, AtAfterReturning, AtAfterThrowing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class AspectJAnnotation<A extends Annotation>
/*     */   {
/* 171 */     private static final String[] EXPRESSION_ATTRIBUTES = new String[] { "pointcut", "value" };
/*     */     
/* 173 */     private static Map<Class<?>, AbstractAspectJAdvisorFactory.AspectJAnnotationType> annotationTypeMap = new HashMap<>(8);
/*     */     
/*     */     static {
/* 176 */       annotationTypeMap.put(Pointcut.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtPointcut);
/* 177 */       annotationTypeMap.put(Around.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtAround);
/* 178 */       annotationTypeMap.put(Before.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtBefore);
/* 179 */       annotationTypeMap.put(After.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtAfter);
/* 180 */       annotationTypeMap.put(AfterReturning.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtAfterReturning);
/* 181 */       annotationTypeMap.put(AfterThrowing.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtAfterThrowing);
/*     */     }
/*     */ 
/*     */     
/*     */     private final A annotation;
/*     */     
/*     */     private final AbstractAspectJAdvisorFactory.AspectJAnnotationType annotationType;
/*     */     
/*     */     private final String pointcutExpression;
/*     */     private final String argumentNames;
/*     */     
/*     */     public AspectJAnnotation(A annotation) {
/* 193 */       this.annotation = annotation;
/* 194 */       this.annotationType = determineAnnotationType(annotation);
/*     */       try {
/* 196 */         this.pointcutExpression = resolveExpression(annotation);
/* 197 */         Object argNames = AnnotationUtils.getValue((Annotation)annotation, "argNames");
/* 198 */         this.argumentNames = (argNames instanceof String) ? (String)argNames : "";
/*     */       }
/* 200 */       catch (Exception ex) {
/* 201 */         throw new IllegalArgumentException((new StringBuilder()).append(annotation).append(" is not a valid AspectJ annotation").toString(), ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     private AbstractAspectJAdvisorFactory.AspectJAnnotationType determineAnnotationType(A annotation) {
/* 206 */       AbstractAspectJAdvisorFactory.AspectJAnnotationType type = annotationTypeMap.get(annotation.annotationType());
/* 207 */       if (type != null) {
/* 208 */         return type;
/*     */       }
/* 210 */       throw new IllegalStateException("Unknown annotation type: " + annotation);
/*     */     }
/*     */     
/*     */     private String resolveExpression(A annotation) {
/* 214 */       for (String attributeName : EXPRESSION_ATTRIBUTES) {
/* 215 */         Object val = AnnotationUtils.getValue((Annotation)annotation, attributeName);
/* 216 */         if (val instanceof String) {
/* 217 */           String str = (String)val;
/* 218 */           if (!str.isEmpty()) {
/* 219 */             return str;
/*     */           }
/*     */         } 
/*     */       } 
/* 223 */       throw new IllegalStateException("Failed to resolve expression: " + annotation);
/*     */     }
/*     */     
/*     */     public AbstractAspectJAdvisorFactory.AspectJAnnotationType getAnnotationType() {
/* 227 */       return this.annotationType;
/*     */     }
/*     */     
/*     */     public A getAnnotation() {
/* 231 */       return this.annotation;
/*     */     }
/*     */     
/*     */     public String getPointcutExpression() {
/* 235 */       return this.pointcutExpression;
/*     */     }
/*     */     
/*     */     public String getArgumentNames() {
/* 239 */       return this.argumentNames;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 244 */       return this.annotation.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class AspectJAnnotationParameterNameDiscoverer
/*     */     implements ParameterNameDiscoverer
/*     */   {
/*     */     private AspectJAnnotationParameterNameDiscoverer() {}
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String[] getParameterNames(Method method) {
/* 258 */       if (method.getParameterCount() == 0) {
/* 259 */         return new String[0];
/*     */       }
/* 261 */       AbstractAspectJAdvisorFactory.AspectJAnnotation<?> annotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(method);
/* 262 */       if (annotation == null) {
/* 263 */         return null;
/*     */       }
/* 265 */       StringTokenizer nameTokens = new StringTokenizer(annotation.getArgumentNames(), ",");
/* 266 */       if (nameTokens.countTokens() > 0) {
/* 267 */         String[] names = new String[nameTokens.countTokens()];
/* 268 */         for (int i = 0; i < names.length; i++) {
/* 269 */           names[i] = nameTokens.nextToken();
/*     */         }
/* 271 */         return names;
/*     */       } 
/*     */       
/* 274 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String[] getParameterNames(Constructor<?> ctor) {
/* 281 */       throw new UnsupportedOperationException("Spring AOP cannot handle constructor advice");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/annotation/AbstractAspectJAdvisorFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */