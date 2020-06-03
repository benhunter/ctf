/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.support.BindStatus;
/*     */ import org.springframework.web.servlet.support.RequestDataValueProcessor;
/*     */ import org.springframework.web.servlet.tags.EditorAwareTag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDataBoundFormElementTag
/*     */   extends AbstractFormTag
/*     */   implements EditorAwareTag
/*     */ {
/*     */   protected static final String NESTED_PATH_VARIABLE_NAME = "nestedPath";
/*     */   @Nullable
/*     */   private String path;
/*     */   @Nullable
/*     */   private String id;
/*     */   @Nullable
/*     */   private BindStatus bindStatus;
/*     */   
/*     */   public void setPath(String path) {
/*  79 */     this.path = path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String getPath() throws JspException {
/*  87 */     String resolvedPath = (String)evaluate("path", this.path);
/*  88 */     return (resolvedPath != null) ? resolvedPath : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setId(@Nullable String id) {
/*  98 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getId() {
/* 107 */     return this.id;
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
/*     */   protected void writeDefaultAttributes(TagWriter tagWriter) throws JspException {
/* 121 */     writeOptionalAttribute(tagWriter, "id", resolveId());
/* 122 */     writeOptionalAttribute(tagWriter, "name", getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String resolveId() throws JspException {
/* 133 */     Object id = evaluate("id", getId());
/* 134 */     if (id != null) {
/* 135 */       String idString = id.toString();
/* 136 */       return StringUtils.hasText(idString) ? idString : null;
/*     */     } 
/* 138 */     return autogenerateId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String autogenerateId() throws JspException {
/* 148 */     String name = getName();
/* 149 */     return (name != null) ? StringUtils.deleteAny(name, "[]") : null;
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
/*     */   @Nullable
/*     */   protected String getName() throws JspException {
/* 163 */     return getPropertyPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BindStatus getBindStatus() throws JspException {
/* 170 */     if (this.bindStatus == null) {
/*     */       
/* 172 */       String nestedPath = getNestedPath();
/* 173 */       String pathToUse = (nestedPath != null) ? (nestedPath + getPath()) : getPath();
/* 174 */       if (pathToUse.endsWith(".")) {
/* 175 */         pathToUse = pathToUse.substring(0, pathToUse.length() - 1);
/*     */       }
/* 177 */       this.bindStatus = new BindStatus(getRequestContext(), pathToUse, false);
/*     */     } 
/* 179 */     return this.bindStatus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getNestedPath() {
/* 188 */     return (String)this.pageContext.getAttribute("nestedPath", 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPropertyPath() throws JspException {
/* 198 */     String expression = getBindStatus().getExpression();
/* 199 */     return (expression != null) ? expression : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected final Object getBoundValue() throws JspException {
/* 208 */     return getBindStatus().getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected PropertyEditor getPropertyEditor() throws JspException {
/* 216 */     return getBindStatus().getEditor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final PropertyEditor getEditor() throws JspException {
/* 226 */     return getPropertyEditor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String convertToDisplayString(@Nullable Object value) throws JspException {
/* 234 */     PropertyEditor editor = (value != null) ? getBindStatus().findEditor(value.getClass()) : null;
/* 235 */     return getDisplayString(value, editor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String processFieldValue(@Nullable String name, String value, String type) {
/* 243 */     RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
/* 244 */     ServletRequest request = this.pageContext.getRequest();
/* 245 */     if (processor != null && request instanceof HttpServletRequest) {
/* 246 */       value = processor.processFormFieldValue((HttpServletRequest)request, name, value, type);
/*     */     }
/* 248 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doFinally() {
/* 256 */     super.doFinally();
/* 257 */     this.bindStatus = null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/AbstractDataBoundFormElementTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */