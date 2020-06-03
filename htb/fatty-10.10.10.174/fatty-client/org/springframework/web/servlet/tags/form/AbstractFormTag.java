/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.servlet.tags.HtmlEscapingAwareTag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractFormTag
/*     */   extends HtmlEscapingAwareTag
/*     */ {
/*     */   @Nullable
/*     */   protected Object evaluate(String attributeName, @Nullable Object value) throws JspException {
/*  50 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void writeOptionalAttribute(TagWriter tagWriter, String attributeName, @Nullable String value) throws JspException {
/*  64 */     if (value != null) {
/*  65 */       tagWriter.writeOptionalAttributeValue(attributeName, getDisplayString(evaluate(attributeName, value)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TagWriter createTagWriter() {
/*  76 */     return new TagWriter(this.pageContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int doStartTagInternal() throws Exception {
/*  86 */     return writeTagContent(createTagWriter());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDisplayString(@Nullable Object value) {
/*  94 */     return ValueFormatter.getDisplayString(value, isHtmlEscape());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDisplayString(@Nullable Object value, @Nullable PropertyEditor propertyEditor) {
/* 104 */     return ValueFormatter.getDisplayString(value, propertyEditor, isHtmlEscape());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isDefaultHtmlEscape() {
/* 112 */     Boolean defaultHtmlEscape = getRequestContext().getDefaultHtmlEscape();
/* 113 */     return (defaultHtmlEscape == null || defaultHtmlEscape.booleanValue());
/*     */   }
/*     */   
/*     */   protected abstract int writeTagContent(TagWriter paramTagWriter) throws JspException;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/AbstractFormTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */