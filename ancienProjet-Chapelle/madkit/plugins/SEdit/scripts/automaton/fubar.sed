<?xml version="1.0" encoding="UTF-8"?>
<structure name="example" type="automaton">
    <nodes>
        <node id="N0" label="fuuuuubar" type="state">
            <graphic x="96" y="64">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="word">fuuuuubar</property>
        </node>
        <node id="N1" label="" type="state">
            <graphic x="243" y="64">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N2" label="" type="state">
            <graphic x="397" y="122">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N4" label="" type="state">
            <graphic x="243" y="166">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N5" label="f" type="transition">
            <graphic x="176" y="64">
                <property name="width">10</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="transitionvalue">f</property>
        </node>
        <node id="N6" label="u" type="transition">
            <graphic x="309" y="12">
                <property name="width">10</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="transitionvalue">u</property>
        </node>
        <node id="N7" label="b" type="transition">
            <graphic x="309" y="103">
                <property name="width">10</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="transitionvalue">b</property>
        </node>
        <node id="N8" label="a" type="transition">
            <graphic x="312" y="166">
                <property name="width">10</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="transitionvalue">a</property>
        </node>
        <node id="N9" label="r" type="transition">
            <graphic x="183" y="166">
                <property name="width">10</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="transitionvalue">r</property>
        </node>
        <node id="N10" label="" type="state">
            <graphic x="109" y="166">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
    </nodes>
    <arrows>
        <arrow from="N0" id="A0" to="N5" type="link">
            <graphic x="148" y="84">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N5" id="A1" to="N1" type="link">
            <graphic x="222" y="84">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N1" id="A2" to="N6" type="link">
            <graphic x="313" y="84">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N6" id="A3" to="N1" type="link">
            <graphic x="263" y="32">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N1" id="A4" to="N7" type="link">
            <graphic x="288" y="103">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N7" id="A5" to="N2" type="link">
            <graphic x="345" y="129">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N2" id="A6" to="N8" type="link">
            <graphic x="346" y="161">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N8" id="A7" to="N4" type="link">
            <graphic x="290" y="186">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N4" id="A8" to="N9" type="link">
            <graphic x="225" y="186">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
        <arrow from="N9" id="A9" to="N10" type="link">
            <graphic x="158" y="186">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
        </arrow>
    </arrows>
</structure>
