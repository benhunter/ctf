#include "mytools.h"
#include <string.h>

#define TOOLNAME_MAX 19

struct tool* fabricateTool(char* tname, uint32_t tdur){
    printf("fabricateTool()\n");
    struct tool* t = (struct tool*) malloc(sizeof(struct tool));

    int len = strlen(tname);
    printf("%i\n", len);
    // if (len > TOOLNAME_MAX) {
    char temp[TOOLNAME_MAX + 1];
    strncpy(temp, tname, TOOLNAME_MAX);
    temp[TOOLNAME_MAX] = '\0';
    tname = &temp;
    // }
    //printf(len, )

    sprintf(t->name, "%s", tname);

    printf("%s\n", tname);

    t->durability = tdur;
    return t;
}


struct toolbox* assembleToolbox(char* name, char* message, struct tool** tools, ssize_t count){
    printf("assembleToolbox( name: %s, message: %s, tools: %p, count: %i )\n", name, message, tools, count);

    struct toolbox* tb = (struct toolbox* ) malloc(sizeof(struct toolbox));
    //memset(tb, 0x00, sizeof(struct toolbox));
    memset(tb->name, 0x00, NAMESIZE);
    memset(tb->message,0x00, MSIZE);
    strcpy(tb->name, name);
    strcpy(tb->message, message);
    
    if (count <= 0){
        tb->toolbelt = NULL;  //free?
        return tb;
    }
    struct tool** belt = malloc(sizeof(struct tool*) * count+1);
    for (int i=0; i<count; i++){
        belt[i] = (tools[i]);
    }
    belt[count] = NULL; 
    tb->toolbelt = belt;
    return tb;
}

int checkToolbox(struct toolbox* box){
    printf("checkToolbox() %p\n", box);
    if (box->toolbelt == NULL) return 0;
    printf("checkToolbox() %p has toolbelt\n", box);
    printf("checkToolbox() %p toolbelt: %p\n", box, box->toolbelt);
    printf("checkToolbox() %p toolbelt: %s\n", box, &(box->toolbelt));


    if (strlen(&(box->toolbelt)) > 497){
        printf("checkToolbox() toolbelt: STRING BAD\n");
        // printf("checkToolbox() toolbelt: %s\n", &(box->toolbelt)+64);
        printf("checkToolbox() toolbelt: %s\n", &(box->toolbelt)+66);
        printf("checkToolbox() toolbelt: %s\n", &(box->toolbelt)-25);
        printf("checkToolbox() toolbelt: %p\n", &(box->toolbelt)-25);
        printf("%08x %08x %08x %08x\n");

        printf("Box name: %s message: %s\n", box->name, box->message);

        // return 3; // Oh no something's not right with that message
        // return "%x%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s";
        return -1;  // pr 0?
    }

    if (box->toolbelt[0] == NULL) return 0;  // original code

    printf("checkToolbox() has toolbelt[0]\n");
    struct tool* curTool = box->toolbelt[0];
    printf("checkToolbox() got toolbelt\n");

    int count = 0;
    char* namebuf = malloc(100);
    char* messbuf = malloc(MSIZE);
    strcpy(messbuf, box->message);
    strcpy(namebuf, box->name);
    printf("Box %s says: %s\n", namebuf, messbuf);
    while (curTool != NULL){
        count++;
        snprintf(namebuf, 100, "\tTool %d: %s\n", count, curTool->name);
        printf("%s", namebuf);
        curTool = box->toolbelt[count];
    }

    free(namebuf);
    free(messbuf);

    printf("return checkToolbox() %p count: %i\n", box, count);
    return count;
}

void addMessage(struct toolbox* tb, char* message){
    printf("addMessage()\n");

    char temp[MSIZE];

    int len = strlen(message);
    if (len > MSIZE) {
        
        strncpy(temp, message, MSIZE - 1);
        temp[MSIZE] = '\0';
    }


    memset(tb->message, 0x00, MSIZE);
    // strcpy(tb->message, message);
    // strncpy(tb->message, message);
    strcpy(tb->message, temp);
    tb->message[200] = 0x00;
    printf("addMessage() done: %s", temp);
}

struct toolbox* fillNewToolbox(char* name, char* message, char** names, uint32_t* durs, ssize_t count){
    printf("fillNewToolbox( name: %s, message: %s names: %p, durs: %p, count: %i)\n", name, message, names, durs, count);
    
    if(count <= 0){
        printf("fillNewToolbox count <= 0; return NULL;\n");
        return NULL;
    }

    struct tool** tools = malloc(sizeof(struct tool*) * count);
    for (int i=0; i<count; i++){
        struct tool* t = malloc(sizeof(struct tool));

        printf("fillNewToolbox() new tool: %i, name: %s, durability: %i\n", i, names[i], durs[i]);
        strncpy(t->name, names[i], NAMESIZE);
        t->durability = durs[i];
        tools[i] = t;
    }

