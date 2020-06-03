/*    */ package org.springframework.web.servlet.tags.form;
/*    */ 
/*    */ import javax.servlet.jsp.PageContext;
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
/*    */ 
/*    */ 
/*    */ abstract class TagIdGenerator
/*    */ {
/* 39 */   private static final String PAGE_CONTEXT_ATTRIBUTE_PREFIX = TagIdGenerator.class.getName() + ".";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String nextId(String name, PageContext pageContext) {
/* 45 */     String attributeName = PAGE_CONTEXT_ATTRIBUTE_PREFIX + name;
/* 46 */     Integer currentCount = (Integer)pageContext.getAttribute(attributeName);
/* 47 */     currentCount = Integer.valueOf((currentCount != null) ? (currentCount.intValue() + 1) : 1);
/* 48 */     pageContext.setAttribute(attributeName, currentCount);
/* 49 */     return name + currentCount;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/TagIdGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */