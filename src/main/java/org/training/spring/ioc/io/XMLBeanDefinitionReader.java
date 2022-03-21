package org.training.spring.ioc.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.training.spring.ioc.entity.BeanDefinition;
import org.training.spring.ioc.exception.SourceParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLBeanDefinitionReader implements BeanDefinitionReader {

	private static final SAXParserFactory SAX_PARSER_FACTORY = SAXParserFactory.newInstance();

	private List<BeanDefinition> beanDefinitions;
	private List<String> contextFiles;

	public XMLBeanDefinitionReader(String... path) {
		contextFiles = new ArrayList<>(Arrays.asList(path));
		beanDefinitions = new ArrayList<>();
	}

	@Override
	public List<BeanDefinition> getBeanDefinitions() {
		try {
			SAXParser saxParser = SAX_PARSER_FACTORY.newSAXParser();
			DefaultHandler beanDefinitionHandler = new BeanDefinitionHandler();
			for (String contextFile : contextFiles) {
				saxParser.parse(getClass().getResourceAsStream(contextFile), beanDefinitionHandler);
			}
			return beanDefinitions;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new SourceParseException("parsing failed", e);
		}
	}

	private class BeanDefinitionHandler extends DefaultHandler {

		private static final String BEANS = "beans";
		private static final String BEAN = "bean";
		private static final String ID = "id";
		private static final String CLASS = "class";
		private static final String PROPERTY = "property";
		private static final String NAME = "name";
		private static final String VALUE = "value";
		private static final String REF = "ref";

		private boolean beansTagStarted = false;

		private BeanDefinition beanDefinition;

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) {

			if (qName.equalsIgnoreCase(BEANS)) {
				beansTagStarted = true;
				return;
			}

			if (qName.equalsIgnoreCase(BEAN)) {
				beanDefinition = BeanDefinition.builder().id(attributes.getValue(ID))
						.className(attributes.getValue(CLASS)).build();
				return;
			}

			if (qName.equalsIgnoreCase(PROPERTY)) {

				if (beanDefinition == null) {
					throw new SourceParseException(String.format("no start tag for %s encountered", BEAN));
				}

				String name = attributes.getValue(NAME);
				if (name == null) {
					throw new SourceParseException(String.format("no %s attribute found for %s tag", NAME, BEAN));
				}

				String value = attributes.getValue(VALUE);
				String ref = attributes.getValue(REF);

				if (ref == null) {
					beanDefinition.getDependencies().put(name, value);
				} else {
					beanDefinition.getRefDependencies().put(name, ref);
				}
			}

		}

		@Override
		public void endElement(String uri, String localName, String qName) {

			if (qName.equals(BEAN)) {

				if (!beansTagStarted) {
					throw new SourceParseException(String.format("start tag %s not found", BEANS));
				}

				beanDefinitions.add(beanDefinition);
				beanDefinition = null;
			}
		}

	}

}
