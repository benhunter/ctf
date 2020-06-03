/*    */ package org.springframework.validation;
/*    */ 
/*    */ import org.springframework.beans.ConfigurablePropertyAccessor;
/*    */ import org.springframework.beans.PropertyAccessorFactory;
/*    */ import org.springframework.lang.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DirectFieldBindingResult
/*    */   extends AbstractPropertyBindingResult
/*    */ {
/*    */   @Nullable
/*    */   private final Object target;
/*    */   private final boolean autoGrowNestedPaths;
/*    */   @Nullable
/*    */   private transient ConfigurablePropertyAccessor directFieldAccessor;
/*    */   
/*    */   public DirectFieldBindingResult(@Nullable Object target, String objectName) {
/* 54 */     this(target, objectName, true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DirectFieldBindingResult(@Nullable Object target, String objectName, boolean autoGrowNestedPaths) {
/* 64 */     super(objectName);
/* 65 */     this.target = target;
/* 66 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public final Object getTarget() {
/* 73 */     return this.target;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final ConfigurablePropertyAccessor getPropertyAccessor() {
/* 83 */     if (this.directFieldAccessor == null) {
/* 84 */       this.directFieldAccessor = createDirectFieldAccessor();
/* 85 */       this.directFieldAccessor.setExtractOldValueForEditor(true);
/* 86 */       this.directFieldAccessor.setAutoGrowNestedPaths(this.autoGrowNestedPaths);
/*    */     } 
/* 88 */     return this.directFieldAccessor;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ConfigurablePropertyAccessor createDirectFieldAccessor() {
/* 96 */     if (this.target == null) {
/* 97 */       throw new IllegalStateException("Cannot access fields on null target instance '" + getObjectName() + "'");
/*    */     }
/* 99 */     return PropertyAccessorFactory.forDirectFieldAccess(this.target);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/DirectFieldBindingResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */