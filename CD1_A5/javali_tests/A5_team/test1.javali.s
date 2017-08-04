# Primitive Type Vtable entries:
.section .data
intLabel_L144: .long 0
floatLabel_L145: .long 0
booleanLabel_L146: .long 0
A_Class_L136Array_L147: .long 0
Main_L134Array_L148: .long 0
intLabel_L144Array_L149: .long 0
floatLabel_L145Array_L150: .long 0
booleanLabel_L146Array_L151: .long 0
A_Super_Class_L135Array_L152: .long 0
.globl main
.type	main, @function
.section .data
intFormatString_L153: .string "%d"
floatFormatString_L154: .string "%f"
newLineString_L155: .string "\n"
double_L156: 
  .long 0 
  .long 0
double_L157: 
  .long 0 
  .long 0
.section .text
# main program entry
    main:
    pushl $8
    call	malloc
    addl $4, %esp
    movl %eax, (-8)(%esp)
    movl $Main_L134, (%eax)
    call Main_main_L143
    movl $0, %eax
    ret
    
# checkIsSubclass:
    IsSubtypeOfCheck_L158:
    movl (-4)(%esp), %eax
    start_Label_L161:
    cmpl (-8)(%esp), %eax
    je ok_Label_L159
    cmpl $0, %eax
    je nok_Label_L160
    movl (%eax), %eax
    jmp start_Label_L161
    nok_Label_L160:
    # Exception:
        movl $1, %eax
        movl %eax, (%esp)
        call exit
    ok_Label_L159:
    ret
    
    
# Emitting class Main {...}
    # Class Table:
        .section .data
        Main_L134:
        # superclass Table Pointer
        .long 0
        # virtual method table
        .long Main_main_L143
    # Methods:
        .section .text
        # Emitting void main(...) {...}
            Main_main_L143:
            # Emitting (...)
                # Emitting a = new A_Class()
                    # Emitting new A_Class()
                        addl $(-24), %esp
                        pushl $12
                        call malloc
                        addl $(28), %esp
                        movl %eax, %esi
                        movl $A_Class_L136, (%eax)
                    movl %esi, (-12)(%esp)
                # Emitting a.x = 99
                    # Emitting 99
                        movl $99, %esi
                    # store reg to temp
                        movl %esi, (-20)(%esp)
                    # Emitting a
                        movl (-12)(%esp), %esi
                    # restore reg from temp
                        movl (-20)(%esp), %edi
                    movl %edi, (4)(%esi)
                # Emitting a.y = 0.5000000000
                    # Emitting 0.5000000000
                        movl $1056964608, %esi
                    # store reg to temp
                        movl %esi, (-20)(%esp)
                    # Emitting a
                        movl (-12)(%esp), %esi
                    # restore reg from temp
                        movl (-20)(%esp), %edi
                    movl %edi, (8)(%esi)
                # Emitting object = a
                    # Emitting a
                        movl (-12)(%esp), %esi
                    movl (-4)(%esp), %eax
                    movl %esi, (4)(%eax)
                # Emitting object.updateAndPrint(...)
                    # Emitting 12
                        movl $12, %esi
                    movl %esi, (-32)(%esp)
                    # Emitting object
                        movl (-4)(%esp), %eax
                        movl (4)(%eax), %esi
                    movl %esi, (-28)(%esp)
                    cmpl $0, %esi
                    jne Ok_L162
                    # Exception:
                        movl $4, %eax
                        movl %eax, (%esp)
                        call exit
                    Ok_L162:
                    movl (%esi), %eax
                    movl (4)(%eax), %eax
                    addl $(-20), %esp
                    call *%eax
                    addl $(20), %esp
                # Emitting object.updateAndPrint(...)
                    # Emitting 14
                        movl $14, %esi
                    movl %esi, (-32)(%esp)
                    # Emitting object
                        movl (-4)(%esp), %eax
                        movl (4)(%eax), %esi
                    movl %esi, (-28)(%esp)
                    cmpl $0, %esi
                    jne Ok_L163
                    # Exception:
                        movl $4, %eax
                        movl %eax, (%esp)
                        call exit
                    Ok_L163:
                    movl (%esi), %eax
                    movl (4)(%eax), %eax
                    addl $(-20), %esp
                    call *%eax
                    addl $(20), %esp
                # Emitting write(a.getX(...))
                    # Emitting a.getX(...)
                        # Emitting a
                            movl (-12)(%esp), %esi
                        movl %esi, (-28)(%esp)
                        cmpl $0, %esi
                        jne Ok_L164
                        # Exception:
                            movl $4, %eax
                            movl %eax, (%esp)
                            call exit
                        Ok_L164:
                        movl (%esi), %eax
                        movl (16)(%eax), %eax
                        addl $(-20), %esp
                        call *%eax
                        addl $(20), %esp
                        movl %eax, %esi
                    movl %esi, (-24)(%esp)
                    movl $intFormatString_L153, %eax
                    movl %eax, (-28)(%esp)
                    addl $(-28), %esp
                    call	printf
                    addl $(28), %esp
                # Emitting writeln()
                    movl $newLineString_L155, %eax
                    movl %eax, (-24)(%esp)
                    addl $(-24), %esp
                    call	printf
                    addl $(24), %esp
            ret
    
# Emitting class A_Super_Class {...}
    # Class Table:
        .section .data
        A_Super_Class_L135:
        # superclass Table Pointer
        .long 0
        # virtual method table
        .long A_Super_Class_updateAndPrint_L137
        .long A_Super_Class_print_L138
        .long A_Super_Class_update_L139
    # Methods:
        .section .text
        # Emitting void updateAndPrint(...) {...}
            A_Super_Class_updateAndPrint_L137:
            # Emitting (...)
                # Emitting this.update(...)
                    # Emitting amount
                        movl (-8)(%esp), %esi
                    movl %esi, (-24)(%esp)
                    # Emitting this
                        movl (-4)(%esp), %esi
                    movl %esi, (-20)(%esp)
                    cmpl $0, %esi
                    jne Ok_L165
                    # Exception:
                        movl $4, %eax
                        movl %eax, (%esp)
                        call exit
                    Ok_L165:
                    movl (%esi), %eax
                    movl (12)(%eax), %eax
                    addl $(-12), %esp
                    call *%eax
                    addl $(12), %esp
                # Emitting this.print(...)
                    # Emitting this
                        movl (-4)(%esp), %esi
                    movl %esi, (-20)(%esp)
                    cmpl $0, %esi
                    jne Ok_L166
                    # Exception:
                        movl $4, %eax
                        movl %eax, (%esp)
                        call exit
                    Ok_L166:
                    movl (%esi), %eax
                    movl (8)(%eax), %eax
                    addl $(-12), %esp
                    call *%eax
                    addl $(12), %esp
            ret
        # Emitting void print(...) {...}
            A_Super_Class_print_L138:
            # Emitting (...)
            ret
        # Emitting void update(...) {...}
            A_Super_Class_update_L139:
            # Emitting (...)
            ret
    
# Emitting class A_Class {...}
    # Class Table:
        .section .data
        A_Class_L136:
        # superclass Table Pointer
        .long A_Super_Class_L135
        # virtual method table
        .long A_Super_Class_updateAndPrint_L137
        .long A_Class_print_L140
        .long A_Class_update_L141
        .long A_Class_getX_L142
    # Methods:
        .section .text
        # Emitting void print(...) {...}
            A_Class_print_L140:
            # Emitting (...)
                # Emitting write(x)
                    # Emitting x
                        movl (-4)(%esp), %eax
                        movl (4)(%eax), %esi
                    movl %esi, (-12)(%esp)
                    movl $intFormatString_L153, %eax
                    movl %eax, (-16)(%esp)
                    addl $(-16), %esp
                    call	printf
                    addl $(16), %esp
                # Emitting writeln()
                    movl $newLineString_L155, %eax
                    movl %eax, (-12)(%esp)
                    addl $(-12), %esp
                    call	printf
                    addl $(12), %esp
                # Emitting writef(y)
                    # Emitting y
                        movl (-4)(%esp), %eax
                        movl (8)(%eax), %esi
                    movl %esi, (-12)(%esp)
                    flds (-12)(%esp)
                    fstpl (-12)(%esp)
                    movl $floatFormatString_L154, %eax
                    movl %eax, (-16)(%esp)
                    addl $(-16), %esp
                    call	printf
                    addl $(16), %esp
                # Emitting writeln()
                    movl $newLineString_L155, %eax
                    movl %eax, (-12)(%esp)
                    addl $(-12), %esp
                    call	printf
                    addl $(12), %esp
                # Emitting writeln()
                    movl $newLineString_L155, %eax
                    movl %eax, (-12)(%esp)
                    addl $(-12), %esp
                    call	printf
                    addl $(12), %esp
            ret
        # Emitting void update(...) {...}
            A_Class_update_L141:
            # Emitting (...)
                # Emitting x = amount
                    # Emitting amount
                        movl (-8)(%esp), %esi
                    movl (-4)(%esp), %eax
                    movl %esi, (4)(%eax)
            ret
        # Emitting int getX(...) {...}
            A_Class_getX_L142:
            # Emitting (...)
                # Emitting return x
                    # Emitting x
                        movl (-4)(%esp), %eax
                        movl (4)(%eax), %esi
                    movl %esi, %eax
                    ret
            ret
    
