<?xml version="1.0" encoding="UTF-8"?>
<structure name="example" type="automaton">
    <nodes>
        <node id="N0" label="tatatatitititatata" type="state">
            <graphic x="78" y="154">
                <property name="height">40</property>
                <property name="width">40</property>
            </graphic>
            <property name="word">tatatatitititatata</property>
        </node>
        <node id="N1" label="" type="state">
            <graphic x="391" y="154">
                <property name="height">40</property>
                <property name="width">40</property>
            </graphic>
        </node>
        <node id="N3" label="t" type="transition">
            <graphic x="252" y="154"/>
            <property name="transitionvalue">t</property>
        </node>
        <node id="N4" label="i" type="transition">
            <graphic x="250" y="76"/>
            <property name="transitionvalue">i</property>
        </node>
        <node id="N5" label="a" type="transition">
            <graphic x="251" y="239"/>
            <property name="transitionvalue">a</property>
        </node>
    </nodes>
    <arrows>
        <arrow from="N0" id="A0" to="N3" type="link">
            <graphic x="176" y="174"/>
        </arrow>
        <arrow from="N3" id="A1" to="N1" type="link">
            <graphic x="286" y="174"/>
        </arrow>
        <arrow from="N1" id="A2" to="N4" type="link">
            <graphic x="333" y="96"/>
        </arrow>
        <arrow from="N1" id="A3" to="N5" type="link">
            <graphic x="335" y="259"/>
        </arrow>
        <arrow from="N4" id="A6" to="N0" type="link">
            <graphic x="200" y="96"/>
        </arrow>
        <arrow from="N5" id="A7" to="N0" type="link">
            <graphic x="169" y="259"/>
        </arrow>
    </arrows>
    <property name="gridsize">10</property>
    <property name="displaygrid">false</property>
    <property name="snaptogrid">false</property>
</structure>
