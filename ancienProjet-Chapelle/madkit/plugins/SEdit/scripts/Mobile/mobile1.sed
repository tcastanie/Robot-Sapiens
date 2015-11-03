<?xml version="1.0" encoding="UTF-8"?>
<structure name="example" type="mobile">
    <nodes>
        <node id="N1" type="mobilenode">
            <graphic x="434" y="205">
                <property name="height">44</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="imageaddress">images/agent1.gif</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="dy">-0.12026355635852404</property>
            <property name="dx">0.3802229418263419</property>
            <property name="fixed">false</property>
        </node>
        <node id="N2" type="mobilenode">
            <graphic x="336" y="255">
                <property name="height">44</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="imageaddress">images/agent1.gif</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="dy">0.07740091732205279</property>
            <property name="dx">0.3995613817786986</property>
            <property name="fixed">false</property>
        </node>
        <node id="N3" type="Fixed">
            <graphic x="421" y="326">
                <property name="height">32</property>
                <property name="displaylabel">true</property>
                <property name="width">32</property>
                <property name="imageaddress">images/world/wall.gif</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="dy">0.12208689610990764</property>
            <property name="dx">-1.0106371256943856</property>
            <property name="fixed">true</property>
        </node>
        <node id="N4" type="mobilenode">
            <graphic x="307" y="363">
                <property name="height">44</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="imageaddress">images/agent1.gif</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="dy">0.489361400795558</property>
            <property name="dx">0.3577259853463032</property>
            <property name="fixed">false</property>
        </node>
        <node id="N5" type="mobilenode">
            <graphic x="541" y="342">
                <property name="height">44</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="imageaddress">images/agent1.gif</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="dy">0.05819915025538913</property>
            <property name="dx">-0.14618805713199057</property>
            <property name="fixed">false</property>
        </node>
        <node id="N6" type="mobilenode">
            <graphic x="552" y="202">
                <property name="height">44</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="imageaddress">images/agent1.gif</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="dy">-0.23939736132012762</property>
            <property name="dx">0.2370508377430527</property>
            <property name="fixed">false</property>
        </node>
    </nodes>
    <arrows>
        <arrow from="N2" id="A0" to="N3" type="Link">
            <graphic x="396" y="309">
                <property name="height">0</property>
                <property name="endingform">0</property>
                <property name="startingform">0</property>
                <property name="linestyle">1</property>
                <property name="displaylabel">true</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N3" id="A1" to="N1" type="Link">
            <graphic x="445" y="284">
                <property name="height">0</property>
                <property name="endingform">0</property>
                <property name="startingform">0</property>
                <property name="linestyle">1</property>
                <property name="displaylabel">true</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N2" id="A2" to="N4" type="Link">
            <graphic x="341" y="331">
                <property name="height">0</property>
                <property name="endingform">0</property>
                <property name="startingform">0</property>
                <property name="linestyle">1</property>
                <property name="displaylabel">true</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N4" id="A3" to="N3" type="Link">
            <graphic x="382" y="363">
                <property name="height">0</property>
                <property name="endingform">0</property>
                <property name="startingform">0</property>
                <property name="linestyle">1</property>
                <property name="displaylabel">true</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N3" id="A4" to="N5" type="Link">
            <graphic x="499" y="353">
                <property name="height">0</property>
                <property name="endingform">0</property>
                <property name="startingform">0</property>
                <property name="linestyle">1</property>
                <property name="displaylabel">true</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N5" id="A5" to="N6" type="Link">
            <graphic x="566" y="294">
                <property name="height">0</property>
                <property name="endingform">0</property>
                <property name="startingform">0</property>
                <property name="linestyle">1</property>
                <property name="displaylabel">true</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N6" id="A6" to="N1" type="Link">
            <graphic x="513" y="225">
                <property name="height">0</property>
                <property name="endingform">0</property>
                <property name="startingform">0</property>
                <property name="linestyle">1</property>
                <property name="displaylabel">true</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N2" id="A7" to="N1" type="Link">
            <graphic x="405" y="252">
                <property name="height">0</property>
                <property name="endingform">0</property>
                <property name="startingform">0</property>
                <property name="linestyle">1</property>
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
