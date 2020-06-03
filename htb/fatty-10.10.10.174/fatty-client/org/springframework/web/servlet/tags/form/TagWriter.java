/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TagWriter
/*     */ {
/*     */   private final SafeWriter writer;
/*  49 */   private final Deque<TagStateEntry> tagState = new ArrayDeque<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TagWriter(PageContext pageContext) {
/*  58 */     Assert.notNull(pageContext, "PageContext must not be null");
/*  59 */     this.writer = new SafeWriter(pageContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TagWriter(Writer writer) {
/*  68 */     Assert.notNull(writer, "Writer must not be null");
/*  69 */     this.writer = new SafeWriter(writer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startTag(String tagName) throws JspException {
/*  79 */     if (inTag()) {
/*  80 */       closeTagAndMarkAsBlock();
/*     */     }
/*  82 */     push(tagName);
/*  83 */     this.writer.append("<").append(tagName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeAttribute(String attributeName, String attributeValue) throws JspException {
/*  93 */     if (currentState().isBlockTag()) {
/*  94 */       throw new IllegalStateException("Cannot write attributes after opening tag is closed.");
/*     */     }
/*  96 */     this.writer.append(" ").append(attributeName).append("=\"")
/*  97 */       .append(attributeValue).append("\"");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeOptionalAttributeValue(String attributeName, @Nullable String attributeValue) throws JspException {
/* 106 */     if (StringUtils.hasText(attributeValue)) {
/* 107 */       writeAttribute(attributeName, attributeValue);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendValue(String value) throws JspException {
/* 117 */     if (!inTag()) {
/* 118 */       throw new IllegalStateException("Cannot write tag value. No open tag available.");
/*     */     }
/* 120 */     closeTagAndMarkAsBlock();
/* 121 */     this.writer.append(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forceBlock() throws JspException {
/* 132 */     if (currentState().isBlockTag()) {
/*     */       return;
/*     */     }
/* 135 */     closeTagAndMarkAsBlock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endTag() throws JspException {
/* 144 */     endTag(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endTag(boolean enforceClosingTag) throws JspException {
/* 155 */     if (!inTag()) {
/* 156 */       throw new IllegalStateException("Cannot write end of tag. No open tag available.");
/*     */     }
/* 158 */     boolean renderClosingTag = true;
/* 159 */     if (!currentState().isBlockTag())
/*     */     {
/* 161 */       if (enforceClosingTag) {
/* 162 */         this.writer.append(">");
/*     */       } else {
/*     */         
/* 165 */         this.writer.append("/>");
/* 166 */         renderClosingTag = false;
/*     */       } 
/*     */     }
/* 169 */     if (renderClosingTag) {
/* 170 */       this.writer.append("</").append(currentState().getTagName()).append(">");
/*     */     }
/* 172 */     this.tagState.pop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void push(String tagName) {
/* 180 */     this.tagState.push(new TagStateEntry(tagName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void closeTagAndMarkAsBlock() throws JspException {
/* 187 */     if (!currentState().isBlockTag()) {
/* 188 */       currentState().markAsBlockTag();
/* 189 */       this.writer.append(">");
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean inTag() {
/* 194 */     return !this.tagState.isEmpty();
/*     */   }
/*     */   
/*     */   private TagStateEntry currentState() {
/* 198 */     return this.tagState.element();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class TagStateEntry
/*     */   {
/*     */     private final String tagName;
/*     */ 
/*     */     
/*     */     private boolean blockTag;
/*     */ 
/*     */     
/*     */     public TagStateEntry(String tagName) {
/* 212 */       this.tagName = tagName;
/*     */     }
/*     */     
/*     */     public String getTagName() {
/* 216 */       return this.tagName;
/*     */     }
/*     */     
/*     */     public void markAsBlockTag() {
/* 220 */       this.blockTag = true;
/*     */     }
/*     */     
/*     */     public boolean isBlockTag() {
/* 224 */       return this.blockTag;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class SafeWriter
/*     */   {
/*     */     @Nullable
/*     */     private PageContext pageContext;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private Writer writer;
/*     */ 
/*     */ 
/*     */     
/*     */     public SafeWriter(PageContext pageContext) {
/* 242 */       this.pageContext = pageContext;
/*     */     }
/*     */     
/*     */     public SafeWriter(Writer writer) {
/* 246 */       this.writer = writer;
/*     */     }
/*     */     
/*     */     public SafeWriter append(String value) throws JspException {
/*     */       try {
/* 251 */         getWriterToUse().write(String.valueOf(value));
/* 252 */         return this;
/*     */       }
/* 254 */       catch (IOException ex) {
/* 255 */         throw new JspException("Unable to write to JspWriter", ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     private Writer getWriterToUse() {
/* 260 */       Writer writer = (this.pageContext != null) ? (Writer)this.pageContext.getOut() : this.writer;
/* 261 */       Assert.state((writer != null), "No Writer available");
/* 262 */       return writer;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/TagWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */