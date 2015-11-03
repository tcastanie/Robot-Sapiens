<?xml version="1.0" encoding="UTF-8"?>
<structure name="example" type="Petri">
    <nodes>
        <node id="N0" type="Place">
            <graphic x="97" y="35">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N1" type="Place">
            <graphic x="226" y="50">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N2" type="Transition">
            <graphic x="174" y="146">
                <property name="height">32</property>
                <property name="displaylabel">true</property>
                <property name="width">80</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N4" type="Place">
            <graphic x="222" y="239">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N3" type="Transition">
            <graphic x="400" y="161">
                <property name="height">32</property>
                <property name="displaylabel">true</property>
                <property name="width">80</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
    </nodes>
    <arrows>
        <arrow from="N0" id="A0" to="N2" type="Consumer">
            <graphic x="165" y="108">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="displaylabel">false</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="weight">1</property>
        </arrow>
        <arrow from="N1" id="A1" to="N2" type="Consumer">
            <graphic x="230" y="116">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="displaylabel">false</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="weight">1</property>
        </arrow>
        <arrow from="N2" id="A2" to="N4" type="Producer">
            <graphic x="228" y="210">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="displaylabel">false</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="weight">1</property>
        </arrow>
        <arrow from="N4" id="A3" to="N3" type="Consumer">
            <graphic x="341" y="218">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="displaylabel">false</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="weight">1</property>
        </arrow>
        <arrow from="N3" id="A4" to="N1" type="Producer">
            <graphic x="343" y="123">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="displaylabel">false</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="weight">1</property>
        </arrow>
    </arrows>
</structure>
