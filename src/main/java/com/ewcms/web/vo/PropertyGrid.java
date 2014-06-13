package com.ewcms.web.vo;

import java.io.Serializable;
import java.util.Map;

public class PropertyGrid implements Serializable{

	private static final long serialVersionUID = -7573244306517205481L;
	
	private Object name;
    private Object value;
    private String group;
    private Map<String,Object> editor;
    
    public PropertyGrid(Object name,Object value,String group, Map<String, Object> editor){
        this.name = name;
        this.value = value;
        this.group = group;
        this.editor = editor;
    }
    
    public Object getName() {
        return name;
    }
    
    public Object getValue() {
        return value;
    }
    
    public String getGroup() {
        return group;
    }

	public Map<String, Object> getEditor() {
		return editor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropertyGrid other = (PropertyGrid) obj;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
