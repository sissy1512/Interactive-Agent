package traverser;

public class condition{
	private String path = null;
	private String value = null;
	
	public condition(String path, String value){
		this.path = path;
		this.value = value;
	}
	
	public void setPath(String p){
		path = p;
	}
	
	public void setValue(String v){
		value = v;
	}
	
	public String getPath(){
		return path;
	}
	public String getValue(){
		return value;
	}
}
