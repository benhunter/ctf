/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class SimpleCommandLinePropertySource
/*     */   extends CommandLinePropertySource<CommandLineArgs>
/*     */ {
/*     */   public SimpleCommandLinePropertySource(String... args) {
/*  90 */     super((new SimpleCommandLineArgsParser()).parse(args));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleCommandLinePropertySource(String name, String[] args) {
/*  98 */     super(name, (new SimpleCommandLineArgsParser()).parse(args));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getPropertyNames() {
/* 106 */     return StringUtils.toStringArray(this.source.getOptionNames());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean containsOption(String name) {
/* 111 */     return this.source.containsOption(name);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected List<String> getOptionValues(String name) {
/* 117 */     return this.source.getOptionValues(name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<String> getNonOptionArgs() {
/* 122 */     return this.source.getNonOptionArgs();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/env/SimpleCommandLinePropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */