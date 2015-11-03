<?xml version="1.0" encoding="UTF-8"?>
<structure name="example" type="Petri">
    <nodes>
        <node id="N0" type="Place">
            <graphic x="97" y="35">
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N1" type="Place">
            <graphic x="226" y="50">
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N2" label="Transform" type="Transition">
            <graphic x="144" y="170"/>
        </node>
        <node id="N4" type="Place">
            <graphic x="270" y="273">
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N3" label="DoIt" type="Transition">
            <graphic x="401" y="161"/>
        </node>
    </nodes>
    <arrows>
        <arrow from="N0" id="A0" label="(p ?x ?y)" to="N2" type="Consumer">
            <graphic x="166" y="108"/>
            <property name="weight">1</property>
            <property name="filterstring">(p ?x ?y)</property>
        </arrow>
        <arrow from="N1" id="A1" to="N2" type="Consumer">
            <graphic x="230" y="116"/>
            <property name="weight">1</property>
        </arrow>
        <arrow from="N2" id="A2" label="(p ?x ?x)" to="N4" type="Producer">
            <graphic x="215" y="246"/>
            <property name="weight">1</property>
            <property name="filterstring">(p ?x ?x)</property>
        </arrow>
        <arrow from="N4" id="A3" label="(p ?x ?x)" to="N3" type="Consumer">
            <graphic x="441" y="293"/>
            <property name="weight">1</property>
            <property name="filterstring">(p ?x ?x)</property>
        </arrow>
        <arrow from="N3" id="A4" to="N1" type="Producer">
            <graphic x="343" y="123"/>
            <property name="weight">1</property>
        </arrow>
    </arrows>
    <property name="gridsize">10</property>
    <property name="displaygrid">false</property>
    <property name="snaptogrid">false</property>
</structure>
