package cd.codegen;

public class CompilerData {
	
	public ConstantValue<String> stringConstants;
	public StaticVar integerVariables;

	
	public CompilerData()
	{
		stringConstants = new ConstantValue<String>("StringConst", "string");
		integerVariables = new StaticVar("integerVar", "long", "0");
	}
	
	public void emit(AstCodeGenerator acg)
	{
		stringConstants.emitString(acg); 
		integerVariables.emit(acg);
	}
}
