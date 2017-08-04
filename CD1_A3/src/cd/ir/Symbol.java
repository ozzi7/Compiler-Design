package cd.ir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import cd.ir.Ast.ClassDecl;

public abstract class Symbol {
	
	public final String name;
	
	public static abstract class TypeSymbol extends Symbol {
		
		public TypeSymbol(String name) {
			super(name);
		}

		public abstract boolean isReferenceType();
		
		public abstract boolean isSubtypeOf(TypeSymbol other);
		
		public String toString() {
			return name;
		}
		
	}
	
	public static class PrimitiveTypeSymbol extends TypeSymbol {
		public PrimitiveTypeSymbol(String name) {
			super(name);
		}
		
		public boolean isSubtypeOf(TypeSymbol other)
		{
			if (this.equals(other))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
		public boolean isReferenceType() {
			return false;
		}
	}
	
	public static class ArrayTypeSymbol extends TypeSymbol {
		public final TypeSymbol elementType;
		
		public ArrayTypeSymbol(TypeSymbol elementType) {
			super(elementType.name+"[]");
			this.elementType = elementType;
		}
		
		public boolean isSubtypeOf(TypeSymbol other)
		{
			if (other.name == "Object" || other.equals(this))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
		public boolean isReferenceType() {
			return true;
		}
	}
	
	public static class ClassSymbol extends TypeSymbol {
		public final Ast.ClassDecl ast;
		public ClassSymbol superClass;
		public final VariableSymbol thisSymbol =
			new VariableSymbol("this", this);
		public final Map<String, VariableSymbol> fields = 
			new HashMap<String, VariableSymbol>();
		public final Map<String, MethodSymbol> methods =
			new HashMap<String, MethodSymbol>();

		public ClassSymbol(Ast.ClassDecl ast) {
			super(ast.name);
			this.ast = ast;
//			this.superClasses.add(this);
		}
		
		public boolean isSubtypeOf(TypeSymbol other)
		{
			if (this.name.equals("~null"))
			{
				if (other.isReferenceType())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			
			if (other.name.equals("Object"))
				return true;
			if (superClass == null)
				return false;
			if (other.equals(this))
				return true;
			if (superClass.name.equals("Object"))
				return false;
			
			return superClass.isSubtypeOf(other);
		}
		/** Used to create the default {@code Object} 
		 *  and {@code <null>} types */
		public ClassSymbol(String name) {
			super(name);
			this.ast = null;
		}
		
		public boolean isReferenceType() {
			return true;
		}
		
		public VariableSymbol getField(String name) {
			VariableSymbol fsym = fields.get(name);
			if (fsym == null && superClass != null)
				return superClass.getField(name);
			return fsym;
		}
		
		public MethodSymbol getMethod(String name) {
			MethodSymbol msym = methods.get(name);
			if (msym == null && superClass != null)
				return superClass.getMethod(name);
			return msym;
		}
	}

	public static class MethodSymbol extends Symbol {
		
		public final Ast.MethodDecl ast;
		public final Map<String, VariableSymbol> locals =
			new HashMap<String, VariableSymbol>();
		public final List<VariableSymbol> parameters =
			new ArrayList<VariableSymbol>();
		
		public TypeSymbol returnType;
		
		public MethodSymbol(Ast.MethodDecl ast) {
			super(ast.name);
			this.ast = ast;
		}
		
		public boolean hasSameTypes(MethodSymbol other)
		{
			if (!returnType.equals(other.returnType))
				return false;
			
			if (parameters.size() != other.parameters.size())
				return false;
			
			for (int i=0; i< parameters.size(); i++)
			{
				if (!parameters.get(i).type.equals(other.parameters.get(i).type))
					return false;
			}
			return true;
		}
		
		public String toString() {
			return name + "(...)";
		}
	}
	
	public static class VariableSymbol extends Symbol {
		
		public static enum Kind { PARAM, LOCAL, FIELD };
		public final TypeSymbol type;
		public final Kind kind;
		
		public VariableSymbol(String name, TypeSymbol type) {
			this(name, type, Kind.PARAM);
		}

		public VariableSymbol(String name, TypeSymbol type, Kind kind) {
			super(name);
			this.type = type;
			this.kind = kind;		
		}
		
		public String toString() {
			return name;
		}
	}

	protected Symbol(String name) {
		this.name = name;
	}

}
