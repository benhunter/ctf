/*    */ package org.springframework.web.servlet.mvc.condition;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import org.springframework.lang.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractRequestCondition<T extends AbstractRequestCondition<T>>
/*    */   implements RequestCondition<T>
/*    */ {
/*    */   public boolean isEmpty() {
/* 41 */     return getContent().isEmpty();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract Collection<?> getContent();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract String getToStringInfix();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object other) {
/* 61 */     if (this == other) {
/* 62 */       return true;
/*    */     }
/* 64 */     if (other == null || getClass() != other.getClass()) {
/* 65 */       return false;
/*    */     }
/* 67 */     return getContent().equals(((AbstractRequestCondition)other).getContent());
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 72 */     return getContent().hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 77 */     StringBuilder builder = new StringBuilder("[");
/* 78 */     for (Iterator<?> iterator = getContent().iterator(); iterator.hasNext(); ) {
/* 79 */       Object expression = iterator.next();
/* 80 */       builder.append(expression.toString());
/* 81 */       if (iterator.hasNext()) {
/* 82 */         builder.append(getToStringInfix());
/*    */       }
/*    */     } 
/* 85 */     builder.append("]");
/* 86 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/condition/AbstractRequestCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */