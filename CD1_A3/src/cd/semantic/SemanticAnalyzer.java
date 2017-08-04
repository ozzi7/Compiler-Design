package cd.semantic;

import java.util.HashMap;
import java.util.List;

import cd.Main;
import cd.exceptions.SemanticFailure;
import cd.exceptions.SemanticFailure.Cause;
import cd.exceptions.ToDoException;
import cd.ir.Ast;
import cd.ir.Ast.BinaryOp.BOp;
import cd.ir.Ast.Seq;
import cd.ir.Ast.VarDecl;
import cd.ir.AstVisitor;
import cd.ir.ExprVisitor;
import cd.ir.Symbol;
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
import cd.ir.Symbol.ClassSymbol;
import cd.ir.Symbol.MethodSymbol;
import cd.ir.Symbol.ArrayTypeSymbol;
import cd.ir.Symbol.PrimitiveTypeSymbol;
import cd.ir.Symbol.TypeSymbol;
import cd.ir.Symbol.VariableSymbol;
import cd.ir.Symbol.VariableSymbol.Kind;

public class SemanticAnalyzer {
	
	public final Main main;
	
	public final SemanticAstVisitor astVisitor = new SemanticAstVisitor();
	public final SemanticExprVisitor exprVisitor = new SemanticExprVisitor();
	
	public final HashMap<String, ClassSymbol> classSymbolTable = new HashMap<String, Symbol.ClassSymbol>();
	public final HashMap<String, ArrayTypeSymbol> arraySymbolTable = new HashMap<String, Symbol.ArrayTypeSymbol>();
	public final HashMap<String, PrimitiveTypeSymbol> primitiveTypeSymbolTable = new HashMap<String, Symbol.PrimitiveTypeSymbol>();
	
	public SemanticAnalyzer(Main main) {
		this.main = main;
		
		PrimitiveTypeSymbol intSym = new PrimitiveTypeSymbol("int");
		primitiveTypeSymbolTable.put("int", intSym);
		addToArrayTable(intSym);
		
		PrimitiveTypeSymbol booleanSym = new PrimitiveTypeSymbol("boolean");
		primitiveTypeSymbolTable.put("boolean", booleanSym);
		addToArrayTable(booleanSym);
		
		PrimitiveTypeSymbol floatSym = new PrimitiveTypeSymbol("float");
		primitiveTypeSymbolTable.put("float", floatSym);
		addToArrayTable(floatSym);

		primitiveTypeSymbolTable.put("void", new PrimitiveTypeSymbol("void"));
		
		ClassSymbol objectClassSymbol = new ClassSymbol("Object");
		objectClassSymbol.superClass = null;
		classSymbolTable.put("Object", objectClassSymbol);
		
		ClassSymbol nullSymbol = new ClassSymbol("~null");
		nullSymbol.superClass = null;
		classSymbolTable.put("~null", nullSymbol);
	}
	
	public void addToArrayTable(TypeSymbol ts)
	{
		arraySymbolTable.put(ts.name+"[]", new ArrayTypeSymbol(ts));
	}
			
	
	public void check(List<ClassDecl> classDecls) throws SemanticFailure 
	{
		handleClassDecls(classDecls);
		
		// find entry Main.main()
		
		ClassSymbol mainClass = classSymbolTable.get("Main");
		if (mainClass == null)
			throw new SemanticFailure(Cause.INVALID_START_POINT, "No class Main"); 
			
		MethodSymbol mainMethod = mainClass.getMethod("main");
		if (mainMethod == null)
			throw new SemanticFailure(Cause.INVALID_START_POINT, "No method main in Main"); 
		
		if (!mainMethod.returnType.equals(primitiveTypeSymbolTable.get("void")))
			throw new SemanticFailure(Cause.INVALID_START_POINT, "Main.main should return void"); 
		
		if (mainMethod.parameters.size() != 0)
			throw new SemanticFailure(Cause.INVALID_START_POINT, "Main.main should take no arguments"); 
	}

