/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.io.ResourceEditor;
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
/*    */ public class URLEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   private final ResourceEditor resourceEditor;
/*    */   
/*    */   public URLEditor() {
/* 56 */     this.resourceEditor = new ResourceEditor();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public URLEditor(ResourceEditor resourceEditor) {
/* 64 */     Assert.notNull(resourceEditor, "ResourceEditor must not be null");
/* 65 */     this.resourceEditor = resourceEditor;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAsText(String text) throws IllegalArgumentException {
/* 71 */     this.resourceEditor.setAsText(text);
/* 72 */     Resource resource = (Resource)this.resourceEditor.getValue();
/*    */     try {
/* 74 */       setValue((resource != null) ? resource.getURL() : null);
/*    */     }
/* 76 */     catch (IOException ex) {
/* 77 */       throw new IllegalArgumentException("Could not retrieve URL for " + resource + ": " + ex.getMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 83 */     URL value = (URL)getValue();
/* 84 */     return (value != null) ? value.toExternalForm() : "";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/propertyeditors/URLEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */