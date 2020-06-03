/*    */ package org.springframework.format.number;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.Set;
/*    */ import org.springframework.context.support.EmbeddedValueResolutionSupport;
/*    */ import org.springframework.format.AnnotationFormatterFactory;
/*    */ import org.springframework.format.Formatter;
/*    */ import org.springframework.format.Parser;
/*    */ import org.springframework.format.Printer;
/*    */ import org.springframework.format.annotation.NumberFormat;
/*    */ import org.springframework.util.NumberUtils;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class NumberFormatAnnotationFormatterFactory
/*    */   extends EmbeddedValueResolutionSupport
/*    */   implements AnnotationFormatterFactory<NumberFormat>
/*    */ {
/*    */   public Set<Class<?>> getFieldTypes() {
/* 44 */     return NumberUtils.STANDARD_NUMBER_TYPES;
/*    */   }
/*    */ 
/*    */   
/*    */   public Printer<Number> getPrinter(NumberFormat annotation, Class<?> fieldType) {
/* 49 */     return (Printer<Number>)configureFormatterFrom(annotation);
/*    */   }
/*    */ 
/*    */   
/*    */   public Parser<Number> getParser(NumberFormat annotation, Class<?> fieldType) {
/* 54 */     return (Parser<Number>)configureFormatterFrom(annotation);
/*    */   }
/*    */ 
/*    */   
/*    */   private Formatter<Number> configureFormatterFrom(NumberFormat annotation) {
/* 59 */     String pattern = resolveEmbeddedValue(annotation.pattern());
/* 60 */     if (StringUtils.hasLength(pattern)) {
/* 61 */       return new NumberStyleFormatter(pattern);
/*    */     }
/*    */     
/* 64 */     NumberFormat.Style style = annotation.style();
/* 65 */     if (style == NumberFormat.Style.CURRENCY) {
/* 66 */       return new CurrencyStyleFormatter();
/*    */     }
/* 68 */     if (style == NumberFormat.Style.PERCENT) {
/* 69 */       return new PercentStyleFormatter();
/*    */     }
/*    */     
/* 72 */     return new NumberStyleFormatter();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/number/NumberFormatAnnotationFormatterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */