package com.ewcms.common.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 抽象实体基类，提供统一的ID，和相关的基本功能方法. 如果是 Sequence 请参考{@link BaseSequenceEntity}
 * 
 * @author wu_zhijun
 */
@MappedSuperclass
public abstract class BaseAutoEntity<ID extends Serializable> extends AbstractEntity<ID> {

	private static final long serialVersionUID = -5537165749926030368L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ID id;

    @Override
    public ID getId() {
        return id;
    }

    @Override
    public void setId(ID id) {
        this.id = id;
    }

}
