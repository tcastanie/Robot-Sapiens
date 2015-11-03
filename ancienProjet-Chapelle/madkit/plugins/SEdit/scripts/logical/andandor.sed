<?xml version="1.0" encoding="UTF-8"?>
<structure name="example" type="logicalgates">
    <nodes>
        <node id="N13" label="outlet2" type="outlet">
            <graphic x="278" y="117">
                <property name="width">6</property>
                <property name="height">6</property>
                <property name="displaylabel">false</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N14" label="inlet1" type="inlet">
            <graphic x="232" y="109">
                <property name="width">6</property>
                <property name="height">6</property>
                <property name="displaylabel">false</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="value">true</property>
        </node>
        <node id="N15" label="inlet0" type="inlet">
            <graphic x="232" y="123">
                <property name="width">6</property>
                <property name="height">6</property>
                <property name="displaylabel">false</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="value">false</property>
        </node>
        <node id="N24" label="inlet1" type="inlet">
            <graphic x="232" y="331">
                <property name="width">6</property>
                <property name="height">6</property>
                <property name="displaylabel">false</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="value">true</property>
        </node>
        <node id="N25" label="inlet0" type="inlet">
            <graphic x="232" y="345">
                <property name="width">6</property>
                <property name="height">6</property>
                <property name="displaylabel">false</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="value">true</property>
        </node>
        <node id="N26" label="outlet2" type="outlet">
            <graphic x="278" y="339">
                <property name="width">6</property>
                <property name="height">6</property>
                <property name="displaylabel">false</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N33" label="outlet2" type="outlet">
            <graphic x="429" y="230">
                <property name="width">6</property>
                <property name="height">6</property>
                <property name="displaylabel">false</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N34" label="inlet1" type="inlet">
            <graphic x="383" y="222">
                <property name="width">6</property>
                <property name="height">6</property>
                <property name="displaylabel">false</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="value">false</property>
        </node>
        <node id="N35" label="inlet0" type="inlet">
            <graphic x="383" y="236">
                <property name="width">6</property>
                <property name="height">6</property>
                <property name="displaylabel">false</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="value">true</property>
        </node>
        <node id="N0" type="switch">
            <graphic x="68" y="53">
                <property name="width">28</property>
                <property name="height">42</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="value">true</property>
        </node>
        <node id="N2" type="switch">
            <graphic x="66" y="149">
                <property name="width">28</property>
                <property name="height">42</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="value">false</property>
        </node>
        <node id="N4" type="display">
            <graphic x="238" y="26">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N5" type="display">
            <graphic x="238" y="180">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N12" type="andgate">
            <connector name="inlet1" ref="N14" side="Left"/>
            <connector name="inlet0" ref="N15" side="Left"/>
            <connector name="outlet2" ref="N13" side="Right"/>
            <graphic x="235" y="101">
                <property name="width">46</property>
                <property name="height">38</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N27" type="switch">
            <graphic x="68" y="272">
                <property name="width">28</property>
                <property name="height">42</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
                <property name="imageaddress">images/logical/logical_button_switch.gif</property>
            </graphic>
            <property name="value">true</property>
        </node>
        <node id="N28" type="switch">
            <graphic x="66" y="368">
                <property name="width">28</property>
                <property name="height">42</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="value">true</property>
        </node>
        <node id="N29" type="display">
            <graphic x="238" y="245">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N30" type="display">
            <graphic x="238" y="399">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N31" type="andgate">
            <connector name="inlet1" ref="N24" side="Left"/>
            <connector name="inlet0" ref="N25" side="Left"/>
            <connector name="outlet2" ref="N26" side="Right"/>
            <graphic x="235" y="323">
                <property name="width">46</property>
                <property name="height">38</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N32" type="orgate">
            <connector name="inlet1" ref="N34" side="Left"/>
            <connector name="inlet0" ref="N35" side="Left"/>
            <connector name="outlet2" ref="N33" side="Right"/>
            <graphic x="386" y="214">
                <property name="width">46</property>
                <property name="height">38</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N36" type="display">
            <graphic x="514" y="213">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N37" type="display">
            <graphic x="389" y="100">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N38" type="display">
            <graphic x="389" y="322">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
    </nodes>
    <arrows>
        <arrow from="N0" id="A0" to="N4" type="link">
            <graphic x="176" y="74">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N2" id="A2" to="N5" type="link">
            <graphic x="176" y="170">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N0" id="A4" to="N14" type="link">
            <graphic x="176" y="106">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N2" id="A5" to="N15" type="link">
            <graphic x="176" y="153">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N27" id="A8" to="N29" type="link">
            <graphic x="176" y="293">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N28" id="A9" to="N30" type="link">
            <graphic x="176" y="389">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N27" id="A10" to="N24" type="link">
            <graphic x="176" y="325">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N28" id="A11" to="N25" type="link">
            <graphic x="176" y="372">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N13" id="A12" to="N34" type="link">
            <graphic x="331" y="200">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N26" id="A13" to="N35" type="link">
            <graphic x="331" y="265">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N33" id="A14" to="N36" type="link">
            <graphic x="480" y="233">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N13" id="A15" to="N37" type="link">
            <graphic x="331" y="120">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N26" id="A16" to="N38" type="link">
            <graphic x="331" y="342">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
    </arrows>
</structure>
