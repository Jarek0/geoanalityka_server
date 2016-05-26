package pl.gisexpert.cms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LayerInfo implements Serializable {

	private static final long serialVersionUID = 2502395616763332833L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private LayerType type;
	
	public LayerInfo() {
		super();
	}

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

	public LayerType getType() {
		return type;
	}

	public void setType(LayerType type) {
		this.type = type;
	}
	
   
}
