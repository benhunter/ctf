/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.tagext.BodyContent;
/*     */ import javax.servlet.jsp.tagext.BodyTag;
/*     */ import javax.servlet.jsp.tagext.Tag;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.servlet.support.BindStatus;
/*     */ import org.springframework.web.util.TagUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OptionTag
/*     */   extends AbstractHtmlElementBodyTag
/*     */   implements BodyTag
/*     */ {
/*     */   public static final String VALUE_VARIABLE_NAME = "value";
/*     */   public static final String DISPLAY_VALUE_VARIABLE_NAME = "displayValue";
/*     */   private static final String SELECTED_ATTRIBUTE = "selected";
/*     */   private static final String VALUE_ATTRIBUTE = "value";
/*     */   private static final String DISABLED_ATTRIBUTE = "disabled";
/*     */   @Nullable
/*     */   private Object value;
/*     */   @Nullable
/*     */   private String label;
/*     */   @Nullable
/*     */   private Object oldValue;
/*     */   @Nullable
/*     */   private Object oldDisplayValue;
/*     */   private boolean disabled;
/*     */   
/*     */   public void setValue(Object value) {
/* 253 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object getValue() {
/* 261 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDisabled(boolean disabled) {
/* 268 */     this.disabled = disabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isDisabled() {
/* 275 */     return this.disabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLabel(String label) {
/* 283 */     this.label = label;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getLabel() {
/* 291 */     return this.label;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderDefaultContent(TagWriter tagWriter) throws JspException {
/* 297 */     Object value = this.pageContext.getAttribute("value");
/* 298 */     String label = getLabelValue(value);
/* 299 */     renderOption(value, label, tagWriter);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderFromBodyContent(BodyContent bodyContent, TagWriter tagWriter) throws JspException {
/* 304 */     Object value = this.pageContext.getAttribute("value");
/* 305 */     String label = bodyContent.getString();
/* 306 */     renderOption(value, label, tagWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onWriteTagContent() {
/* 314 */     assertUnderSelectTag();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void exposeAttributes() throws JspException {
/* 319 */     Object value = resolveValue();
/* 320 */     this.oldValue = this.pageContext.getAttribute("value");
/* 321 */     this.pageContext.setAttribute("value", value);
/* 322 */     this.oldDisplayValue = this.pageContext.getAttribute("displayValue");
/* 323 */     this.pageContext.setAttribute("displayValue", getDisplayString(value, getBindStatus().getEditor()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BindStatus getBindStatus() {
/* 328 */     return (BindStatus)this.pageContext.getAttribute("org.springframework.web.servlet.tags.form.SelectTag.listValue");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void removeAttributes() {
/* 333 */     if (this.oldValue != null) {
/* 334 */       this.pageContext.setAttribute("value", this.oldValue);
/* 335 */       this.oldValue = null;
/*     */     } else {
/*     */       
/* 338 */       this.pageContext.removeAttribute("value");
/*     */     } 
/*     */     
/* 341 */     if (this.oldDisplayValue != null) {
/* 342 */       this.pageContext.setAttribute("displayValue", this.oldDisplayValue);
/* 343 */       this.oldDisplayValue = null;
/*     */     } else {
/*     */       
/* 346 */       this.pageContext.removeAttribute("displayValue");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderOption(Object value, String label, TagWriter tagWriter) throws JspException {
/* 351 */     tagWriter.startTag("option");
/* 352 */     writeOptionalAttribute(tagWriter, "id", resolveId());
/* 353 */     writeOptionalAttributes(tagWriter);
/* 354 */     String renderedValue = getDisplayString(value, getBindStatus().getEditor());
/* 355 */     renderedValue = processFieldValue(getSelectTag().getName(), renderedValue, "option");
/* 356 */     tagWriter.writeAttribute("value", renderedValue);
/* 357 */     if (isSelected(value)) {
/* 358 */       tagWriter.writeAttribute("selected", "selected");
/*     */     }
/* 360 */     if (isDisabled()) {
/* 361 */       tagWriter.writeAttribute("disabled", "disabled");
/*     */     }
/* 363 */     tagWriter.appendValue(label);
/* 364 */     tagWriter.endTag();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String autogenerateId() throws JspException {
/* 369 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getLabelValue(Object resolvedValue) throws JspException {
/* 379 */     String label = getLabel();
/* 380 */     Object labelObj = (label == null) ? resolvedValue : evaluate("label", label);
/* 381 */     return getDisplayString(labelObj, getBindStatus().getEditor());
/*     */   }
/*     */   
/*     */   private void assertUnderSelectTag() {
/* 385 */     TagUtils.assertHasAncestorOfType((Tag)this, SelectTag.class, "option", "select");
/*     */   }
/*     */   
/*     */   private SelectTag getSelectTag() {
/* 389 */     return (SelectTag)findAncestorWithClass((Tag)this, SelectTag.class);
/*     */   }
/*     */   
/*     */   private boolean isSelected(Object resolvedValue) {
/* 393 */     return SelectedValueComparator.isSelected(getBindStatus(), resolvedValue);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Object resolveValue() throws JspException {
/* 398 */     return evaluate("value", getValue());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/OptionTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */