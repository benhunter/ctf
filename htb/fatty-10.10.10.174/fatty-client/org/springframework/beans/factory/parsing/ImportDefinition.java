/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.beans.BeanMetadataElement;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class ImportDefinition
/*    */   implements BeanMetadataElement
/*    */ {
/*    */   private final String importedResource;
/*    */   @Nullable
/*    */   private final Resource[] actualResources;
/*    */   @Nullable
/*    */   private final Object source;
/*    */   
/*    */   public ImportDefinition(String importedResource) {
/* 47 */     this(importedResource, null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ImportDefinition(String importedResource, @Nullable Object source) {
/* 56 */     this(importedResource, null, source);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ImportDefinition(String importedResource, @Nullable Resource[] actualResources, @Nullable Object source) {
/* 65 */     Assert.notNull(importedResource, "Imported resource must not be null");
/* 66 */     this.importedResource = importedResource;
/* 67 */     this.actualResources = actualResources;
/* 68 */     this.source = source;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getImportedResource() {
/* 76 */     return this.importedResource;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public final Resource[] getActualResources() {
/* 81 */     return this.actualResources;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public final Object getSource() {
/* 87 */     return this.source;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/parsing/ImportDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */