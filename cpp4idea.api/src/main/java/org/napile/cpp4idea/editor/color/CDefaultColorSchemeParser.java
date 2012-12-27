/*
 * Copyright 2011 napile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.napile.cpp4idea.editor.color;


import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;

/**
 * @author VISTALL
 * @date 11:42/15.12.2011
 */
public class CDefaultColorSchemeParser implements ApplicationComponent
{
	private static final Logger LOGGER = Logger.getInstance(CDefaultColorSchemeParser.class);

	public CDefaultColorSchemeParser()
	{
		//
	}

	@Override
	public void initComponent()
	{
		SAXBuilder builder = new SAXBuilder(false);
		try
		{
			Document document = builder.build(CDefaultColorSchemeParser.class.getResourceAsStream("CDefaultColorScheme.xml"));

			List list = document.getRootElement().getChildren();
			for(Object o : list)
			{
				Element element = (Element)o;

				TextAttributesKey key = TextAttributesKey.find(element.getAttributeValue("name"));

				TextAttributes attributes = new TextAttributes();
				attributes.readExternal(element.getChild("value"));

				key.myDefaultAttributes = attributes;
			}
		}
		catch(Exception e)
		{
			LOGGER.error(e);
		}
	}

	@Override
	public void disposeComponent()
	{

	}

	@NotNull
	@Override
	public String getComponentName()
	{
		return "CDefaultColorSchemesManager";
	}
}
