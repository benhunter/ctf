/*    */ package org.springframework.core.env;
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
/*    */ class SimpleCommandLineArgsParser
/*    */ {
/*    */   public CommandLineArgs parse(String... args) {
/* 61 */     CommandLineArgs commandLineArgs = new CommandLineArgs();
/* 62 */     for (String arg : args) {
/* 63 */       if (arg.startsWith("--")) {
/* 64 */         String optionName, optionText = arg.substring(2, arg.length());
/*    */         
/* 66 */         String optionValue = null;
/* 67 */         if (optionText.contains("=")) {
/* 68 */           optionName = optionText.substring(0, optionText.indexOf('='));
/* 69 */           optionValue = optionText.substring(optionText.indexOf('=') + 1, optionText.length());
/*    */         } else {
/*    */           
/* 72 */           optionName = optionText;
/*    */         } 
/* 74 */         if (optionName.isEmpty() || (optionValue != null && optionValue.isEmpty())) {
/* 75 */           throw new IllegalArgumentException("Invalid argument syntax: " + arg);
/*    */         }
/* 77 */         commandLineArgs.addOptionArg(optionName, optionValue);
/*    */       } else {
/*    */         
/* 80 */         commandLineArgs.addNonOptionArg(arg);
/*    */       } 
/*    */     } 
/* 83 */     return commandLineArgs;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/env/SimpleCommandLineArgsParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */