package cd.codegen;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import cd.Config;
import cd.Main;
import cd.debug.AstOneLine;
import cd.exceptions.ToDoException;
import cd.ir.Ast;
import cd.ir.Ast.Assign;
import cd.ir.Ast.BinaryOp;
import cd.ir.Ast.BooleanConst;
import cd.ir.Ast.BuiltInRead;
import cd.ir.Ast.BuiltInReadFloat;
import cd.ir.Ast.BuiltInWrite;
import cd.ir.Ast.BuiltInWriteFloat;
import cd.ir.Ast.BuiltInWriteln;
import cd.ir.Ast.Cast;
import cd.ir.Ast.ClassDecl;
import cd.ir.Ast.Expr;
import cd.ir.Ast.Field;
import cd.ir.Ast.FloatConst;
import cd.ir.Ast.IfElse;
import cd.ir.Ast.Index;
import cd.ir.Ast.IntConst;
import cd.ir.Ast.MethodCall;
import cd.ir.Ast.MethodCallExpr;
import cd.ir.Ast.MethodDecl;
import cd.ir.Ast.NewArray;
import cd.ir.Ast.NewObject;
import cd.ir.Ast.NullConst;
import cd.ir.Ast.ReturnStmt;
import cd.ir.Ast.ThisRef;
import cd.ir.Ast.UnaryOp;
import cd.ir.Ast.Var;
import cd.ir.Ast.WhileLoop;
import cd.ir.AstVisitor;
import cd.ir.ExprVisitor;
import cd.ir.Symbol.ArrayTypeSymbol;
import cd.ir.Symbol.ClassSymbol;
import cd.util.Tuple;

public class AstCodeGenerator {

	protected final RegsNeededVisitor regsNeededVisitor = new RegsNeededVisitor();
	protected final ExprGenerator eg = new ExprGenerator();
	protected final StmtDeclGenerator sdg = new StmtDeclGenerator();
	protected final Writer out;
	protected final Main main;
	
	protected final ClassTable classTable = new ClassTable(); 
	
	protected Ast.ClassDecl currentClass;
	protected ClassData currentClassData;
	protected Ast.MethodDecl currentMethod;
	protected MethodData currentMethodData;
	protected MethodData currentStackFrame;
	
	String progReturnAddressLabel;
	String intFormatStringLabel;
	String floatFormatStringLabel;
	String newLineStringLabel;
	
	String isSubtypeOfLabel;
	
	String doubleLabel1;
	String doubleLabel2;
	
	// Simplistic Register management:

	List<String> regs = new ArrayList<String>();
	List<String> allRegs = new ArrayList<String>();

	final String BP = "%eax", SP = "%esp";

	public AstCodeGenerator(Main main, Writer out) {
		this.out = out;
		this.main = main;
	}

	public void debug(String format, Object... args) {
		this.main.debug(format, args);
	}

	/**
	 * Main method. Causes us to emit x86 assembly corresponding to {@code ast}
	 * into {@code file}. Throws a {@link RuntimeException} should any I/O error
	 * occur.
	 * 
	 * <p>
	 * The generated file will be divided into three sections:
	 * <ol>
	 * <li>Prologue: Generated by {@link #emitPrologue()}. This contains any
	 * introductory declarations and the like.
	 * <li>Body: Generated by {@link ExprGenerator}. This contains the main
	 * method definitions.
	 * <li>Epilogue: Generated by {@link #emitEpilogue()}. This contains any
	 * final declarations required.
	 * </ol>
	 */
	public void go(List<? extends ClassDecl> astRoots) {
		
		initRegs();
		
		for (ClassDecl ast : astRoots) {
			classTable.addClass(ast);
		}
		classTable.update(regsNeededVisitor);
		
		ClassData intTable = new ClassData();
		intTable.updated = true;
		intTable.superClass = null;
		intTable.labelName = "intLabel_L"+(ClassTable.counter++);
		classTable.classes.put("int", intTable);
		
		ClassData floatTable = new ClassData();
		floatTable.updated = true;
		floatTable.superClass = null;
		floatTable.labelName = "floatLabel_L"+(ClassTable.counter++);
		classTable.classes.put("foat", floatTable);
		
		ClassData booleanTable = new ClassData();
		booleanTable.updated = true;
		booleanTable.superClass = null;
		booleanTable.labelName = "booleanLabel_L"+(ClassTable.counter++);
		classTable.classes.put("boolean", booleanTable);
		
		emitComment("Primitive Type Vtable entries:");
		emit(".section .data");
		emit(intTable.labelName+": .long 0");
		emit(floatTable.labelName+": .long 0");
		emit(booleanTable.labelName+": .long 0");
		
		HashMap<String, ClassData> arrayTypes = new HashMap<String, ClassData>();
		
		for (Entry<String, ClassData> v : classTable.classes.entrySet())
		{
			ClassData cd = v.getValue();
			
			ClassData newCd = new ClassData();
			newCd.labelName = cd.labelName+"Array_L"+(ClassTable.counter++);
			newCd.superClass = null;
			newCd.updated = true;
			arrayTypes.put(v.getKey()+"[]", newCd);
			emit(newCd.labelName+": .long 0");
		}
		classTable.classes.putAll(arrayTypes);
		
		generateAsmHeader();
		
		for (ClassDecl ast : astRoots) {
			sdg.gen(ast);
		}
		

		
		
		
		assert regs.isEmpty();
	}

	protected void generateAsmHeader()
	{
		ClassData classData = classTable.classes.get("Main");
		MethodData methodData = classData.methods.get(classData.methodNumber.get("main"));
		
		emit(".globl main");
		emit(".type	main, @function");
		
		emit(".section .data");
//		progReturnAddressLabel = "progReturnAddress_L"+(ClassTable.counter++);
//		emit(progReturnAddressLabel+": .long 0");
		intFormatStringLabel = "intFormatString_L"+(ClassTable.counter++);
		emit(intFormatStringLabel+":", ".string \"%d\"");
		floatFormatStringLabel = "floatFormatString_L"+(ClassTable.counter++);
		emit(floatFormatStringLabel+":", ".string \"%f\"");
		newLineStringLabel = "newLineString_L"+(ClassTable.counter++);
		emit(newLineStringLabel+": .string \"\\n\"");
		doubleLabel1 = "double_L"+(ClassTable.counter++);
		doubleLabel2 = "double_L"+(ClassTable.counter++);

		emit(doubleLabel1+": \n  .long 0 \n  .long 0");
		emit(doubleLabel2+": \n  .long 0 \n  .long 0");

		emit(".section .text");
		emitIndent("main program entry");
			emit("main:");
//			emit("movl", "(%esp)", "%eax");
//			emit("movl", "%eax", progReturnAddressLabel);

			
			// malloc memory for mainObject
			emit("pushl $"+(classData.fieldOffsets.size()*4+4));
			emit("call	malloc");
			emit("addl", "$4", "%esp");
			
			// push address to mainObject to stack and call Main.main()
			emit("movl", "%eax", "(-8)(%esp)");
			
			// set vtable
			emit("movl", "$"+classData.labelName, "(%eax)");
			
			
			emit("call "+methodData.labelName);
			
			emit("movl", "$0", "%eax");
			emit("ret");
			emit("");			
		emitUndent();
		
		
		// cast check method
		emitIndent("checkIsSubclass:");
		isSubtypeOfLabel = "IsSubtypeOfCheck_L"+(ClassTable.counter++);
		String okLabel = "ok_Label_L"+(ClassTable.counter++);
		String nokLabel = "nok_Label_L"+(ClassTable.counter++);
		String startLabel = "start_Label_L"+(ClassTable.counter++);
		
		emit(isSubtypeOfLabel+":");
		emit("movl", "(-4)(%esp)", "%eax");
		
		
		emit(startLabel+":");
		emit("cmpl", "(-8)(%esp)", "%eax");
		emit("je", okLabel);
		emit("cmpl", "$0", "%eax");
		emit("je", nokLabel);
		emit("movl", "(%eax)", "%eax");
		emit("jmp", startLabel);
		
		emit(nokLabel+":");
		exception("$1");
		
		emit(okLabel+":");
		emit("ret");
		emit("");
		emit("");
		emitUndent();
	}

	
	void exception(String from)
	{
		emitIndent("Exception:");
		emit("movl", from, "%eax");
		emit("movl", "%eax", "(%esp)");
		emit("call exit");
//		
//		
//		emit("movl", from, "%eax");
//		emit("pushl", progReturnAddressLabel);
//		emit("ret");
		emitUndent();
	}
	
	ArrayList<Tuple<String, String>> saveToStack()
	{
		ArrayList<Tuple<String, String>> regsStored = new ArrayList<Tuple<String,String>>();
		for (String r: allRegs)
		{
			if (!isFree(r))
			{
				regsStored.add(new Tuple<String, String>(r, currentMethodData.storeRegToTemp(r, AstCodeGenerator.this)));
				releaseReg(r);
			}
		}
		return regsStored;
	}
	
	void restoreRegsFromStack(ArrayList<Tuple<String, String>> regsStored)
	{
		for(Tuple<String, String> sr: regsStored)
		{
			getReg(sr.a);
			currentMethodData.restoreRegFromTemp(sr.b, sr.a, AstCodeGenerator.this);
		}
	}

	protected void initRegs() {
		regs.clear();
		
		regs.add("%ebp");
		regs.add("%ebx");
		regs.add("%ecx");
		regs.add("%edx");
		regs.add("%edi");
		regs.add("%esi");
		
		allRegs.addAll(regs);
	}

	protected String getReg() {
		if (regs.isEmpty())
			throw new ToDoException("Not enought registers");
		return regs.remove(regs.size()-1);
	}
	
	protected String getReg(String name)
	{
		regs.remove(name);
		return name;
	}

	protected void releaseReg(String reg) {
		if (regs.contains(reg))
			throw new ToDoException("Register already used");
		regs.add(reg);
	}

	protected boolean isFree(String reg) {
		return regs.contains(reg);
	}

	
	/**
	 * Generates code to evaluate expressions. After emitting the code, returns
	 * a String which indicates the register where the result can be found.
	 */
	protected class ExprGenerator extends ExprVisitor<String, Void> {

		public String gen(Expr ast) {
			return visit(ast, null);
		}

		@Override
		public String visit(Expr ast, Void arg) {

			try {
				emitIndent("Emitting " + AstOneLine.toString(ast));
				return super.visit(ast, null);
			} finally {
				emitUndent();
			}

		}

		@Override
		public String binaryOp(BinaryOp ast, Void arg) {
			int leftRN = regsNeededVisitor.calc(ast.left());
			int rightRN = regsNeededVisitor.calc(ast.right());
			boolean spill = regsNeededVisitor.calc(ast) > regs.size();
			String leftReg = null;
			String rightReg = null;
			if (leftRN >= rightRN) {
				if (spill)
				{
					leftReg = gen(ast.left());
					String temp = currentMethodData.storeRegToTemp(leftReg, AstCodeGenerator.this);
					releaseReg(leftReg);
					rightReg = gen(ast.right());
					leftReg = getReg();
					currentMethodData.restoreRegFromTemp(temp, leftReg, AstCodeGenerator.this);
				}
				else
				{
					leftReg = gen(ast.left());
					rightReg = gen(ast.right());
				}
			} else {
				if (spill)
				{
					rightReg = gen(ast.right());
					String temp = currentMethodData.storeRegToTemp(rightReg, AstCodeGenerator.this);
					releaseReg(rightReg);
					leftReg = gen(ast.left());
					rightReg = getReg();
					currentMethodData.restoreRegFromTemp(temp, rightReg, AstCodeGenerator.this);
				}
				else
				{
					rightReg = gen(ast.right());
					leftReg = gen(ast.left());
				}
			}
			debug("Binary Op: %s (%s,%s)", ast, leftReg, rightReg);
			
			if (ast.left().type.name.equals("float"))
			{
				String leftOnStack;
				String rightOnStack;
				
				switch (ast.operator) {
				case B_PLUS:
					emit("movl", leftReg, doubleLabel1);
					emit("movl", rightReg, doubleLabel2);
					emit("flds", doubleLabel1);
					emit("fadds", doubleLabel2);
					emit("fstps", doubleLabel1);
					emit("movl", doubleLabel1, leftReg);
					break;
				case B_MINUS:
					emit("movl", leftReg, doubleLabel1);
					emit("movl", rightReg, doubleLabel2);
					emit("flds", doubleLabel1);
					emit("fsubs", doubleLabel2);
					emit("fstps", doubleLabel1);
					emit("movl", doubleLabel1, leftReg);
					break;
				case B_TIMES:
					emit("movl", leftReg, doubleLabel1);
					emit("movl", rightReg, doubleLabel2);
					emit("flds", doubleLabel1);
					emit("fmuls", doubleLabel2);
					emit("fstps", doubleLabel1);
					emit("movl", doubleLabel1, leftReg);
					break;
				case B_DIV:
					emit("movl", leftReg, doubleLabel1);
					emit("movl", rightReg, doubleLabel2);
					emit("flds", doubleLabel1);
					emit("fdivs", doubleLabel2);
					emit("fstps", doubleLabel1);
					emit("movl", doubleLabel1, leftReg);
					break;
				case B_EQUAL:
					emit("cmpl", rightReg, leftReg);
					emit("sete", "%al");
					emit("movzbl", "%al", leftReg);
					break;
				case B_NOT_EQUAL:
					emit("cmpl", rightReg, leftReg);
					emit("setne", "%al");
					emit("movzbl", "%al", leftReg);
					break;
				case B_LESS_THAN:
					emit("movl", leftReg, doubleLabel1);
					emit("movl", rightReg, doubleLabel2);
					emit("flds", doubleLabel1);
					emit("flds", doubleLabel2);
					emit("fucomip",	"%st(1)", "%st");
					emit("fstp", "%st(0)");
					emit("seta", "%al");
					emit("movzbl", "%al", leftReg);
					break;
				case B_LESS_OR_EQUAL:
					emit("movl", leftReg, doubleLabel1);
					emit("movl", rightReg, doubleLabel2);
					emit("flds", doubleLabel1);
					emit("flds", doubleLabel2);
					emit("fucomip",	"%st(1)", "%st");
					emit("fstp", "%st(0)");
					emit("setae", "%al");
					emit("movzbl", "%al", leftReg);
					break;
				case B_GREATER_THAN:
					emit("movl", leftReg, doubleLabel1);
					emit("movl", rightReg, doubleLabel2);
					emit("flds", doubleLabel2);
					emit("flds", doubleLabel1);
					emit("fucomip",	"%st(1)", "%st");
					emit("fstp", "%st(0)");
					emit("setae", "%al");
					emit("movzbl", "%al", leftReg);
					break;
				case B_GREATER_OR_EQUAL:
					emit("movl", leftReg, doubleLabel1);
					emit("movl", rightReg, doubleLabel2);
					emit("flds", doubleLabel2);
					emit("flds", doubleLabel1);
					emit("fucomip",	"%st(1)", "%st");
					emit("fstp", "%st(0)");
					emit("seta", "%al");
					emit("movzbl", "%al", leftReg);
					break;
				default:
					{
						throw new ToDoException();
					}
				}
			}
			else
			{
				switch (ast.operator) {
				case B_TIMES:
					emit("imul", rightReg, leftReg);
					break;
				case B_DIV:
				{
					// put left arg to eax
					String oldEAX = null;
					String oldEDX = null;
					boolean eaxUsed = false;
					if (!isFree("%ebp") && !(leftReg.equals("%ebp") || rightReg.equals("%ebp")))
					{
						oldEAX = currentMethodData.storeRegToTemp("%ebp", AstCodeGenerator.this);
						eaxUsed = true;
					}
					boolean edxUsed = false;
					if (!isFree("%edx") && !(leftReg.equals("%edx") || rightReg.equals("%edx")))
					{
						oldEDX = currentMethodData.storeRegToTemp("%edx", AstCodeGenerator.this);
						edxUsed = true;
					}
					
					emit("movl", rightReg, "%ebp");
					emit("movl", leftReg, "%eax" );
					emit("cdq");
					emit("idivl", "%ebp");
					emit("movl", "%eax", leftReg);
					
					if (eaxUsed)
					{
						currentMethodData.restoreRegFromTemp(oldEAX, "%ebp", AstCodeGenerator.this);
					}
					if (edxUsed)
					{
						currentMethodData.restoreRegFromTemp(oldEDX, "%edx", AstCodeGenerator.this);
					}			
					break;
				}
				case B_MOD:
				{
					// put left arg to eax
					String oldEAX = null;
					String oldEDX = null;
					boolean eaxUsed = false;
					if (!isFree("%ebp") && !(leftReg.equals("%ebp") || rightReg.equals("%ebp")))
					{
						oldEAX = currentMethodData.storeRegToTemp("%ebp", AstCodeGenerator.this);
						eaxUsed = true;
					}
					boolean edxUsed = false;
					if (!isFree("%edx") && !(leftReg.equals("%edx") || rightReg.equals("%edx")))
					{
						oldEDX = currentMethodData.storeRegToTemp("%edx", AstCodeGenerator.this);
						edxUsed = true;
					}
					
					emit("movl", rightReg, "%ebp");
					emit("movl", leftReg, "%eax" );
					emit("cdq");
					emit("idivl", "%ebp");
					emit("movl", "%edx", leftReg);
					
					if (eaxUsed)
					{
						currentMethodData.restoreRegFromTemp(oldEAX, "%ebp", AstCodeGenerator.this);
					}
					if (edxUsed)
					{
						currentMethodData.restoreRegFromTemp(oldEDX, "%edx", AstCodeGenerator.this);
					}			
					break;
				}
				case B_PLUS:
					emit("add", rightReg, leftReg);
					break;
				case B_MINUS:
					emit("sub", rightReg, leftReg);
					break;
				case B_AND:
					emit("andl", rightReg, leftReg);
					break;
				case B_OR:
					emit("orl", rightReg, leftReg);
					break;
				case B_LESS_THAN:
					emit("cmpl", rightReg, leftReg);
					emit("setl", "%al");
					emit("movzbl", "%al", leftReg);
					break;
				case B_LESS_OR_EQUAL:
					emit("cmpl", rightReg, leftReg);
					emit("setle", "%al");
					emit("movzbl", "%al", leftReg);
					break;
				case B_GREATER_THAN:
					emit("cmpl", rightReg, leftReg);
					emit("setg", "%al");
					emit("movzbl", "%al", leftReg);
					break;
				case B_GREATER_OR_EQUAL:
					emit("cmpl", rightReg, leftReg);
					emit("setge", "%al");
					emit("movzbl", "%al", leftReg);
					break;
				case B_EQUAL:
					emit("cmpl", rightReg, leftReg);
					emit("sete", "%al");
					emit("movzbl", "%al", leftReg);
					break;
				case B_NOT_EQUAL:
					emit("cmpl", rightReg, leftReg);
					emit("setne", "%al");
					emit("movzbl", "%al", leftReg);
					break;
				default:
					{
						throw new ToDoException();
					}
				}
			}
			releaseReg(rightReg);
			return leftReg;
		}

		@Override
		public String booleanConst(BooleanConst ast, Void arg) {
			String temp = getReg();
			if (ast.value == true)
				emit("movl", "$" + 1, temp);
			else
				emit("movl", "$" + 0, temp);
			return temp;
		}

		@Override
		public String builtInRead(BuiltInRead ast, Void arg) {
			String eaxTemp = null;

			ArrayList<Tuple<String, String>> regsStored = saveToStack();
			String tmp = currentMethodData.getTemp();
			
			
			
			emit("leal", currentMethodData.getAddress(tmp), "%eax");
			emit("movl", "%eax", "("+(-currentMethodData.size)+")(%esp)");
			emit("movl", "$"+intFormatStringLabel, "("+(-currentMethodData.size-4)+")(%esp)");
			emit("addl", "$("+(-currentMethodData.size-4)+")", "%esp");
			emit("call "+Config.SCANF);
			emit("addl", "$("+(currentMethodData.size+4)+")", "%esp");
			restoreRegsFromStack(regsStored);
			String reg = getReg();
			emit("movl", currentMethodData.getAddress(tmp), reg);
			
			currentMethodData.freeTemp(tmp);
			
			return reg;
		}

		@Override
		public String builtInReadFloat(BuiltInReadFloat ast, Void arg) {
			String eaxTemp = null;
			
			ArrayList<Tuple<String, String>> regsStored = saveToStack();
			String tmp = currentMethodData.getTemp();
			
			
			emit("leal", currentMethodData.getAddress(tmp), "%eax");
			emit("movl", "%eax", "("+(-currentMethodData.size)+")(%esp)");
			emit("movl", "$"+floatFormatStringLabel, "("+(-currentMethodData.size-4)+")(%esp)");
			emit("addl", "$("+(-currentMethodData.size-4)+")", "%esp");
			emit("call "+Config.SCANF);
			emit("addl", "$("+(currentMethodData.size+4)+")", "%esp");
			restoreRegsFromStack(regsStored);
			String reg = getReg();
			emit("movl", currentMethodData.getAddress(tmp), reg);
			
			currentMethodData.freeTemp(tmp);
			
			return reg;
		}

		@Override
		public String cast(Cast ast, Void arg) {
			String reg = visit(ast.arg(), arg);
			if (ast.typeSym instanceof ClassSymbol)
			{
				if (!((ClassSymbol)(ast.arg().type)).isSubtypeOf(ast.typeSym))
				{
					ClassData classData = classTable.classes.get(ast.typeSym.name);
					
					// Check legal Downcast
					emit("movl", "("+reg+")", "%eax");
					emit("movl", "%eax", "("+(-currentMethodData.size-4)+")(%esp)");
					emit("movl", "$"+classData.labelName, "("+(-currentMethodData.size-8)+")(%esp)");
					emit("addl", "$("+(-currentMethodData.size+4)+")", "%esp");
					emit("call", isSubtypeOfLabel);
					emit("addl", "$("+(+currentMethodData.size-4)+")", "%esp");
				}
			}
			else if (ast.typeSym instanceof ArrayTypeSymbol)
			{
				ClassData classData = classTable.classes.get(ast.type.name);
				if (classData != null)
				{
					String okLabel = "OK_L"+(ClassTable.counter++);
					emit("movl", "("+reg+")", "%eax");
					emit("cmpl", "$"+classData.labelName, "%eax");
					emit("je", okLabel);
					exception("$1");
					emit(okLabel+":");
				}
			}

			return reg;
		}

		@Override
		public String index(Index ast, Void arg) {
			String leftReg = eg.visit(ast.left(), arg);
			
			String tmp = currentMethodData.storeRegToTemp(leftReg, AstCodeGenerator.this);
			releaseReg(leftReg);
			String rightReg = eg.visit(ast.right(), arg);
			
			
			
			// check array bounds
			String okLabel = "boundsOk_L"+(ClassTable.counter++);
			String nokLabel = "boundsNOK_L"+(ClassTable.counter++);
			emit("movl", currentMethodData.getAddress(tmp), "%eax");
			emit("movl", "4(%eax)", "%eax");
			emit("cmpl", "%eax", rightReg);
			emit("jge", nokLabel);
			emit("cmpl", "$0", rightReg);
			emit("jge", okLabel);
			emit(nokLabel+":");
			exception("$3");
			emit(okLabel+":");
			
			
			
			emit("addl", "$2", rightReg);
			emit("shl", "$2", rightReg);
			emit("addl", currentMethodData.getAddress(tmp), rightReg);
			emit("movl", "("+rightReg+")", rightReg);
			
			currentMethodData.freeTemp(tmp);
			return rightReg;
		}

		@Override
		public String intConst(IntConst ast, Void arg) {
			String temp = getReg();
			emit("movl", "$" + ast.value, temp);
			return temp;
		}

		@Override
		public String floatConst(FloatConst ast, Void arg) {
			
			String temp = getReg();
			emit("movl", "$" + Float.floatToRawIntBits(ast.value), temp);
			return temp;
		}

		@Override
		public String field(Field ast, Void arg) {
			String reg = eg.visit(ast.arg(), arg);
			
			emit("cmpl", "$0", reg);
			String endLable = "end_L"+(ClassTable.counter++);
			emit("jne", endLable);
			exception("$4");
			emit(endLable+":");
			
			ClassData classData = classTable.classes.get(ast.arg().type.name);
			
			emit("movl", "("+classData.fieldOffsets.get(ast.fieldName)+")("+reg+")", reg);
			return reg;
		}

		@Override
		public String newArray(NewArray ast, Void arg) {
			
			ArrayTypeSymbol ats = ((ArrayTypeSymbol)ast.type);
			ClassData classData = classTable.classes.get(ats.name);
			
			String reg = eg.visit(ast.arg(), arg);
			
			ArrayList<Tuple<String, String>> regsStored = saveToStack();
			
			// check size
			String okLabel = "OK_L"+(ClassTable.counter++);
			emit("cmpl", "$0", reg);
			emit("jge", okLabel);
			exception("$5");
			emit(okLabel+":");
			
			
			// calc address
			emit("movl", reg, "%eax");
			emit("addl", "$2", "%eax"); // add 1 for length field
			emit("shl", "$2", "%eax"); // mult with 4;
			
			
			// malloc memory for array

			emit("addl", "$("+(-currentMethodData.size)+")", "%esp");

			emit("pushl", "%eax");
			emit("call malloc");
			emit("addl", "$("+(currentMethodData.size+4)+")", "%esp");
			restoreRegsFromStack(regsStored);
			emit("movl", reg, "4(%eax)");
			
			if (classData == null) //Object
			{
				emit("movl", "$0", "(%eax)");
			}
			else
			{
				emit("movl", "$"+classData.labelName, "(%eax)");
			}

			//currentMethodData.restoreRegFromTemp(tmp, "%eax", AstCodeGenerator.this);


			emit("movl", "%eax", reg);
			return reg;
		}

		@Override
		public String newObject(NewObject ast, Void arg) {
			ClassData classData = classTable.classes.get(ast.typeName);
			int size = 0;
			String address;
			if (classData != null)
			{
				size = classData.fieldOffsets.size()*4+4;
				address = "$"+classData.labelName;
			}
			else
			{
				size = 4;
				address = "$0";
			}

			// store used registers
			ArrayList<Tuple<String, String>> regsStored = saveToStack();
			
			// malloc memory for objct		
			
			
			emit("addl", "$("+(-currentMethodData.size)+")", "%esp");

			emit("pushl", "$"+(size));
			emit("call malloc");
			emit("addl", "$("+(currentMethodData.size+4)+")", "%esp");
			
			restoreRegsFromStack(regsStored);
			
			String reg = getReg();
			emit("movl", "%eax", reg);
			emit("movl", address, "(%eax)");
			
			return reg;
		}

		@Override
		public String nullConst(NullConst ast, Void arg) {
			String reg = getReg();
			emit("movl", "$0", reg);
			return reg;
		}

		@Override
		public String thisRef(ThisRef ast, Void arg) {
			String reg = getReg();
			emit("movl", "(-4)(%esp)", reg);
			return reg;
		}

		@Override
		public String methodCall(MethodCallExpr ast, Void arg) {
			
			// store used registers
			ArrayList<Tuple<String, String>> regsStored = saveToStack();

			
			// calculate args and put them o the stack
			int off = 8;
			for(Expr c : ast.argumentsWithoutReceiver())
			{
				String reg = eg.visit(c, arg);
				emit("movl", reg, "("+(-currentMethodData.size-off)+")(%esp)");
				releaseReg(reg);
				off+=4;
			}
			
			// calculate receiver and put it on the stack
			String reg = eg.visit(ast.receiver(), arg);
			emit("movl", reg, "("+(-currentMethodData.size-4)+")(%esp)");
			releaseReg(reg);
			
			// check for null ref of this object
			String okLabel = "Ok_L"+(ClassTable.counter++);
			emit("cmpl", "$0", reg);
			emit("jne", okLabel);
			exception("$4");
			emit(okLabel+":");
			
			ClassData receiverClassData = classTable.classes.get(ast.receiver().type.name);
			int methodNum = receiverClassData.methodNumber.get(ast.methodName);

			emit("movl", "("+reg+")", "%eax");
			emit("movl", "("+(methodNum*4+4)+")(%eax)", "%eax");
			emit("addl", "$("+(-currentMethodData.size+4)+")", "%esp");
			emit("call", "*%eax");
			emit("addl", "$("+(currentMethodData.size-4)+")", "%esp");
			
			// restore used registers
			restoreRegsFromStack(regsStored);
			
			// return value to freeRegister
			String retReg = getReg();
			emit("movl", "%eax", retReg);
			
			return retReg;
		}

		@Override
		public String unaryOp(UnaryOp ast, Void arg) {
			String argReg = gen(ast.arg());
			
			if (ast.arg().type.name.equals("float"))
			{
				switch (ast.operator) {
				case U_PLUS:
					break;
	
				case U_MINUS:
					emit("movl", argReg, doubleLabel1);
					emit("flds", doubleLabel1);
					emit("fchs");
					emit("fstps", doubleLabel1);
					emit("movl", doubleLabel1, argReg);
					break;
				}
			}
			else
			{
				switch (ast.operator) {
				case U_PLUS:
					break;
	
				case U_MINUS:
					emit("negl", argReg);
					break;
	
				case U_BOOL_NOT:
					emit("negl", argReg);
					emit("incl", argReg);
					break;
				}
			}
			return argReg;
		}

		@Override
		public String var(Var ast, Void arg) {
			String reg = getReg();
			if (currentMethodData.offset.containsKey(ast.name))
			{
				int offset = currentMethodData.offset.get(ast.name);
				emit("movl", "("+offset+")(%esp)", reg);
			}
			else
			{
				int offset = currentClassData.fieldOffsets.get(ast.name);
				emit("movl", "(-4)(%esp)", "%eax" );
				emit("movl", "("+offset+")(%eax)", reg);
			}
			return reg;
		}
	}

	/**
	 * Generates code to process statements and declarations.
	 */
	public class StmtDeclGenerator extends AstVisitor<String, Void> {

		public void gen(Ast ast) {
			visit(ast, null);
		}

		@Override
		public String visit(Ast ast, Void arg) {
			try {
				emitIndent("Emitting " + AstOneLine.toString(ast));
				String reg = super.visit(ast, arg);
				if (regs.size() != 6)
					throw new ToDoException("to few registers returned in "+currentMethodData.labelName+" "+ast.getClass().getName()+": "+regs.size());	
				return reg;
			} finally {
				emitUndent();
			}
		}
				
		@Override
		public String methodCall(MethodCall ast, Void arg) {
			int off = 8;	
			
			for(Expr c : ast.argumentsWithoutReceiver())
			{
				String reg = eg.visit(c, arg);
				emit("movl", reg, "("+(-currentMethodData.size-off)+")(%esp)");
				releaseReg(reg);
				off+=4;
			}
			
			String reg = eg.visit(ast.receiver(), arg);
			emit("movl", reg, "("+(-currentMethodData.size-4)+")(%esp)");
			releaseReg(reg);
			
			
			// check for null ref of this object
			String okLabel = "Ok_L"+(ClassTable.counter++);
			emit("cmpl", "$0", reg);
			emit("jne", okLabel);
			exception("$4");
			emit(okLabel+":");

			
			ClassData receiverClassData = classTable.classes.get(ast.receiver().type.name);
			int methodNum = receiverClassData.methodNumber.get(ast.methodName);

			emit("movl", "("+reg+")", "%eax");
			emit("movl", "("+(methodNum*4+4)+")(%eax)", "%eax");
			emit("addl", "$("+(-currentMethodData.size+4)+")", "%esp");
			emit("call", "*%eax");
			emit("addl", "$("+(currentMethodData.size-4)+")", "%esp");
			
			return null;
		}

		// Emit vtable for arrays of this class:
		@Override
		public String classDecl(ClassDecl ast, Void arg) {
			currentClass = ast;
			currentClassData = classTable.classes.get(ast.name);
			
			emitIndent("Class Table:");
				emit(".section", ".data");
				emit(currentClassData.labelName+":");
				
				emitComment("superclass Table Pointer");
				if (currentClassData.superClass == null)
					emit(".long", "0");
				else
					emit(".long", currentClassData.superClass.labelName);
				
				emitComment("virtual method table");
				for (MethodData md : currentClassData.methods)
				{
					emit(".long", md.labelName);
				}
			emitUndent();
			
			emitIndent("Methods:");
			emit(".section", ".text");
				for (MethodDecl md : ast.childrenOfType(MethodDecl.class))
				{
					visit(md, arg);
				}
			emitUndent();
			
			emit("");
			
			return "";
		}

		@Override
		public String methodDecl(MethodDecl ast, Void arg) {
			currentMethod = ast;
			currentMethodData = currentClassData.methods.get(currentClassData.methodNumber.get(ast.name).intValue());
			
			int initialTemps = currentMethodData.freeTemps.size();
				
			
			emit(currentMethodData.labelName+":");
			
			visit(ast.body(),arg);
			
			emit("ret");
			
			if (initialTemps != currentMethodData.freeTemps.size())
				throw new ToDoException("temps not the same in "+currentMethodData.labelName);
			
			return "";
		}

		@Override
		public String ifElse(IfElse ast, Void arg) {
			String temp = eg.visit(ast.condition(),arg);
			emit("cmpl", c(0), temp);
			releaseReg(temp);
			String elseLabel = "else_L"+(ClassTable.counter++);
			String endifLable = "endif_L"+(ClassTable.counter++);
			emit("je", elseLabel);
			emitIndent(null);
			visit(ast.then(),arg);
			emit("jmp", endifLable);
			emitUndent();
			emit(elseLabel+":");
			emitIndent(null);
			visit(ast.otherwise(),arg);
			emitUndent();
			emit(endifLable+":");
			
			return "";
		}

		@Override
		public String whileLoop(WhileLoop ast, Void arg) {
			
			String whileTest = "whileTest_L"+(ClassTable.counter++);
			String whileEnd = "whileEnd_L"+(ClassTable.counter++);
			
			emit(whileTest+":");
			String condReg = eg.visit(ast.condition(), arg);
			emit("cmpl", c(0), condReg);
			releaseReg(condReg);
			emit("je", whileEnd);
			
			sdg.visit(ast.body(), arg);
			emit("jmp", whileTest);
			emit(whileEnd+":");
			
			return null;
		}

		@Override
		public String assign(Assign ast, Void arg) {
			String moveDest = null;
			String leftReg = "";
			String rightReg = "";
			
			if (ast.left() instanceof Ast.Var)
			{
				Ast.Var var = (Ast.Var)ast.left();
				if (currentMethodData.offset.containsKey(var.name))
				{
					moveDest = "("+currentMethodData.offset.get(var.name)+")(%esp)";
					rightReg = eg.visit(ast.right(), arg);
					emit("movl", rightReg, moveDest);
					releaseReg(rightReg);
				}
				else
				{
					rightReg = eg.visit(ast.right(), arg);
					emit("movl", "(-4)(%esp)", "%eax");
					moveDest = "("+currentClassData.fieldOffsets.get(var.name)+")(%eax)";
					emit("movl", rightReg, moveDest);
					releaseReg(rightReg);
				}
			}
			else if (ast.left() instanceof Ast.Field)
			{
				Ast.Field field = (Ast.Field)ast.left();
				rightReg = eg.visit(ast.right(), arg);
				String tmp = currentMethodData.storeRegToTemp(rightReg, AstCodeGenerator.this);
				releaseReg(rightReg);
				leftReg = eg.visit(field.arg(), arg);
				rightReg = getReg();
				currentMethodData.restoreRegFromTemp(tmp,rightReg, AstCodeGenerator.this);
				ClassData classData = classTable.classes.get(field.arg().type.name);
				
				emit("movl", rightReg, "("+classData.fieldOffsets.get(field.fieldName)+")("+leftReg+")");
				releaseReg(rightReg);
				releaseReg(leftReg);
			}
			else if (ast.left() instanceof Ast.Index)
			{
				Ast.Index index = (Ast.Index)ast.left();
				
				
				String leftIndReg = eg.visit(index.left(), arg);
				String tmp = currentMethodData.storeRegToTemp(leftIndReg, AstCodeGenerator.this);
				releaseReg(leftIndReg);
				String rightIndReg = eg.visit(index.right(), arg);
				
				// check array bounds
				String okLabel = "boundsOk_L"+(ClassTable.counter++);
				String nokLabel = "boundsNOK_L"+(ClassTable.counter++);
				emit("movl", currentMethodData.getAddress(tmp), "%eax");
				emit("movl", "4(%eax)", "%eax");
				emit("cmpl", "%eax", rightIndReg);
				emit("jge", nokLabel);
				emit("cmpl", "$0", rightIndReg);
				emit("jge", okLabel);
				emit(nokLabel+":");
				exception("$3");
				emit(okLabel+":");
				
				
				emit("addl", "$2", rightIndReg); // add 2 for length field
				emit("shl", "$2", rightIndReg); // mult with 4;
				emit("addl", rightIndReg, currentMethodData.getAddress(tmp));
				releaseReg(rightIndReg);
				
				rightReg = eg.visit(ast.right(),arg);
				
				leftReg = getReg();
				currentMethodData.restoreRegFromTemp(tmp,leftReg, AstCodeGenerator.this);
				emit("movl", rightReg, "("+leftReg+")");
				releaseReg(leftReg);
				releaseReg(rightReg);
				
//				emit("movl", "("+leftReg+")", "%eax");
//				emit("")
				
				
				
			}
			return null;
		}

