/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.io.IOException;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.tagext.Tag;
/*     */ import javax.servlet.jsp.tagext.TagSupport;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class TransformTag
/*     */   extends HtmlEscapingAwareTag
/*     */ {
/*     */   @Nullable
/*     */   private Object value;
/*     */   @Nullable
/*     */   private String var;
/*  98 */   private String scope = "page";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(Object value) {
/* 109 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVar(String var) {
/* 119 */     this.var = var;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScope(String scope) {
/* 130 */     this.scope = scope;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int doStartTagInternal() throws JspException {
/* 136 */     if (this.value != null) {
/*     */       
/* 138 */       EditorAwareTag tag = (EditorAwareTag)TagSupport.findAncestorWithClass((Tag)this, EditorAwareTag.class);
/* 139 */       if (tag == null) {
/* 140 */         throw new JspException("TransformTag can only be used within EditorAwareTag (e.g. BindTag)");
/*     */       }
/*     */ 
/*     */       
/* 144 */       String result = null;
/* 145 */       PropertyEditor editor = tag.getEditor();
/* 146 */       if (editor != null) {
/*     */         
/* 148 */         editor.setValue(this.value);
/* 149 */         result = editor.getAsText();
/*     */       }
/*     */       else {
/*     */         
/* 153 */         result = this.value.toString();
/*     */       } 
/* 155 */       result = htmlEscape(result);
/* 156 */       if (this.var != null) {
/* 157 */         this.pageContext.setAttribute(this.var, result, TagUtils.getScope(this.scope));
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/* 162 */           this.pageContext.getOut().print(result);
/*     */         }
/* 164 */         catch (IOException ex) {
/* 165 */           throw new JspException(ex);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 170 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/TransformTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */