<?xml version="1.0" encoding="UTF-8"?>
<structure name="example" type="tutorial2">
    <nodes>
        <node id="N0" type="round node">
            <graphic x="192" y="134">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N1" type="round node">
            <graphic x="437" y="396">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N2" type="iconic node">
            <graphic x="400" y="115">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="imageaddress">images/monalisa.gif</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
    </nodes>
    <arrows>
        <arrow from="N0" id="A0" to="N1" type="link3">
            <graphic x="334" y="285">
                <property name="height">0</property>
                <property name="endingform">5</property>
                <property name="startingform">4</property>
                <property name="linestyle">2</property>
                <property name="displaylabel">true</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N0" id="A1" label="toto" to="N2" type="link3">
            <graphic x="316" y="145">
                <property name="height">0</property>
                <property name="endingform">5</property>
                <property name="startingform">4</property>
                <property name="linestyle">2</property>
                <property name="displaylabel">true</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
            <property name="comment">une fleche</property>
        </arrow>
    </arrows>
    <property name="gridsize">10</property>
    <property name="displaygrid">false</property>
    <property name="snaptogrid">false</property>
</structure>
