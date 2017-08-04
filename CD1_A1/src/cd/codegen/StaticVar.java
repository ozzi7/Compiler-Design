package cd.codegen;

import java.util.HashMap;

public class StaticVar {

	private String assemblyName;
	private String labelPrefix;
	private String defaultVal;
	private HashMap<String, String> map;
	private int counter;
	
	public StaticVar(String labelPrefix, String assemblyName, String defaultVal)
	{
		this.assemblyName = assemblyName;
		this.labelPrefix = labelPrefix;
		this.defaultVal = defaultVal;
		map = new HashMap<String, String>();
		counter = 0;
	}
	
	public String getLabel(String name)
	{
		String label = map.get(name);
		if (label == null)
		{
			String newLabel = labelPrefix+(counter++);
			map.put(name, newLabel);
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
		for (String name : map.keySet())
		{
			String label = map.get(name);
			acg.emit(label+":");
			acg.emitIndent(null);
			acg.emit("."+assemblyName+" "+defaultVal);
			acg.emitUndent();
		}
		acg.emitUndent();
		acg.emit("");
	}
}