		@Override
		public String builtInWrite(BuiltInWrite ast, Void arg) {
			
			String reg = eg.visit(ast.arg(), arg);

			emit("movl", reg, "("+(-currentMethodData.size)+")(%esp)");
			emit("movl", "$"+intFormatStringLabel,"%eax");
			emit("movl", "%eax", "("+(-currentMethodData.size-4)+")(%esp)");
			emit("addl", "$("+(-currentMethodData.size-4)+")" , "%esp");
			emit("call	printf");
			emit("addl", "$("+(currentMethodData.size+4)+")" , "%esp");
			releaseReg(reg);
			
			return null;
		}

		@Override
		public String builtInWriteFloat(BuiltInWriteFloat ast, Void arg) {
			String reg = eg.visit(ast.arg(), arg);

			emit("movl", reg, "("+(-currentMethodData.size)+")(%esp)");
			
			emit("flds", "("+(-currentMethodData.size)+")(%esp)");
			emit("fstpl", "("+(-currentMethodData.size)+")(%esp)");
			
			emit("movl", "$"+floatFormatStringLabel,"%eax");
			emit("movl", "%eax", "("+(-currentMethodData.size-4)+")(%esp)");
			emit("addl", "$("+(-currentMethodData.size-4)+")" , "%esp");
			emit("call	printf");
			emit("addl", "$("+(currentMethodData.size+4)+")" , "%esp");
			releaseReg(reg);
			return null;
		}

		@Override
		public String builtInWriteln(BuiltInWriteln ast, Void arg) {

			emit("movl", "$"+newLineStringLabel,"%eax");
			emit("movl", "%eax", "("+(-currentMethodData.size)+")(%esp)");
			emit("addl", "$("+(-currentMethodData.size)+")" , "%esp");
			emit("call	printf");
			emit("addl", "$("+(currentMethodData.size)+")" , "%esp");
			return null;
		}

		@Override
		public String returnStmt(ReturnStmt ast, Void arg) {
			if (ast.arg() != null)
			{
				String res = eg.visit(ast.arg(), arg);
				emit("movl", res, "%eax");
				releaseReg(res);
			}
			emit("ret");
			
			return null;
		}

	}

	// -------------------------------------------------------
	// EMIT CODE

	private StringBuilder indent = new StringBuilder();

	/** Creates an constant operand relative to another operand. */
	protected String c(int i) {
		return "$" + i;
	}

	/** Creates an constant operand with the address of a label. */
	protected String c(String lbl) {
		return "$" + lbl;
	}

	/** Creates an operand relative to another operand. */
	protected String o(int offset, String reg) {
		return String.format("%d(%s)", offset, reg);
	}

	/** Creates an operand addressing an item in an array */
	protected String a(String arrReg, String idxReg) {
		final int offset = Config.SIZEOF_PTR; // one word in front for vptr
		final int mul = Config.SIZEOF_PTR; // assume all arrays of 4-byte elem
		return String.format("%d(%s,%s,%d)", offset, arrReg, idxReg, mul);
	}

	protected void emitIndent(String comment) {
		if (comment != null)
			emitComment(comment);
		this.indent.append("    ");
	}

	protected void emitCommentSection(String name) {
		int indentLen = this.indent.length();
		int breakLen = 68 - indentLen - name.length();
		StringBuffer sb = new StringBuffer();
		sb.append(Config.COMMENT_SEP).append(" ");
		for (int i = 0; i < indentLen; i++)
			sb.append("_");
		sb.append(name);
		for (int i = 0; i < breakLen; i++)
			sb.append("_");

		try {
			out.write(sb.toString());
			out.write("\n");
		} catch (IOException e) {
		}
	}

	protected void emitComment(String comment) {
		emit(Config.COMMENT_SEP + " " + comment);
	}

	protected void emitUndent() {
		this.indent.setLength(this.indent.length() - 4);
	}

	protected void emit(String op, String src, String dest) {
		emit(String.format("%s %s, %s", op, src, dest));
	}

	public void emit(String op, int src, String dest) {
		emit(op, c(src), dest);
	}

	protected void emit(String op, String dest) {
		emit(op + " " + dest);
	}

	protected void emit(String op, int dest) {
		emit(op, c(dest));
	}

	protected void emitMove(String src, String dest) {
		if (!src.equals(dest))
			emit("movl", src, dest);
	}

	protected void emitLoad(int srcOffset, String src, String dest) {
		emitMove(o(srcOffset, src), dest);
	}

	protected void emitStore(String src, int destOffset, String dest) {
		emitMove(src, o(destOffset, dest));
	}

	protected void emitConstantData(String data) {
		emit(String.format("%s %s", Config.DOT_INT, data));
	}

	private int counter = 0;

	protected String uniqueLabel() {
		String labelName = "label" + counter++;
		return labelName;
	}

	protected void emitLabel(String main) {
		try {
			out.write(main + ":" + "\n");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void emit(String op) {
		try {
			out.write(indent.toString());
			out.write(op);
			out.write("\n");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void emitMethodSuffix(boolean returnNull) {
			if (returnNull)
				emit("movl", "$0", "%eax");
			emit("leave");
			emit("ret");
	}

}
