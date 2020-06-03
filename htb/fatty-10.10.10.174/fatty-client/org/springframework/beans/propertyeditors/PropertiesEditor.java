/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Map;
/*    */ import java.util.Properties;
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
/*    */ public class PropertiesEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(@Nullable String text) throws IllegalArgumentException {
/* 51 */     Properties props = new Properties();
/* 52 */     if (text != null) {
/*    */       
/*    */       try {
/* 55 */         props.load(new ByteArrayInputStream(text.getBytes(StandardCharsets.ISO_8859_1)));
/*    */       }
/* 57 */       catch (IOException ex) {
/*    */         
/* 59 */         throw new IllegalArgumentException("Failed to parse [" + text + "] into Properties", ex);
/*    */       } 
/*    */     }
/*    */     
/* 63 */     setValue(props);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(Object value) {
/* 71 */     if (!(value instanceof Properties) && value instanceof Map) {
/* 72 */       Properties props = new Properties();
/* 73 */       props.putAll((Map<?, ?>)value);
/* 74 */       super.setValue(props);
/*    */     } else {
/*    */       
/* 77 */       super.setValue(value);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/propertyeditors/PropertiesEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */