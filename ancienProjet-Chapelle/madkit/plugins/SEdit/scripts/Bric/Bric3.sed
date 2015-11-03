<?xml version="1.0" encoding="UTF-8"?>
<structure name="example" type="Bric1">
    <nodes>
        <node id="N0" label="StringMessage" type="InPort">
            <graphic x="123" y="86">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">50</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N2" type="Place">
            <graphic x="599" y="86">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N3" type="Transition">
            <graphic x="453" y="194">
                <property name="height">32</property>
                <property name="displaylabel">true</property>
                <property name="width">80</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="actionstring">(set-recipient ?from)</property>
        </node>
        <node id="N4" type="OutStringMessage">
            <graphic x="636" y="190">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">60</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="messagetype">String</property>
        </node>
        <node id="N5" type="Transition">
            <graphic x="352" y="90">
                <property name="height">32</property>
                <property name="displaylabel">true</property>
                <property name="width">80</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="actionstring">(println "test")</property>
        </node>
        <node id="N1" type="Place">
            <graphic x="226" y="240">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
    </nodes>
    <arrows>
        <arrow from="N0" id="A0" label="(StringMessage ?from ?s)"
            to="N5" type="Consumer">
            <graphic x="259" y="106">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="displaylabel">true</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="weight">1</property>
            <property name="filterstring">(StringMessage ?from ?s)</property>
        </arrow>
        <arrow from="N2" id="A1" label="(p ?s)" to="N3" type="Consumer">
            <graphic x="349" y="210">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="displaylabel">true</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="weight">1</property>
            <property name="filterstring">(p ?s)</property>
        </arrow>
        <arrow from="N5" id="A2" label="(p ?s)" to="N2" type="Producer">
            <graphic x="513" y="106">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="displaylabel">true</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="weight">1</property>
            <property name="filterstring">(p ?s)</property>
        </arrow>
        <arrow from="N3" id="A3" label="?s" to="N4" type="Producer">
            <graphic x="580" y="210">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="displaylabel">true</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="filter">?s</property>
            <property name="weight">1</property>
            <property name="filterstring">?s</property>
        </arrow>
        <arrow from="N5" id="A4" label="?from" to="N1" type="Producer">
            <graphic x="285" y="184">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="displaylabel">true</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="filter">?from</property>
            <property name="weight">1</property>
            <property name="filterstring">?from</property>
        </arrow>
        <arrow from="N1" id="A5" label="?from" to="N3" type="Consumer">
            <graphic x="377" y="260">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="displaylabel">true</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="filter">?from</property>
            <property name="weight">1</property>
            <property name="filterstring">?from</property>
        </arrow>
    </arrows>
</structure>
