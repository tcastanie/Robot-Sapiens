<?xml version="1.0" encoding="UTF-8"?>
<structure name="example" type="Petri">
    <nodes>
        <node id="N0" type="Place">
            <graphic x="76" y="269">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N1" type="Place">
            <graphic x="377" y="316">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N2" type="Place">
            <graphic x="525" y="115">
                <property name="height">40</property>
                <property name="displaylabel">true</property>
                <property name="width">40</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N3" label="Noeud truc much" type="Transition">
            <graphic x="189" y="63">
                <property name="height">32</property>
                <property name="displaylabel">true</property>
                <property name="width">80</property>
                <property name="labellocation">4</property>
            </graphic>
            <property name="comment">Un drôle de noeud</property>
        </node>
        <node id="N4" type="Transition">
            <graphic x="197" y="380">
                <property name="height">32</property>
                <property name="displaylabel">true</property>
                <property name="width">80</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N5" type="Transition">
            <graphic x="587" y="318">
                <property name="height">32</property>
                <property name="displaylabel">true</property>
                <property name="width">80</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
        <node id="N6" type="Transition">
            <graphic x="357" y="84">
                <property name="height">32</property>
                <property name="displaylabel">true</property>
                <property name="width">80</property>
                <property name="labellocation">4</property>
            </graphic>
        </node>
    </nodes>
    <arrows>
        <arrow from="N0" id="A0" to="N3" type="Consumer">
            <graphic x="96" y="200">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="displaylabel">false</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N0" id="A1" to="N4" type="Consumer">
            <graphic x="177" y="321">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="displaylabel">false</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N1" id="A2" to="N3" type="Consumer">
            <graphic x="267" y="244">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="displaylabel">false</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N5" id="A3" to="N2" type="Producer">
            <graphic x="579" y="242">
                <property name="height">0</property>
                <property name="endingform">1</property>
                <property name="startingform">0</property>
                <property name="displaylabel">false</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N1" id="A4" to="N4" type="Inhibitor">
            <graphic x="313" y="346">
                <property name="height">0</property>
                <property name="endingform">3</property>
                <property name="startingform">0</property>
                <property name="displaylabel">false</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
        <arrow from="N1" id="A5" to="N6" type="Informer">
            <graphic x="401" y="268">
                <property name="height">0</property>
                <property name="endingform">2</property>
                <property name="startingform">0</property>
                <property name="displaylabel">false</property>
                <property name="linestyle">2</property>
                <property name="width">0</property>
                <property name="labellocation">5</property>
            </graphic>
        </arrow>
    </arrows>
</structure>
