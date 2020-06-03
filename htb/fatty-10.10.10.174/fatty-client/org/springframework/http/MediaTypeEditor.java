/*    */ package org.springframework.http;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class MediaTypeEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text) {
/* 36 */     if (StringUtils.hasText(text)) {
/* 37 */       setValue(MediaType.parseMediaType(text));
/*    */     } else {
/*    */       
/* 40 */       setValue(null);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 46 */     MediaType mediaType = (MediaType)getValue();
/* 47 */     return (mediaType != null) ? mediaType.toString() : "";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/MediaTypeEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */