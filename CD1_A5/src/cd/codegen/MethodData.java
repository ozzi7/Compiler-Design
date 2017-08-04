package cd.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import cd.ir.Ast;
import cd.ir.Ast.MethodDecl;

public class MethodData {
	public String labelName;
	public MethodDecl ast;
	public HashMap<String, Integer> offset = new HashMap<>();
	public int size;
	//public HashMap<String, String> storedData = new HashMap<String, String>();
	
	public ArrayList<String> freeTemps = new ArrayList<>();
	
	public MethodData(Ast.MethodDecl ast, String className, RegsNeededVisitor regsNeededVisitor)
	{
		this.ast = ast;
		
		int tempsNeeded = regsNeededVisitor.visit(ast, null);
		
		size = 8;
		for (String name : ast.argumentNames)
		{
			add(name);
		}
		
		for (Ast.VarDecl varDecl : ast.decls().childrenOfType(Ast.VarDecl.class))
		{
			add(varDecl.name);
		}
		
		for (int i=0; i<tempsNeeded;i++)
		{
			add("&temp"+i);
			freeTemps.add("&temp"+i);
		}
		
		labelName = className+"_"+ast.name+"_L"+(ClassTable.counter++);
	}
	
	public String getTemp()
	{
		assert !freeTemps.isEmpty();
		return freeTemps.remove(freeTemps.size()-1);
	}
	
	public void freeTemp(String name)
	{
		assert freeTemps.contains(name);
		freeTemps.add(name);
	}
	
	public String storeRegToTemp(String reg, AstCodeGenerator gen)
	{
		String temp = getTemp();

		gen.emitIndent("store reg to temp");
		gen.emit("movl", reg, "("+offset.get(temp)+")(%esp)");
		gen.emitUndent();

		return temp;
	}
	
	public void restoreRegFromTemp(String temp, String reg, AstCodeGenerator gen)
	{
		freeTemp(temp);
		gen.emitIndent("restore reg from temp");
		gen.emit("movl", "("+offset.get(temp)+")(%esp)", reg);
		gen.emitUndent();
	}
	
	private void add(String name)
	{
		offset.put(name, -size);
		size += 4;
	}
	
	public String getAddress(String temp)
	{
		return "("+offset.get(temp)+")(%esp)";
	}
	
}
