package cd.parser;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import java_cup.runtime.Symbol;

public class DebugFormatter {
	
	private static Map<Integer, String> symbolNames;
	
	private static String symbolName(int symNum) {
		if (symbolNames == null) {
			symbolNames = new HashMap<Integer, String>();
			
			for (Field rfld : sym.class.getDeclaredFields()) {
				rfld.setAccessible(true);
				if (rfld.getType() == int.class) {
					int val;
					try {
						val = rfld.getInt(null);
					} catch (IllegalArgumentException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
					symbolNames.put(val, rfld.getName());
				}
			}
		}
		
		if (symbolNames.containsKey(symNum))
			return symbolNames.get(symNum);
		return Integer.toString(symNum);
	}
	
	public static String format(
			String message,
			Symbol cur_token)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(message);
		if (cur_token != null) {
			sb.append(" / Current Token=");
			sb.append(symbolName(cur_token.sym));
			if (cur_token.value != null)
				sb.append("(").append(cur_token.value).append(")");
			sb.append("@").append(cur_token.left);
		}
		return sb.toString();
		
	}

}
