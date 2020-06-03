/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import javax.servlet.jsp.JspException;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractCheckedElementTag
/*     */   extends AbstractHtmlInputElementTag
/*     */ {
/*     */   protected void renderFromValue(@Nullable Object value, TagWriter tagWriter) throws JspException {
/*  42 */     renderFromValue(value, value, tagWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderFromValue(@Nullable Object item, @Nullable Object value, TagWriter tagWriter) throws JspException {
/*  53 */     String displayValue = convertToDisplayString(value);
/*  54 */     tagWriter.writeAttribute("value", processFieldValue(getName(), displayValue, getInputType()));
/*  55 */     if (isOptionSelected(value) || (value != item && isOptionSelected(item))) {
/*  56 */       tagWriter.writeAttribute("checked", "checked");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isOptionSelected(@Nullable Object value) throws JspException {
/*  65 */     return SelectedValueComparator.isSelected(getBindStatus(), value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderFromBoolean(Boolean boundValue, TagWriter tagWriter) throws JspException {
/*  74 */     tagWriter.writeAttribute("value", processFieldValue(getName(), "true", getInputType()));
/*  75 */     if (boundValue.booleanValue()) {
/*  76 */       tagWriter.writeAttribute("checked", "checked");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String autogenerateId() throws JspException {
/*  86 */     String id = super.autogenerateId();
/*  87 */     return (id != null) ? TagIdGenerator.nextId(id, this.pageContext) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract int writeTagContent(TagWriter paramTagWriter) throws JspException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isValidDynamicAttribute(String localName, Object value) {
/* 104 */     return !"type".equals(localName);
/*     */   }
/*     */   
/*     */   protected abstract String getInputType();
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/AbstractCheckedElementTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */