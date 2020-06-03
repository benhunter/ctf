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
/*     */ 
/*     */ public class CheckboxTag
/*     */   extends AbstractSingleCheckedElementTag
/*     */ {
/*     */   protected int writeTagContent(TagWriter tagWriter) throws JspException {
/* 228 */     super.writeTagContent(tagWriter);
/*     */     
/* 230 */     if (!isDisabled()) {
/*     */       
/* 232 */       tagWriter.startTag("input");
/* 233 */       tagWriter.writeAttribute("type", "hidden");
/* 234 */       String name = "_" + getName();
/* 235 */       tagWriter.writeAttribute("name", name);
/* 236 */       tagWriter.writeAttribute("value", processFieldValue(name, "on", "hidden"));
/* 237 */       tagWriter.endTag();
/*     */     } 
/*     */     
/* 240 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeTagDetails(TagWriter tagWriter) throws JspException {
/* 245 */     tagWriter.writeAttribute("type", getInputType());
/*     */     
/* 247 */     Object boundValue = getBoundValue();
/* 248 */     Class<?> valueType = getBindStatus().getValueType();
/*     */     
/* 250 */     if (Boolean.class == valueType || boolean.class == valueType) {
/*     */       
/* 252 */       if (boundValue instanceof String) {
/* 253 */         boundValue = Boolean.valueOf((String)boundValue);
/*     */       }
/* 255 */       Boolean booleanValue = (boundValue != null) ? (Boolean)boundValue : Boolean.FALSE;
/* 256 */       renderFromBoolean(booleanValue, tagWriter);
/*     */     }
/*     */     else {
/*     */       
/* 260 */       Object value = getValue();
/* 261 */       if (value == null) {
/* 262 */         throw new IllegalArgumentException("Attribute 'value' is required when binding to non-boolean values");
/*     */       }
/* 264 */       Object resolvedValue = (value instanceof String) ? evaluate("value", value) : value;
/* 265 */       renderFromValue(resolvedValue, tagWriter);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getInputType() {
/* 271 */     return "checkbox";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/CheckboxTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */