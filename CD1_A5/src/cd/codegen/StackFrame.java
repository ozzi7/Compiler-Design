package cd.codegen;

import java.util.HashMap;

import cd.ir.Ast;

public class StackFrame {
	public HashMap<String, Integer> offset = new HashMap<>();
	public int size;
	
	public StackFrame(Ast.MethodDecl ast)
	{
		size = 0;
		for (String name : ast.argumentNames)
		{
			add(name);
		}
		
		for (Ast.VarDecl varDecl : ast.decls().childrenOfType(Ast.VarDecl.class))
		{
			add(varDecl.name);
		}
		
	}
	
	private void add(String name)
	{
		offset.put(name, size);
		size += 4;
	}
	
}
