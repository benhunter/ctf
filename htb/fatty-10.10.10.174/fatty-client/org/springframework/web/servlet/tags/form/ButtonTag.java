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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ButtonTag
/*     */   extends AbstractHtmlElementTag
/*     */ {
/*     */   public static final String DISABLED_ATTRIBUTE = "disabled";
/*     */   @Nullable
/*     */   private TagWriter tagWriter;
/*     */   @Nullable
/*     */   private String name;
/*     */   @Nullable
/*     */   private String value;
/*     */   private boolean disabled;
/*     */   
/*     */   public void setName(String name) {
/*  98 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getName() {
/* 107 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(@Nullable String value) {
/* 114 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getValue() {
/* 122 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDisabled(boolean disabled) {
/* 129 */     this.disabled = disabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisabled() {
/* 136 */     return this.disabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int writeTagContent(TagWriter tagWriter) throws JspException {
/* 142 */     tagWriter.startTag("button");
/* 143 */     writeDefaultAttributes(tagWriter);
/* 144 */     tagWriter.writeAttribute("type", getType());
/* 145 */     writeValue(tagWriter);
/* 146 */     if (isDisabled()) {
/* 147 */       tagWriter.writeAttribute("disabled", "disabled");
/*     */     }
/* 149 */     tagWriter.forceBlock();
/* 150 */     this.tagWriter = tagWriter;
/* 151 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeValue(TagWriter tagWriter) throws JspException {
/* 160 */     String valueToUse = (getValue() != null) ? getValue() : getDefaultValue();
/* 161 */     tagWriter.writeAttribute("value", processFieldValue(getName(), valueToUse, getType()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDefaultValue() {
/* 169 */     return "Submit";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getType() {
/* 178 */     return "submit";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int doEndTag() throws JspException {
/* 186 */     Assert.state((this.tagWriter != null), "No TagWriter set");
/* 187 */     this.tagWriter.endTag();
/* 188 */     return 6;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/ButtonTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */