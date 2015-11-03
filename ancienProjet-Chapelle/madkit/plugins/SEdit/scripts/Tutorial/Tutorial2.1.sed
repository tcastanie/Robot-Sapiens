<?xml version="1.0" encoding="UTF-8"?>
<structure name="example" type="tutorial2">
    <nodes>
        <node id="N0" type="round node">
            <graphic x="323" y="13">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N2" type="round node">
            <graphic x="365" y="96">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N3" type="round node">
            <graphic x="128" y="99">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N5" type="rectangular node">
            <graphic x="491" y="34">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">80</property>
                <property name="labellocation">5</property>
            </graphic>
        </node>
        <node id="N7" label="An Icon" type="iconic node">
            <graphic x="20" y="18">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="imageaddress">images/monalisa.gif</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="comment">A beautiful icon, la joconde</property>
        </node>
        <node id="N10" label="An edited label" type="iconic node">
            <graphic x="248" y="24">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="imageaddress">images/monalisa.gif</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="comment">A beautiful icon, la joconde</property>
        </node>
        <node id="N1" label="An Icon" type="iconic node">
            <graphic x="704" y="34">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="imageaddress">images/monalisa.gif</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="comment">A beautiful icon, la joconde</property>
        </node>
    </nodes>
    <arrows>
        <arrow from="N3" id="A1" to="N10" type="link2">
            <graphic x="208" y="81">
                <property name="height">0</property>
                <property name="endingform">3</property>
                <property name="startingform">2</property>
                <property name="linestyle">1</property>
                <property name="displaylabel">false</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N3" id="A2" to="N7" type="link2">
            <graphic x="94" y="78">
                <property name="height">0</property>
                <property name="endingform">3</property>
                <property name="startingform">2</property>
                <property name="linestyle">1</property>
                <property name="displaylabel">false</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N10" id="A4" to="N7" type="link3">
            <graphic x="137" y="14">
                <property name="height">0</property>
                <property name="endingform">5</property>
                <property name="startingform">4</property>
                <property name="linestyle">2</property>
                <property name="displaylabel">true</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N0" id="A0" to="N5" type="link1">
            <graphic x="430" y="33">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="displaylabel">false</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N7" id="A3" to="N5" type="link1">
            <graphic x="300" y="90">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="displaylabel">false</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N5" id="A7" to="N1" type="link1">
            <graphic x="634" y="54">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="linestyle">2</property>
                <property name="displaylabel">false</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N5" id="A8" label="An arrow" to="N2" type="link3">
            <graphic x="533" y="116">
                <property name="height">0</property>
                <property name="endingform">5</property>
                <property name="startingform">4</property>
                <property name="linestyle">2</property>
                <property name="displaylabel">true</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
    </arrows>
    <property name="gridsize">10</property>
    <property name="displaygrid">false</property>
    <property name="snaptogrid">false</property>
</structure>