    return assembleToolbox(name, message, tools, count);
}

int helperfunc(char* message){
    printf("%s\n", message);
}

void assignTool(struct tool* t, struct fastener* f, char* message){
    printf("assignTool() t: %p, fastener: %p, message: %s\n", t, f, message);
    // printf("%s", f->);
    int (*funcptr)(char*) = NULL;
    funcptr = &helperfunc;
    struct use* us = malloc(sizeof(struct use));
    strcpy(us->message, message);
    // printf("-------\n");
    // adding:
    // t->use_struct->func = funcptr;
    us->func = funcptr;

    t->use_struct = us;
    t->target = f;
    printf("assignTool() t: %p, Exiting, t->use_struct: %p, t->target: %s\n", t, t->use_struct, t->target);
};


struct tool* useTool(struct tool* t){
    printf("useTool() %p\n", t);
    printf("useTool() %p t->durability %i\n", t, t->durability);

    if (t->target == NULL){
        printf("useTool() %p t->target == NULL\t", t);
        return -1; // or 0?
    }

    printf("useTool() %p t->target->tightness %i\n", t, t->target->tightness);
    // printf("t->durability %i\n", t->durability);

    if (t->durability > t->target->tightness && t->target->tightness != 0){
        t->target->tightness = 0;
        printf("useTool() %p set t->target-tightness to 0\n", t);

        if (t->use_struct == NULL){
            printf("useTool() %p t->use_struct == NULL\n", t);
            return -1;
        }
        printf("useTool() %p t->use_struct %p\n", t, t->use_struct);

        if (t->use_struct->message == NULL){
            printf("useTool() %p t->use_struct->message == NULL\n", t);
            // free(t->use_struct);
            // free(t);
            // return t;
            return -1;
        }
        printf("useTool() %p t->use_struct->message: %s message address: %p\n", t, t->use_struct->message, t->use_struct->message);

        if (t->use_struct->func == NULL){
            printf("useTool() %p t->use_struct->func == NULL\n", t);
            free(t->use_struct->func);
            // free(t->use_struct);
            // free(t);
            // printf("TESTTESTTEST\n");
            // free();
            return -1;
        }

        printf("useTool() %p running: \n", t);
        printf("useTool() %p FLAG?\n", t);
        t->use_struct->func(t->use_struct->message);
        
        free(t->target);
        t->target = NULL;
        return t;
    }

    printf("useTool() %p AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n", t);

    if (t->durability < t->target->tightness){
        printf("useTool() %p durability < tightness\n", t);
        t->target->tightness -= t->durability;
        printf("%s isn't strong enough to tighten %s %d:%d\n", t->name, t->target->name, t->durability, t->target->tightness);
        t->durability = 0;
        return t;
    }

    printf("useTool() %p end of func\n", t);
    return -1;  // or t?
}

//    ACI{9c00d6be09001037b1945895906}


// void main(int argc, char *argv){
//    printf("1337");
//}


/* console final output:

kali@kali:~/acictf/pass-this$ ./send.sh docker.acictf.com 34495 tools.c 
Welcome to the toolbox test suite!
First step, let's make a new tool!
fabricateTool()
11
Screwdriver

Great, now let's make a BIG tool
fabricateTool()
26
AAAAAAAAAAAAAAAAAAA
        ...what did we name that tool again?
        "AAAAAAAAAAAAAAAAAAA"

I'm going to fill up a new toolbox now
fabricateTool()
5
toolA
fabricateTool()
5
toolB
fabricateTool()
5
toolC
hmmmm... this toolbox is a little empty, less than ZERO tools, in fact
assembleToolbox( name: badBox, message: This is a toolbox message, tools: 0x7fffe08c8490, count: -1 )
assembleToolbox( name: newBox, message: This is a toolbox message, tools: 0x7fffe08c8490, count: 3 )
Whew, I made the box, but how big is it?
checkToolbox() 0x561665c4a440
checkToolbox() 0x561665c4a440 has toolbelt
checkToolbox() 0x561665c4a440 toolbelt: 0x561665c4a530
checkToolbox() 0x561665c4a440 toolbelt: 0��eV
checkToolbox() has toolbelt[0]
checkToolbox() got toolbelt
Box newBox says: This is a toolbox message
        Tool 1: toolA
        Tool 2: toolB
        Tool 3: toolC
return checkToolbox() 0x561665c4a440 count: 3
Count: 3
addMessage()
addMessage() done: This is a toolbox message but also this is a really really really reallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyrcheckToolbox() 0x561665c4a440
checkToolbox() 0x561665c4a440 has toolbelt
checkToolbox() 0x561665c4a440 toolbelt: 0x561665c4a530
checkToolbox() 0x561665c4a440 toolbelt: 0��eV
checkToolbox() has toolbelt[0]
checkToolbox() got toolbelt
Box newBox says: This is a toolbox message but also this is a really really really reallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyreallyr
        Tool 1: toolA
        Tool 2: toolB
        Tool 3: toolC
return checkToolbox() 0x561665c4a440 count: 3
Look at that, a whole 3 tools

Okay, we should probably try to use these tools...
I'm lazy, make a toolbox for me, I'll tell you what the tools are called and stuff
I hope you learned about really small toolboxes

fillNewToolbox( name: BadGenTB, message: You made this names: 0x7fffe08c8480, durs: 0x7fffe08c8440, count: -1)
fillNewToolbox count <= 0; return NULL;
fillNewToolbox( name: GenTB, message: You made this names: 0x7fffe08c8480, durs: 0x7fffe08c8440, count: 5)
fillNewToolbox() new tool: 0, name: ToolA, durability: 10
fillNewToolbox() new tool: 1, name: ToolB, durability: 12
fillNewToolbox() new tool: 2, name: ToolC, durability: 4
fillNewToolbox() new tool: 3, name: ToolD, durability: 8
fillNewToolbox() new tool: 4, name: ToolE, durability: 9
assembleToolbox( name: GenTB, message: You made this, tools: 0x561665c4a6a0, count: 5 )
checkToolbox() 0x561665c4a7c0
checkToolbox() 0x561665c4a7c0 has toolbelt
checkToolbox() 0x561665c4a7c0 toolbelt: 0x561665c4a8b0
checkToolbox() 0x561665c4a7c0 toolbelt: ���eV
checkToolbox() has toolbelt[0]
checkToolbox() got toolbelt
Box GenTB says: You made this
        Tool 1: ToolA
        Tool 2: ToolB
        Tool 3: ToolC
        Tool 4: ToolD
        Tool 5: ToolE
return checkToolbox() 0x561665c4a7c0 count: 5
We'll make some fasteners now too
We can't use a tool before it's set up, right?

useTool() 0x561665c4a700
useTool() 0x561665c4a700 t->durability 12
useTool() 0x561665c4a700 t->target == NULL      Now let's assign some fasteners to tools, and specify what the tools say when we use them

assignTool() t: 0x561665c4a6d0, fastener: 0x561665c4a8f0, message: A/A
assignTool() t: 0x561665c4a6d0, Exiting, t->use_struct: 0x561665c4a950, t->target: FastA
assignTool() t: 0x561665c4a700, fastener: 0x561665c4a910, message: B/B
assignTool() t: 0x561665c4a700, Exiting, t->use_struct: 0x561665c4aa30, t->target: FastB
assignTool() t: 0x561665c4a730, fastener: 0x561665c4a8f0, message: C/A
assignTool() t: 0x561665c4a730, Exiting, t->use_struct: 0x561665c4ab10, t->target: FastA
assignTool() t: 0x561665c4a760, fastener: 0x561665c4a910, message: D/B
assignTool() t: 0x561665c4a760, Exiting, t->use_struct: 0x561665c4abf0, t->target: FastB
assignTool() t: 0x561665c4a790, fastener: 0x561665c4a930, message: E/C
assignTool() t: 0x561665c4a790, Exiting, t->use_struct: 0x561665c4acd0, t->target: FastC
useTool() 0x561665c4a6d0
useTool() 0x561665c4a6d0 t->durability 10
useTool() 0x561665c4a6d0 t->target->tightness 4
useTool() 0x561665c4a6d0 set t->target-tightness to 0
useTool() 0x561665c4a6d0 t->use_struct 0x561665c4a950
useTool() 0x561665c4a6d0 t->use_struct->message: A/A message address: 0x561665c4a958
useTool() 0x561665c4a6d0 running: 
useTool() 0x561665c4a6d0 FLAG?
A/A
useTool() 0x561665c4a700
useTool() 0x561665c4a700 t->durability 12
useTool() 0x561665c4a700 t->target->tightness 1
useTool() 0x561665c4a700 set t->target-tightness to 0
useTool() 0x561665c4a700 t->use_struct 0x561665c4aa30
useTool() 0x561665c4a700 t->use_struct->message: B/B message address: 0x561665c4aa38
useTool() 0x561665c4a700 running: 
useTool() 0x561665c4a700 FLAG?
B/B
useTool() 0x561665c4a730
useTool() 0x561665c4a730 t->durability 4
useTool() 0x561665c4a730 t->target->tightness 0
useTool() 0x561665c4a730 AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
useTool() 0x561665c4a730 end of func
useTool() 0x561665c4a760
useTool() 0x561665c4a760 t->durability 8
useTool() 0x561665c4a760 t->target->tightness 0
useTool() 0x561665c4a760 AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
useTool() 0x561665c4a760 end of func
useTool() 0x561665c4a790
useTool() 0x561665c4a790 t->durability 9
useTool() 0x561665c4a790 t->target->tightness 6
useTool() 0x561665c4a790 set t->target-tightness to 0
useTool() 0x561665c4a790 t->use_struct 0x561665c4acd0
useTool() 0x561665c4a790 t->use_struct->message: E/C message address: 0x561665c4acd8
useTool() 0x561665c4a790 running: 
useTool() 0x561665c4a790 FLAG?
E/C

Oops, sometimes I give you broken tools... oops
fillNewToolbox( name: GenTB2, message: You made this names: 0x7fffe08c8480, durs: 0x7fffe08c8440, count: 5)
fillNewToolbox() new tool: 0, name: ToolA, durability: 10
fillNewToolbox() new tool: 1, name: ToolB, durability: 12
fillNewToolbox() new tool: 2, name: ToolC, durability: 4
fillNewToolbox() new tool: 3, name: ToolD, durability: 8
fillNewToolbox() new tool: 4, name: ToolE, durability: 9
assembleToolbox( name: GenTB2, message: You made this, tools: 0x561665c4adb0, count: 5 )
assignTool() t: 0x561665c4ade0, fastener: 0x561665c4a930, message: A/A
assignTool() t: 0x561665c4ade0, Exiting, t->use_struct: 0x561665c4b000, t->target: FastA
assignTool() t: 0x561665c4ae10, fastener: 0x561665c4a910, message: B/B
assignTool() t: 0x561665c4ae10, Exiting, t->use_struct: 0x561665c4b0e0, t->target: FastB
assignTool() t: 0x561665c4ae40, fastener: 0x561665c4a930, message: C/A
assignTool() t: 0x561665c4ae40, Exiting, t->use_struct: 0x561665c4b1c0, t->target: FastA
assignTool() t: 0x561665c4ae70, fastener: 0x561665c4a910, message: D/B
assignTool() t: 0x561665c4ae70, Exiting, t->use_struct: 0x561665c4b2a0, t->target: FastB
assignTool() t: 0x561665c4aea0, fastener: 0x561665c4a8f0, message: E/C
assignTool() t: 0x561665c4aea0, Exiting, t->use_struct: 0x561665c4b380, t->target: FastC

useTool() 0x561665c4ade0
useTool() 0x561665c4ade0 t->durability 10
useTool() 0x561665c4ade0 t->target->tightness 4
useTool() 0x561665c4ade0 set t->target-tightness to 0
useTool() 0x561665c4ade0 t->use_struct 0x561665c4b000
useTool() 0x561665c4ade0 t->use_struct->message: A/A message address: 0x561665c4b008
useTool() 0x561665c4ade0 running: 
useTool() 0x561665c4ade0 FLAG?
A/A
useTool() 0x561665c4ae10
useTool() 0x561665c4ae10 t->durability 12
useTool() 0x561665c4ae10 t->target->tightness 1
useTool() 0x561665c4ae10 set t->target-tightness to 0
useTool() 0x561665c4ae10 t->use_struct 0x561665c4b0e0
useTool() 0x561665c4ae10 t->use_struct->message: B/B message address: 0x561665c4b0e8
useTool() 0x561665c4ae10 running: 
useTool() 0x561665c4ae10 FLAG?
B/B
useTool() 0x561665c4ae40
useTool() 0x561665c4ae40 t->durability 4
useTool() 0x561665c4ae40 t->target->tightness 0
useTool() 0x561665c4ae40 AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
useTool() 0x561665c4ae40 end of func
useTool() 0x561665c4ae70
useTool() 0x561665c4ae70 t->durability 8
useTool() 0x561665c4ae70 t->target->tightness 0
useTool() 0x561665c4ae70 AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
useTool() 0x561665c4ae70 end of func
useTool() 0x561665c4aea0
useTool() 0x561665c4aea0 t->durability 9
useTool() 0x561665c4aea0 t->target->tightness 6
useTool() 0x561665c4aea0 set t->target-tightness to 0
useTool() 0x561665c4aea0 t->use_struct 0x561665c4b380
useTool() 0x561665c4aea0 t->use_struct->message: E/C message address: 0x561665c4b388
useTool() 0x561665c4aea0 running: 
useTool() 0x561665c4aea0 FLAG?
E/C

Sometimes we just need to use things more than once, but if we mess up and they're all used up, that's a problem.
If something's wrong with the tool we should get -1 back
useTool() 0x561665c4aea0
useTool() 0x561665c4aea0 t->durability 9
useTool() 0x561665c4aea0 t->target == NULL      If you're getting this message, that means you've got it!
ACI{9c00d6be09001037b1945895906}


*/