// drillsolve.c

typedef unsigned char   undefined;

typedef unsigned char    byte;
typedef unsigned char    dwfenc;
typedef unsigned int    dword;
typedef unsigned long    qword;
typedef unsigned int    uint;
typedef unsigned long    ulong;
typedef unsigned int    undefined4;
typedef unsigned long    undefined8;
typedef unsigned short    word;
typedef struct Elf64_Dyn Elf64_Dyn, *PElf64_Dyn;

typedef enum Elf64_DynTag {
    DT_AUDIT=1879047932,
    DT_AUXILIARY=2147483645,
    DT_BIND_NOW=24,
    DT_CHECKSUM=1879047672,
    DT_CONFIG=1879047930,
    DT_DEBUG=21,
    DT_DEPAUDIT=1879047931,
    DT_ENCODING=32,
    DT_FEATURE_1=1879047676,
    DT_FILTER=2147483647,
    DT_FINI=13,
    DT_FINI_ARRAY=26,
    DT_FINI_ARRAYSZ=28,
    DT_FLAGS=30,
    DT_FLAGS_1=1879048187,
    DT_GNU_CONFLICT=1879047928,
    DT_GNU_CONFLICTSZ=1879047670,
    DT_GNU_HASH=1879047925,
    DT_GNU_LIBLIST=1879047929,
    DT_GNU_LIBLISTSZ=1879047671,
    DT_GNU_PRELINKED=1879047669,
    DT_HASH=4,
    DT_INIT=12,
    DT_INIT_ARRAY=25,
    DT_INIT_ARRAYSZ=27,
    DT_JMPREL=23,
    DT_MOVEENT=1879047674,
    DT_MOVESZ=1879047675,
    DT_MOVETAB=1879047934,
    DT_NEEDED=1,
    DT_NULL=0,
    DT_PLTGOT=3,
    DT_PLTPAD=1879047933,
    DT_PLTPADSZ=1879047673,
    DT_PLTREL=20,
    DT_PLTRELSZ=2,
    DT_POSFLAG_1=1879047677,
    DT_PREINIT_ARRAYSZ=33,
    DT_REL=17,
    DT_RELA=7,
    DT_RELACOUNT=1879048185,
    DT_RELAENT=9,
    DT_RELASZ=8,
    DT_RELCOUNT=1879048186,
    DT_RELENT=19,
    DT_RELSZ=18,
    DT_RPATH=15,
    DT_RUNPATH=29,
    DT_SONAME=14,
    DT_STRSZ=10,
    DT_STRTAB=5,
    DT_SYMBOLIC=16,
    DT_SYMENT=11,
    DT_SYMINENT=1879047679,
    DT_SYMINFO=1879047935,
    DT_SYMINSZ=1879047678,
    DT_SYMTAB=6,
    DT_TEXTREL=22,
    DT_TLSDESC_GOT=1879047927,
    DT_TLSDESC_PLT=1879047926,
    DT_VERDEF=1879048188,
    DT_VERDEFNUM=1879048189,
    DT_VERNEED=1879048190,
    DT_VERNEEDNUM=1879048191,
    DT_VERSYM=1879048176
} Elf64_DynTag;

struct Elf64_Dyn {
    enum Elf64_DynTag d_tag;
    qword d_val;
};

typedef struct Elf64_Ehdr Elf64_Ehdr, *PElf64_Ehdr;

struct Elf64_Ehdr {
    byte e_ident_magic_num;
    char e_ident_magic_str[3];
    byte e_ident_class;
    byte e_ident_data;
    byte e_ident_version;
    byte e_ident_pad[9];
    word e_type;
    word e_machine;
    dword e_version;
    qword e_entry;
    qword e_phoff;
    qword e_shoff;
    dword e_flags;
    word e_ehsize;
    word e_phentsize;
    word e_phnum;
    word e_shentsize;
    word e_shnum;
    word e_shstrndx;
};

typedef struct Elf64_Phdr Elf64_Phdr, *PElf64_Phdr;

typedef enum Elf_ProgramHeaderType {
    PT_DYNAMIC=2,
    PT_GNU_EH_FRAME=1685382480,
    PT_GNU_RELRO=1685382482,
    PT_GNU_STACK=1685382481,
    PT_INTERP=3,
    PT_LOAD=1,
    PT_NOTE=4,
    PT_NULL=0,
    PT_PHDR=6,
    PT_SHLIB=5,
    PT_TLS=7
} Elf_ProgramHeaderType;

struct Elf64_Phdr {
    enum Elf_ProgramHeaderType p_type;
    dword p_flags;
    qword p_offset;
    qword p_vaddr;
    qword p_paddr;
    qword p_filesz;
    qword p_memsz;
    qword p_align;
};

typedef struct Elf64_Rela Elf64_Rela, *PElf64_Rela;

struct Elf64_Rela {
    qword r_offset; /* location to apply the relocation action */
    qword r_info; /* the symbol table index and the type of relocation */
    qword r_addend; /* a constant addend used to compute the relocatable field value */
};

typedef struct Elf64_Shdr Elf64_Shdr, *PElf64_Shdr;

typedef enum Elf_SectionHeaderType {
    SHT_CHECKSUM=1879048184,
    SHT_DYNAMIC=6,
    SHT_DYNSYM=11,
    SHT_FINI_ARRAY=15,
    SHT_GNU_ATTRIBUTES=1879048181,
    SHT_GNU_HASH=1879048182,
    SHT_GNU_LIBLIST=1879048183,
    SHT_GNU_verdef=1879048189,
    SHT_GNU_verneed=1879048190,
    SHT_GNU_versym=1879048191,
    SHT_GROUP=17,
    SHT_HASH=5,
    SHT_INIT_ARRAY=14,
    SHT_NOBITS=8,
    SHT_NOTE=7,
    SHT_NULL=0,
    SHT_PREINIT_ARRAY=16,
    SHT_PROGBITS=1,
    SHT_REL=9,
    SHT_RELA=4,
    SHT_SHLIB=10,
    SHT_STRTAB=3,
    SHT_SUNW_COMDAT=1879048187,
    SHT_SUNW_move=1879048186,
    SHT_SUNW_syminfo=1879048188,
    SHT_SYMTAB=2,
    SHT_SYMTAB_SHNDX=18
} Elf_SectionHeaderType;

struct Elf64_Shdr {
    dword sh_name;
    enum Elf_SectionHeaderType sh_type;
    qword sh_flags;
    qword sh_addr;
    qword sh_offset;
    qword sh_size;
    dword sh_link;
    dword sh_info;
    qword sh_addralign;
    qword sh_entsize;
};

typedef struct Elf64_Sym Elf64_Sym, *PElf64_Sym;

struct Elf64_Sym {
    dword st_name;
    byte st_info;
    byte st_other;
    word st_shndx;
    qword st_value;
    qword st_size;
};

typedef ulong size_t;

typedef long __time_t;

typedef __time_t time_t;

typedef struct eh_frame_hdr eh_frame_hdr, *Peh_frame_hdr;

struct eh_frame_hdr {
    byte eh_frame_hdr_version; /* Exception Handler Frame Header Version */
    dwfenc eh_frame_pointer_encoding; /* Exception Handler Frame Pointer Encoding */
    dwfenc eh_frame_desc_entry_count_encoding; /* Encoding of # of Exception Handler FDEs */
    dwfenc eh_frame_table_encoding; /* Exception Handler Table Encoding */
};

typedef struct fde_table_entry fde_table_entry, *Pfde_table_entry;

struct fde_table_entry {
    dword initial_loc; /* Initial Location */
    dword data_loc; /* Data location */
};


long crashInHere(long ptrTo32Bytes,long int0x20)

{
  int iRand;
  time_t timeSeedMaybe;
  long in_FS_OFFSET;
  ulong counter;
  undefined8 local_48;
  undefined8 local_40;
  undefined8 local_38;
  undefined8 local_30;
  undefined8 local_28;
  undefined8 local_20;
  long local_10;
  
  local_10 = *(long *)(in_FS_OFFSET + 0x28);
  timeSeedMaybe = time((time_t *)0x0);
  srand((uint)timeSeedMaybe);
  local_48 = 0x6867666564636261;
  local_40 = 0x706f6e6d6c6b6a69;
  local_38 = 0x7877767574737271;
  local_30 = 0x4645444342417a79;
  local_28 = 0x3332314b4a494847;
  local_20 = 0x30393837363534;
  if (int0x20 != 0) {
    counter = 0;
    while (counter < int0x20 - 1U) {
      iRand = rand();
      *(undefined *)(ptrTo32Bytes + counter) =
           *(undefined *)((long)&local_48 + (long)(iRand % 0x2f));
      counter = counter + 1;
    }
    *(undefined *)((int0x20 - 1U) + ptrTo32Bytes) = 0;
  }
                    /* Should be true because it was assigned true at the top. */
  if (local_10 != *(long *)(in_FS_OFFSET + 0x28)) {
                    /* WARNING: Subroutine does not return */
                    /* Intentional Crash here maybe? Should never run */
    __stack_chk_fail();
  }
  return ptrTo32Bytes;
}


void * generatePassword(char *param_1)
{
  char cVar1;
  void *ptrToHugeMallocRAX;
  size_t sVar2;
  size_t int26;
  int counter;
  
                    /* Huge malloc */
//   ptrToHugeMallocRAX = malloc(0xffffffffffffffff);
    ptrToHugeMallocRAX = malloc(0xffff);


  sVar2 = strlen(param_1);
  counter = 0;
  while ((ulong)(long)counter < sVar2) {
    cVar1 = param_1[counter];
    int26 = strlen("ABCDEFGHIJKHIJKLMNOPQRSTUVWXYZ");
                    /* COREDUMP HERE */
    *(char *)((long)ptrToHugeMallocRAX + (long)counter) =
         "ABCDEFGHIJKHIJKLMNOPQRSTUVWXYZ"[(int)((ulong)(long)cVar1 % int26)];
    counter = counter + 1;
  }
  return ptrToHugeMallocRAX;
}


undefined8 mainLookingFunc(void)

{
  char *ptrTo32Bytes;
  char *chrPtrPassword;
  
  ptrTo32Bytes = (char *)malloc(0x20);
  crashInHere((long)ptrTo32Bytes,0x20);
  chrPtrPassword = (char *)generatePassword(ptrTo32Bytes);
  puts(chrPtrPassword);
  free(ptrTo32Bytes);
  free(chrPtrPassword);
  return 0;
}


