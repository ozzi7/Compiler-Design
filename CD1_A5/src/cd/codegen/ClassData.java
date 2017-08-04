package cd.codegen;

import java.util.ArrayList;
import java.util.HashMap;

import cd.ir.Ast;


public class ClassData {
	String labelName;
	
	boolean updated = false;
	
	Ast.ClassDecl ast;
	
	ClassData superClass;

	HashMap<String,Integer> fieldOffsets = new HashMap<String,Integer>();
	
	ArrayList<MethodData> methods = new ArrayList<MethodData>();
	HashMap<String,Integer> methodNumber = new HashMap<String,Integer>();;

	void update(RegsNeededVisitor regsNeededVisitor)
	{
		if (!updated)
		{
			int nextOffset = 4;
			if (superClass != null)
			{
				superClass.update(regsNeededVisitor);
				
				nextOffset = superClass.fieldOffsets.size()*4+4;
				
				fieldOffsets.putAll(superClass.fieldOffsets);
				
				methods.addAll(superClass.methods);
				methodNumber.putAll(superClass.methodNumber);
			}
			
			for (Ast.VarDecl md : ast.childrenOfType(Ast.VarDecl.class))
			{
				fieldOffsets.put(md.name, nextOffset);
				nextOffset+=4;
			}
			
			for (Ast.MethodDecl md : ast.childrenOfType(Ast.MethodDecl.class))
			{
				MethodData methodData = new MethodData(md, ast.name, regsNeededVisitor);
				Integer i = methodNumber.get(md.name);
				if (i == null)
				{
					methods.add(methodData);
					methodNumber.put(md.name, methods.size()-1);
				}
				else
				{
					methods.set(i,methodData);
				}
			}
			
			updated = true;
		}
	}
}
