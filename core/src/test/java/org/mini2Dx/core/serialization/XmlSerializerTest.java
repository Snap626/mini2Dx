/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.serialization;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.serialization.dummy.TestAbstractImplObject;
import org.mini2Dx.core.serialization.dummy.TestChildObject;
import org.mini2Dx.core.serialization.dummy.TestConstuctorArgObject;
import org.mini2Dx.core.serialization.dummy.TestInterface;
import org.mini2Dx.core.serialization.dummy.TestInterfaceImpl;
import org.mini2Dx.core.serialization.dummy.TestParentObject;
import org.mini2Dx.natives.Os;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import junit.framework.Assert;

/**
 * Utility class for {@link XmlSerializer} user acceptance tests
 */
public abstract class XmlSerializerTest {
	private XmlSerializer xmlSerializer;
	private TestParentObject parentObject;
	
	@Before
	public void setUp() {
		xmlSerializer = createXmlSerializer();
		parentObject = createTestParentObject();
	}
	
	public abstract XmlSerializer createXmlSerializer();
	
	@Test
	public void testXmlSerialization() throws SerializationException {
		String xml = xmlSerializer.toXml(parentObject);
		Assert.assertEquals(true, xml.length() > 2);
		System.out.println(xml);
		
		TestParentObject result = xmlSerializer.fromXml(xml, TestParentObject.class);
		Assert.assertTrue(result.isPostDeserializeCalled());
		
		Assert.assertEquals(parentObject.getSuperField(), result.getSuperField());
		Assert.assertEquals(parentObject.getEnumValue(), result.getEnumValue());
		Assert.assertEquals(parentObject.isBooleanValue(), result.isBooleanValue());
		Assert.assertEquals(parentObject.getByteValue(), result.getByteValue());
		Assert.assertEquals(parentObject.getFloatValue(), result.getFloatValue());
		Assert.assertEquals(parentObject.getIntValue(), result.getIntValue());
		Assert.assertEquals(parentObject.getIntArrayValue().length, result.getIntArrayValue().length);
		for(int i = 0; i < parentObject.getIntArrayValue().length; i++) {
			Assert.assertEquals(parentObject.getIntArrayValue()[i], result.getIntArrayValue()[i]);
		}
		Assert.assertEquals(parentObject.getLongValue(), result.getLongValue());
		Assert.assertEquals(parentObject.getShortValue(), result.getShortValue());
		Assert.assertEquals(parentObject.getStringValue(), result.getStringValue());
		Assert.assertEquals(parentObject.getStringArrayValue().length, result.getStringArrayValue().length);
		for(int i = 0; i < parentObject.getStringArrayValue().length; i++) {
			Assert.assertEquals(parentObject.getStringArrayValue()[i], result.getStringArrayValue()[i]);
		}
		Assert.assertEquals(parentObject.getListValues().size(), result.getListValues().size());
		Assert.assertEquals(parentObject.getListValues(), result.getListValues());
		Assert.assertEquals(parentObject.getMapValues().size(), result.getMapValues().size());
		for(String key : parentObject.getMapValues().keySet()) {
			Assert.assertEquals(true, result.getMapValues().containsKey(key));
			Assert.assertEquals(parentObject.getMapValues().get(key), result.getMapValues().get(key));
		}
		
		Assert.assertEquals(parentObject.getChildObject().getIntValue(), result.getChildObject().getIntValue());
		Assert.assertEquals(parentObject.getChildObjectArray().length, result.getChildObjectArray().length);
		for(int i = 0; i < parentObject.getChildObjectArray().length; i++) {
			Assert.assertEquals(parentObject.getChildObjectArray()[i], result.getChildObjectArray()[i]);
		}
		
		Assert.assertEquals(parentObject.getChildren().size(), result.getChildren().size());
		for(int i = 0; i < parentObject.getChildren().size(); i++) {
			Assert.assertEquals(parentObject.getChildren().get(i).getIntValue(), result.getChildren().get(i).getIntValue());
		}
		
		Assert.assertEquals(parentObject.getMapObjectValues().size(), result.getMapObjectValues().size());
		for(String key : parentObject.getMapObjectValues().keySet()) {
			Assert.assertEquals(true, result.getMapObjectValues().containsKey(key));
			Assert.assertEquals(parentObject.getMapObjectValues().get(key), result.getMapObjectValues().get(key));
		}
		
		Assert.assertNotSame(parentObject.getIgnoredValue(), result.getIgnoredValue());
		Assert.assertEquals(parentObject.getInterfaceObject(), result.getInterfaceObject());
		Assert.assertEquals(parentObject.getInterfaceObjectList().size(), result.getInterfaceObjectList().size());
		for(int i = 0; i < parentObject.getInterfaceObjectList().size(); i++) {
			Assert.assertEquals(parentObject.getInterfaceObjectList().get(i), result.getInterfaceObjectList().get(i));
		}
		
		Assert.assertEquals(parentObject.getFinalStringList().size(), result.getFinalStringList().size());
		for(int i = 0; i < parentObject.getFinalStringList().size(); i++) {
			Assert.assertEquals(parentObject.getFinalStringList().get(i), result.getFinalStringList().get(i));
		}
		Assert.assertEquals(parentObject.getFinalStringMap().size(), result.getFinalStringMap().size());
		for(String key : parentObject.getFinalStringMap().keySet()) {
			Assert.assertEquals(parentObject.getFinalStringMap().get(key), result.getFinalStringMap().get(key));
		}
		Assert.assertEquals(parentObject.getFinalStringArray().length, result.getFinalStringArray().length);
		for(int i = 0; i < parentObject.getFinalStringArray().length; i++) {
			Assert.assertEquals(parentObject.getFinalStringArray()[i], result.getFinalStringArray()[i]);
		}
		Assert.assertEquals(parentObject.getAbstractObject().getValue(), result.getAbstractObject().getValue());
	
		Assert.assertEquals(parentObject.getGdxObjectMap().size, result.getGdxObjectMap().size);
		ObjectMap.Entries<String, String> entries = parentObject.getGdxObjectMap().entries();
		while(entries.hasNext()) {
			ObjectMap.Entry<String, String> entry = entries.next();
			Assert.assertEquals(entry.value, result.getGdxObjectMap().get(entry.key));
		}
		
		Assert.assertEquals(parentObject.getGdxArray().size, result.getGdxArray().size);
		for(int i = 0; i < parentObject.getGdxArray().size; i++) {
			Assert.assertEquals(parentObject.getGdxArray().get(i), result.getGdxArray().get(i));
		}
	}
	
	@Test
	public void testPrettyXmlDeserialization() throws SerializationException {
		String xml = "<?xml version=\"1.0\"?>\n";
		xml += "<data>\n";
		xml += "    <intValue>255</intValue>\n";
		xml += "    <booleanValue>true</booleanValue>\n";
		xml += "    <byteValue>1</byteValue>\n";
		xml += "    <shortValue>655</shortValue>\n";
		xml += "    <longValue>9223372036854775807</longValue>\n";
		xml += "    <floatValue>2.5</floatValue>\n";
		xml += "    <intArrayValue length=\"3\">\n";
		xml += "        <value>1</value>\n";
		xml += "        <value>2</value>\n";
		xml += "        <value>3</value>\n";
		xml += "    </intArrayValue>\n";
		xml += "    <stringArrayValue length=\"2\">\n";
		xml += "        <value>item1</value>\n";
		xml += "        <value>item2</value>\n";
		xml += "    </stringArrayValue>\n";
		xml += "    <stringValue>hello</stringValue>\n";
		xml += "    <enumValue>UNKNOWN</enumValue>\n";
		xml += "    <mapValues>\n";
		xml += "        <entry>\n";
		xml += "            <key>key</key>\n";
		xml += "            <value>77</value>\n";
		xml += "        </entry>\n";
		xml += "    </mapValues>\n";
		xml += "    <listValues>\n";
		xml += "        <value>itemA</value>\n";
		xml += "        <value>itemB</value>\n";
		xml += "    </listValues>\n";
		xml += "    <childObject>\n";
		xml += "        <intValue>34</intValue>\n";
		xml += "    </childObject>\n";
		xml += "    <childObjectArray length=\"3\">\n";
		xml += "        <value>\n";
		xml += "            <intValue>51</intValue>\n";
		xml += "        </value>\n";
		xml += "        <value>\n";
		xml += "            <intValue>57</intValue>\n";
		xml += "        </value>\n";
		xml += "    </childObjectArray>\n";
		xml += "    <optionalChildObject/>\n";
		xml += "    <children>\n";
		xml += "        <value>\n";
		xml += "            <intValue>35</intValue>\n";
		xml += "        </value>\n";
		xml += "        <value>\n";
		xml += "            <intValue>36</intValue>\n";
		xml += "        </value>\n";
		xml += "    </children>\n";
		xml += "    <superField>super super</superField>\n";
		xml += "    <argObject argValue=\"cargValue\">\n";
		xml += "    </argObject>\n";
		xml += "    <interfaceObject id=\"id-5\" class=\"org.mini2Dx.core.serialization.dummy.TestInterfaceImpl\">\n";
		xml += "    </interfaceObject>\n";
		xml += "    <interfaceObjectList>\n";
		xml += "        <value id=\"id-3\" class=\"org.mini2Dx.core.serialization.dummy.TestInterfaceImpl\">\n";
		xml += "        </value>\n";
		xml += "        <value id=\"id-4\" class=\"org.mini2Dx.core.serialization.dummy.TestInterfaceImpl\">\n";
		xml += "        </value>\n";
		xml += "    </interfaceObjectList>\n";
		xml += "    <finalStringList>\n";
		xml += "        <value>fstr1</value>\n";
		xml += "        <value>fstr2</value>\n";
		xml += "    </finalStringList>\n";
		xml += "    <finalStringArray length=\"3\">\n";
		xml += "        <value>fstr3</value>\n";
		xml += "        <value>fstr4</value>\n";
		xml += "        <value>fstr5</value>\n";
		xml += "    </finalStringArray>\n";
		xml += "    <finalStringMap>\n";
		xml += "        <entry>\n";
		xml += "            <key>fkey1</key>\n";
		xml += "            <value>fstr6</value>\n";
		xml += "        </entry>\n";
		xml += "        <entry>\n";
		xml += "            <key>fkey2</key>\n";
		xml += "            <value>fstr7</value>\n";
		xml += "        </entry>\n";
		xml += "    </finalStringMap>\n";
		xml += "    <abstractObject class=\"org.mini2Dx.core.serialization.dummy.TestAbstractImplObject\">\n";
		xml += "        <value>91</value>\n";
		xml += "    </abstractObject>\n";
		xml += "    <gdxObjectMap>\n";
		xml += "        <entry>\n";
		xml += "            <key>testGdxKey</key>\n";
		xml += "            <value>testGdxValue</value>\n";
		xml += "        </entry>\n";
		xml += "    </gdxObjectMap>\n";
		xml += "    <gdxArray>\n";
		xml += "        <value>testGdxArrayValue</value>\n";
		xml += "    </gdxArray>\n";
		xml += "</data>";
		
		TestParentObject result = xmlSerializer.fromXml(xml, TestParentObject.class);
		Assert.assertEquals(parentObject.getSuperField(), result.getSuperField());
		Assert.assertEquals(parentObject.getEnumValue(), result.getEnumValue());
		Assert.assertEquals(parentObject.isBooleanValue(), result.isBooleanValue());
		Assert.assertEquals(parentObject.getByteValue(), result.getByteValue());
		Assert.assertEquals(parentObject.getFloatValue(), result.getFloatValue());
		Assert.assertEquals(parentObject.getIntValue(), result.getIntValue());
		Assert.assertEquals(parentObject.getIntArrayValue().length, result.getIntArrayValue().length);
		for(int i = 0; i < parentObject.getIntArrayValue().length; i++) {
			Assert.assertEquals(parentObject.getIntArrayValue()[i], result.getIntArrayValue()[i]);
		}
		Assert.assertEquals(parentObject.getLongValue(), result.getLongValue());
		Assert.assertEquals(parentObject.getShortValue(), result.getShortValue());
		Assert.assertEquals(parentObject.getStringValue(), result.getStringValue());
		Assert.assertEquals(parentObject.getStringArrayValue().length, result.getStringArrayValue().length);
		for(int i = 0; i < parentObject.getStringArrayValue().length; i++) {
			Assert.assertEquals(parentObject.getStringArrayValue()[i], result.getStringArrayValue()[i]);
		}
		Assert.assertEquals(parentObject.getListValues().size(), result.getListValues().size());
		Assert.assertEquals(parentObject.getListValues(), result.getListValues());
		Assert.assertEquals(parentObject.getMapValues().size(), result.getMapValues().size());
		for(String key : parentObject.getMapValues().keySet()) {
			Assert.assertEquals(true, result.getMapValues().containsKey(key));
			Assert.assertEquals(parentObject.getMapValues().get(key), result.getMapValues().get(key));
		}
		
		Assert.assertEquals(parentObject.getChildObject().getIntValue(), result.getChildObject().getIntValue());
		Assert.assertEquals(parentObject.getChildObjectArray().length, result.getChildObjectArray().length);
		for(int i = 0; i < parentObject.getChildObjectArray().length; i++) {
			Assert.assertEquals(parentObject.getChildObjectArray()[i], result.getChildObjectArray()[i]);
		}
		
		Assert.assertEquals(parentObject.getChildren().size(), result.getChildren().size());
		for(int i = 0; i < parentObject.getChildren().size(); i++) {
			Assert.assertEquals(parentObject.getChildren().get(i).getIntValue(), result.getChildren().get(i).getIntValue());
		}
		
		Assert.assertNotSame(parentObject.getIgnoredValue(), result.getIgnoredValue());
		Assert.assertEquals(parentObject.getInterfaceObject(), result.getInterfaceObject());
		Assert.assertEquals(parentObject.getInterfaceObjectList().size(), result.getInterfaceObjectList().size());
		for(int i = 0; i < parentObject.getInterfaceObjectList().size(); i++) {
			Assert.assertEquals(parentObject.getInterfaceObjectList().get(i), result.getInterfaceObjectList().get(i));
		}
		Assert.assertEquals(parentObject.getAbstractObject().getValue(), result.getAbstractObject().getValue());
	
		Assert.assertEquals(parentObject.getGdxObjectMap().size, result.getGdxObjectMap().size);
		ObjectMap.Entries<String, String> entries = parentObject.getGdxObjectMap().entries();
		while(entries.hasNext()) {
			ObjectMap.Entry<String, String> entry = entries.next();
			Assert.assertEquals(entry.value, result.getGdxObjectMap().get(entry.key));
		}
		
		Assert.assertEquals(parentObject.getGdxArray().size, result.getGdxArray().size);
		for(int i = 0; i < parentObject.getGdxArray().size; i++) {
			Assert.assertEquals(parentObject.getGdxArray().get(i), result.getGdxArray().get(i));
		}
	}
	
	protected TestParentObject createTestParentObject() {
		TestParentObject parentObject = new TestParentObject();
		parentObject.setSuperField("super super");
		parentObject.setBooleanValue(true);
		parentObject.setByteValue((byte) 1);
		parentObject.setFloatValue(2.5f);
		parentObject.setIgnoredValue(1);
		parentObject.setIntValue(255);
		parentObject.setEnumValue(Os.UNKNOWN);
		
		parentObject.setListValues(new ArrayList<String>());
		parentObject.getListValues().add("itemA");
		parentObject.getListValues().add("itemB");
		
		parentObject.setLongValue(Long.MAX_VALUE);
		parentObject.setMapValues(new HashMap<String, Integer>());
		parentObject.getMapValues().put("key", 77);
		
		parentObject.setShortValue((short) 655);
		parentObject.setStringValue("hello");
		parentObject.setStringArrayValue(new String[] { "item1", "item2" });
		parentObject.setIntArrayValue(new int[] { 1, 2, 3 });
		
		parentObject.setChildObject(new TestChildObject(34));
		parentObject.setChildObjectArray(new TestChildObject[3]);
		parentObject.getChildObjectArray()[0] = new TestChildObject(51);
		parentObject.getChildObjectArray()[1] = new TestChildObject(57);
		
		parentObject.setChildren(new ArrayList<TestChildObject>());
		parentObject.getChildren().add(new TestChildObject(35));
		parentObject.getChildren().add(new TestChildObject(36));
		
		parentObject.setMapObjectValues(new HashMap<String, TestChildObject>());
		parentObject.getMapObjectValues().put("key1", new TestChildObject(100));
		parentObject.getMapObjectValues().put("key2", new TestChildObject(101));
		
		parentObject.setArgObject(new TestConstuctorArgObject("cargValue"));
		parentObject.setInterfaceObject(new TestInterfaceImpl("id-5"));
		parentObject.setInterfaceObjectList(new ArrayList<TestInterface>());
		parentObject.getInterfaceObjectList().add(new TestInterfaceImpl("id-3"));
		parentObject.getInterfaceObjectList().add(new TestInterfaceImpl("id-4"));
		
		parentObject.getFinalStringList().add("fstr1");
		parentObject.getFinalStringList().add("fstr2");
		
		parentObject.getFinalStringArray()[0] = "fstr3";
		parentObject.getFinalStringArray()[1] = "fstr4";
		parentObject.getFinalStringArray()[2] = "fstr5";
		
		parentObject.getFinalStringMap().put("fkey1", "fstr6");
		parentObject.getFinalStringMap().put("fkey2", "fstr7");
		
		parentObject.setAbstractObject(new TestAbstractImplObject());
		parentObject.getAbstractObject().setValue(91);
		
		parentObject.setGdxObjectMap(new ObjectMap<String, String>());
		parentObject.getGdxObjectMap().put("testGdxKey", "testGdxValue");
		
		parentObject.setGdxArray(new Array<String>());
		parentObject.getGdxArray().add("testGdxArrayValue");
		return parentObject;
	}
}
