/*     */ package org.springframework.web.bind.support;
/*     */ 
/*     */ import org.springframework.beans.PropertyEditorRegistrar;
/*     */ import org.springframework.beans.PropertyEditorRegistry;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.validation.BindingErrorProcessor;
/*     */ import org.springframework.validation.MessageCodesResolver;
/*     */ import org.springframework.validation.Validator;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigurableWebBindingInitializer
/*     */   implements WebBindingInitializer
/*     */ {
/*     */   private boolean autoGrowNestedPaths = true;
/*     */   private boolean directFieldAccess = false;
/*     */   @Nullable
/*     */   private MessageCodesResolver messageCodesResolver;
/*     */   @Nullable
/*     */   private BindingErrorProcessor bindingErrorProcessor;
/*     */   @Nullable
/*     */   private Validator validator;
/*     */   @Nullable
/*     */   private ConversionService conversionService;
/*     */   @Nullable
/*     */   private PropertyEditorRegistrar[] propertyEditorRegistrars;
/*     */   
/*     */   public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths) {
/*  74 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoGrowNestedPaths() {
/*  81 */     return this.autoGrowNestedPaths;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setDirectFieldAccess(boolean directFieldAccess) {
/*  92 */     this.directFieldAccess = directFieldAccess;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDirectFieldAccess() {
/*  99 */     return this.directFieldAccess;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setMessageCodesResolver(@Nullable MessageCodesResolver messageCodesResolver) {
/* 110 */     this.messageCodesResolver = messageCodesResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final MessageCodesResolver getMessageCodesResolver() {
/* 118 */     return this.messageCodesResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setBindingErrorProcessor(@Nullable BindingErrorProcessor bindingErrorProcessor) {
/* 129 */     this.bindingErrorProcessor = bindingErrorProcessor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final BindingErrorProcessor getBindingErrorProcessor() {
/* 137 */     return this.bindingErrorProcessor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setValidator(@Nullable Validator validator) {
/* 144 */     this.validator = validator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final Validator getValidator() {
/* 152 */     return this.validator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setConversionService(@Nullable ConversionService conversionService) {
/* 160 */     this.conversionService = conversionService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final ConversionService getConversionService() {
/* 168 */     return this.conversionService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setPropertyEditorRegistrar(PropertyEditorRegistrar propertyEditorRegistrar) {
/* 175 */     this.propertyEditorRegistrars = new PropertyEditorRegistrar[] { propertyEditorRegistrar };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setPropertyEditorRegistrars(@Nullable PropertyEditorRegistrar[] propertyEditorRegistrars) {
/* 182 */     this.propertyEditorRegistrars = propertyEditorRegistrars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final PropertyEditorRegistrar[] getPropertyEditorRegistrars() {
/* 190 */     return this.propertyEditorRegistrars;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void initBinder(WebDataBinder binder) {
/* 196 */     binder.setAutoGrowNestedPaths(this.autoGrowNestedPaths);
/* 197 */     if (this.directFieldAccess) {
/* 198 */       binder.initDirectFieldAccess();
/*     */     }
/* 200 */     if (this.messageCodesResolver != null) {
/* 201 */       binder.setMessageCodesResolver(this.messageCodesResolver);
/*     */     }
/* 203 */     if (this.bindingErrorProcessor != null) {
/* 204 */       binder.setBindingErrorProcessor(this.bindingErrorProcessor);
/*     */     }
/* 206 */     if (this.validator != null && binder.getTarget() != null && this.validator
/* 207 */       .supports(binder.getTarget().getClass())) {
/* 208 */       binder.setValidator(this.validator);
/*     */     }
/* 210 */     if (this.conversionService != null) {
/* 211 */       binder.setConversionService(this.conversionService);
/*     */     }
/* 213 */     if (this.propertyEditorRegistrars != null)
/* 214 */       for (PropertyEditorRegistrar propertyEditorRegistrar : this.propertyEditorRegistrars)
/* 215 */         propertyEditorRegistrar.registerCustomEditors((PropertyEditorRegistry)binder);  
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/support/ConfigurableWebBindingInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */