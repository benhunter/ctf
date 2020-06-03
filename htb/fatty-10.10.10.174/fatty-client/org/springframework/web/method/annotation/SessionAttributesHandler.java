/*     */ package org.springframework.web.method.annotation;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.bind.annotation.SessionAttributes;
/*     */ import org.springframework.web.bind.support.SessionAttributeStore;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SessionAttributesHandler
/*     */ {
/*  51 */   private final Set<String> attributeNames = new HashSet<>();
/*     */   
/*  53 */   private final Set<Class<?>> attributeTypes = new HashSet<>();
/*     */   
/*  55 */   private final Set<String> knownAttributeNames = Collections.newSetFromMap(new ConcurrentHashMap<>(4));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final SessionAttributeStore sessionAttributeStore;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionAttributesHandler(Class<?> handlerType, SessionAttributeStore sessionAttributeStore) {
/*  68 */     Assert.notNull(sessionAttributeStore, "SessionAttributeStore may not be null");
/*  69 */     this.sessionAttributeStore = sessionAttributeStore;
/*     */     
/*  71 */     SessionAttributes ann = (SessionAttributes)AnnotatedElementUtils.findMergedAnnotation(handlerType, SessionAttributes.class);
/*  72 */     if (ann != null) {
/*  73 */       Collections.addAll(this.attributeNames, ann.names());
/*  74 */       Collections.addAll(this.attributeTypes, ann.types());
/*     */     } 
/*  76 */     this.knownAttributeNames.addAll(this.attributeNames);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSessionAttributes() {
/*  85 */     return (!this.attributeNames.isEmpty() || !this.attributeTypes.isEmpty());
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
/*     */   public boolean isHandlerSessionAttribute(String attributeName, Class<?> attributeType) {
/*  98 */     Assert.notNull(attributeName, "Attribute name must not be null");
/*  99 */     if (this.attributeNames.contains(attributeName) || this.attributeTypes.contains(attributeType)) {
/* 100 */       this.knownAttributeNames.add(attributeName);
/* 101 */       return true;
/*     */     } 
/*     */     
/* 104 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void storeAttributes(WebRequest request, Map<String, ?> attributes) {
/* 115 */     attributes.forEach((name, value) -> {
/*     */           if (value != null && isHandlerSessionAttribute(name, value.getClass())) {
/*     */             this.sessionAttributeStore.storeAttribute(request, name, value);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> retrieveAttributes(WebRequest request) {
/* 130 */     Map<String, Object> attributes = new HashMap<>();
/* 131 */     for (String name : this.knownAttributeNames) {
/* 132 */       Object value = this.sessionAttributeStore.retrieveAttribute(request, name);
/* 133 */       if (value != null) {
/* 134 */         attributes.put(name, value);
/*     */       }
/*     */     } 
/* 137 */     return attributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cleanupAttributes(WebRequest request) {
/* 147 */     for (String attributeName : this.knownAttributeNames) {
/* 148 */       this.sessionAttributeStore.cleanupAttribute(request, attributeName);
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
/*     */   Object retrieveAttribute(WebRequest request, String attributeName) {
/* 160 */     return this.sessionAttributeStore.retrieveAttribute(request, attributeName);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/annotation/SessionAttributesHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */