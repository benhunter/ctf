/*     */ package org.springframework.web.jsf.el;
/*     */ 
/*     */ import java.beans.FeatureDescriptor;
/*     */ import java.util.Iterator;
/*     */ import javax.el.ELContext;
/*     */ import javax.el.ELException;
/*     */ import javax.el.ELResolver;
/*     */ import javax.el.PropertyNotWritableException;
/*     */ import javax.faces.context.FacesContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.jsf.FacesContextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpringBeanFacesELResolver
/*     */   extends ELResolver
/*     */ {
/*     */   @Nullable
/*     */   public Object getValue(ELContext elContext, @Nullable Object base, Object property) throws ELException {
/*  76 */     if (base == null) {
/*  77 */       String beanName = property.toString();
/*  78 */       WebApplicationContext wac = getWebApplicationContext(elContext);
/*  79 */       if (wac.containsBean(beanName)) {
/*  80 */         elContext.setPropertyResolved(true);
/*  81 */         return wac.getBean(beanName);
/*     */       } 
/*     */     } 
/*  84 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getType(ELContext elContext, @Nullable Object base, Object property) throws ELException {
/*  90 */     if (base == null) {
/*  91 */       String beanName = property.toString();
/*  92 */       WebApplicationContext wac = getWebApplicationContext(elContext);
/*  93 */       if (wac.containsBean(beanName)) {
/*  94 */         elContext.setPropertyResolved(true);
/*  95 */         return wac.getType(beanName);
/*     */       } 
/*     */     } 
/*  98 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(ELContext elContext, @Nullable Object base, Object property, Object value) throws ELException {
/* 103 */     if (base == null) {
/* 104 */       String beanName = property.toString();
/* 105 */       WebApplicationContext wac = getWebApplicationContext(elContext);
/* 106 */       if (wac.containsBean(beanName)) {
/* 107 */         if (value == wac.getBean(beanName)) {
/*     */           
/* 109 */           elContext.setPropertyResolved(true);
/*     */         } else {
/*     */           
/* 112 */           throw new PropertyNotWritableException("Variable '" + beanName + "' refers to a Spring bean which by definition is not writable");
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadOnly(ELContext elContext, @Nullable Object base, Object property) throws ELException {
/* 121 */     if (base == null) {
/* 122 */       String beanName = property.toString();
/* 123 */       WebApplicationContext wac = getWebApplicationContext(elContext);
/* 124 */       if (wac.containsBean(beanName)) {
/* 125 */         return true;
/*     */       }
/*     */     } 
/* 128 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext elContext, @Nullable Object base) {
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getCommonPropertyType(ELContext elContext, @Nullable Object base) {
/* 139 */     return Object.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected WebApplicationContext getWebApplicationContext(ELContext elContext) {
/* 150 */     FacesContext facesContext = FacesContext.getCurrentInstance();
/* 151 */     return FacesContextUtils.getRequiredWebApplicationContext(facesContext);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/jsf/el/SpringBeanFacesELResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */