package cd.codegen;

import java.util.HashMap;

public class ConstantValue<T> {

	private String assemblyName;
	private String labelPrefix;
	private HashMap<T, String> map;
	private int counter;
	
	public ConstantValue(String labelPrefix, String assemblyName)
	{
		this.assemblyName = assemblyName;
		this.labelPrefix = labelPrefix;
		map = new HashMap<T, String>();
		counter = 0;
	}
	
	public String constantLabel(T value)
	{
		String label = map.get(value);
		if (label == null)
		{
			String newLabel = labelPrefix+(counter++);
			map.put(value, newLabel);
			return newLabel;
		}
		else
		{
			return label;
		}
	}
	
	public void emit(AstCodeGenerator acg)
	{
		acg.emit("");
		acg.emit(" # "+labelPrefix+" Section");
		acg.emitIndent(null);
		acg.emit(".section .data");
		for (T key : map.keySet())
		{
			String label = map.get(key);
			acg.emit(label+":");
			acg.emitIndent(null);
			acg.emit("."+assemblyName+" "+key);
			acg.emitUndent();
		}
		acg.emitUndent();
		acg.emit("");
	}
	
	// adds " to output
	public void emitString(AstCodeGenerator acg)
	{
		acg.emit("");
		acg.emit(" # "+labelPrefix+" Section");
		acg.emitIndent(null);
		acg.emit(".section .data");
		for (T key : map.keySet())
		{
			String label = map.get(key);
			acg.emit(label+":");
			acg.emitIndent(null);
			acg.emit("."+assemblyName+" \""+key+"\"");
			acg.emitUndent();
		}
		acg.emitUndent();
		acg.emit("");
	}
}
