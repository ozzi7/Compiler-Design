package cd.codegen;

import java.util.HashMap;

public class StackManager {
	private HashMap<String, Integer> map = new HashMap<String, Integer>();
	private int offset = -4;
	
	public void clear()
	{
		map.clear();
		offset = -4;
	}
	
	public void saveVariable(String name, AstCodeGenerator acg, String reg)
	{
		Integer inOff = map.get(name);
		if (inOff == null)
		{
			acg.emit("pushl", "%"+reg);
			int newOff = offset;
			map.put(name, newOff);
			offset -= 4;
		}
		else
		{
			acg.emit("movl", "%"+reg, inOff+"(%ebp)");
		}
	}
	
}
