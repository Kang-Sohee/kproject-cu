package com.ohdocha.cu.kprojectcu.util;



import net.sf.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.jackson.JsonNodeValueReader;


public class JsonUtil {

	
	// string 형의 JSON 값을 파싱 
	
	static JSONParser jsonParser;
	static ModelMapper modelMapper; 
	
	/** 초기화 */ 
	static { 
		modelMapper = new ModelMapper(); 
		modelMapper.getConfiguration().addValueReader(new JsonNodeValueReader());
		modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE); 
		jsonParser = new JSONParser();
	}

	
	public static Object convertJsonStrToClass(String jsonStr , Class obj){
		try { 
			JSONObject jsonObj = (JSONObject) jsonParser.parse(jsonStr);
			return modelMapper.map(jsonObj, obj); 
		} catch (Exception e) {
			e.printStackTrace(); return null; 
		}
	}
}
