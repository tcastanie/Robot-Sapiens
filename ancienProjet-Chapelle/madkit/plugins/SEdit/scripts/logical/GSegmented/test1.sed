<?xml version="1.0" encoding="UTF-8"?>
<structure name="example" type="logicalgates_seg">
    <nodes>
        <node id="N1" label="inlet1" type="inlet">
            <graphic x="106" y="90">
                <property name="displaylabel">false</property>
            </graphic>
            <property name="value">true</property>
        </node>
        <node id="N2" label="inlet0" type="inlet">
            <graphic x="106" y="104">
                <property name="displaylabel">false</property>
            </graphic>
            <property name="value">true</property>
        </node>
        <node id="N3" label="outlet2" type="outlet">
            <graphic x="152" y="98">
                <property name="displaylabel">false</property>
            </graphic>
        </node>
        <node id="N5" label="inlet1" type="inlet">
            <graphic x="319" y="196">
                <property name="displaylabel">false</property>
            </graphic>
            <property name="value">true</property>
        </node>
        <node id="N6" label="inlet0" type="inlet">
            <graphic x="319" y="210">
                <property name="displaylabel">false</property>
            </graphic>
            <property name="value">false</property>
        </node>
        <node id="N7" label="outlet2" type="outlet">
            <graphic x="365" y="204">
                <property name="displaylabel">false</property>
            </graphic>
        </node>
        <node id="N11" label="inlet0" type="inlet">
            <graphic x="191" y="304">
                <property name="displaylabel">false</property>
            </graphic>
            <property name="value">true</property>
        </node>
        <node id="N12" label="outlet1" type="outlet">
            <graphic x="231" y="304">
                <property name="displaylabel">false</property>
            </graphic>
        </node>
        <node id="N0" type="andgate">
            <connector name="inlet1" ref="N1" side="Left"/>
            <connector name="inlet0" ref="N2" side="Left"/>
            <connector name="outlet2" ref="N3" side="Right"/>
            <graphic x="109" y="82"/>
        </node>
        <node id="N4" type="orgate">
            <connector name="inlet1" ref="N5" side="Left"/>
            <connector name="inlet0" ref="N6" side="Left"/>
            <connector name="outlet2" ref="N7" side="Right"/>
            <graphic x="322" y="188">
                <property name="height">38</property>
                <property name="width">46</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N8" type="display">
            <graphic x="492" y="161">
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N9" type="display">
            <graphic x="345" y="40">
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N10" type="notgate">
            <connector name="inlet0" ref="N11" side="Left"/>
            <connector name="outlet1" ref="N12" side="Right"/>
            <graphic x="194" y="286">
                <property name="height">42</property>
                <property name="width">40</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N16" type="switch">
            <graphic x="27" y="36">
                <property name="height">42</property>
                <property name="width">28</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="value">true</property>
        </node>
        <node id="N17" type="switch">
            <graphic x="30" y="152">
                <property name="height">42</property>
                <property name="width">28</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="value">true</property>
        </node>
    </nodes>
    <arrows>
        <arrow from="N3" id="A0" to="N5" type="link2">
            <graphic x="238" y="101">
                <property name="linestyle">1</property>
                <property name="displaylabel">false</property>
            </graphic>
        </arrow>
        <arrow from="N3" id="A1" to="N9" type="link2">
            <graphic x="260" y="101">
                <property name="linestyle">1</property>
                <property name="displaylabel">false</property>
            </graphic>
        </arrow>
        <arrow from="N17" id="A3" to="N2" type="link2">
            <graphic x="76" y="173">
                <property name="linestyle">1</property>
                <property name="displaylabel">false</property>
            </graphic>
        </arrow>
        <arrow from="N16" id="A4" to="N1" type="link2">
            <graphic x="75" y="57">
                <property name="linestyle">1</property>
                <property name="displaylabel">false</property>
            </graphic>
        </arrow>
        <arrow from="N3" id="A5" to="N11" type="link2">
            <graphic x="174" y="101">
                <property name="linestyle">1</property>
                <property name="displaylabel">false</property>
            </graphic>
        </arrow>
        <arrow from="N12" id="A6" to="N6" type="link2">
            <graphic x="278" y="307">
                <property name="linestyle">1</property>
                <property name="displaylabel">false</property>
            </graphic>
        </arrow>
        <arrow from="N7" id="A7" to="N8" type="link2">
            <graphic x="440" y="207">
                <property name="linestyle">1</property>
                <property name="displaylabel">false</property>
            </graphic>
        </arrow>
    </arrows>
    <property name="gridsize">10</property>
    <property name="displaygrid">false</property>
    <property name="snaptogrid">false</property>
</structure>
