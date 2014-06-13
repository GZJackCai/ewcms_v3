/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.web.util;

import com.ewcms.site.model.Channel;

public class ChannelNode {
    private Long id;
    private String name;
    private Channel.Type channelType;
    private String channelTypeDes;
    private boolean children;
    private boolean publicable;
    private String sort;
    private Integer mask;
    
//    public ChannelNode(final Channel channel){
//        this(channel,new HashSet<Permission>());
//    }
    
    public ChannelNode(final Channel channel,final Integer mask){
        id = channel.getId();
        name = channel.getName();
        children = channel.hasChildren();
        publicable = channel.getPublicenable();
        channelType = channel.getType();
        channelTypeDes = channel.getTypeDescription();
        try{
        	sort = Long.toString(channel.getSort());
        }catch(Exception e){
        	sort = "1";
        }
        this.mask = mask;
    }
    
    public ChannelNode(){};
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isChildren() {
        return children;
    }
    public void setChildren(boolean children) {
        this.children = children;
    }
    
    public boolean isPublicable() {
		return publicable;
	}

	public void setPublicable(boolean publicable) {
		this.publicable = publicable;
	}

    public Integer getMask() {
		return mask;
	}

	public void setMask(Integer mask) {
		this.mask = mask;
	}

	public Channel.Type getChannelType() {
		return channelType;
	}

	public void setChannelType(Channel.Type channelType) {
		this.channelType = channelType;
	}

	public String getChannelTypeDes() {
		return channelTypeDes;
	}

	public void setChannelTypeDes(String channelTypeDes) {
		this.channelTypeDes = channelTypeDes;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        ChannelNode other = (ChannelNode) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName()).append("{");
        builder.append("id:").append(id).append(";");
        builder.append("name:").append(name).append(";");
        builder.append("children:").append(children).append(";");
        builder.append("publicable:").append(publicable).append(";");
        builder.append("permissions:[");
//        for(Permission permission : permissions){
//            builder.append(permission.toString()).append(";");
//        }
        builder.append("];");
        builder.append("}");
        
        return builder.toString();
    }
}
