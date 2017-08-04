package cd.codegen;

import java.util.HashMap;

import cd.ir.Ast;

public class ClassTable {
	public static int counter = 0;
	
	public final HashMap<String, ClassData> classes = new HashMap<String, ClassData>();
	
	void addClass(Ast.ClassDecl classDecl)
	{
		ClassData classData = new ClassData();
		classData.ast = classDecl;
		classData.labelName = classDecl.name+"_L"+(counter++);
		classes.put(classDecl.name, classData);
	}
	
	void update(RegsNeededVisitor regsNeededVisitor)
	{
		for (ClassData cd : classes.values())
		{
			cd.superClass = classes.get(cd.ast.superClass);
		}
		
		for (ClassData cd : classes.values())
		{
			cd.update(regsNeededVisitor);
		}
	}
}
