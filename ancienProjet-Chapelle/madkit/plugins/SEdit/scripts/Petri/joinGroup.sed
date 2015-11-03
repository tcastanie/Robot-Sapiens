<?xml version="1.0" encoding="UTF-8"?>
<structure name="example" type="Petri">
    <nodes>
        <node id="N0" type="Place">
            <graphic x="89" y="95">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N1" type="Place">
            <graphic x="384" y="95">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N2" label="CreateGroup" type="Transition">
            <graphic x="217" y="99">
                <property name="height">32</property>
                <property name="displaylabel">true</property>
                <property name="width">80</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="actionstring">(join-group &quot;truc&quot;)</property>
        </node>
    </nodes>
    <arrows>
        <arrow from="N0" id="A0" to="N2" type="Consumer">
            <graphic x="163" y="115">
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
        <arrow from="N2" id="A1" to="N1" type="Producer">
            <graphic x="345" y="115">
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