	public void handleClassDecls(List<ClassDecl> classDecls) throws SemanticFailure 
	{
		// add names to table
		for (ClassDecl cd : classDecls) 
		{
			if (cd.name.equals("Object"))
			{
				throw new SemanticFailure(Cause.OBJECT_CLASS_DEFINED);
			}
			else if (classSymbolTable.containsKey(cd.name))
			{
				throw new SemanticFailure(Cause.DOUBLE_DECLARATION, cd.name);
			}
			else
			{
				cd.sym = new ClassSymbol(cd);
				classSymbolTable.put(cd.sym.name, cd.sym);
				addToArrayTable(cd.sym);
			}
		}
		
		// add superclasses and check existence;
		for (ClassDecl cd : classDecls) 
		{
			if (classSymbolTable.containsKey(cd.superClass))
			{
//				cd.sym.superClasses.add(classSymbolTable.get(cd.superClass).);
				cd.sym.superClass = classSymbolTable.get(cd.superClass);
			}
			else
			{
				throw new SemanticFailure(Cause.NO_SUCH_TYPE, cd.superClass);
			}
		}
		
		// check for circular inheritance
		int classCount = classDecls.size();
		ClassSymbol objectSymbol = classSymbolTable.get("Object");
		for (ClassDecl cd : classDecls) 
		{
			ClassSymbol current = cd.sym;
			boolean foundObject = false;
			for (int i = 0; i < classCount; i++)
			{
				if (current.superClass == objectSymbol)
				{
					foundObject = true;
					break;
				}
				else
				{
					current = current.superClass;
				}
			}
			if (!foundObject)
			{
				throw new SemanticFailure(Cause.CIRCULAR_INHERITANCE); 
			}
		}
		
		// check classDeclBody
		for (ClassDecl cd : classDecls) 
		{
			checkClassDeclBody(cd);
		}
		
		// check overwritten method types
		for (ClassDecl cd : classDecls) 
		{
			checkMethodOverwrite(cd);
		}
		
		// check method body
		for (ClassDecl cd : classDecls)
		{
			for (MethodDecl md : cd.methods())
			{
				checkMethodBody(new ClassMethodPair(cd.sym, md.sym));
			}
		}
		
	}
	
	public void checkClassDeclBody(ClassDecl cd)
	{
		// add and check for field double declarations
		for (VarDecl field : cd.fields())
		{
			if (cd.sym.fields.containsKey(field.name))
			{
				throw new SemanticFailure(Cause.DOUBLE_DECLARATION, field.name+" in class "+cd.sym.name); 
			}
			else
			{
				cd.sym.fields.put(field.name, new VariableSymbol(field.name, findTypeSymbol(field.type), Kind.FIELD));
			}
		}
		
		// add and check for method double declarations
		for (MethodDecl method : cd.methods())
		{
			
			if (cd.sym.methods.containsKey(method.name))
			{
				throw new SemanticFailure(Cause.DOUBLE_DECLARATION, method.name+" in class "+cd.sym.name); 
			}
			else
			{
				MethodSymbol ms = new MethodSymbol(method);
				ms.returnType = findTypeSymbol(method.returnType);
				
				for (int i = 0; i < method.argumentNames.size(); i++)
				{
					if (ms.locals.containsKey(method.argumentNames.get(i)))
					{
						throw new SemanticFailure(Cause.DOUBLE_DECLARATION, "parameter "+method.argumentNames.get(i)); 
					}
					else
					{
						VariableSymbol vs = new VariableSymbol(method.argumentNames.get(i), findTypeSymbol(method.argumentTypes.get(i)), Kind.PARAM);
						ms.parameters.add(vs);
						ms.locals.put(method.argumentNames.get(i), vs);
					}
				}
				
				cd.sym.methods.put(method.name, ms);
				method.sym = ms;
			}
		}		
	}
	
	public void checkMethodOverwrite(ClassDecl cd)
	{
		for (MethodSymbol ms : cd.sym.methods.values())
		{
			String name = ms.name;
			
			MethodSymbol otherM = cd.sym.superClass.getMethod(name);
			if (otherM != null)
			{
				if (!otherM.hasSameTypes(ms))
				{
					throw new SemanticFailure(Cause.INVALID_OVERRIDE, "class "+cd.name+" method "+ms.name); 
				}
			}
		}
	}
	
	public void checkMethodBody(ClassMethodPair classMethodPair)
	{

		MethodDecl md = classMethodPair.methodSym.ast;
		
		
		//check stmts...
		for (Ast ast : md.decls().rwChildren)
		{
			astVisitor.visit(ast, classMethodPair); 
		}
		for (Ast ast : md.body().rwChildren)
		{
			astVisitor.visit(ast, classMethodPair); 
		}
		
		
		// check for return stmts...
		
		checkMethodBodyReturn(md, classMethodPair);
	}
	
	public void checkMethodBodyReturn(MethodDecl md, ClassMethodPair classMethodPair)
	{
		if (!md.sym.returnType.equals(primitiveTypeSymbolTable.get("void")))
		{
			if (!checkPathForReturn(md.body()))
				throw new SemanticFailure(Cause.MISSING_RETURN, "in "+classMethodPair.classSym.name+"."+classMethodPair.methodSym.name+"(...)"); 
		}
	}
	
	boolean checkPathForReturn(Ast inAst)
	{
		if (inAst instanceof Seq)
		{
			for (Ast ast : ((Seq)inAst).rwChildren)
			{
				if (checkPathForReturn(ast))
				{
					return true;
				}
			}
		}
		else if (inAst instanceof IfElse)
		{
			IfElse a = (IfElse)inAst;
			if (checkPathForReturn(a.then()) && checkPathForReturn(a.otherwise()))
			{
				return true;
			}
		}
		else if (inAst instanceof ReturnStmt)
		{
			return true;
		}
		return false;
	}
	
	public TypeSymbol findTypeSymbol(String name)
	{
		TypeSymbol s = primitiveTypeSymbolTable.get(name);
		if (s != null)
			return s;

		s = classSymbolTable.get(name);
		if (s != null)
			return s;
		
		s = arraySymbolTable.get(name);
		if (s != null)
			return s;
		
		throw new SemanticFailure(Cause.NO_SUCH_TYPE, name); 
	}
	
	class SemanticAstVisitor extends AstVisitor<String, ClassMethodPair> 
	{
		@Override
		public String visit(Ast ast, ClassMethodPair arg) {	
			return super.visit(ast, arg);
		}
		
		@Override
		public String varDecl(VarDecl ast, ClassMethodPair arg) {
			if (arg.methodSym.locals.containsKey(ast.name))
			{
				throw new SemanticFailure(Cause.DOUBLE_DECLARATION, "local "+ast.name+" in "+arg.classSym.name+"."+arg.methodSym.name+"(...)"); 
			}
			else
			{
				VariableSymbol vs = new VariableSymbol(ast.name, findTypeSymbol(ast.type),Kind.LOCAL);
				arg.methodSym.locals.put(ast.name, vs);
				return null;
			}
		}
				
		@Override
		public String methodCall(MethodCall ast, ClassMethodPair arg) {
			int len = ast.argumentsWithoutReceiver().size();
			TypeSymbol receiver = exprVisitor.visit(ast.receiver(), arg);
			if (!classSymbolTable.containsKey(receiver.name))
			{
				throw new SemanticFailure(Cause.TYPE_ERROR, "in receiver of "+ast.methodName+" in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
			}
			ClassSymbol receiverClass = (ClassSymbol)receiver;

			MethodSymbol methodSymbol = receiverClass.getMethod(ast.methodName);
			
			if (methodSymbol == null)
			{
				throw new SemanticFailure(Cause.NO_SUCH_METHOD, receiverClass.name+"."+ast.methodName+" in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
			}
			
			if (methodSymbol.parameters.size() != len)
			{
				throw new SemanticFailure(Cause.WRONG_NUMBER_OF_ARGUMENTS, "in call of "+ast.methodName+"(...) in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
			}
			
			for (int i=0; i<len; i++)
			{
				TypeSymbol argSym = exprVisitor.visit(ast.argumentsWithoutReceiver().get(i), arg);
				if (!argSym.isSubtypeOf(methodSymbol.parameters.get(i).type))
				{
					throw new SemanticFailure(Cause.TYPE_ERROR, "in call of "+ast.methodName+"(...) in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
				}
			}
			
			return null;
		}

		@Override
		public String ifElse(IfElse ast, ClassMethodPair arg) {
			
			TypeSymbol condition = exprVisitor.visit(ast.condition(), arg);
			if (condition !=  primitiveTypeSymbolTable.get("boolean")) {
				throw new SemanticFailure(Cause.TYPE_ERROR, "ifelse condition not boolean!");
			}
			for (Ast ifelseStmts : ast.children()) {
				visit(ifelseStmts, arg);
			}
			return null;
		}

		@Override
		public String whileLoop(WhileLoop ast, ClassMethodPair arg) {
			TypeSymbol condition = exprVisitor.visit(ast.condition(), arg);
			if (condition !=  primitiveTypeSymbolTable.get("boolean")) {
				throw new SemanticFailure(Cause.TYPE_ERROR, "whileloop condition not boolean!");
			}
			for (Ast whileLoopStmts : ast.children()) {
				visit(whileLoopStmts, arg);
			}
			return null;
		}

		@Override
		public String assign(Assign ast, ClassMethodPair arg) {
			TypeSymbol right = exprVisitor.visit(ast.right(), arg);
			TypeSymbol left = exprVisitor.visit(ast.left(), arg);
			
			if (!(ast.left() instanceof Var || ast.left() instanceof Field || ast.left() instanceof Index))
				throw new SemanticFailure(Cause.NOT_ASSIGNABLE, "assignment in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
			
			if (!right.isSubtypeOf(left))
			{
				throw new SemanticFailure(Cause.TYPE_ERROR, "assignment in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
			}
			

			
			return null;
		}

		@Override
		public String builtInWrite(BuiltInWrite ast, ClassMethodPair arg) {
			if(ast.children().size() != 1) {
				throw new SemanticFailure(Cause.WRONG_NUMBER_OF_ARGUMENTS, "BuiltInWrite error!");
			}
			
			TypeSymbol ts = exprVisitor.visit(ast.arg(), arg);
			if (!ts.equals(primitiveTypeSymbolTable.get("int"))) {
				throw new SemanticFailure(Cause.TYPE_ERROR, "in writef() in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
			}
			
			return null;
		}

		@Override
		public String builtInWriteFloat(BuiltInWriteFloat ast, ClassMethodPair arg) {
			if(ast.children().size() != 1) {
				throw new SemanticFailure(Cause.WRONG_NUMBER_OF_ARGUMENTS, "BuiltInWriteFloat error!");
			}
			TypeSymbol ts = exprVisitor.visit(ast.arg(), arg);
			if (!ts.equals(primitiveTypeSymbolTable.get("float"))) {
				throw new SemanticFailure(Cause.TYPE_ERROR, "in writef() in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
			}
			
			return null;
		}

		@Override
		public String builtInWriteln(BuiltInWriteln ast, ClassMethodPair arg) {
			if(!(ast.children().isEmpty())) {
				throw new SemanticFailure(Cause.WRONG_NUMBER_OF_ARGUMENTS, "BuiltInWriteln error!");
			}
			return null;
		}

		@Override
		public String returnStmt(ReturnStmt ast, ClassMethodPair arg) {
			TypeSymbol returnArg = exprVisitor.visit(ast.arg(), arg);
			
			if(!(returnArg.isSubtypeOf(arg.methodSym.returnType))) {
				throw new SemanticFailure(Cause.TYPE_ERROR, "Return value not a subtype of formal parameter");
			}
			return null;
		}
	}
	
	class SemanticExprVisitor extends ExprVisitor<TypeSymbol, ClassMethodPair>
	{
		@Override
		public TypeSymbol visit(Expr ast, ClassMethodPair arg) {
			return super.visit(ast, arg);
		}

		@Override
		public TypeSymbol binaryOp(BinaryOp ast, ClassMethodPair arg) {
			
			
			
			TypeSymbol left = visit(ast.left(),arg);
			TypeSymbol right = visit(ast.right(),arg);
			
			switch (ast.operator) {
				case B_AND:
				case B_OR:
					if(left != primitiveTypeSymbolTable.get("boolean") || right != primitiveTypeSymbolTable.get("boolean")) {
						throw new SemanticFailure(Cause.TYPE_ERROR, "BinaryOp error!");
					}
					else {
						return primitiveTypeSymbolTable.get("boolean");
					}
				case B_TIMES:
				case B_DIV:
				case B_PLUS:
				case B_MINUS:
					if(left != right || left == primitiveTypeSymbolTable.get("boolean")) {
						throw new SemanticFailure(Cause.TYPE_ERROR, "BinaryOp error!");
					}
					else {
						return left;
					}
				case B_MOD:
					if(left != right || left != primitiveTypeSymbolTable.get("int")) {
						throw new SemanticFailure(Cause.TYPE_ERROR, "BinaryOp error!");
					}
					else {
						return left;
					}
				case B_GREATER_THAN:
				case B_GREATER_OR_EQUAL:
				case B_LESS_THAN:
				case B_LESS_OR_EQUAL:
					if(left != right || left == primitiveTypeSymbolTable.get("boolean")) {
						throw new SemanticFailure(Cause.TYPE_ERROR);
					}
					else {
						return primitiveTypeSymbolTable.get("boolean");
					}
				case B_EQUAL:
				case B_NOT_EQUAL:
					if(left.isSubtypeOf(right) || right.isSubtypeOf(left)) {
						return primitiveTypeSymbolTable.get("boolean");
					}
					else {
						throw new SemanticFailure(Cause.TYPE_ERROR, "BinaryOp error!");
					}
			}
			return null;
		}

		@Override
		public TypeSymbol booleanConst(BooleanConst ast, ClassMethodPair arg) {
			return primitiveTypeSymbolTable.get("boolean");
		}

		@Override
		public TypeSymbol builtInRead(BuiltInRead ast, ClassMethodPair arg) {
			if(!(ast.children().isEmpty())) {
				throw new SemanticFailure(Cause.WRONG_NUMBER_OF_ARGUMENTS, "BuiltInRead error!");
			}
			return primitiveTypeSymbolTable.get("int");
		}

		@Override
		public TypeSymbol builtInReadFloat(BuiltInReadFloat ast, ClassMethodPair arg) {
			if(!(ast.children().isEmpty())) {
				throw new SemanticFailure(Cause.WRONG_NUMBER_OF_ARGUMENTS, "BuiltInReadFloat error!");
			}
			return primitiveTypeSymbolTable.get("float");
		}

		@Override
		public TypeSymbol cast(Cast ast, ClassMethodPair arg) {
			TypeSymbol argument = visit(ast.arg(),arg);
			TypeSymbol castTo = findTypeSymbol(ast.typeName);
			
			if((!argument.isReferenceType()) || (!castTo.isReferenceType())) {
				throw new SemanticFailure(Cause.TYPE_ERROR, "Cast of/to primitive type!");
			}
			else if(!(argument.isSubtypeOf(castTo) || castTo.isSubtypeOf(argument))) {
				throw new SemanticFailure(Cause.TYPE_ERROR, "Illegal cast (not subtypes)!");
			}
			return castTo;
		}

		@Override
		public TypeSymbol index(Index ast, ClassMethodPair arg) {
			TypeSymbol right = exprVisitor.visit(ast.right(),arg);
			if (!right.equals(primitiveTypeSymbolTable.get("int")))
			{
				throw new SemanticFailure(Cause.TYPE_ERROR, "Array index should be int but is "+right.name+" instead, in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
			}
			
			TypeSymbol left = exprVisitor.visit(ast.left(),arg);
			if (!(left instanceof ArrayTypeSymbol))
			{
				throw new SemanticFailure(Cause.TYPE_ERROR, "Can't dereference non-array type "+left.name+", in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
			}
			ArrayTypeSymbol array = (ArrayTypeSymbol)left;
			return array.elementType;
		}

		@Override
		public TypeSymbol intConst(IntConst ast, ClassMethodPair arg) {
			return primitiveTypeSymbolTable.get("int");
		}

		@Override
		public TypeSymbol floatConst(FloatConst ast, ClassMethodPair arg) {
			return primitiveTypeSymbolTable.get("float");
		}

		@Override
		public TypeSymbol field(Field ast, ClassMethodPair arg) {
			TypeSymbol par = exprVisitor.visit(ast.arg(), arg);
			if (!(par instanceof ClassSymbol))
			{
				throw new SemanticFailure(Cause.TYPE_ERROR, "No class symbol "+par.name+" in "+ast.fieldName+" in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
			}
			ClassSymbol cs = (ClassSymbol)par;
			VariableSymbol vs = cs.getField(ast.fieldName);
			if (vs == null)
			{
				throw new SemanticFailure(Cause.NO_SUCH_FIELD, ast.fieldName+" in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
			}
			return vs.type;
		}

		@Override
		public TypeSymbol newArray(NewArray ast, ClassMethodPair arg) {
			TypeSymbol ts = findTypeSymbol(ast.typeName);
			
			TypeSymbol ind = exprVisitor.visit(ast.arg(),arg);
			if (!ind.equals(primitiveTypeSymbolTable.get("int")))
			{
				throw new SemanticFailure(Cause.TYPE_ERROR, "Array argument should be int but is "+ind.name+" instead, in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
			}
			return ts;
		}

		@Override
		public TypeSymbol newObject(NewObject ast, ClassMethodPair arg) {
			ClassSymbol classSymbol = classSymbolTable.get(ast.typeName);
			if(classSymbol == null) {
				throw new SemanticFailure(Cause.NO_SUCH_TYPE, "Object not found: new " + ast.typeName + "!");
			}
			return classSymbol;
		}

		@Override
		public TypeSymbol nullConst(NullConst ast, ClassMethodPair arg) {
			return classSymbolTable.get("~null");
		}

		@Override
		public TypeSymbol thisRef(ThisRef ast, ClassMethodPair arg) {
			return arg.classSym;
		}

		@Override
		public TypeSymbol methodCall(MethodCallExpr ast, ClassMethodPair arg) {
			int len = ast.argumentsWithoutReceiver().size();
			TypeSymbol receiver = exprVisitor.visit(ast.receiver(), arg);
			if (!classSymbolTable.containsKey(receiver.name))
			{
				throw new SemanticFailure(Cause.TYPE_ERROR, "in receiver of "+ast.methodName+" in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
			}
			ClassSymbol receiverClass = (ClassSymbol)receiver;

			MethodSymbol methodSymbol = receiverClass.getMethod(ast.methodName);
			
			if (methodSymbol == null)
			{
				throw new SemanticFailure(Cause.NO_SUCH_METHOD, receiverClass.name+"."+ast.methodName+" in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
			}
			
			if (methodSymbol.parameters.size() != len)
			{
				throw new SemanticFailure(Cause.WRONG_NUMBER_OF_ARGUMENTS, "in call of "+ast.methodName+"(...) in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
			}
			
			for (int i=0; i<len; i++)
			{
				TypeSymbol argSym = exprVisitor.visit(ast.argumentsWithoutReceiver().get(i), arg);
				if (!argSym.isSubtypeOf(methodSymbol.parameters.get(i).type))
				{
					throw new SemanticFailure(Cause.TYPE_ERROR, "in call of "+ast.methodName+"(...) in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
				}
			}
			
			return methodSymbol.returnType;
 		}

		@Override
		public TypeSymbol unaryOp(UnaryOp ast, ClassMethodPair arg) {
			TypeSymbol argument = visit(ast.arg(),arg);
			
			switch(ast.operator) {
				case U_BOOL_NOT:
					if(argument != primitiveTypeSymbolTable.get("boolean")) {
						throw new SemanticFailure(Cause.TYPE_ERROR, "Unary OP error!");
					}
					else {
						return argument;
					}
				case U_MINUS:
				case U_PLUS:
					if(argument != primitiveTypeSymbolTable.get("int") && argument != primitiveTypeSymbolTable.get("float")) {
						throw new SemanticFailure(Cause.TYPE_ERROR, "Unary OP error!");
					}
					else {
						return argument;
					}
			}
			return null;
		}

		@Override
		public TypeSymbol var(Var ast, ClassMethodPair arg) {

			VariableSymbol vs = arg.methodSym.locals.get(ast.name);
			if (vs == null)
			{
				vs = arg.classSym.getField(ast.name);
			}
			if (vs == null)
			{
				throw new SemanticFailure(Cause.NO_SUCH_VARIABLE, ast.name+" in "+arg.classSym.name+"."+arg.methodSym.name+"(...)");
			}
			else
			{
				return vs.type;
			}
		}
	}
	
	class ClassMethodPair
	{
		public ClassMethodPair(ClassSymbol classSym, MethodSymbol methodSym)
		{
			this.classSym = classSym;
			this.methodSym = methodSym;
		}
		
		public ClassSymbol classSym;
		public MethodSymbol methodSym;
	}

}
