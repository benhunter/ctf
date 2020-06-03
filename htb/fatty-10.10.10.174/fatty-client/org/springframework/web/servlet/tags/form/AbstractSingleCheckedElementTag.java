/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import javax.servlet.jsp.JspException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractSingleCheckedElementTag
/*     */   extends AbstractCheckedElementTag
/*     */ {
/*     */   @Nullable
/*     */   private Object value;
/*     */   @Nullable
/*     */   private Object label;
/*     */   
/*     */   public void setValue(Object value) {
/*  53 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object getValue() {
/*  61 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLabel(Object label) {
/*  69 */     this.label = label;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object getLabel() {
/*  77 */     return this.label;
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
/*  88 */     tagWriter.startTag("input");
/*  89 */     String id = resolveId();
/*  90 */     writeOptionalAttribute(tagWriter, "id", id);
/*  91 */     writeOptionalAttribute(tagWriter, "name", getName());
/*  92 */     writeOptionalAttributes(tagWriter);
/*  93 */     writeTagDetails(tagWriter);
/*  94 */     tagWriter.endTag();
/*     */     
/*  96 */     Object resolvedLabel = evaluate("label", getLabel());
/*  97 */     if (resolvedLabel != null) {
/*  98 */       Assert.state((id != null), "Label id is required");
/*  99 */       tagWriter.startTag("label");
/* 100 */       tagWriter.writeAttribute("for", id);
/* 101 */       tagWriter.appendValue(convertToDisplayString(resolvedLabel));
/* 102 */       tagWriter.endTag();
/*     */     } 
/*     */     
/* 105 */     return 0;
/*     */   }
/*     */   
/*     */   protected abstract void writeTagDetails(TagWriter paramTagWriter) throws JspException;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/AbstractSingleCheckedElementTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */