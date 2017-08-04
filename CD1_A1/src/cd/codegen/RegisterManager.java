package cd.codegen;

import java.util.Arrays;
import java.util.LinkedList;

public class RegisterManager {
	
	private String[] allRegs = {"ebx", "ecx", "edi", "esi", "edx", "eax"};
	
	private LinkedList<String> freeList = new LinkedList<String>();
	
	public RegisterManager()
	{
		reset();
	}
	
	public String getRegister()
	{
		assert freeList.size() > 0;
		return freeList.pop();
	}
	
	public void freeRegister(String name)
	{
		freeList.push(name);
	}
	
	public void reset()
	{
		freeList.clear();
		freeList.addAll(Arrays.asList(allRegs));
	}
	
}
