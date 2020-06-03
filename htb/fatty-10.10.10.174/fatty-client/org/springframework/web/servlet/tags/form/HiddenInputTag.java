/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import javax.servlet.jsp.JspException;
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
/*     */ public class HiddenInputTag
/*     */   extends AbstractHtmlElementTag
/*     */ {
/*     */   public static final String DISABLED_ATTRIBUTE = "disabled";
/*     */   private boolean disabled;
/*     */   
/*     */   public void setDisabled(boolean disabled) {
/*  84 */     this.disabled = disabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisabled() {
/*  91 */     return this.disabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isValidDynamicAttribute(String localName, Object value) {
/* 100 */     return !"type".equals(localName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int writeTagContent(TagWriter tagWriter) throws JspException {
/* 111 */     tagWriter.startTag("input");
/* 112 */     writeDefaultAttributes(tagWriter);
/* 113 */     tagWriter.writeAttribute("type", "hidden");
/* 114 */     if (isDisabled()) {
/* 115 */       tagWriter.writeAttribute("disabled", "disabled");
/*     */     }
/* 117 */     String value = getDisplayString(getBoundValue(), getPropertyEditor());
/* 118 */     tagWriter.writeAttribute("value", processFieldValue(getName(), value, "hidden"));
/* 119 */     tagWriter.endTag();
/* 120 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/HiddenInputTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */