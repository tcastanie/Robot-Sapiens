<?xml version="1.0" encoding="UTF-8"?>
<structure name="example" type="Bric1">
    <nodes>
        <node id="N0" label="StringMessage" type="InPort">
            <graphic x="80" y="63">
                <property name="width">50</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N1" type="Transition">
            <graphic x="239" y="43">
                <property name="width">80</property>
                <property name="height">32</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N2" type="Place">
            <graphic x="335" y="127">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N3" type="Transition">
            <graphic x="208" y="241">
                <property name="width">80</property>
                <property name="height">32</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N4" type="OutStringMessage">
            <graphic x="373" y="258">
                <property name="width">60</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="messagetype">String</property>
        </node>
        <node id="N5" type="Place">
            <graphic x="136" y="178">
                <property name="width">40</property>
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
    </nodes>
    <arrows>
        <arrow from="N0" id="A0" label="(StringMessage ?from ?s)"
            to="N1" type="Consumer">
            <graphic x="168" y="91">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">true</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
            <property name="weight">1</property>
            <property name="filterstring">(StringMessage ?from ?s)</property>
        </arrow>
        <arrow from="N2" id="A1" label="?s" to="N3" type="Consumer">
            <graphic x="301" y="202">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">true</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
            <property name="weight">1</property>
            <property name="filterstring">?s</property>
            <property name="filter">?s</property>
        </arrow>
        <arrow from="N1" id="A2" label="?s" to="N2" type="Producer">
            <graphic x="293" y="123">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">true</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
            <property name="weight">1</property>
            <property name="filterstring">?s</property>
            <property name="filter">?s</property>
        </arrow>
        <arrow from="N3" id="A3" to="N4" type="Producer">
            <graphic x="325" y="267">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">false</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
            <property name="weight">1</property>
        </arrow>
        <arrow from="N5" id="A4" label="?to" to="N3" type="Consumer">
            <graphic x="202" y="227">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">true</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
            <property name="weight">1</property>
            <property name="filterstring">?to</property>
            <property name="filter">?to</property>
        </arrow>
        <arrow from="N1" id="A5" label="?from" to="N5" type="Producer">
            <graphic x="217" y="128">
                <property name="width">0</property>
                <property name="height">0</property>
                <property name="displaylabel">true</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="labellocation">5</property>
                <property name="endingform">1</property>
            </graphic>
            <property name="weight">1</property>
            <property name="filterstring">?from</property>
            <property name="filter">?from</property>
        </arrow>
    </arrows>
</structure>
