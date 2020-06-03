/*    */ package org.springframework.beans.factory.parsing;
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
/*    */ public class BeanEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private String beanDefinitionName;
/*    */   
/*    */   public BeanEntry(String beanDefinitionName) {
/* 35 */     this.beanDefinitionName = beanDefinitionName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 41 */     return "Bean '" + this.beanDefinitionName + "'";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/parsing/BeanEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */