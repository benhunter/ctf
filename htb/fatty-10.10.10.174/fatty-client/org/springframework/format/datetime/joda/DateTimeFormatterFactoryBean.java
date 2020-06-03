/*    */ package org.springframework.format.datetime.joda;
/*    */ 
/*    */ import org.joda.time.format.DateTimeFormatter;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.beans.factory.InitializingBean;
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
/*    */ public class DateTimeFormatterFactoryBean
/*    */   extends DateTimeFormatterFactory
/*    */   implements FactoryBean<DateTimeFormatter>, InitializingBean
/*    */ {
/*    */   @Nullable
/*    */   private DateTimeFormatter dateTimeFormatter;
/*    */   
/*    */   public void afterPropertiesSet() {
/* 46 */     this.dateTimeFormatter = createDateTimeFormatter();
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public DateTimeFormatter getObject() {
/* 52 */     return this.dateTimeFormatter;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> getObjectType() {
/* 57 */     return DateTimeFormatter.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSingleton() {
/* 62 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/joda/DateTimeFormatterFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */