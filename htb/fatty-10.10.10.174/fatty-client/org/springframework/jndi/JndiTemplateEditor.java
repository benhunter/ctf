/*    */ package org.springframework.jndi;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.util.Properties;
/*    */ import org.springframework.beans.propertyeditors.PropertiesEditor;
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
/*    */ public class JndiTemplateEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/* 34 */   private final PropertiesEditor propertiesEditor = new PropertiesEditor();
/*    */ 
/*    */   
/*    */   public void setAsText(@Nullable String text) throws IllegalArgumentException {
/* 38 */     if (text == null) {
/* 39 */       throw new IllegalArgumentException("JndiTemplate cannot be created from null string");
/*    */     }
/* 41 */     if ("".equals(text)) {
/*    */       
/* 43 */       setValue(new JndiTemplate());
/*    */     }
/*    */     else {
/*    */       
/* 47 */       this.propertiesEditor.setAsText(text);
/* 48 */       Properties props = (Properties)this.propertiesEditor.getValue();
/* 49 */       setValue(new JndiTemplate(props));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jndi/JndiTemplateEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */