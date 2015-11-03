<?xml version="1.0" encoding="UTF-8"?>
<structure name="example" type="Petri">
    <nodes>
        <node id="N0" type="Place">
            <graphic x="155" y="30">
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N1" type="Place">
            <graphic x="374" y="21">
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N2" type="Place">
            <graphic x="258" y="30">
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N3" type="Transition">
            <graphic x="336" y="186"/>
        </node>
        <node id="N5" type="Place">
            <graphic x="28" y="93">
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N6" type="Place">
            <graphic x="279" y="356">
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N7" type="Transition">
            <graphic x="81" y="264">
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N8" type="Place">
            <graphic x="295" y="260">
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N9" type="Transition">
            <graphic x="459" y="298">
                <property name="labellocation">4</property>
            </graphic>
        </node>
    </nodes>
    <arrows>
        <arrow from="N0" id="A0" to="N3" type="Consumer">
            <graphic x="195" y="131"/>
            <property name="weight">1</property>
        </arrow>
        <arrow from="N2" id="A1" to="N3" type="Consumer">
            <graphic x="278" y="122"/>
            <property name="weight">1</property>
        </arrow>
        <arrow from="N1" id="A2" to="N3" type="Consumer">
            <graphic x="390" y="120"/>
            <property name="weight">1</property>
        </arrow>
        <arrow from="N3" id="A3" to="N1" type="Producer">
            <graphic x="332" y="112"/>
            <property name="weight">1</property>
        </arrow>
        <arrow from="N5" id="A4" to="N7" type="Consumer">
            <graphic x="79" y="198"/>
            <property name="weight">1</property>
        </arrow>
        <arrow from="N6" id="A5" to="N7" type="Consumer">
            <graphic x="206" y="353"/>
            <property name="weight">1</property>
        </arrow>
        <arrow from="N7" id="A6" to="N8" type="Producer">
            <graphic x="241" y="280"/>
            <property name="weight">1</property>
        </arrow>
        <arrow from="N8" id="A7" to="N9" type="Consumer">
            <graphic x="387" y="280"/>
            <property name="weight">1</property>
        </arrow>
        <arrow from="N9" id="A8" to="N6" type="Producer">
            <graphic x="407" y="358"/>
            <property name="weight">1</property>
        </arrow>
        <arrow from="N3" id="A9" to="N8" type="Producer">
            <graphic x="345" y="241"/>
            <property name="weight">1</property>
        </arrow>
    </arrows>
    <property name="gridsize">10</property>
    <property name="displaygrid">false</property>
    <property name="snaptogrid">false</property>
</structure>
