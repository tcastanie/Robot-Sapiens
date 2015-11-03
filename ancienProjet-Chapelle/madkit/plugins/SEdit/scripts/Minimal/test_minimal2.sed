<?xml version="1.0" encoding="UTF-8"?>
<structure name="example" type="minimal">
    <nodes>
        <node id="N0" type="node1">
            <graphic x="160" y="120">
                <property name="height">40</property>
                <property name="width">40</property>
            </graphic>
        </node>
        <node id="N1" type="node1">
            <graphic x="160" y="80">
                <property name="height">40</property>
                <property name="width">40</property>
            </graphic>
        </node>
        <node id="N2" type="node1">
            <graphic x="240" y="80">
                <property name="height">40</property>
                <property name="width">40</property>
            </graphic>
        </node>
        <node id="N3" type="node1">
            <graphic x="400" y="80">
                <property name="height">40</property>
                <property name="width">40</property>
            </graphic>
        </node>
        <node id="N4" type="node1">
            <graphic x="400" y="120">
                <property name="height">40</property>
                <property name="width">40</property>
            </graphic>
        </node>
        <node id="N5" type="node2">
            <graphic x="160" y="200"/>
        </node>
        <node id="N6" type="node2">
            <graphic x="280" y="200"/>
        </node>
        <node id="N7" type="node2">
            <graphic x="400" y="200"/>
        </node>
    </nodes>
    <arrows>
        <arrow from="N0" id="A0" to="N6" type="link1">
            <graphic x="250" y="180">
                <property name="endingform">2</property>
            </graphic>
        </arrow>
        <arrow from="N2" id="A1" to="N3" type="link2">
            <graphic x="340" y="100"/>
        </arrow>
        <arrow from="N4" id="A2" to="N6" type="link1">
            <graphic x="370" y="180">
                <property name="endingform">2</property>
            </graphic>
        </arrow>
        <arrow from="N7" id="A3" to="N2" type="link1">
            <graphic x="350" y="160">
                <property name="endingform">2</property>
            </graphic>
        </arrow>
        <arrow from="N2" id="A4" to="N1" type="link1">
            <graphic x="220" y="100">
                <property name="endingform">2</property>
            </graphic>
        </arrow>
    </arrows>
    <property name="gridsize">40</property>
    <property name="displaygrid">true</property>
    <property name="snaptogrid">true</property>
</structure>
