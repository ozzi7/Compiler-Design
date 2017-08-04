package cd.codegen;

import java.util.HashMap;

import cd.ir.Ast;
import cd.ir.ExprVisitor;
import cd.ir.Ast.BinaryOp;
import cd.ir.Ast.BooleanConst;
import cd.ir.Ast.BuiltInRead;
import cd.ir.Ast.Cast;
import cd.ir.Ast.Expr;
import cd.ir.Ast.Field;
import cd.ir.Ast.Index;
import cd.ir.Ast.IntConst;
import cd.ir.Ast.NewArray;
import cd.ir.Ast.NewObject;
import cd.ir.Ast.NullConst;
import cd.ir.Ast.ThisRef;
import cd.ir.Ast.UnaryOp;
import cd.ir.Ast.Var;
	
public class RegisterCountUpdater extends ExprVisitor<Integer, Void> {
	
//	public HashMap<Expr, Integer> registerCount = new HashMap<Expr, Integer>();
	
	public Integer getCount(Ast ast) {
		if (ast instanceof Expr)
		{
			Expr expr = (Expr)ast;
			int regCount = expr.getRegCount();
			if (regCount != -1)
			{
				return regCount;
			}
			else
			{
				return visit(expr, null);
			}
		}
		else
		{
			return 0;
		}
	}

	@Override
	public Integer visit(Expr ast, Void arg) {
		return super.visit(ast, null);
	}
	
	@Override
	public Integer binaryOp(BinaryOp ast, Void arg) {
		int left = getCount(ast.left());
		int right = getCount(ast.right());
		int v = -1;
		
		if (left == right)
		{
			v = left+1;
			
		}
		else
		{
			v = Math.max(left, right);
		}
		
		ast.setRegCount(v);
		return v;
	}

	@Override
	public Integer booleanConst(BooleanConst ast, Void arg) {
		throw new RuntimeException("Not required");
	}

	@Override
	public Integer builtInRead(BuiltInRead ast, Void arg) {
		ast.setRegCount(1);
		return 1;
	}

	@Override
	public Integer cast(Cast ast, Void arg) {
		throw new RuntimeException("Not required");
	}

	@Override
	public Integer index(Index ast, Void arg) {
		throw new RuntimeException("Not required");
	}

	@Override
	public Integer intConst(IntConst ast, Void arg) {
		ast.setRegCount(1);
		return 1;
	}

	@Override
	public Integer field(Field ast, Void arg) {
		throw new RuntimeException("Not required");
	}

	@Override
	public Integer newArray(NewArray ast, Void arg) {
		throw new RuntimeException("Not required");
	}

	@Override
	public Integer newObject(NewObject ast, Void arg) {
		throw new RuntimeException("Not required");
	}

	@Override
	public Integer nullConst(NullConst ast, Void arg) {
		throw new RuntimeException("Not required");
	}

	@Override
	public Integer thisRef(ThisRef ast, Void arg) {
		throw new RuntimeException("Not required");
	}

	@Override
	public Integer unaryOp(UnaryOp ast, Void arg) {
		int v = getCount(ast.arg());
		ast.setRegCount(v);
		return v;
	}

	@Override
	public Integer var(Var ast, Void arg) {
		ast.setRegCount(1);
		return 1;
	}
}
