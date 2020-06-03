/*     */ package org.springframework.web.method.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.ui.ModelMap;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.web.HttpSessionRequiredException;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.bind.annotation.ModelAttribute;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.method.support.InvocableHandlerMethod;
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
/*     */ public final class ModelFactory
/*     */ {
/*  64 */   private static final Log logger = LogFactory.getLog(ModelFactory.class);
/*     */   
/*  66 */   private final List<ModelMethod> modelMethods = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final WebDataBinderFactory dataBinderFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final SessionAttributesHandler sessionAttributesHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelFactory(@Nullable List<InvocableHandlerMethod> handlerMethods, WebDataBinderFactory binderFactory, SessionAttributesHandler attributeHandler) {
/*  82 */     if (handlerMethods != null) {
/*  83 */       for (InvocableHandlerMethod handlerMethod : handlerMethods) {
/*  84 */         this.modelMethods.add(new ModelMethod(handlerMethod));
/*     */       }
/*     */     }
/*  87 */     this.dataBinderFactory = binderFactory;
/*  88 */     this.sessionAttributesHandler = attributeHandler;
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
/*     */   public void initModel(NativeWebRequest request, ModelAndViewContainer container, HandlerMethod handlerMethod) throws Exception {
/* 109 */     Map<String, ?> sessionAttributes = this.sessionAttributesHandler.retrieveAttributes((WebRequest)request);
/* 110 */     container.mergeAttributes(sessionAttributes);
/* 111 */     invokeModelAttributeMethods(request, container);
/*     */     
/* 113 */     for (String name : findSessionAttributeArguments(handlerMethod)) {
/* 114 */       if (!container.containsAttribute(name)) {
/* 115 */         Object value = this.sessionAttributesHandler.retrieveAttribute((WebRequest)request, name);
/* 116 */         if (value == null) {
/* 117 */           throw new HttpSessionRequiredException("Expected session attribute '" + name + "'", name);
/*     */         }
/* 119 */         container.addAttribute(name, value);
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
/*     */   private void invokeModelAttributeMethods(NativeWebRequest request, ModelAndViewContainer container) throws Exception {
/* 131 */     while (!this.modelMethods.isEmpty()) {
/* 132 */       InvocableHandlerMethod modelMethod = getNextModelMethod(container).getHandlerMethod();
/* 133 */       ModelAttribute ann = (ModelAttribute)modelMethod.getMethodAnnotation(ModelAttribute.class);
/* 134 */       Assert.state((ann != null), "No ModelAttribute annotation");
/* 135 */       if (container.containsAttribute(ann.name())) {
/* 136 */         if (!ann.binding()) {
/* 137 */           container.setBindingDisabled(ann.name());
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/* 142 */       Object returnValue = modelMethod.invokeForRequest(request, container, new Object[0]);
/* 143 */       if (!modelMethod.isVoid()) {
/* 144 */         String returnValueName = getNameForReturnValue(returnValue, modelMethod.getReturnType());
/* 145 */         if (!ann.binding()) {
/* 146 */           container.setBindingDisabled(returnValueName);
/*     */         }
/* 148 */         if (!container.containsAttribute(returnValueName)) {
/* 149 */           container.addAttribute(returnValueName, returnValue);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private ModelMethod getNextModelMethod(ModelAndViewContainer container) {
/* 156 */     for (ModelMethod modelMethod1 : this.modelMethods) {
/* 157 */       if (modelMethod1.checkDependencies(container)) {
/* 158 */         this.modelMethods.remove(modelMethod1);
/* 159 */         return modelMethod1;
/*     */       } 
/*     */     } 
/* 162 */     ModelMethod modelMethod = this.modelMethods.get(0);
/* 163 */     this.modelMethods.remove(modelMethod);
/* 164 */     return modelMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> findSessionAttributeArguments(HandlerMethod handlerMethod) {
/* 171 */     List<String> result = new ArrayList<>();
/* 172 */     for (MethodParameter parameter : handlerMethod.getMethodParameters()) {
/* 173 */       if (parameter.hasParameterAnnotation(ModelAttribute.class)) {
/* 174 */         String name = getNameForParameter(parameter);
/* 175 */         Class<?> paramType = parameter.getParameterType();
/* 176 */         if (this.sessionAttributesHandler.isHandlerSessionAttribute(name, paramType)) {
/* 177 */           result.add(name);
/*     */         }
/*     */       } 
/*     */     } 
/* 181 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateModel(NativeWebRequest request, ModelAndViewContainer container) throws Exception {
/* 192 */     ModelMap defaultModel = container.getDefaultModel();
/* 193 */     if (container.getSessionStatus().isComplete()) {
/* 194 */       this.sessionAttributesHandler.cleanupAttributes((WebRequest)request);
/*     */     } else {
/*     */       
/* 197 */       this.sessionAttributesHandler.storeAttributes((WebRequest)request, (Map<String, ?>)defaultModel);
/*     */     } 
/* 199 */     if (!container.isRequestHandled() && container.getModel() == defaultModel) {
/* 200 */       updateBindingResult(request, defaultModel);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateBindingResult(NativeWebRequest request, ModelMap model) throws Exception {
/* 208 */     List<String> keyNames = new ArrayList<>(model.keySet());
/* 209 */     for (String name : keyNames) {
/* 210 */       Object value = model.get(name);
/* 211 */       if (value != null && isBindingCandidate(name, value)) {
/* 212 */         String bindingResultKey = BindingResult.MODEL_KEY_PREFIX + name;
/* 213 */         if (!model.containsAttribute(bindingResultKey)) {
/* 214 */           WebDataBinder dataBinder = this.dataBinderFactory.createBinder(request, value, name);
/* 215 */           model.put(bindingResultKey, dataBinder.getBindingResult());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isBindingCandidate(String attributeName, Object value) {
/* 225 */     if (attributeName.startsWith(BindingResult.MODEL_KEY_PREFIX)) {
/* 226 */       return false;
/*     */     }
/*     */     
/* 229 */     if (this.sessionAttributesHandler.isHandlerSessionAttribute(attributeName, value.getClass())) {
/* 230 */       return true;
/*     */     }
/*     */     
/* 233 */     return (!value.getClass().isArray() && !(value instanceof java.util.Collection) && !(value instanceof Map) && 
/* 234 */       !BeanUtils.isSimpleValueType(value.getClass()));
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
/*     */   public static String getNameForParameter(MethodParameter parameter) {
/* 247 */     ModelAttribute ann = (ModelAttribute)parameter.getParameterAnnotation(ModelAttribute.class);
/* 248 */     String name = (ann != null) ? ann.value() : null;
/* 249 */     return StringUtils.hasText(name) ? name : Conventions.getVariableNameForParameter(parameter);
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
/*     */   public static String getNameForReturnValue(@Nullable Object returnValue, MethodParameter returnType) {
/* 265 */     ModelAttribute ann = (ModelAttribute)returnType.getMethodAnnotation(ModelAttribute.class);
/* 266 */     if (ann != null && StringUtils.hasText(ann.value())) {
/* 267 */       return ann.value();
/*     */     }
/*     */     
/* 270 */     Method method = returnType.getMethod();
/* 271 */     Assert.state((method != null), "No handler method");
/* 272 */     Class<?> containingClass = returnType.getContainingClass();
/* 273 */     Class<?> resolvedType = GenericTypeResolver.resolveReturnType(method, containingClass);
/* 274 */     return Conventions.getVariableNameForReturnType(method, resolvedType, returnValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ModelMethod
/*     */   {
/*     */     private final InvocableHandlerMethod handlerMethod;
/*     */     
/* 283 */     private final Set<String> dependencies = new HashSet<>();
/*     */     
/*     */     public ModelMethod(InvocableHandlerMethod handlerMethod) {
/* 286 */       this.handlerMethod = handlerMethod;
/* 287 */       for (MethodParameter parameter : handlerMethod.getMethodParameters()) {
/* 288 */         if (parameter.hasParameterAnnotation(ModelAttribute.class)) {
/* 289 */           this.dependencies.add(ModelFactory.getNameForParameter(parameter));
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public InvocableHandlerMethod getHandlerMethod() {
/* 295 */       return this.handlerMethod;
/*     */     }
/*     */     
/*     */     public boolean checkDependencies(ModelAndViewContainer mavContainer) {
/* 299 */       for (String name : this.dependencies) {
/* 300 */         if (!mavContainer.containsAttribute(name)) {
/* 301 */           return false;
/*     */         }
/*     */       } 
/* 304 */       return true;
/*     */     }
/*     */     
/*     */     public List<String> getUnresolvedDependencies(ModelAndViewContainer mavContainer) {
/* 308 */       List<String> result = new ArrayList<>(this.dependencies.size());
/* 309 */       for (String name : this.dependencies) {
/* 310 */         if (!mavContainer.containsAttribute(name)) {
/* 311 */           result.add(name);
/*     */         }
/*     */       } 
/* 314 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 319 */       return this.handlerMethod.getMethod().toGenericString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/annotation/ModelFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */