package de.escalon.rest.bt.dragon;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class DragonResourceAssembler extends ResourceAssemblerSupport<Dragon, DragonResource>{

	public DragonResourceAssembler() {
		super(DragonController.class, DragonResource.class);
	}

	public DragonResource toResource(Dragon entity) {
		DragonResource to = createResource(entity);
		to.setName(entity.getName());
		return to;
	}

}
