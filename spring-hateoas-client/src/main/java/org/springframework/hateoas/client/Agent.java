package org.springframework.hateoas.client;

import static org.springframework.hateoas.client.Identifiers.describedBy;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.client.hc.CommonsHttpClient4Browser;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

public class Agent {

	private List<Action> script = new ArrayList<Action>();

	private Map<String, SubmitFormAction> submits = new HashMap<String, SubmitFormAction>();

	private Identifier currentIdentifier;

	private Browser browser;

	public Agent(Browser browser) {
		super();
		this.browser = browser;
	}

	public void addSubmitFormAction(SubmitFormAction action) {
		submits.put(action.getFormName(), action);
	}

	public void addFollowRelAction(FollowRelAction action) {
		script.add(action);
	}

//	public List<Action> getScript() {
//		return script;
//	}

	public SubmitFormAction getSubmitFormAction(Browsable currentResource) {
		List<String> formsInResource = currentResource.getForms();
		SubmitFormAction ret = null;
		for (String formName : formsInResource) {
			SubmitFormAction submit = submits.get(formName);
			if (submit != null) {
				ret = submit;
				break;
			}
		}
		return ret;
	}

	/**
	 * Uses describedBy rel in currentContext to build a plan of follow rel actions which must be executed to achieve the
	 * given goal.
	 * 
	 * @param goalUri resource which must be retrieved in order to achieve the goal, e.g. a transfer resource from a bank
	 *          account.
	 * @param currentContext to start from, must have a describedBy rel.
	 */
	public void identifyGoal(String goalUri, URI currentContext) {
		Browser rdfBrowser = new CommonsHttpClient4Browser(currentContext);
		rdfBrowser.followRel("describedBy");
		Browsable rdfDescription = rdfBrowser.getCurrentResource();
		Object o = rdfDescription.getParsedContent();
		if (o instanceof Model) {
			Model model = (Model) o;
			Resource goal = model.createResource(goalUri);

			Resource current = goal;
			List<Resource> path = new ArrayList<Resource>();
			do {
				// build path from innermost out, excluding goal
				StmtIterator statements = model.listStatements(current, RDFS.domain, (RDFNode) null);
				if (statements.hasNext()) {
					current = statements.next().getObject().asResource();
					path.add(current);
				} else {
					current = null;
				}
			} while (current != null);

			if (path.isEmpty()) {
				throw new IllegalStateException("no path to goal " + goal.toString());
			}

			currentIdentifier = describedBy(goal.getURI());

			// TODO allow to add identifiers using containing, saves one loop
			for (Resource resource : path) {
				currentIdentifier = currentIdentifier.within(describedBy(resource.getURI()));
			}
			if (path.size() > 1) {
				// outermost is the current resource, no need to follow rel to it
				// exclude outermost from path
				for (int i = path.size() - 2; i > -1; i--) {
					Resource resource = path.get(i);
					this.addFollowRelAction(new FollowRelAction(resource.getURI()));
				}
			}
			// add goal rel as last action
			this.addFollowRelAction(new FollowRelAction(goal.getURI()));

			// TODO must get rid of the fixed sequence of actions for forms
			// rather be able to answer certain data requests
			// need an inventory of actions to try on a resource
			// two approaches: have an rdf with full resource description and client has a single
			// resource as its goal
			// or the client has context knowledge: goal is transfer within account within bank
			// TODO: sensing: if the client has only partial knowledge about all necessary steps,
			// rdf provides the bridge to the next known context element
			// TODO: idea for proof of concept: from finding an item in a list to using a form
		} else {
			throw new IllegalArgumentException("resource does not contain an rdf model");
		}

	}

	public void setGoal(Identifier identifier) {
		this.currentIdentifier = identifier;

	}
	
	public Browsable browseForGoal() throws GoalNotFoundException {
		Browsable currentResource = browser.getCurrentResource();
		Iterator<Action> actions = script.iterator();
		Action currentAction = actions.next();
		Browsable found = null;
		while (found == null) {
			// TODO exit condition if not found (remove #product links from ProductController to trigger endless loop)
			if (currentIdentifier.foundIn(currentResource)) {
				// next identifier
				currentIdentifier = currentIdentifier.getContainedIdentifier();
				if (currentIdentifier == null) {
					// no identifiers left, we have a match
					found = currentResource;
					break;
				}
			} else {
				// TODO the rels to use might be embedded within collection items
				if (currentAction != null && currentAction.possibleOn(currentResource)) {
					currentResource = currentAction.execute(browser);
					if (actions.hasNext())
						currentAction = actions.next();
					else
						currentAction = null;
				} else {
					// see if we can handle the current resource as a form
					SubmitFormAction submit = getSubmitFormAction(currentResource);
					if (submit != null) {
						currentResource = submit.execute(browser);
					} else {
						// TODO: "sensing"
						throw new GoalNotFoundException("failed to reach goal");
					}
				}
			}
		}
		return found;
	}

}
