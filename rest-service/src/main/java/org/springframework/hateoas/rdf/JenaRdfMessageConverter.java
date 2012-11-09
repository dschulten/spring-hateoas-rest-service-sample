package org.springframework.hateoas.rdf;

import java.io.IOException;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class JenaRdfMessageConverter extends AbstractHttpMessageConverter<Model> {

	private String lang = "RDF/XML-ABBREV";

	@Override
	protected boolean supports(Class<?> clazz) {
		return Model.class.isAssignableFrom(clazz);
	}

	@Override
	protected Model readInternal(Class<? extends Model> clazz, HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		
		Model model = ModelFactory.createDefaultModel();
		return model.read(inputMessage.getBody(), lang);
	}

	public String getLang() {
		return lang;
	}

	/**
	 * Sets the language used for serialization. Default is "RDF/XML-ABBREV".
	 * @param lang
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	@Override
	protected void writeInternal(Model model, HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {

		model.write(outputMessage.getBody(), lang);
	}

}
