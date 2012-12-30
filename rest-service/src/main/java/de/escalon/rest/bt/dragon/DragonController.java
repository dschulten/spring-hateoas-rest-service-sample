package de.escalon.rest.bt.dragon;

import static org.springframework.hateoas.mvc.ControllerActionBuilder.createActionFor;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.action.ActionDescriptor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

public class DragonController {

	@Autowired
	DragonAccess dragonAccess;

	@Autowired
	ThiefAccess thiefAccess;

	@RequestMapping("/entry")
	public HttpEntity<ActionDescriptor> start() {
		return new HttpEntity<ActionDescriptor>(createActionFor(methodOn(DragonController.class).livingdragon(null, null),
				"dragonName"));
	}

	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value = "/livingdragon", method = RequestMethod.POST)
	public HttpEntity<DragonResource> livingdragon(@RequestParam String dragonName, @RequestParam String homeLocation) {
		Dragon dragon = new Dragon(dragonName, homeLocation);
		int id = dragonAccess.createDragon(dragon);
		DragonResourceAssembler assembler = new DragonResourceAssembler();
		DragonResource dragonResource = assembler.toResource(dragon);
		// clients should follow all concurrent links when they find them in a response
		// either in the order of occurrence or truly concurrently, with two browsers
		dragonResource.add(linkTo(methodOn(DragonController.class).guardTreasure(id)).withRel("concurrent"));
		dragonResource.add(linkTo(methodOn(DragonController.class).robMoreTreasures(id)).withRel("concurrent"));
		return new HttpEntity<DragonResource>(dragonResource);
	}

	@RequestMapping("/guardingdragon/{id}")
	public HttpEntity<DragonResource> guardTreasure(@PathVariable int id) {
		Dragon dragon = dragonAccess.readDragon(id);
		DragonResourceAssembler assembler = new DragonResourceAssembler();
		DragonResource dragonResource = assembler.toResource(dragon);
		dragonResource.add(linkTo(methodOn(DragonController.class).isThiefNearTreasureCondition(id)).withRel("condition"));
		return new HttpEntity<DragonResource>(dragonResource);

	}

	@RequestMapping("/sensingthiefdragon/{id}")
	public HttpEntity<DragonResource> isThiefNearTreasureCondition(@PathVariable int id) {
		Dragon dragon = dragonAccess.readDragon(id);
		DragonResourceAssembler assembler = new DragonResourceAssembler();
		DragonResource dragonResource = assembler.toResource(dragon);
		// if dragon is at home and thief nearby home
		if (dragon.getHomeLocation().equals(dragon.getLocation())
				&& dragon.getHomeLocation().equals(thiefAccess.getThiefLocation())) {
			dragonResource.add(linkTo(methodOn(DragonController.class).eatThiefAction(id)).withRel("action"));
		} else {
			// TODO how do I express that dragon can't eat a thief now
			// what is the equivalent of a failure in http?
			// a back rel in the response would require the callee too know where the caller was
			// this will not work, or I get no reusable behavior subtrees.
			//
			// the bt could be inside the dragon, the links could be built from it
			// it might be able to handle a back upon failure. The server knows what's next.
			// For a coffeshop, the bt must be in the shop, the links built from it
			// if I do it that way, how do I represent the states as resource uris?
			// feels more natural to get the same resource over and over in different states
			// where states are rel combinations
			// what have I got?
			// status - e.g. multiple choice
			// headers
			// content
			// rels in response -> transitions to other states
			// rel describedBy in a response says what the item is.
			// what is the equivalent of a subroutine -> wrong approach!
			//
			// how to make the bts interoperable? It is not necessary. What might be necessary
			// is that the client tells the server what it wants, and the server must
			// guide the client. How to tell what I want?

			// Suppose I have a price-sensitive agent which does not always want to buy,
			// but only if there are special deals? That should be possible. The client should be
			// able to follow its own conditions, be free to choose.
			// This must be done based on the content, e.g. a price list of goods with buy rels
			// how does the client know what to do on such a page? If it is a form with input type select items, it could
			// understand. Somehow the client must be able to recognize that some rel is the path to its goal,
			// and it must understand when its goal is reached.
			dragonResource.add(linkTo(methodOn(DragonController.class).eatThiefAction(id)).withRel("failure"));
		}
		return new HttpEntity<DragonResource>(dragonResource);
	}

	@RequestMapping("/eatingthiefdragon/{id}")
	public HttpEntity<DragonResource> eatThiefAction(@PathVariable int id) {
		Dragon dragon = dragonAccess.readDragon(id);
		DragonResourceAssembler assembler = new DragonResourceAssembler();
		DragonResource dragonResource = assembler.toResource(dragon);
		// if dragon is at home and thief nearby home
		if (dragon.getHomeLocation().equals(dragon.getLocation())) {
			dragonResource.add(linkTo(methodOn(DragonController.class).eatThiefAction(id)).withRel("action"));
		} else {
			throw new BehaviorFailedException("not at home");
		}
		return new HttpEntity<DragonResource>(dragonResource);
	}

	public HttpEntity<DragonResource> robMoreTreasures(@PathVariable int id) {
		Dragon dragon = dragonAccess.readDragon(id);
		DragonResourceAssembler assembler = new DragonResourceAssembler();
		DragonResource dragonResource = assembler.toResource(dragon);
		return new HttpEntity<DragonResource>(dragonResource);
	}

}
