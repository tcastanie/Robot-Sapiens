<?xml version="1.0" encoding="UTF-8"?>
<structure name="example" type="automaton">
    <nodes>
        <node id="N0" label="" type="state">
            <graphic x="140" y="18">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N1" label="" type="state">
            <graphic x="291" y="18">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N2" label="" type="state">
            <graphic x="441" y="18">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N3" label="papapou" type="state">
            <graphic x="10" y="18">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="word">papapou</property>
        </node>
        <node id="N4" label="p" type="transition">
            <graphic x="88" y="18">
                <property name="width">10</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="transitionvalue">p</property>
        </node>
        <node id="N5" label="o" type="transition">
            <graphic x="227" y="18">
                <property name="width">10</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="transitionvalue">o</property>
        </node>
        <node id="N6" label="u" type="transition">
            <graphic x="382" y="18">
                <property name="width">10</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="transitionvalue">u</property>
        </node>
        <node id="N7" label="a" type="transition">
            <graphic x="88" y="85">
                <property name="width">10</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="transitionvalue">a</property>
        </node>
    </nodes>
    <arrows>
        <arrow from="N3" id="A0" to="N4" type="link">
            <graphic x="61" y="38">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N4" id="A1" to="N0" type="link">
            <graphic x="126" y="38">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N0" id="A2" to="N5" type="link">
            <graphic x="196" y="38">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N5" id="A3" to="N1" type="link">
            <graphic x="271" y="38">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N1" id="A4" to="N6" type="link">
            <graphic x="349" y="38">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N6" id="A5" to="N2" type="link">
            <graphic x="424" y="38">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N0" id="A6" to="N7" type="link">
            <graphic x="126" y="71">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N7" id="A7" to="N3" type="link">
            <graphic x="61" y="71">
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
